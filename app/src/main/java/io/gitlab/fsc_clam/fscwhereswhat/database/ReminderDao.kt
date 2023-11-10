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
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBReminder
import kotlinx.coroutines.flow.Flow

/**
 * The dao for user reminders stored in the room database
 * @property insert inserts a single reminder into the table
 * @property delete deletes a single reminder from the table
 * @property getAll returns a list of all reminders as DBReminders
 * @property getById returns a specific reminder using its id as a parameter
 * @property getAllFlow returns all reminders as flows, allowing the reminder view to react to changes
 */
@Dao
interface ReminderDao {
	@Insert (onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(vararg reminder: DBReminder)

	@Delete
	suspend fun delete(vararg reminder: DBReminder)

	@Query("SELECT * FROM reminder")
	suspend fun getAll(): List<DBReminder>

	/** Get reminder by id **/
	@Query("SELECT * FROM reminder WHERE eventId = :id")
	suspend fun getById(id: Int) : DBReminder

	/** Returns all reminders with Flow **/
	@Query("SELECT * FROM reminder")
	fun getAllFlow(): Flow<List<DBReminder>>
}