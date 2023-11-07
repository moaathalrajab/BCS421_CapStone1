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

import android.graphics.Color
import androidx.compose.runtime.collectAsState
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.OptionsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Sets colors for the pinpoints of Buildings and Events from the Explanation Screen in Onboarding View
 */
class ImplOptionsViewModel(): OptionsViewModel() {

	private val _buildingColor = MutableStateFlow<Color>(Color())
	override val buildingColor: StateFlow<Color> = _buildingColor.asStateFlow()

	private val _eventColor = MutableStateFlow<Color>(Color())
	override val eventColor: StateFlow<Color> = _eventColor.asStateFlow()

	override fun setBuildingColor(color: Color) {
		_buildingColor.value = color
	}

	override fun setEventColor(color: Color) {
		_eventColor.value = color
	}
}