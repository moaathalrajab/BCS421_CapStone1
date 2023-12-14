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

import io.gitlab.fsc_clam.fscwhereswhat.model.local.Event
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Token
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining operations for interacting with events from Ram Central.
 */
interface RamCentralRepository {

	/**
	 * Get all events
	 */
	fun getAll(): Flow<List<Event>>

	/**
	 * Retrieves an event with the specified eventID.
	 *
	 * @param id The ID of the event to be retrieved.
	 * @return The event with the given ID.
	 */
	suspend fun getEvent(id: Long): Event?

	/**
	 * Adds a new event to the repository.
	 *
	 * @param event The event to be added.
	 */
	suspend fun addEvent(event: Event)

	/**
	 * Updates an existing event in the repository.
	 *
	 * @param event The event to be updated.
	 */
	suspend fun updateEvent(event: Event)

	/**
	 * Deletes an event from the repository.
	 *
	 * @param event The event to be deleted.
	 */
	suspend fun deleteEvent(event: Event)

	/**
	 * Searches for events matching a given token
	 *
	 * @param take how many events to return
	 */
	fun searchRemote(token: Token, take: Int = 10): Flow<List<Event>>

	/**
	 * Searches for events matching a given token
	 *
	 * @param take how many events to return
	 */
	fun search(token: Token, take: Int = 10): Flow<List<Event>>

	/**
	 * Binding point for Implementation getter
	 */
	companion object
}