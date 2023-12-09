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

package io.gitlab.fsc_clam.fscwhereswhat.repo.impl

import io.gitlab.fsc_clam.fscwhereswhat.model.local.Reminder
import io.gitlab.fsc_clam.fscwhereswhat.model.local.ReminderTime
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.ReminderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * A fake implementation of [ReminderRepository] for testing purposes.
 */
class FakeReminderRepo : ReminderRepository {

	// Internal state for storing reminders
	private val reminderState = MutableStateFlow(listOf<Reminder>(Reminder(0, ReminderTime.START)))

	/**
	 * Retrieves a reminder with the specified event ID from the fake repository.
	 *
	 * @param eventId The ID of the event associated with the reminder.
	 * @return A [Flow] emitting the reminder with the given event ID, or null if not found.
	 */
	override fun getReminder(eventId: Long): Flow<Reminder?> =
		reminderState.map { reminderList -> reminderList.find { it.eventId == eventId } } // eventId matches eventID of argument

	/**
	 * Retrieves all reminders stored in the fake repository.
	 *
	 * @return A [Flow] emitting a list of all reminders.
	 */
	override fun getAllReminders(): Flow<List<Reminder>> = reminderState

	/**
	 * Updates an existing reminder in the fake repository.
	 *
	 * @param reminder The reminder to be updated.
	 */
	override suspend fun updateReminder(reminder: Reminder) {
		withContext(Dispatchers.IO) {
			val reminderList = ArrayList(reminderState.value) // Casting to ArrayList
			val i = reminderList.indexOfFirst { it.eventId == reminder.eventId }
			if (i != -1) {
				reminderList[i] = reminder
				reminderState.value = reminderList
			}
		}
	}

	/**
	 * Deletes a reminder from the fake repository.
	 *
	 * @param reminder The reminder to be deleted.
	 */
	override suspend fun deleteReminder(reminder: Reminder) {
		withContext(Dispatchers.IO) {
			val reminderList = ArrayList(reminderState.value) // Casting to ArrayList
			reminderList.removeIf { it.eventId == reminder.eventId }
			reminderState.value = reminderList
		}
	}

	/**
	 * Creates a new reminder in the fake repository.
	 *
	 * @param reminder The reminder to be created.
	 */
	override suspend fun createReminder(reminder: Reminder) {
		withContext(Dispatchers.IO) {
			val reminderList = ArrayList(reminderState.value) // Casting to ArrayList
			reminderList.add(reminder)
			reminderState.value = reminderList
		}
	}
}

