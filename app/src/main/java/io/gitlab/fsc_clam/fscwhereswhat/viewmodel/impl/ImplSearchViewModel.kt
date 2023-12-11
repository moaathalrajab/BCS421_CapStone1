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

import android.app.Application
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityItem
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.OSMRepository
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.QueryRepository
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.RamCentralRepository
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.ImplQueryRepository.Companion.get
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.SearchViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Implementation of the SearchViewModel.
 *
 * It manages the user input query, active filter, and the list of entities for search results.
 *
 * @param application The application context.
 * @param QueryRepository The repository for managing queries and entities.
 */
class ImplSearchViewModel(
	application: Application,
	private val queryRepository: QueryRepository
) : SearchViewModel(application) {

	private val QueryRepo: QueryRepository = QueryRepository.get()
	private lateinit var OSMRepo: OSMRepository
	private lateinit var RamCentralRepo: RamCentralRepository

	/**
	 * Holds the user input from the search bar.
	 */
	private val _query = MutableStateFlow("")
	override val query: StateFlow<String>
		get() = _query

	/**
	 * Holds the active filter for EntityType to show results for Building, Event, Node, or All.
	 */
	private val _activeFilter = MutableStateFlow<EntityType?>(null)
	override val activeFilter: StateFlow<EntityType?>
		get() = _activeFilter

	/**
	 * Holds the list of entities to populate search results.
	 */
	private val _entities = MutableStateFlow<List<EntityItem>>(emptyList())
	override val entities: StateFlow<List<EntityItem>>
		get() = _entities

	/**
	 * Sets the user input query.
	 *
	 * @param query The string input from the user.
	 */
	override fun setQuery(query: String) {
		_query.value = query
	}

	/**
	 * Changes the active filter for EntityType.
	 *
	 * @param filter The entity type being filtered.
	 */
	override fun setActiveFilter(filter: EntityType?) {
		_activeFilter.value = filter // update the entities based on the new filter
		queryRepository.setActiveFilter(filter)
	}
}