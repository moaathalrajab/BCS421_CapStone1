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

import io.gitlab.fsc_clam.fscwhereswhat.model.local.NoteItem
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.NoteRepo
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.NotesViewModel
import kotlinx.coroutines.flow.StateFlow

class ImplNotesViewModel: NotesViewModel() {
	private lateinit var repo: NoteRepo
	override val notes: StateFlow<List<NoteItem>>
		get() = TODO("Not yet implemented")

	override fun updateNote(note: NoteItem) {
		TODO("Not yet implemented")
	}

	override fun deleteNote(note: NoteItem) {
		TODO("Not yet implemented")
	}
}