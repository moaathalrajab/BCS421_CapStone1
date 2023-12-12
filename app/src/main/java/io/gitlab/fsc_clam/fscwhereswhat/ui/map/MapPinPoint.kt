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

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.OsmAndroidComposable
import com.utsman.osmandcompose.rememberMarkerState
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Pinpoint
import org.osmdroid.util.GeoPoint

/**
 * Represents a given pinpoint on a map
 */
@Composable
@OsmAndroidComposable
fun MapPinPoint(pinpoint: Pinpoint, setFocus: (Pinpoint) -> Unit) {
	val context = LocalContext.current
	//the icon is chosen based on EntityType
	val icon = AppCompatResources.getDrawable(context, pinpoint.type.drawable)

	SideEffect {
		icon?.setTint(pinpoint.color)
	}

	Marker(
		state = rememberMarkerState(
			geoPoint = GeoPoint(
				pinpoint.latitude,
				pinpoint.longitude
			)
		),
		icon = icon
	) {
		LaunchedEffect(key1 = pinpoint) {
			setFocus(pinpoint)
		}
	}
}