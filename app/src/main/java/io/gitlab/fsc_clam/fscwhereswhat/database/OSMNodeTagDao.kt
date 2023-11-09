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
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBOSMNodeTag

/**
 * The dao for open street map node tags stored in the room database
 * @property insert inserts a single tag into the table
 * @property delete deletes a single tag from the table
 * @property getAllByNode returns a list of all tags as DBOSMNodeTags
 * @property getById returns a specific tag using its id as a parameter
 */
@Dao
interface OSMNodeTagDao {
	@Insert (onConflict = OnConflictStrategy.REPLACE)
	fun insert(vararg nodeTag: DBOSMNodeTag)

	@Delete
	fun delete(vararg nodeTag: DBOSMNodeTag)

	/** get nodeTag by nodeId **/
	@Query("SELECT * FROM osm_node_tag WHERE nodeId = :id")
	fun getAllByNode(id: Int) : List<DBOSMNodeTag>

	/** Get nodeTag by id **/
	@Query("SELECT * FROM osm_node_tag WHERE id = :id")
	fun getById(id: Int) : DBOSMNodeTag
}