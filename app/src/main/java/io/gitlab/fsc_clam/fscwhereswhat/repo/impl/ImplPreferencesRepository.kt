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

import android.app.Application
import android.content.Context
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseAuth
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.providers.impl.FBPreferences
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.PreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


/**
 * Implementation of the Preferences Repository
 */
class ImplPreferencesRepository(application: Application) : PreferencesRepository {
	/**
	 * Instance of the users Firebase Authentication
	 */
	private val firebaseAuth = FirebaseAuth.getInstance()

	/**
	 * Instance of FBPReferences for Firebase Interaction
	 */
	private val fbp = FBPreferences()

	/**
	 * Connection to Preferences in SharedPreferences
	 */
	private val sharedPref =
		application.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)

	override fun getColor(type: EntityType): Flow<Int> {
		val fUID = firebaseAuth.currentUser?.uid

		// First check if current user is logged in
		if (fUID != null) {
			val fbColors = fbp.getColor(fUID)

			return fbColors.map {
				(it[type.name]!!)
			}
		} else {
			// Current user is null; check SharedPreferences
			return callbackFlow {
				val listener = OnSharedPreferenceChangeListener { sp, key ->
					if (key == type.name) { // Ensure the key that updated is ours
						val color = sp.getInt(type.name, type.defaultColor)
						trySend(color)
					}
				}

				send(sharedPref.getInt(type.name, type.defaultColor)) // send first

				sharedPref.registerOnSharedPreferenceChangeListener(listener)

				awaitClose {
					sharedPref.unregisterOnSharedPreferenceChangeListener(listener)
				}
			}
		}
	}

	override suspend fun setColor(type: EntityType, color: Int) {
		withContext(Dispatchers.IO) {
			val userId = firebaseAuth.currentUser?.uid
			if (userId != null) {
				fbp.setColor(userId, type.name, color)
			} else {
				sharedPref.edit(true) {
					putInt(type.name, color)
				}
			}
		}
	}

	override fun getIsFirst(): Flow<Boolean> =
		callbackFlow {
			val listener = OnSharedPreferenceChangeListener { sp, key ->
				if (key == FIRST_TIME) { // Ensure the key that updated is ours
					trySend(sp.getBoolean(FIRST_TIME, true))
				}
			}

			send(sharedPref.getBoolean(FIRST_TIME, true)) // send first value

			sharedPref.registerOnSharedPreferenceChangeListener(listener)

			awaitClose {
				sharedPref.unregisterOnSharedPreferenceChangeListener(listener)
			}
		}

	override fun setNotFirst() {
		sharedPref.edit(true) {
			putBoolean(FIRST_TIME, false)
		}
	}

	companion object {
		private const val FIRST_TIME = "first_time_1"

		private var repo: ImplPreferencesRepository? = null

		/**
		 * Get the implementation of [PreferencesRepository]
		 */
		@Synchronized
		fun PreferencesRepository.Companion.get(
			application: Application
		): PreferencesRepository {
			if (repo == null) {
				repo = ImplPreferencesRepository(application)
			}

			return repo!!
		}
	}
}