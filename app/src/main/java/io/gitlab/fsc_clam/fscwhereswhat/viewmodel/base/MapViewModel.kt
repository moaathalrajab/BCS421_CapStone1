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
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Pinpoint
import io.gitlab.fsc_clam.fscwhereswhat.model.local.User
import kotlinx.coroutines.flow.StateFlow

/**
 * For Map View
 */
abstract class MapViewModel(application: Application) : AndroidViewModel(application) {
	/**
	 * If user has logged in, stores user data
	 */
	abstract val user: StateFlow<User?>

	/**
	 * When user types in search bar
	 */
	abstract val query: StateFlow<String?>

	/**
	 * The current active filter of map view - shows Building, Event, Node, or All
	 */
	abstract val activeFilter: StateFlow<EntityType?>

	/**
	 * Contains list of markers for entities
	 */
	abstract val pinpoints: StateFlow<List<Pinpoint>>

	/**
	 * The current longitude of user
	 */
	abstract val longitude: StateFlow<Double>

	/**
	 * The current latitude of user
	 */
	abstract val latitude: StateFlow<Double>

	/**
	 * If user clicks a pinpoint, that pinpoint is zoomed in to focus
	 */
	abstract val focus: StateFlow<Pinpoint?>

	/**
	 * Building color of pinpoints
	 */
	abstract val buildingColor: StateFlow<Int>

	/**
	 * Event color of pinpoints
	 */
	abstract val eventColor: StateFlow<Int>

	/**
	 * Node color of pinpoints
	 */
	abstract val nodeColor: StateFlow<Int>

	/**
	 * Sets active filter when user clicks filter button
	 */
	abstract fun setActiveFilter(filter: EntityType?)

	/**
	 * When user clicks pinpoint, zoom in to the point
	 */
	abstract fun setFocus(pinpoint: Pinpoint?)

}