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
import io.gitlab.fsc_clam.fscwhereswhat.R
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Image
import io.gitlab.fsc_clam.fscwhereswhat.model.local.NoteItem
import io.gitlab.fsc_clam.fscwhereswhat.model.local.OSMEntity
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.NoteRepository
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.OSMRepository
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.RamCentralRepository
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.FakeOSMRepo
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.ImplNoteRepository.Companion.get
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.ImplRamCentralRepository.Companion.get
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.NotesViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.net.URL

/**
 * Fake ViewModel for NotesView
 */
class ImplNotesViewModel(application: Application) : NotesViewModel(application) {
	private val repo = NoteRepository.get(application)
	private val osmRepo: OSMRepository = FakeOSMRepo()
	private val ramRepo = RamCentralRepository.get(application)

	override val notes = repo.getAllNotes().map { list ->
		list.map { note ->
			val refName: String
			val refImage: Image

			when (note.type) {
				EntityType.EVENT -> {
					val event = ramRepo.getEvent(note.reference)
					refName = event?.name ?: "Unknown event"
					if (event == null) {
						refImage = Image.Drawable(R.drawable.baseline_error_24)
					} else {
						refImage = Image.Asset(event.image)
					}
				}

				else -> {
					val entity = osmRepo.get(note.reference)

					refName = entity?.name ?: " Unknown location"

					if (entity == null) {
						refImage = Image.Drawable(R.drawable.baseline_error_24)
					} else {
						refImage = Image.Drawable(
							when (entity) {
								is OSMEntity.Building -> R.drawable.building_icon
								is OSMEntity.Node -> R.drawable.node_icon
							}
						)
					}
				}
			}

			NoteItem(
				note.comment,
				note.reference,
				note.type,
				refImage,
				refName
			)
		}
	}.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

	override fun updateNote(note: NoteItem) {
		viewModelScope.launch {
			repo.updateNote(Note(note.comment, note.reference, note.type))
		}
	}

	override fun deleteNote(note: NoteItem) {
		viewModelScope.launch {
			repo.deleteNote(Note(note.comment, note.reference, note.type))
		}
	}
}