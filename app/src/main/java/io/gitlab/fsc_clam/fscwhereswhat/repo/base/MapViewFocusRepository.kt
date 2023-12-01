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

package io.gitlab.fsc_clam.fscwhereswhat.repo.base

import io.gitlab.fsc_clam.fscwhereswhat.model.local.Pinpoint
import kotlinx.coroutines.flow.StateFlow

/**
 * Repository defining the focus on the MapViewModel
 */
interface MapViewFocusRepository {

	/**
	 * The pinpoint the mapviewmodel is currently focused on
	 */
	val focus: StateFlow<Pinpoint?>

	/**
	 * Sets the focus in the repository
	 *
	 * @param pinpoint is the pinpoint gaining focus
	 */
	fun setFocus(pinpoint: Pinpoint)

	/**
	 * Binding point for implementation getter
	 */
	companion object
}