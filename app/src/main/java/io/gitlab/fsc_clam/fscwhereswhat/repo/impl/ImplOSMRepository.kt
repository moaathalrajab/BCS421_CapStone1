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

import android.app.Application
import io.gitlab.fsc_clam.fscwhereswhat.datasource.base.OSMDataBaseDataSource
import io.gitlab.fsc_clam.fscwhereswhat.datasource.impl.ImplOSMDataBaseDataSource.Companion.get
import io.gitlab.fsc_clam.fscwhereswhat.model.local.OSMEntity
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Token
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.OSMRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ImplOSMRepository(application: Application) : OSMRepository {
	private val dataSource = OSMDataBaseDataSource.get(application)

	override suspend fun query(token: Token): Flow<List<OSMEntity>> {
		return dataSource.query(token)
	}

	override suspend fun queryNearby(latitude: Double, longitude: Double): Flow<List<OSMEntity>> {
		return dataSource.queryNearby(latitude, longitude)
	}

	override suspend fun get(id: Long): OSMEntity? {
		return withContext(Dispatchers.IO) {
			dataSource.get(id)
		}
	}

	override suspend fun update(entities: List<OSMEntity>) {
		withContext(Dispatchers.IO) {
			dataSource.update(entities)
		}
	}





	companion object {
		private var repo: ImplOSMRepository? = null

		/**
		 * Get the implementation of [OSMRepository]
		 */
		@Synchronized
		fun OSMRepository.Companion.get(
			application: Application
		): OSMRepository {
			if (repo == null) {
				repo = ImplOSMRepository(application)
			}

			return repo!!
		}
	}
}

