/*
 *  Copyright (c) 2023 TEAM CLAM
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.gitlab.fsc_clam.fscwhereswhat.repo.impl

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import android.location.LocationManager
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import androidx.core.content.getSystemService
import androidx.core.location.LocationListenerCompat
import androidx.core.location.LocationManagerCompat
import androidx.core.location.LocationRequestCompat
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map

class ImplLocationRepository(application: Application) : LocationRepository {
	private val manager: LocationManager = application.getSystemService()!!

	@SuppressLint("MissingPermission")
	val locations: Flow<Location> = callbackFlow {
		val manager: LocationManager = application.getSystemService()!!
		val provider =
			if (SDK_INT >= VERSION_CODES.S) {
				LocationManager.FUSED_PROVIDER
			} else {
				LocationManager.GPS_PROVIDER
			}

		// Get last known location to at least have the UI work quickly
		val lastLocation = manager.getLastKnownLocation(provider)

		if (lastLocation != null)
			send(lastLocation)

		// Provide new locations
		val locationListener = LocationListenerCompat { newLocation ->
			trySend(newLocation)
		}

		// Start listening to location updates
		LocationManagerCompat.requestLocationUpdates(
			manager,
			provider,
			LocationRequestCompat.Builder(500)
				.setQuality(LocationRequestCompat.QUALITY_HIGH_ACCURACY)
				.build(),
			Dispatchers.Default.asExecutor(),
			locationListener
		)

		// No one is subscribed anymore, close flow and remove listener
		awaitClose {
			LocationManagerCompat.removeUpdates(manager, locationListener)
		}
	}

	override val longitude = locations.map { it.longitude }

	override val latitude = locations.map { it.latitude }

	override val bearing = locations.map { it.bearing }

	companion object {
		private var repo: ImplLocationRepository? = null

		/**
		 * Get implementation of ImplLocationRepository
		 */
		@Synchronized
		fun LocationRepository.Companion.get(application: Application): LocationRepository {
			if (repo == null)
				repo = ImplLocationRepository(application)

			return repo!!
		}

		const val defaultLong = -73.42942148736103
		const val defaultLat = 40.75145183660949
	}
}