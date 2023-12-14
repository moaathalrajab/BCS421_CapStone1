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

import android.content.Context
import io.gitlab.fsc_clam.fscwhereswhat.database.AppDatabase
import io.gitlab.fsc_clam.fscwhereswhat.datasource.base.OSMDataBaseDataSource
import io.gitlab.fsc_clam.fscwhereswhat.datasource.impl.ImplOSMDataBaseDataSource.Companion.get
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBEvent
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Event
import io.gitlab.fsc_clam.fscwhereswhat.model.local.OSMEntity
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Token
import io.gitlab.fsc_clam.fscwhereswhat.model.remote.RamCentralDiscoveryEventSearchResult
import io.gitlab.fsc_clam.fscwhereswhat.providers.base.RamCentralAPI
import io.gitlab.fsc_clam.fscwhereswhat.providers.base.RamCentralAPI.Companion.dateTimeFormat
import io.gitlab.fsc_clam.fscwhereswhat.providers.impl.OkHttpRamCentralAPI
import io.gitlab.fsc_clam.fscwhereswhat.providers.okHttpClient
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.RamCentralRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.net.URL
import java.util.Date

class ImplRamCentralRepository(
	context: Context
) : RamCentralRepository {
	private val api: RamCentralAPI = OkHttpRamCentralAPI(okHttpClient)

	private val database = AppDatabase.get(context).eventDao

	// TODO implementation bind
	private val osmDataSource = OSMDataBaseDataSource.get(context)

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
			url = url.toString(),
			startsOn = startsOn,
			endsOn = endsOn,
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
			url = URL(url),
			startsOn = startsOn,
			endsOn = endsOn

		)

	override fun getAll(): Flow<List<Event>> =
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

	override suspend fun search(token: Token, take: Int): Flow<List<Event>> = flow {
		emit(emptyList()) // start empty

		// Perform a search
		val result = api.search(
			RamCentralAPI.dateTimeFormat.format(Date()),
			RamCentralAPI.OrderByField.ENDS_ON,
			RamCentralAPI.OrderByDirection.ASCENDING,
			RamCentralAPI.Status.APPROVED,
			take,
			token.strings.joinToString(" ")
		)

		// Loops through the search results and ensures they are in the local database
		result.value.forEach { remoteEvent ->
			val fullEvent = api.getEvent(remoteEvent.id) // Required for RSVP boolean

			val localEvent = getEvent(remoteEvent.id)

			val osm = findOSM(remoteEvent) // Query for OSM

			if (localEvent != null) {
				val updatedLocal = localEvent.copy(
					name = remoteEvent.name,
					image = URL(remoteEvent.imagePath ?: remoteEvent.organizationProfilePicture),
					description = remoteEvent.description,
					instructions = "", // TODO Instructions
					locationName = osm?.name ?: remoteEvent.name,
					locationId = osm?.id ?: 0L,
					hasRSVP = fullEvent.rsvpSettings?.shouldAllowGuests?:false, // TODO verify if this is right? Since there doesn't seem to be a field specifically for rsvp??
					url = URL(eventBaseURL + remoteEvent.id),
					startsOn = dateTimeFormat.parse(remoteEvent.startsOn).time,
					endsOn = dateTimeFormat.parse(remoteEvent.endsOn).time
				)

				updateEvent(updatedLocal)
			} else {
				addEvent(
					Event(
						id = remoteEvent.id,
						name = remoteEvent.name,
						image = URL("https://farmingdale.campuslabs.com/engage/image/" +
								(remoteEvent.imagePath ?: remoteEvent.organizationProfilePicture)
						),
						description = remoteEvent.description,
						instructions = "", // TODO Instructions
						locationName = osm?.name ?: remoteEvent.name,
						locationId = osm?.id ?: 0L,
						hasRSVP = fullEvent.rsvpSettings?.shouldAllowGuests?:false, // TODO verify if this is right? Since there doesn't seem to be a field specifically for rsvp??
						url = URL(eventBaseURL + remoteEvent.id),
						startsOn = dateTimeFormat.parse(remoteEvent.startsOn).time,
						endsOn = dateTimeFormat.parse(remoteEvent.endsOn).time
					)
				)
			}
		}

		emitAll(
			database.getAll(
				result.value.map { it.id }
			).map { list -> list.map { it.toModel() } } // Convert to model flow
		)
	}.combine(
		database.getAll().map { list ->
			list.filter { event ->
				token.strings.any {
					event.description.contains(it)
				}
			}
		}.map { list -> list.map { it.toModel() } } // Convert to model flow
	) { a, b -> a + b }
		.map { it.distinctBy { list -> list.id } } // Ensure no duplicates
		.flowOn(Dispatchers.IO)

	private suspend fun findOSM(remoteEvent: RamCentralDiscoveryEventSearchResult.Event): OSMEntity? {
		return if (remoteEvent.latitude != null && remoteEvent.longitude != null) {
			osmDataSource.getNear(remoteEvent.latitude, remoteEvent.longitude)
		} else {
			if (remoteEvent.name == "Student Government Association’s: Snack, Study and Chill")
			{
				osmDataSource.getLikeName("Delores Quintyne Hall")
			}

			osmDataSource.getLikeName(remoteEvent.location).firstOrNull()
		}
	}

	companion object {
		private const val eventBaseURL = "https://farmingdale.campuslabs.com/engage/event/"
		private var repo: ImplRamCentralRepository? = null

		@Synchronized
		fun RamCentralRepository.Companion.get(
			context: Context
		): RamCentralRepository {
			if (repo == null) {
				repo = ImplRamCentralRepository(context)
			}

			return repo!!
		}
	}
}