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
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.utsman.osmandcompose.DefaultMapProperties
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.ZoomButtonVisibility
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.rememberMarkerState
import com.utsman.osmandcompose.rememberOverlayManagerState
import io.gitlab.fsc_clam.fscwhereswhat.R
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Pinpoint
import io.gitlab.fsc_clam.fscwhereswhat.model.local.User
import io.gitlab.fsc_clam.fscwhereswhat.providers.MapBoxTileSource
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.MapViewModel
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.impl.ImplMapViewModel
import org.osmdroid.util.GeoPoint

/**
 * MapView contains the viewmodels and the MapContent
 */
@Composable
fun MapView() {
	val mapViewModel: MapViewModel = viewModel<ImplMapViewModel>()
	val user by mapViewModel.user.collectAsState()
	val pinpoints by mapViewModel.pinpoints.collectAsState()
	val latitude by mapViewModel.latitude.collectAsState()
	val longitude by mapViewModel.longitude.collectAsState()
	val activeFilter by mapViewModel.activeFilter.collectAsState()
	val buildingColor by mapViewModel.buildingColor.collectAsState()
	val eventColor by mapViewModel.eventColor.collectAsState()
	val nodeColor by mapViewModel.nodeColor.collectAsState()
	val focus by mapViewModel.focus.collectAsState()
	Scaffold(
		bottomBar = {
			// TODO banner ad here
		}
	) {
		//pinpoints is fake data currently
		MapContent(
			it,
			user = user,
			latitude = latitude,
			longitude = longitude,
			pinPoints = pinpoints,
			activeFilter = activeFilter,
			buildingColor = buildingColor,
			eventColor = eventColor,
			nodeColor = nodeColor,
			focus = focus,
			setActiveFilter = mapViewModel::setActiveFilter,
			setFocus = mapViewModel::setFocus
		)
	}

}

@Preview
@Composable
fun PreviewMapView() {
	MapView()
}

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
	padding: PaddingValues,
	user: User?,
	latitude: Double,
	longitude: Double,
	pinPoints: List<Pinpoint>,
	activeFilter: EntityType?,
	buildingColor: Int,
	eventColor: Int,
	nodeColor: Int,
	focus: Pinpoint?,
	setActiveFilter: (EntityType?) -> Unit,
	setFocus: (Pinpoint?) -> Unit
) {
	//@Deprecated("For testing purposes only)
	//Remove this later, @Deprecated seems to only work for functions
	var isEntityDetailVisible by remember { mutableStateOf(false) }
	val sheetState = rememberModalBottomSheetState()

	val context = LocalContext.current
	val cameraState = rememberCameraState {
		geoPoint = GeoPoint(longitude, latitude)
		zoom = 18.0// optional, default is 5.0
	}

	fun onRecenter() {
		cameraState.geoPoint = GeoPoint(longitude, latitude)
	}

	val userMarker = rememberMarkerState(
		geoPoint = GeoPoint(
			latitude,
			longitude
		)
	)

	LaunchedEffect(longitude, latitude) {
		Log.d("compose", "Update camera")
		userMarker.geoPoint = GeoPoint(longitude, latitude)
	}

	// define properties with remember with default value
	val mapProperties = remember {
		DefaultMapProperties
			.copy(
				isTilesScaledToDpi = true,
				tileSources = MapBoxTileSource,
				isEnableRotationGesture = true,
				zoomButtonVisibility = ZoomButtonVisibility.NEVER,
			)
	}

	val overlayManagerState = rememberOverlayManagerState()
	Box(
		modifier = Modifier
			.padding(padding)
			.fillMaxSize()
	) {
		//creates map from OSM
		OpenStreetMap(
			modifier = Modifier.fillMaxSize(),
			cameraState = cameraState,
			properties = mapProperties,
			overlayManagerState = overlayManagerState,
		) {
			pinPoints.filter {
				// Don't display other pin points if we have a focus
				if (focus != null)
					it == focus
				else true
			}.forEach { pinpoint ->
				//for each pinpoint create a marker
				if (pinpoint.type == activeFilter || activeFilter == null) {
					val point = rememberMarkerState(
						geoPoint = GeoPoint(
							pinpoint.latitude.toDouble(),
							pinpoint.longitude.toDouble()
						)
					)
					//the icon is chosen based on EntityType
					val icon = when (pinpoint.type) {
						EntityType.BUILDING -> context.getDrawable(R.drawable.map_building)
						EntityType.EVENT -> context.getDrawable(R.drawable.map_event)
						EntityType.NODE -> context.getDrawable(R.drawable.map_node)
					}

					Marker(
						state = point,
						icon = icon,
					) {
						setFocus(pinpoint)
					}
				}
			}

			Marker(
				state = userMarker,
				icon = context.getDrawable(R.drawable.baseline_navigation_24),
			) {
			}
		}
		//Creates the Map UI after map creation
		Box(
			modifier = Modifier
				.fillMaxSize()
		) {
			MapUI(
				user = user,
				activeFilter = activeFilter,
				buildingColor = buildingColor,
				eventColor = eventColor,
				nodeColor = nodeColor,
				setActiveFilter = setActiveFilter,
				onRecenter = ::onRecenter
			)
		}
	}
	//when pinpoint is clicked show entity detail
	if (isEntityDetailVisible) {
		ModalBottomSheet(
			onDismissRequest = {
				setFocus(null)
			},
			sheetState = sheetState
		) {
			Box(
				modifier = Modifier
					.padding(8.dp)
					.fillMaxHeight(.7f)
			) {
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
		PaddingValues(8.dp),
		user,
		latitude = 40.75175,
		longitude = -73.42902,
		listOf(
			Pinpoint(
				40.75175f,
				-73.42902f,
				0,
				0,
				EntityType.NODE,
				false
			),
			Pinpoint(
				40.751485f,
				-73.428329f,
				0,
				0,
				EntityType.BUILDING,
				false
			),
			Pinpoint(
				40.751632f,
				-73.428936f,
				0,
				0,
				EntityType.EVENT,
				false
			)
		),
		null,
		Color.Red.toArgb(),
		Color.Red.toArgb(),
		Color.Red.toArgb(),
		null,
		{}
	) {}
}