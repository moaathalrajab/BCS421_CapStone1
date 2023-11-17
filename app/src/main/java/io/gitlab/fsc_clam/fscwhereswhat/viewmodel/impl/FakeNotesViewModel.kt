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

import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Image
import io.gitlab.fsc_clam.fscwhereswhat.model.local.NoteItem
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.NotesViewModel
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Fake ViewModel for NotesView
 */
class FakeNotesViewModel: NotesViewModel() {
	//private lateinit var repo: NoteRepo
	override val notes: MutableStateFlow<List<NoteItem>> = MutableStateFlow(arrayListOf(
		NoteItem(
			"", 3, EntityType.EVENT,
			Image.Drawable(3), ""
		)
	))

	override fun updateNote(note: NoteItem) {
		val noteList = ArrayList(notes.value)
		val pos = noteList.indexOf(note)
		noteList[pos] = note
		notes.value = noteList
	}

	override fun deleteNote(note: NoteItem) {
		val noteList = ArrayList(notes.value)
		noteList.remove(note)
		notes.value = noteList
	}
}