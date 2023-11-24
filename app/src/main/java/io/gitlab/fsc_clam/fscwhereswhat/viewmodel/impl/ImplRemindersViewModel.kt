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

package io.gitlab.fsc_clam.fscwhereswhat.viewmodel.impl

import androidx.lifecycle.viewModelScope
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Reminder
import io.gitlab.fsc_clam.fscwhereswhat.model.local.ReminderItem
import io.gitlab.fsc_clam.fscwhereswhat.model.local.ReminderTime
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.RamCentralRepo
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.ReminderRepo
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.FakeRamCentralRepo
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.FakeReminderRepo
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.RemindersViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.net.URL

/**
 * Implementation of the [RemindersViewModel] interface that handles interactions
 * between the UI and the data layer for reminders.
 *
 * @param repo The repository for handling reminders.
 * @param ramCentralRepo The repository for handling central data (e.g., events).
 */
class ImplRemindersViewModel(
	private val repo: ReminderRepo = FakeReminderRepo(),
	private val ramCentralRepo: RamCentralRepo = FakeRamCentralRepo()
) : RemindersViewModel() {

	/**
	 * A [StateFlow] emitting a list of [ReminderItem]s derived from reminders in the repository.
	 */
	override val reminders: StateFlow<List<ReminderItem>> =
		repo.getAllReminders().map { reminderItems ->
			reminderItems.map { reminder ->
				val event = ramCentralRepo.getEvent(reminder.eventId)
				ReminderItem(
					eventId = event.id,
					eventName = event.name,
					imageURL = URL("https://example.com/image.jpg"), // Example
					remind = reminder.remind,
					date = "Event date example"
				)
			}
		}.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

	/**
	 * Deletes a reminder from the repository.
	 *
	 * @param reminderItem The [ReminderItem] representing the reminder to be deleted.
	 */
	override fun deleteReminder(reminderItem: ReminderItem) {
		viewModelScope.launch {
			repo.deleteReminder(
				Reminder(
					eventId = reminderItem.eventId,
					remind = reminderItem.remind
				)
			)
		}
	}

	/**
	 * Updates the reminder time in the repository.
	 *
	 * @param id The ID of the reminder to be updated.
	 * @param time The new [ReminderTime] for the reminder.
	 */
	override fun updateReminderTime(id: Int, time: ReminderTime) {
		viewModelScope.launch {
			repo.updateReminder(Reminder(id, time))
		}
	}
}



