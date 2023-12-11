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
data class RamCentralDiscoveryEventSearchResult(
	val value: List<Event>
){
	/**
	 * Note, This class does not contain all fields that are provided by the API.
	 */
	@Serializable
	data class Event(
		val id: Long,
		val organizationId: Int,
		val organizationIds: List<Int>,
		val organizationName: String,
		val organizationProfilePicture: String,
		val organizationNames: List<String>,
		val name: String,
		val description: String,
		val location: String,
		val startsOn: String,
		val endsOn: String,
		val imagePath: String?,
		val latitude: String?,
		val longitude: String?,
	)
}
