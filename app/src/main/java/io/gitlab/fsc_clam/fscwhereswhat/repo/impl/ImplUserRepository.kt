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

import com.google.firebase.auth.FirebaseAuth
import io.gitlab.fsc_clam.fscwhereswhat.model.local.User
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.UserRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Implementation of the User Repository
 */
class ImplUserRepository : UserRepository {
	private val firebaseAuth = FirebaseAuth.getInstance()

	override val user: Flow<User?> = callbackFlow {
		val listener = FirebaseAuth.AuthStateListener { auth ->
			// This is invoked on the UI thread, so keep it snappy

			trySend(
				auth.currentUser?.let {
					User(
						it.uid,
						it.displayName ?: "Unknwon",
						it.photoUrl,
					)
				}
			)
		}

		// Add listener
		firebaseAuth.addAuthStateListener(listener)

		awaitClose {
			// Remove Listener when no longer needed
			firebaseAuth.removeAuthStateListener(listener)
		}
	}

	companion object {
		private var repo: ImplUserRepository? = null

		/**
		 * Get user repository
		 */
		fun UserRepository.Companion.get(): UserRepository {
			if (repo == null)
				repo = ImplUserRepository()

			return repo!!
		}
	}
}