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
import io.gitlab.fsc_clam.fscwhereswhat.database.AppDatabase
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBEvent
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Event
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.RamCentralRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.net.URL

class ImplRamCentralRepository(
	application: Application
) : RamCentralRepository {
	private val database = AppDatabase.get(application).eventDao
	private fun Event.toDB(): DBEvent =
		DBEvent(
			id = id,
			name = name,
			image = image.toString(),
			description = description,
			instructions = instructions,
			locationName = locationName,
			locationId = locationId,
			hasRSVP = hasRSVP,
			url = url.toString()
		)

	private fun DBEvent.toModel(): Event =
		Event(
			id = id,
			name = name,
			image = URL(image),
			description = description,
			instructions = instructions,
			locationName = locationName,
			locationId = locationId,
			hasRSVP = hasRSVP,
			url = URL(url)
		)

	override suspend fun getAll(): Flow<List<Event>> =
		database.getAll()
			.map { list -> list.map { it.toModel() } }
			.flowOn(Dispatchers.IO)

	override suspend fun getEvent(id: Long): Event? =
		withContext(Dispatchers.IO) {
			database.getById(id)?.toModel()
		}

	override suspend fun addEvent(event: Event) {
		withContext(Dispatchers.IO) {
			database.insert(event.toDB())
		}
	}

	override suspend fun updateEvent(event: Event) {
		withContext(Dispatchers.IO) {
			database.update(event.toDB())
		}
	}

	override suspend fun deleteEvent(event: Event) {
		withContext(Dispatchers.IO) {
			database.delete(event.toDB())
		}
	}

	companion object {
		private var repo: ImplRamCentralRepository? = null

		@Synchronized
		fun RamCentralRepository.Companion.get(
			application: Application
		): RamCentralRepository {
			if (repo == null) {
				repo = ImplRamCentralRepository(application)
			}

			return repo!!
		}
	}
}