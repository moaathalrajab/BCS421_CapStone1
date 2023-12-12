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

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.utsman.osmandcompose.DefaultMapProperties
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.OverlayManagerState
import com.utsman.osmandcompose.ZoomButtonVisibility
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import io.gitlab.fsc_clam.fscwhereswhat.common.FSC_LAT
import io.gitlab.fsc_clam.fscwhereswhat.common.FSC_LOG
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Pinpoint
import io.gitlab.fsc_clam.fscwhereswhat.model.local.User
import io.gitlab.fsc_clam.fscwhereswhat.providers.MapBoxXYTileSource
import io.gitlab.fsc_clam.fscwhereswhat.ui.entity.EntityDetail
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint


/**
 * Creates the content of Map View, including the MapUI and the OSM Map
 * @param user is the google account if logged in
 * @param pinPoints is the list of PinPoints visible in the Map
 * @param activeFilter is the current filter selected for EntityType. If null, all filters are active
 * @param buildingColor is the color of building pinpoints
 * @param eventColor is the color of event pinpoints
 * @param nodeColor is the color of node pinpoints
 * @param setActiveFilter is the function from the viewmodel that sets current active filter when user presses filter button
 * @param setFocus is when the user clicks on the pinpoint
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapContent(
	snackbarState: SnackbarHostState,
	user: User?,
	query: String?,
	cameraLatitude: Double,
	cameraLongitude: Double,
	userLatitude: Double,
	userLongitude: Double,
	pinPoints: List<Pinpoint>,
	activeFilter: EntityType?,
	focus: Pinpoint?,
	setActiveFilter: (EntityType?) -> Unit,
	setFocus: (Pinpoint?) -> Unit,
	openSearch: () -> Unit,
	navigateToMore: () -> Unit,
	login: () -> Unit,
	saveCameraState: (lat: Double, log: Double) -> Unit
) {
	val cameraState = rememberCameraState {
		geoPoint = GeoPoint(cameraLatitude, cameraLongitude)
		zoom = 18.5// optional, default is 5.0
	}

	// Save camera state
	DisposableEffect(cameraState) {
		onDispose {
			saveCameraState(cameraState.geoPoint.latitude, cameraState.geoPoint.longitude)
		}
	}

	/**
	 * Recenter location to where the user is
	 */
	fun onRecenter() {
		cameraState.geoPoint = GeoPoint(latitude, longitude)
	}

	val userMarkerState = rememberMarkerState(
		geoPoint = GeoPoint(
			userLatitude,
			userLongitude
		)
	)

	// Update user location
	LaunchedEffect(userLongitude, userLatitude) {
		userMarkerState.geoPoint = GeoPoint(userLatitude, userLongitude)
	}

	// define properties with remember with default value
	val mapProperties = remember {
		DefaultMapProperties
			.copy(
				isTilesScaledToDpi = true,
				tileSources = MapBoxXYTileSource,
				isEnableRotationGesture = true,
				zoomButtonVisibility = ZoomButtonVisibility.NEVER,
			)
	}

	Scaffold(
		bottomBar = {
			Column {
				MapBottomBar(
					activeFilter,
					setActiveFilter,
					openSearch,
					query,
					navigateToMore
				)
			}
		},
		snackbarHost = {
			SnackbarHost(snackbarState)
		}
	) {
		Box(Modifier.padding(it)) // ignore the padding

		Box(
			modifier = Modifier
				.fillMaxSize()
		) {
			//creates map from OSM
			OpenStreetMap(
				modifier = Modifier.fillMaxSize(),
				cameraState = cameraState,
				properties = mapProperties,
				overlayManagerState = rememberSaveable(key = null, saver = Saver(
					save = {
						null
					},
					restore = {
						OverlayManagerState(null)
					}
				)) {
					OverlayManagerState(null)
				},
			) {
				pinPoints.forEach { pinpoint ->
					MapPinPoint(pinpoint, setFocus)
				}

				MapUserMarker(userMarkerState)
			}

			//Creates the Map UI after map creation
			MapOverview(
				user = user,
				onRecenter = ::onRecenter,
				login = login
			)
		}
		if (focus != null) {
			ModalBottomSheet(onDismissRequest = { setFocus(null) }) {
				EntityDetail()
			}
		}
	}
}

@Preview
@Composable
fun PreviewMapContent() {
	val user = User(":", "", Uri.parse("https://google.com"))
	MapContent(
		user = user,
		query = null,
		userLatitude = 40.75175,
		userLongitude = -73.42902,
		pinPoints = listOf(
			Pinpoint(
				40.75175,
				-73.42902,
				0,
				0,
				EntityType.NODE,
			),
			Pinpoint(
				40.751485,
				-73.428329,
				0,
				0,
				EntityType.BUILDING,
			),
			Pinpoint(
				40.751632,
				-73.428936,
				0,
				0,
				EntityType.EVENT,
			)
		),
		activeFilter = null,
		focus = null,
		setActiveFilter = {},
		setFocus = {},
		openSearch = {},
		navigateToMore = {},
		login = {},
		snackbarState = SnackbarHostState(),
		cameraLatitude = FSC_LAT,
		cameraLongitude = FSC_LOG,
		saveCameraState = { _, _ -> }
	)
}