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
import com.google.firebase.auth.FirebaseAuth
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.providers.impl.FBPreferences
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.PreferencesRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map


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
	private val sharedPref = application.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)

	override fun getColor(type: EntityType): Flow<String> {

		// First check if current user is logged in
		if (firebaseAuth.currentUser != null) {

			val user = firebaseAuth.currentUser!!.displayName
			val fbColors = fbp.getColor(user.toString())

			return fbColors.map {
				(it[type.toString()]!!)
			}

		}

		// Current user is null; check SharedPreferences
		else {
			return callbackFlow {

				val listener = OnSharedPreferenceChangeListener { sp, _ ->
					val color = sp.getString(type.toString(), "#fc0303")
					trySend(color!!)
				}

				sharedPref.registerOnSharedPreferenceChangeListener(listener)

				awaitClose() {
					sharedPref.unregisterOnSharedPreferenceChangeListener(listener)
				}
			}
		}
	}

	override suspend fun setColor(type: EntityType, color: Int) {
		val user = firebaseAuth.currentUser!!.displayName
		fbp.setColor(user!!, type.toString(), color.toString())
	}

	companion object {
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