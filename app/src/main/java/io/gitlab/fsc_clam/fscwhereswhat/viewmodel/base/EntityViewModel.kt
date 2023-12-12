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
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.OpeningHours
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Reminder
import kotlinx.coroutines.flow.StateFlow

abstract class EntityViewModel(application: Application) : AndroidViewModel(application) {
	abstract fun updaterNote(newNote: String)

	abstract val notes: StateFlow<String?>
	abstract val reminder: StateFlow<Reminder?>

	abstract val type: StateFlow<EntityType?>
	abstract val name: StateFlow<String>
	abstract val oh: StateFlow<List<OpeningHours>>
	abstract val hasRSVP: StateFlow<Boolean>
	abstract val url: StateFlow<String?>
	abstract val imageURL: StateFlow<String?>
}