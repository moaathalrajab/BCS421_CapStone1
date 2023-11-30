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

package io.gitlab.fsc_clam.fscwhereswhat.repo.impl

import android.app.Application
import io.gitlab.fsc_clam.fscwhereswhat.database.AppDatabase
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBNote
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Note
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ImplNoteRepository(application: Application) : NoteRepository {
	private val db = AppDatabase.get(application).noteDao

	override fun getNote(parentId: Int): Flow<Note?> =
		db.getById(parentId)
			.map { it?.toModel() }
			.flowOn(Dispatchers.IO)

	override fun getAllNotes(): Flow<List<Note>> =
		db.getAllFlow()
			.map { dbNotes -> dbNotes.map { it.toModel() } }
			.flowOn(Dispatchers.IO)

	override suspend fun updateNote(note: Note) =
		withContext(Dispatchers.IO) {
			db.update(note.toDB())
		}

	override suspend fun deleteNote(note: Note) =
		withContext(Dispatchers.IO) {
			db.delete(note.toDB())
		}

	override suspend fun createNote(note: Note) =
		withContext(Dispatchers.IO) {
			db.insert(note.toDB())
		}

	/**
	 * Convert model to db
	 */
	private fun Note.toDB() = DBNote(comment, reference, type)

	/**
	 * Convert db to model
	 */
	private fun DBNote.toModel() = Note(comment, reference, type)

	companion object {
		private var repo: ImplNoteRepository? = null

		/**
		 * Get the implementation of [NoteRepository]
		 */
		@Synchronized
		fun NoteRepository.Companion.get(
			application: Application
		): NoteRepository {
			if (repo == null) {
				repo = ImplNoteRepository(application)
			}

			return repo!!
		}
	}
}