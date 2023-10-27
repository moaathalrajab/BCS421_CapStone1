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

import androidx.lifecycle.ViewModel
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.NodeType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.OpeningHours
import io.gitlab.fsc_clam.fscwhereswhat.model.local.ReminderTime
import kotlinx.coroutines.flow.StateFlow
import java.net.URL

abstract class EntityDetailViewModel : ViewModel() {
	abstract val name: StateFlow<String>
	abstract val note: StateFlow<String>
	abstract val url: StateFlow<URL?>
	abstract val shareURL: StateFlow<URL>

	abstract val openingHours: StateFlow<OpeningHours?>
	abstract val description: StateFlow<String?>
	abstract val type: StateFlow<EntityType>
	abstract val nodeType: StateFlow<NodeType?>

	//Events
	abstract val image: StateFlow<URL?>
	abstract val instructions: StateFlow<String?>
	abstract val hasRsvp: StateFlow<Boolean>
	abstract val hasReminder: StateFlow<Boolean>
	abstract val reminderTime: StateFlow<ReminderTime?>

	abstract fun setReminderTime(time: ReminderTime)

	abstract fun setNote(note: String)

	abstract fun deleteReminder()

	abstract fun deleteNote()

}