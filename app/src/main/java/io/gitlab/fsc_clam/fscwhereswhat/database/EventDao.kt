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
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBEvent

@Dao
interface EventDao {
	@Insert (onConflict = OnConflictStrategy.REPLACE)
	fun insert(vararg event: DBEvent)

	@Delete
	fun delete(vararg event: DBEvent)

	@Query("SELECT * FROM Events")
	fun getAll(): List<DBEvent>

	/** Get by Id **/
	@Query("SELECT * FROM Events WHERE id = :event")
	fun getById(event: Int) : DBEvent

	/** Get by location id **/
	@Query("SELECT * FROM Events WHERE locationId = :id")
	fun getByLocationId(id: Int) : List<DBEvent>
}