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

import androidx.activity.result.ActivityResult
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.gitlab.fsc_clam.fscwhereswhat.model.local.User
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.UserRepository
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.ImplUserRepository.Companion.get
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.LoginViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ImplLoginViewModel : LoginViewModel() {
	private val firebaseAuth = FirebaseAuth.getInstance()

	private val userRepo = UserRepository.get()

	override val user: StateFlow<User?> = userRepo.user
		.stateIn(viewModelScope, SharingStarted.Eagerly, null)

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
}