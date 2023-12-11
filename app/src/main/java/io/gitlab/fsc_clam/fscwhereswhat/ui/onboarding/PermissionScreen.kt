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

import android.Manifest
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import io.gitlab.fsc_clam.fscwhereswhat.R
import io.gitlab.fsc_clam.fscwhereswhat.ui.theme.bodyFont
import io.gitlab.fsc_clam.fscwhereswhat.ui.theme.headFont

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(
	locationPermissionsState: MultiplePermissionsState
) {
	val activity = (LocalContext.current as? Activity)
	var showExitDialog by remember { mutableStateOf(false) }
	val allPermissionsRevoked =
		locationPermissionsState.permissions.size == locationPermissionsState.revokedPermissions.size
	val fineLocationPermission =
		rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)

	Column(
		modifier = Modifier
			.padding(16.dp)
			.fillMaxSize()
			.background(Color.White)
			.verticalScroll(rememberScrollState()),
		verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(
			text = stringResource(id = R.string.permissions),
			fontFamily = headFont,
			fontSize = 46.sp
		)
		Text(
			text = let {
				if (locationPermissionsState.allPermissionsGranted)
					stringResource(id = R.string.permissions_thank)
				else if (!allPermissionsRevoked)
				//if user grants only coarse location, tell user map will not be as accurate
					stringResource(id = R.string.coarse_location_only)
				else
				//tell user the app cannot function without location permissions
					stringResource(id = R.string.request_location_permissions)
			},
			fontFamily = bodyFont,
			textAlign = TextAlign.Center
		)
		if (locationPermissionsState.allPermissionsGranted) {
			//show nothing
		} else if (locationPermissionsState.shouldShowRationale) {
			showExitDialog = true
		} else {
			Button(onClick = {
				locationPermissionsState.launchMultiplePermissionRequest()
				if (!allPermissionsRevoked)
					fineLocationPermission.launchPermissionRequest()
			}
			) {
				Text(text = stringResource(id = R.string.request_permissions))
			}
		}
	}
	if (showExitDialog) {
		Dialog(onDismissRequest = {
			showExitDialog = false
			activity?.finish()
		}
		) {
			Card(
				modifier = Modifier.fillMaxWidth()
			) {
				Column(
					modifier = Modifier.padding(16.dp),
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.spacedBy(8.dp),
				) {
					Text(text = stringResource(id = R.string.need_permissions))

					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.Center
					) {
						TextButton(onClick = {
							showExitDialog = false
							activity?.finish()
						}) {
							Text(text = stringResource(id = R.string.ok))
						}
					}
				}
			}
		}
	}
}