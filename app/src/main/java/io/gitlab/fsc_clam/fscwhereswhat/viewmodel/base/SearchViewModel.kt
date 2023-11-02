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
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityItem
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import kotlinx.coroutines.flow.StateFlow

/**
 * For the Search View
 */
abstract class SearchViewModel : ViewModel() {
	/**
	 * Holds user input from search bar
	 */
	abstract val query: StateFlow<String>

	/**
	 * Active filter for EntityType to show results for Building, Event, Node, or All
	 */
	abstract val activeFilter: StateFlow<EntityType?>

	/**
	 * List of entities to populate search results
	 */
	abstract val entities: StateFlow<List<EntityItem>>

	/**
	 * Sets query
	 */
	abstract fun setQuery(query: String)

	/**
	 * Changes active filter
	 */
	abstract fun setActiveFilter(filter: EntityType?)


}