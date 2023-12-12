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
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBOSMBuildingOH
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBOSMNodeOH

/**
 * The dao for open street map node opening hours are stored
 */
@Dao
interface OSMBuildingOHDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(nodeTag: DBOSMNodeOH)

	@Update
	suspend fun update(nodeTag: DBOSMNodeOH)

	@Delete
	suspend fun delete(nodeTag: DBOSMNodeOH)

	/** get nodeTag by nodeId **/
	@Query("SELECT * FROM osm_way_oh WHERE parentId = :id")
	suspend fun getAllByParent(id: Long): List<DBOSMBuildingOH>
}