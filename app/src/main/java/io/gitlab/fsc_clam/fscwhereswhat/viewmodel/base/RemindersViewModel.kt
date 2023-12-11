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

package io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.gitlab.fsc_clam.fscwhereswhat.model.local.ReminderItem
import io.gitlab.fsc_clam.fscwhereswhat.model.local.ReminderTime
import kotlinx.coroutines.flow.StateFlow

/**
 * For Reminders View
 */
abstract class RemindersViewModel(application: Application) : AndroidViewModel(application) {
	/**
	 * Holds list of ReminderItems which contain data on reminder for entity
	 * @see ReminderItem
	 */
	abstract val reminders: StateFlow<List<ReminderItem>>

	/**
	 * Deletes reminder from view
	 */
	abstract fun deleteReminder(reminderItem: ReminderItem)

	/**
	 * Updates ReminderTime for Event
	 * @param id is the event ID
	 */
	abstract fun updateReminderTime(id: Long, time: ReminderTime)


}