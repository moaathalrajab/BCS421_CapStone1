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

import androidx.room.*
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType

/**
 * This is a note for a given entity
 * @param comment is the String comment inputted by the user
 * @param reference is the id of the associated entity
 * @param type defines the type of entity that the reference is
 */
@Entity(tableName = "note")
data class DBNote(
	val comment: String,
	@PrimaryKey val reference: Int,
	val type: EntityType
)