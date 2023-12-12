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

import io.gitlab.fsc_clam.fscwhereswhat.model.local.NodeType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.OSMEntity
import io.gitlab.fsc_clam.fscwhereswhat.model.local.OpeningHours
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Token
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.OSMRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class FakeOSMRepo: OSMRepository {

	private val entities: MutableStateFlow<List<OSMEntity>> = MutableStateFlow(
		listOf<OSMEntity>(
			OSMEntity.Node(
				id =0,
				lat = 43.0,
				long = -73.0,
				name = "Example",
				description = "Description",
				hours = listOf(OpeningHours.everyDay),
				nodeType = NodeType.SOS
			)
		)
	)
	override suspend fun query(token: Token): Flow<List<OSMEntity>> {
		return entities
	}

	override suspend fun queryNearby(latitude: Double, longitude: Double): Flow<List<OSMEntity>> {
		return entities
	}

	override suspend fun get(id: Long): OSMEntity {
		return entities.value.find { it.id == id }?: throw NoSuchElementException("Entity with cannot be found with $id")
	}


	override suspend fun update(entities: List<OSMEntity>) {
		withContext(Dispatchers.IO){
			val entityList = ArrayList(this@FakeOSMRepo.entities.value)
			entityList.addAll(entityList.size - 1, entities)
			this@FakeOSMRepo.entities.value = entityList
		}
	}
}