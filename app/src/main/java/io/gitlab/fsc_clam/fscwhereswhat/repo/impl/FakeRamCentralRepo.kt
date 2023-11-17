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
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.RamCentralRepo
import java.net.URL

class FakeRamCentralRepo : RamCentralRepo {
	private val events: MutableMap<Int, Event> = mutableMapOf()

	override suspend fun getEvent(id: Int): Event {
		return events[id] ?: throw NoSuchElementException("Event with id $id not found")
	}

	override suspend fun addEvent(event: Event) {
		events[event.id] = event
	}

	override suspend fun updateEvent(event: Event) {
		if (events.containsKey(event.id)) {
			events[event.id] = event
		} else {
			throw NoSuchElementException("Event with id ${event.id} not found")
		}
	}

	override suspend fun deleteEvent(event: Event) {
		events.remove(event.id)
	}
}
