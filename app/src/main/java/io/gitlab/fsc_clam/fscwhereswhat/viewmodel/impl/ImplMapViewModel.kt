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

package io.gitlab.fsc_clam.fscwhereswhat.viewmodel.impl

import android.app.Application
import android.graphics.Color
import androidx.activity.result.ActivityResult
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.gitlab.fsc_clam.fscwhereswhat.common.FSC_LAT
import io.gitlab.fsc_clam.fscwhereswhat.common.FSC_LOG
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.OSMEntity
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Pinpoint
import io.gitlab.fsc_clam.fscwhereswhat.model.local.User
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.LocationRepository
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.PreferencesRepository
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.RamCentralRepository
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.UserRepository
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.FakeOSMRepo
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.ImplLocationRepository.Companion.get
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.ImplPreferencesRepository.Companion.get
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.ImplRamCentralRepository.Companion.get
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.ImplUserRepository.Companion.get
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.MapViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

class ImplMapViewModel(application: Application) : MapViewModel(application) {
	private val userRepo = UserRepository.get()
	private val prefRepo = PreferencesRepository.get(application)
	private val osmRepo = FakeOSMRepo()
	private val ramCentralRepo = RamCentralRepository.get(application)
	private val locationRepo = LocationRepository.get(application)

	override val user: StateFlow<User?> = userRepo.user.stateIn(
		viewModelScope, SharingStarted.Eagerly, null
	)

	override val query: MutableStateFlow<String?> = MutableStateFlow(null)

	override val activeFilter: MutableStateFlow<EntityType?> = MutableStateFlow(null)

	override val focus: MutableStateFlow<Pinpoint?> = MutableStateFlow(null)

	override val userLongitude: StateFlow<Double> =
		locationRepo.longitude.stateIn(viewModelScope, SharingStarted.Eagerly, FSC_LOG)

	override val userLatitude: StateFlow<Double> =
		locationRepo.latitude.stateIn(viewModelScope, SharingStarted.Eagerly, FSC_LAT)

	override val buildingColor: StateFlow<Int> =
		prefRepo.getColor(EntityType.BUILDING).stateIn(
			viewModelScope,
			SharingStarted.Eagerly, Color.BLACK
		)

	override val eventColor: StateFlow<Int> =
		prefRepo.getColor(EntityType.EVENT).stateIn(
			viewModelScope,
			SharingStarted.Eagerly, Color.BLACK
		)

	override val nodeColor: StateFlow<Int> =
		prefRepo.getColor(EntityType.NODE).stateIn(
			viewModelScope,
			SharingStarted.Eagerly, Color.BLACK
		)

	/**
	 * lat to log
	 */
	private val coordinates: Flow<Pair<Double, Double>> =
		userLatitude.combine(userLongitude) { lat, long ->
			lat to long
		}

	private val osmPinpoint: StateFlow<List<Pinpoint>> = coordinates.transform { (lat, long) ->
		emitAll(
			osmRepo.queryNearby(lat, long).map { entities ->
				entities.map { entity ->
					when (osmRepo.get(entity.id)) {
						is OSMEntity.Building -> {
							Pinpoint(
								latitude = entity.lat,
								longitude = entity.long,
								color = buildingColor.value,
								id = entity.id,
								type = EntityType.BUILDING,
							)
						}

						is OSMEntity.Node -> {
							Pinpoint(
								latitude = entity.lat,
								longitude = entity.long,
								color = nodeColor.value,
								id = entity.id,
								type = EntityType.NODE,
							)
						}
					}
				}
			}
		)
	}.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

	private val eventPinpoint: StateFlow<List<Pinpoint>> = ramCentralRepo.getAll().map { events ->
		events.map { event ->
			val osmEvent = osmRepo.get(event.id)
			Pinpoint(
				latitude = osmEvent.lat,
				longitude = osmEvent.long,
				color = eventColor.value,
				id = event.id,
				type = EntityType.EVENT
			)
		}
	}.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

	override val pinpoints: StateFlow<List<Pinpoint>> =
		osmPinpoint.combine(eventPinpoint) { osm, event ->
			osm + event
		}.combine(activeFilter) { list, filter ->
			if (filter == null)
				list
			else list.filter { it.type == filter }
		}.combine(focus) { list, focus ->
			// Don't display other pin points if we have a focus
			if (focus == null)
				list
			else list.filter { it.id == focus.id }
		}.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

	override fun setActiveFilter(filter: EntityType?) {
		activeFilter.value = filter
	}

	override fun setFocus(pinpoint: Pinpoint?) {
		focus.value = pinpoint
	}

	private val firebaseAuth = FirebaseAuth.getInstance()

	override val exception = MutableSharedFlow<Throwable>()

	override fun handleSignInResult(result: ActivityResult) {
		GoogleSignIn.getSignedInAccountFromIntent(result.data)
			.addOnSuccessListener(::login)
			.addOnFailureListener(::sendException)
	}

	private fun login(googleSignInAccount: GoogleSignInAccount) {
		firebaseAuth.signInWithCredential(
			GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)
		).addOnFailureListener(::sendException)
	}

	private fun sendException(e: Exception) {
		e.printStackTrace()
		viewModelScope.launch {
			exception.emit(e)
		}
	}
	init {
		/* TODO figure out view reentering automatically
		val viewDistancePerc = .005

		viewModelScope.launch {
			cameraLatitude.combine(cameraLongitude) { a, b -> a to b }.collect { (cLat, cLog) ->

				coordinates.debounce(100).collect { (uLat, uLog) ->
					val minLat = uLat - (uLat * viewDistancePerc)
					val maxLat = uLat + (uLat * viewDistancePerc)

					val minLong = uLog - (uLog * viewDistancePerc)
					val maxLong = uLog + (uLog * viewDistancePerc)

					if (cLat <= minLat || cLat >= maxLat && cLog <= minLong || cLog >= maxLong) {
						// The camera is too far away with the user moving, recenter
						Log.d("MapViewModel", "Recentering")
						cameraLatitude.emit(uLat)
						cameraLongitude.emit(uLog)
					}
				}
			}
		}
		 */
	}
}