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

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * For the More View
 */
abstract class MoreViewModel(application: Application) : AndroidViewModel(application) {

	abstract val exceptions : Flow<Throwable>
	abstract val cacheStatus : Flow<Boolean>

	/**
	 * Color of Building pinpoints
	 */
	abstract val buildingColor: StateFlow<Int>

	/**
	 * Color of Event pinpoints
	 */
	abstract val eventColor: StateFlow<Int>

	/**
	 * Color of utility pinpoints
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
	 * Sets color of utility pinpoints
	 */
	abstract fun setUtilityColor(color: Int)

	/**
	 * Clear cache on app
	 */
	abstract fun clearCache()

}