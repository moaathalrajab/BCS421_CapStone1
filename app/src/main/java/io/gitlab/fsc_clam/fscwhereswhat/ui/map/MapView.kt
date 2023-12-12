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

package io.gitlab.fsc_clam.fscwhereswhat.ui.map

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import io.gitlab.fsc_clam.fscwhereswhat.R
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.MapViewModel
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.impl.ImplMapViewModel

/**
 * MapView contains the viewmodels and the MapContent
 */
@Composable
fun MapView(
	openSearch: () -> Unit,
	navigateToMore: () -> Unit
) {
	val mapViewModel: MapViewModel = viewModel<ImplMapViewModel>()
	val user by mapViewModel.user.collectAsState()
	val query by mapViewModel.query.collectAsState()
	val pinpoints by mapViewModel.pinpoints.collectAsState()
	val latitude by mapViewModel.latitude.collectAsState()
	val longitude by mapViewModel.longitude.collectAsState()
	val activeFilter by mapViewModel.activeFilter.collectAsState()
	val focus by mapViewModel.focus.collectAsState()


	val exception by mapViewModel.exception.collectAsState(null)
	val snackbarState = remember { SnackbarHostState() }

	// Display exceptions
	LaunchedEffect(exception) {
		if (exception != null) {
			snackbarState.showSnackbar(exception?.message ?: "Unknown Error")
		}
	}

	val context = LocalContext.current
	val gsoClient = GoogleSignIn.getClient(
		context as Activity,
		GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestIdToken(stringResource(R.string.default_web_client_id))
			.requestEmail()
			.build()
	)

	val signInLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.StartActivityForResult(),
		onResult = mapViewModel::handleSignInResult
	)

	var fromLogIn by remember { mutableStateOf(false) }

	// Feedback that the sign in worked
	LaunchedEffect(user) {
		if (user != null && fromLogIn) {
			snackbarState.showSnackbar("Welcome ${user!!.name}!")
			fromLogIn = false
		}
	}

	Scaffold(
		bottomBar = {
			// TODO banner ad here
		},
		snackbarHost = {
			SnackbarHost(snackbarState)
		}
	) {
		//pinpoints is fake data currently
		MapContent(
			it,
			user = user,
			query = query,
			latitude = latitude,
			longitude = longitude,
			pinPoints = pinpoints,
			activeFilter = activeFilter,
			focus = focus,
			setActiveFilter = mapViewModel::setActiveFilter,
			setFocus = mapViewModel::setFocus,
			openSearch = openSearch,
			navigateToMore = navigateToMore,
			login = {
				fromLogIn = true
				signInLauncher.launch(gsoClient.signInIntent)
			}
		)
	}
}

@Preview
@Composable
fun PreviewMapView() {
	MapView(
		openSearch = {},
		navigateToMore = {}
	)
}
