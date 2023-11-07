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

package io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

/**
 * For Options Screen in Onboarding Process
 */
abstract class OptionsViewModel : ViewModel() {
	/**
	 * Color of Building pinpoints
	 */
	abstract val buildingColor: StateFlow<Int>

	/**
	 * Color of Event pinpoints
	 */
	abstract val eventColor: StateFlow<Int>

	/**
	 * Color of Utility pinpoints
	 */
	abstract val utilityColor: StateFlow<Int>

	/**
	 * Sets color of Building pinpoints
	 */
	abstract fun setBuildingColor(color: Int)

	/**
	 * Sets color of Event pinpoints
	 */
	abstract fun setEventColor(color: Int)

	/**
	 * Sets color of Utility pinpoints
	 */
	abstract fun setUtilityColor(color: Int)

}