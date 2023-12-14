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

package io.gitlab.fsc_clam.fscwhereswhat.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This is a RamCentral event
 * @param id is the id of the specific event
 * @param name is the name of the event on RamCentral
 * @param image is the image URL of the event from RamCentral
 * @param description is the description of the event on RamCentral
 * @param instructions if the event has requirements
 * @param locationName of where the event is located
 * @param locationId is the mapping between the event and OSM building
 * @param hasRSVP defines if the event requires an RSVP to attend
 * @param url is the link to the event on RamCentral
 */
@Entity(tableName = "event")
data class DBEvent(
	@PrimaryKey val id: Long,
	val name: String,
	val image: String,
	val description: String,
	val instructions: String?,
	val locationName: String,
	val locationId: Long,
	val hasRSVP: Boolean,
	val url: String,
	val startsOn: Long,
	val endsOn: Long
)