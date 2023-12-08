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

import io.gitlab.fsc_clam.fscwhereswhat.model.local.Event
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.RamCentralRepository
import java.net.URL

/**
 * A fake implementation of [RamCentralRepository] for testing purposes.
 */
class FakeRamCentralRepository : RamCentralRepository {

	//Internal storage for events
	private val events: MutableMap<Int, Event> = mutableMapOf(
		0 to Event(
			0,
			"Basketball",
			URL("https://tse1.mm.bing.net/th?id=OIP.OZQP0Ud2cFFmyo6yphrd1QAAAA&pid=Api"),
			"Basketball Competition",
			"Just bring yourself and appropriate clothes!",
			"Nold hall",
			10,
			false,
			URL("https://tse1.mm.bing.net/th?id=OIP.OZQP0Ud2cFFmyo6yphrd1QAAAA&pid=Api")
		)
	)

	/**
	 * Retrieves an event with the specified ID from the fake repository.
	 *
	 * @param id The ID of the event to be retrieved.
	 * @return The event with the given ID.
	 * @throws NoSuchElementException if no event with the specified ID is found.
	 */
	override suspend fun getEvent(id: Int): Event {
		return events[id] ?: throw NoSuchElementException("Event with id $id not found")
	}

	/**
	 * Adds a new event to the fake repository.
	 *
	 * @param event The event to be added.
	 */
	override suspend fun addEvent(event: Event) {
		events[event.id] = event
	}

	/**
	 * Updates an existing event in the fake repository.
	 *
	 * @param event The event to be updated.
	 */
	override suspend fun updateEvent(event: Event) = addEvent(event)

	/**
	 * Deletes an event from the fake repository.
	 *
	 * @param event The event to be deleted.
	 */
	override suspend fun deleteEvent(event: Event) {
		events.remove(event.id)
	}
}
