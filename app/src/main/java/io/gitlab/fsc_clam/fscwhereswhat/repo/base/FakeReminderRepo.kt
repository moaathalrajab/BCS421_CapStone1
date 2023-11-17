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
import io.gitlab.fsc_clam.fscwhereswhat.model.local.ReminderTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

// ALL EMITS ARE EXAMPLES

class FakeReminderRepo : ReminderRepo {

	private val reminderState =
		MutableStateFlow(listOf<Reminder>(Reminder(0, ReminderTime.START)))

	override fun getReminder(): Flow<Reminder> = flow {
		emit(Reminder(0, ReminderTime.START)) // DEFAULT
	}

	override fun getAllReminders(): Flow<List<Reminder>> = reminderState

	override suspend fun updateReminder(reminder: Reminder) {
		withContext(Dispatchers.IO) {
			val reminderList = ArrayList(reminderState.value) // Casting to ArrayList
			val i = reminderList.indexOfFirst { it.eventId == reminder.eventId }
			if (i != -1) {
				reminderList[i] = reminder
			}
			reminderState.value = reminderList
		}
	}

	override suspend fun deleteReminder(reminder: Reminder) {
		withContext(Dispatchers.IO) {
			val reminderList = ArrayList(reminderState.value) // Casting to ArrayList
			reminderList.removeIf { it.eventId == reminder.eventId }
			reminderState.value = reminderList
		}
	}

	override suspend fun createReminder(reminder: Reminder) {
		withContext(Dispatchers.IO) {
			val reminderList = ArrayList(reminderState.value) // Casting to ArrayList
			reminderList.add(reminder)
			reminderState.value = reminderList
		}
	}
}

