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
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Image
import io.gitlab.fsc_clam.fscwhereswhat.model.local.NoteItem
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.NotesViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * Fake ViewModel for NotesView
 */
class ImplNotesViewModel: NotesViewModel() {
	//private lateinit var repo: FakeNotesRepo
	override val notes: MutableStateFlow<List<NoteItem>> = MutableStateFlow(arrayListOf(
		NoteItem(
			"This is a comment", 3, EntityType.EVENT,
			Image.Drawable(3), "Event Name"
		)
	))

	override fun updateNote(note: NoteItem) {
		viewModelScope.launch {
			val noteList = ArrayList(notes.value)
			var pos = noteList.indexOfFirst {
				it.reference == note.reference
			}
			if(pos != -1) {
				noteList[pos] = note
				notes.value = noteList
			}
			//repo.updateNote(Note(note.comment, note.reference, note.type))
		}
	}

	override fun deleteNote(note: NoteItem) {
		viewModelScope.launch {
			val noteList = ArrayList(notes.value)
			noteList.remove(note)
			notes.value = noteList
			//repo.deleteNote(Note(note.comment, note.reference, note.type))
		}
	}
}