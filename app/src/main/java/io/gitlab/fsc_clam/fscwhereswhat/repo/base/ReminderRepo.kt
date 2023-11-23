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

package io.gitlab.fsc_clam.fscwhereswhat.repo.base

import io.gitlab.fsc_clam.fscwhereswhat.model.local.Reminder
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining operations for interacting with reminders in a repository.
 */
interface ReminderRepo {

	/**
	 * Retrieves a reminder by the event ID.
	 *
	 * @param eventId The ID of the event associated with the reminder.
	 * @return A [Flow] emitting the reminder with the given event ID, or null if not found.
	 */
	fun getReminder(eventId: Int): Flow<Reminder?>

	/**
	 * Retrieves all reminders stored in the repository.
	 *
	 * @return A [Flow] emitting a list of all reminders.
	 */
	fun getAllReminders(): Flow<List<Reminder>>

	/**
	 * Updates an existing reminder in the repository.
	 *
	 * @param reminder The reminder to be updated.
	 */
	suspend fun updateReminder(reminder: Reminder)

	/**
	 * Deletes a reminder from the repository.
	 *
	 * @param reminder The reminder to be deleted.
	 */
	suspend fun deleteReminder(reminder: Reminder)

	/**
	 * Creates a new reminder in the repository.
	 *
	 * @param reminder The reminder to be created.
	 */
	suspend fun createReminder(reminder: Reminder)
}