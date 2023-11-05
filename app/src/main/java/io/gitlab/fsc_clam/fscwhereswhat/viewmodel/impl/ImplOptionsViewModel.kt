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
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.OptionsViewModel
import kotlinx.coroutines.flow.StateFlow

class ImplOptionsViewModel(): OptionsViewModel() {
	override val buildingColor: StateFlow<Color>
		get() = TODO("Not yet implemented")
	override val eventColor: StateFlow<Color>
		get() = TODO("Not yet implemented")

	override fun setBuildingColor(color: Color) {
		TODO("Not yet implemented")
	}

	override fun setEventColor(color: Color) {
		TODO("Not yet implemented")
	}
}