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

import androidx.room.*
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBOSMWayTag

/**
 * The dao for open street map way tags held in the room database
 * @property insert inserts a single way tag into the table
 * @property delete deletes a single way tag from the table
 * @property getAll returns a list of all stored way tags as DBOSMWayTags
 * @property getById returns a specific way tag using its id as a parameter
 * @property getByParent returns a specific way tag using its parentId (associated way) as a parameter
 */
@Dao
interface OSMWayTagDao {
	@Insert (onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(vararg wayTag: DBOSMWayTag)

	@Delete
	suspend fun delete(vararg wayTag: DBOSMWayTag)

	@Query("SELECT * FROM osm_way_tag")
	suspend fun getAll() : List<DBOSMWayTag>

	/** Get way tag by id **/
	@Query("SELECT * FROM osm_way_tag WHERE id = :id")
	suspend fun getById(id: Int) : DBOSMWayTag

	/** Get way tags by parent id **/
	@Query("SELECT * FROM osm_way_tag WHERE parentId = :id")
	suspend fun getByParent(id: Int) : List<DBOSMWayTag>
}