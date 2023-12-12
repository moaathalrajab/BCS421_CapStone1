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

import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import kotlinx.coroutines.flow.StateFlow

/**
 * Interface defining query source of truth
 */
interface QueryRepository {
	/**
	 * The current query being stored in the repository
	 */
	val query: StateFlow<String?>

	/**
	 * The current active filter on a query
	 */
	val activeFilter: StateFlow<EntityType?>

	/**
	 * Sets the query in the repository
	 *
	 * @param query is the string input from the user
	 */
	fun setQuery(query: String?)

	/**
	 * Sets an active filter to the repository
	 *
	 * @param filter is the entity type being filtered
	 */
	fun setActiveFilter(filter: EntityType?)

	/**
	 * Binding point for Implementation getter
	 */
	companion object
}