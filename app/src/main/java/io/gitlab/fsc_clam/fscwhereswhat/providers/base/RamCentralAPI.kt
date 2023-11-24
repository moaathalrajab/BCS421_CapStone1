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

package io.gitlab.fsc_clam.fscwhereswhat.providers.base

import io.gitlab.fsc_clam.fscwhereswhat.model.remote.RamCentralDiscoveryEvent
import io.gitlab.fsc_clam.fscwhereswhat.model.remote.RamCentralDiscoveryEventOrganization
import io.gitlab.fsc_clam.fscwhereswhat.model.remote.RamCentralDiscoveryEventSearchResult

/**
 * Reversed engineered API for RamCentral.
 *
 * These functions are assumed based on API calls the website makes at runtime.
 *
 * @see <a href="https://farmingdale.campuslabs.com">RamCentral</a>
 */
interface RamCentralAPI {

	/**
	 * Represents a query parameter.
	 *
	 * Child classes should provide a [query] representation of itself.
	 */
	interface QueryParameter {
		val query: String
	}

	/**
	 * Order by clause, unknown arguments.
	 */
	enum class OrderByField(override val query: String) : QueryParameter {
		/**
		 * Unknown purpose.
		 */
		ENDS_ON("endsOn")
	}

	/**
	 * orderByDirection clause, assuming existence of DESCENDING
	 */
	enum class OrderByDirection : QueryParameter {
		ASCENDING,
		DESCENDING;

		override val query: String = name.lowercase()
	}

	/**
	 * Status of event, Perhaps there is more then Approved?
	 */
	enum class Status(override val query: String) : QueryParameter {
		APPROVED("Approved")
	}

	/**
	 * Search for events.
	 *
	 * @param endsAfter Formatted as "YYYY-MM-DDTHH:MM:SS-05:00" with "-05:00" being the timezone (EST).
	 * @param orderByField What to order by
	 * @param orderByDirection In what direction
	 * @param take How many items to return
	 * @param query Query names
	 *
	 * @see "discovery/event/search?"
	 */
	suspend fun search(
		endsAfter: String,
		orderByField: OrderByField,
		orderByDirection: OrderByDirection,
		status: Status,
		take: Int,
		query: String
	): RamCentralDiscoveryEventSearchResult

	/**
	 * Get event details.
	 *
	 * Seems to provide us enough information.
	 *
	 * @see "discovery/event/[eventId]"
	 */
	suspend fun getEvent(eventId: Long): RamCentralDiscoveryEvent

	/**
	 * Get the organizations hosting a particular event.
	 *
	 * @see "discovery/event/[eventId]/organizations"
	 */
	suspend fun getOrganizationsForEvent(eventId: Long): List<RamCentralDiscoveryEventOrganization>

	/**
	 * Binding point for Implementation getter
	 */
	companion object
}