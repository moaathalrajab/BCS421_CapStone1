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
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBOSMNode

/**
 * The dao for open street map nodes held in the room database
 * @property insert inserts a single node into the table
 * @property delete deletes a single node from the table
 * @property getAll returns a list of all nodes as DBOSMNodes
 * @property getById returns a specific node using its id as a parameter
 */
@Dao
interface OSMNodeDao {
	@Insert (onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(node: DBOSMNode)

	@Update
	suspend fun update(node: DBOSMNode)

	@Delete
	suspend fun delete(node: DBOSMNode)

	@Query("SELECT * FROM osm_node")
	suspend fun getAll() : List<DBOSMNode>

	/** Get by Id **/
	@Query("SELECT * FROM osm_node WHERE id = :node")
	suspend fun getById(node: Long) : DBOSMNode
}