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

package io.gitlab.fsc_clam.fscwhereswhat.model.remote

import kotlinx.serialization.Serializable

/**
 * Note, This class does not contain all fields that are provided by the API.
 */
@Serializable
data class RamCentralDiscoveryEvent(
	val id: Long,
	val organizationId: Long,
	val organizationIds: List<Long>,
	val imagePath: String,
	val imageUrl: String,
	val name: String,
	val description: String,
	val startsOn: String,
	val endsOn: String,
	val address: Address,
	val rsvpSettings: RSVPSettings
) {

	/**
	 * Note, This class does not contain all fields that are provided by the API.
	 */
	@Serializable
	data class Address(
		val locationId: Long,
		val name: String,
		val address: String?,
		val line1: String?,
		val line2: String?,
		val city: String?,
		val state: String?,
		val zip: String?,
		val latitude: String?,
		val longitude: String?,
		val onlineLocation: String?,
		val instructions: String?,
		val roomReservation: String,
		val provider: String?
	)


	/**
	 * Note, This class does not contain all fields that are provided by the API.
	 */
	@Serializable
	data class RSVPSettings(
		val isInviteOnly: Boolean,
		val totalAllowed: Int?,
		val shouldShowRemainingRsvps: Boolean,
		val shouldAllowGuests: Boolean,
		val totalGuestsAllowedPerRsvp: Int?,
		val shouldGuestsCountTowardsTotalAllowed: Boolean,
		val organizationRepresentationEnabled: Boolean,
		val organizationRepresentationRequired: Boolean,
		val totalRsvps: Int,
		val totalGuests: Int,
		val spotsAvailable: Int?,
//		val questions: String?
	)
}
