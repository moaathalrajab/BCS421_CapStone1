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
import kotlinx.coroutines.flow.Flow

/**
 * The dao for events from RamCentral held in the room database
 * @property insert inserts a single event into the table
 * @property delete deletes a single event from the table
 * @property getAll returns a list of all events as DBEvents
 * @property getById returns a specific event using its id as a parameter
 * @property getByLocationId returns a list of all events associated with the locationId parameter
 */

@Dao
interface EventDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(event: DBEvent)

	@Update
	suspend fun update(event: DBEvent)

	@Delete
	suspend fun delete(event: DBEvent)

	@Query("SELECT * FROM event")
	suspend fun getAll(): Flow<List<DBEvent>>

	/** Get specific item by Id **/
	@Query("SELECT * FROM event WHERE id = :event")
	suspend fun getById(event: Long): DBEvent?

	/** Get a list of events by location id **/
	@Query("SELECT * FROM event WHERE locationId = :id")
	suspend fun getByLocationId(id: Int): List<DBEvent>
}