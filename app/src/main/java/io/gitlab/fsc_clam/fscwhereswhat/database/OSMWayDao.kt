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
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBOSMWay

@Dao
interface OSMWayDao {
	@Insert (onConflict = OnConflictStrategy.REPLACE)
	fun insert(vararg note: DBOSMWay)

	@Delete
	fun delete(vararg note: DBOSMWay)

	@Query("SELECT * FROM OSMWays")
	fun getAll() : List<DBOSMWay>

	/** Get way by id **/
	@Query("SELECT * FROM OSMWays WHERE id = :id")
	fun getById(id: Int) : DBOSMWay
}