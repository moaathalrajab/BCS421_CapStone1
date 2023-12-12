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

package io.gitlab.fsc_clam.fscwhereswhat.repo.base

import io.gitlab.fsc_clam.fscwhereswhat.model.local.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
	/**
	 * Get an note by it's parent ID
	 *
	 * @param parentId is the id of the event/utility that the note is attached to
	 * @return a [Flow] returning the note with the given parentId, or null if none found
	 */
	fun getNote(parentId: Long): Flow<Note?>

	/**
	 * Get all notes stored in the repository
	 *
	 * @return a [Flow] of all notes
	 */
	fun getAllNotes(): Flow<List<Note>>

	/**
	 * Updates an existing note in the repository
	 *
	 * @note the note to be updated
	 */
	suspend fun updateNote(note: Note)

	/**
	 * Deletes a note in the repository
	 *
	 * @note the note to be deleted
	 */
	suspend fun deleteNote(note: Note)

	/**
	 * Creates a note and stores it in the repository
	 *
	 * @note the note to be created
	 */
	suspend fun createNote(note: Note)

	/**
	 * Binding point for Implementation getter
	 */
	companion object
}