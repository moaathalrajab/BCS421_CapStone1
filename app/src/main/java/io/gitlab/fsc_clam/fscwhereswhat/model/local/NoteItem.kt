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

package io.gitlab.fsc_clam.fscwhereswhat.model.local

import java.net.URL

/**
 * UI Item for notes
 * @param comment for the note
 * @param reference the parent Id
 * @param type what entity type note is attached to
 * @param image image to display
 * @param referenceName the parent's name
 */
data class NoteItem(
	val comment: String,
	val reference: Int,
	val type: EntityType,
	val image: Image,
	val referenceName: String
)
