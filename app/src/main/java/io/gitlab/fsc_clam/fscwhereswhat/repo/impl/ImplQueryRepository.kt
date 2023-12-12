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

package io.gitlab.fsc_clam.fscwhereswhat.repo.impl

import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.QueryRepository
import kotlinx.coroutines.flow.MutableStateFlow

class ImplQueryRepository : QueryRepository {
	override val query = MutableStateFlow(null as String?)
	override val activeFilter = MutableStateFlow(null as EntityType?)

	override fun setQuery(query: String?) {
		this.query.value = query
	}

	override fun setActiveFilter(filter: EntityType?) {
		activeFilter.value = filter
	}

	companion object {
		private var repo: ImplQueryRepository? = null

		/**
		 * Get the implementation of [QueryRepository]
		 */
		@Synchronized
		fun QueryRepository.Companion.get(
		): QueryRepository {
			if (repo == null) {
				repo = ImplQueryRepository()
			}

			return repo!!
		}
	}
}