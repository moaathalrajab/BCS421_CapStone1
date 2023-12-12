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
import androidx.compose.ui.platform.LocalContext
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.MarkerState
import io.gitlab.fsc_clam.fscwhereswhat.R


@Composable
fun MapUserMarker(userMarker: MarkerState) {
	val context = LocalContext.current
	Marker(
		state = userMarker,
		icon = AppCompatResources.getDrawable(context, R.drawable.baseline_navigation_24),
	) {
	}
}
