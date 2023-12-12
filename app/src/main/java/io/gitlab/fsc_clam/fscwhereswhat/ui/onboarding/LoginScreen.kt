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

package io.gitlab.fsc_clam.fscwhereswhat.ui.onboarding

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import io.gitlab.fsc_clam.fscwhereswhat.R
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.LoginViewModel
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.impl.ImplLoginViewModel

/**
 * Screen to let users log in with Google
 */
@Composable
fun LoginScreen(
	showSnackBar: suspend (String) -> Unit
) {
	val viewModel: LoginViewModel = viewModel<ImplLoginViewModel>()
	val context = LocalContext.current

	val exception by viewModel.exception.collectAsState(null)

	// Display exceptions
	LaunchedEffect(exception) {
		if (exception != null) {
			showSnackBar(exception?.message ?: "Unknown Error")
		}
	}

	val user by viewModel.user.collectAsState()

	val gsoClient = GoogleSignIn.getClient(
		context as Activity,
		GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestIdToken(stringResource(R.string.web_client_id))
			.requestEmail()
			.build()
	)

	val signInLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.StartActivityForResult(),
		onResult = viewModel::handleSignInResult
	)
	var fromLogIn by remember { mutableStateOf(false) }

	// Feedback that the sign in worked
	LaunchedEffect(user) {
		if (user != null && fromLogIn) {
			showSnackBar("Welcome ${user!!.name}!")
			fromLogIn = false
		}
	}
	LoginScreenContent(
		onLogin = {
			fromLogIn = true

			signInLauncher.launch(
				gsoClient.signInIntent
			)
		}
	)
}

@Composable
fun LoginScreenContent(
	onLogin: () -> Unit
) {
	OnboardingScreenPage {
		Column(
			modifier = Modifier
				.padding(8.dp)
				.fillMaxSize(),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			//Heading
			Text(
				text = stringResource(id = R.string.login_heading),
				style = MaterialTheme.typography.headlineLarge
			)
			//Ask users to login
			Text(
				text = stringResource(id = R.string.login_screen_body),
				modifier = Modifier.padding(bottom = 30.dp)
			)
			//Clickable image
			//May convert to IconButton
			// https://developers.google.com/identity/branding-guidelines
			Image(
				painterResource(id = R.drawable.google_signin),
				stringResource(id = R.string.login_button_description),
				Modifier
					.clickable(onClick = onLogin)
					.scale(1.5f)
			)
		}
	}
}

@Preview
@Composable
fun PreviewLoginScreenContent() {
	Surface {
		LoginScreenContent(
			onLogin = {}
		)
	}
}