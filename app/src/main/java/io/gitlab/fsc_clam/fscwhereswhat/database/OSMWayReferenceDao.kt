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

package io.gitlab.fsc_clam.fscwhereswhat.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBOSMWayReference

/**
 * The dao for open street map way references stored in the room database
 * @property insert inserts a single way reference into the table
 * @property delete deletes a single way reference from the table
 * @property getAll returns a list of all way references as DBOSMWayReferences
 * @property getById returns a specific way reference using its id as a parameter
 */
@Dao
interface OSMWayReferenceDao {
	@Insert (onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(ref: DBOSMWayReference)

	@Update
	suspend fun update(ref: DBOSMWayReference)

	@Delete
	suspend fun delete(ref: DBOSMWayReference)

	@Query ("SELECT * FROM osm_way_reference")
	suspend fun getAll() : List<DBOSMWayReference>

	/** Get by id **/
	@Query("SELECT * FROM osm_way_reference WHERE id = :id")
	suspend fun getById(id: Long) : List<DBOSMWayReference>
}