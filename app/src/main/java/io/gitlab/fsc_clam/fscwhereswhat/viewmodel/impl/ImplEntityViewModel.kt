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
import android.util.Log
import androidx.lifecycle.viewModelScope
import io.gitlab.fsc_clam.fscwhereswhat.R
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Image
import io.gitlab.fsc_clam.fscwhereswhat.model.local.NodeType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Note
import io.gitlab.fsc_clam.fscwhereswhat.model.local.OSMEntity
import io.gitlab.fsc_clam.fscwhereswhat.model.local.OpeningHours
import io.gitlab.fsc_clam.fscwhereswhat.model.local.ReminderTime
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
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.EntityDetailViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class ImplEntityViewModel(application: Application) : EntityDetailViewModel(application) {
	private val focusRepo = MapViewFocusRepository.get()
	private val osmRepo = OSMRepository.get(application)
	private val ramRepo = RamCentralRepository.get(application)
	private val notesRepo = NoteRepository.get(application)
	private val reminderRepo = ReminderRepository.get(application)

	val focus = focusRepo.focus

	private val _note = focus.transformLatest { pin ->
		Log.d("ImplEntityViewModel", "Rebuilding note")
		if (pin != null) {
			val noteFlow = notesRepo.getNote(pin.id)

			// If missing note, add a note
			if (noteFlow.first() == null) {
				notesRepo.createNote(Note("", focus.value!!.id, focus.value!!.type))
			}

			emitAll(noteFlow)
		}
	}.stateIn(viewModelScope, SharingStarted.Eagerly, null)

	override val note: StateFlow<String?> = _note.map { it?.comment }
		.stateIn(viewModelScope, SharingStarted.Eagerly, null)

	override suspend fun setNote(newNote: String) {
		Log.d("VM", "setNote")
		withContext(viewModelScope.coroutineContext) {
			val note = _note.value?.copy(comment = newNote)
			if (note != null) {
				notesRepo.updateNote(note)
			}
		}
	}

	override fun deleteReminder() {
		TODO("Not yet implemented")
	}

	override fun deleteNote() {
		TODO("Not yet implemented")
	}

	private val reminder = focus.transformLatest { pin ->
		if (pin?.type != EntityType.EVENT) {
			emit(null)
			return@transformLatest
		}

		emitAll(reminderRepo.getReminder(pin.id))
	}.stateIn(viewModelScope, SharingStarted.Eagerly, null)

	override val reminderTime = reminder.map { it?.remind }
		.stateIn(viewModelScope, SharingStarted.Eagerly, null)

	override fun setReminderTime(time: ReminderTime) {
		TODO("Not yet implemented")
	}

	override val type = focus.map { it?.type }
		.stateIn(viewModelScope, SharingStarted.Eagerly, null)
	override val nodeType: StateFlow<NodeType?>
		get() = TODO("Not yet implemented")

	override val instructions: StateFlow<String?>
		get() = TODO("Not yet implemented")

	override val name = MutableStateFlow("")

	override val openingHours = MutableStateFlow<List<OpeningHours>>(emptyList())

	override val description: StateFlow<String?>
		get() = TODO("Not yet implemented")

	override val hasRSVP = MutableStateFlow(false)

	override val hasReminder: StateFlow<Boolean> = reminder.map { it != null }
		.stateIn(viewModelScope, SharingStarted.Eagerly, false)

	override val url = MutableStateFlow<URL?>(null)

	override val image = MutableStateFlow<Image>(
		Image.Drawable(R.drawable.baseline_error_24)
	)
	override val shareURL: StateFlow<URL>
		get() = TODO("Not yet implemented")

	init {
		viewModelScope.launch {
			focus.filterNotNull().collect { focus ->
				when (focus.type) {
					EntityType.EVENT -> {
						val event = ramRepo.getEvent(focus.id)!!
						name.emit(event.name)
						hasRSVP.emit(event.hasRSVP)
						url.emit(event.url)
						image.emit(
							Image.Asset(event.image)
						)
					}

					else -> {
						val entity = osmRepo.get(focus.id)!!
						name.emit(entity.name)
						openingHours.emit(entity.hours)
						image.emit(
							Image.Drawable(
								when (entity) {
									is OSMEntity.Building -> R.drawable.building_icon
									is OSMEntity.Node -> R.drawable.node
								}
							)
						)
					}
				}
			}
		}
	}
}