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

@Dao
interface OSMWayTagDao {
	@Insert (onConflict = OnConflictStrategy.REPLACE)
	fun insert(vararg wayTag: DBOSMWayTag)

	@Delete
	fun delete(vararg wayTag: DBOSMWayTag)

	@Query("SELECT * FROM OSMWayTags")
	fun getAll() : List<DBOSMWayTag>

	/** Get way tag by id **/
	@Query("SELECT * FROM OSMWayTags WHERE id = :id")
	fun getById(id: Int) : DBOSMWayTag

	/** Get way tags by parent id **/
	@Query("SELECT * FROM OSMWayTags WHERE parentId = :id")
	fun getByParent(id: Int) : List<DBOSMWayTag>
}