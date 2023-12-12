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

import android.app.Application
import androidx.lifecycle.viewModelScope
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Note
import io.gitlab.fsc_clam.fscwhereswhat.model.local.OpeningHours
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.MapViewFocusRepository
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.NoteRepository
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.OSMRepository
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.RamCentralRepository
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.ReminderRepository
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.ImplMapViewFocusRepository.Companion.get
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.ImplNoteRepository.Companion.get
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.ImplOSMRepository.Companion.get
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.ImplRamCentralRepository.Companion.get
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.ImplReminderRepository.Companion.get
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.EntityViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

class ImplEntityViewModel(application: Application) : EntityViewModel(application) {
	private val focusRepo = MapViewFocusRepository.get()
	private val osmRepo = OSMRepository.get(application)
	private val ramRepo = RamCentralRepository.get(application)
	private val notesRepo = NoteRepository.get(application)
	private val reminderRepo = ReminderRepository.get(application)

	val focus = focusRepo.focus
		.stateIn(viewModelScope, SharingStarted.Eagerly, null)

	private val note = focus.filterNotNull().transform { pin ->
		emitAll(notesRepo.getNote(pin.id))
	}.stateIn(viewModelScope, SharingStarted.Eagerly, null)

	override fun updaterNote(newNote: String) {
		viewModelScope.launch {
			val note = note.value?.copy(comment = newNote)

			if (note == null)
				notesRepo.createNote(Note(newNote, focus.value!!.id, focus.value!!.type))
			else {
				notesRepo.updateNote(note)
			}
		}
	}

	override val notes: StateFlow<String?> = note.map { it?.comment }
		.stateIn(viewModelScope, SharingStarted.Eagerly, null)

	override
	val reminder = focus.filterNotNull().transform { pin ->
		if (pin.type != EntityType.EVENT) {
			emit(null)
			return@transform
		}

		emitAll(reminderRepo.getReminder(pin.id))
	}.stateIn(viewModelScope, SharingStarted.Eagerly, null)

	override val type = focus.filterNotNull().map { it.type }
		.stateIn(viewModelScope, SharingStarted.Eagerly, null)
	override val name = MutableStateFlow("")
	override val oh = MutableStateFlow<List<OpeningHours>>(emptyList())
	override val hasRSVP = MutableStateFlow(false)
	override val url = MutableStateFlow<String?>(null)
	override val imageURL = MutableStateFlow<String?>(null)

	init {
		viewModelScope.launch {
			focus.filterNotNull().collect { focus ->
				when (focus.type) {
					EntityType.EVENT -> {
						val event = ramRepo.getEvent(focus.id)!!
						name.emit(event.name)
						hasRSVP.emit(event.hasRSVP)
						url.emit(event.url.toString())
						imageURL.emit(event.image.toString())
					}

					else -> {
						val entity = osmRepo.get(focus.id)!!
						name.emit(entity.name)
						oh.emit(entity.hours)
						url.emit(entity.description)
					}
				}
			}
		}
	}
}