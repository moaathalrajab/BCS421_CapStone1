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

package io.gitlab.fsc_clam.fscwhereswhat.ui.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.gitlab.fsc_clam.fscwhereswhat.R
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Image
import io.gitlab.fsc_clam.fscwhereswhat.model.local.NoteItem
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.NotesViewModel
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.impl.ImplNotesViewModel

/**
 * The overall View of Notes, including ViewModel and NotesContent
 */
@Composable
fun NotesView(
	onBack: () -> Unit
) {
	val notesViewModel: NotesViewModel = viewModel<ImplNotesViewModel>()
	val notes by notesViewModel.notes.collectAsState()
	NotesContent(
		notes = notes,
		onUpdate = notesViewModel::updateNote,
		onDelete = notesViewModel::deleteNote,
		onBack = onBack
	)
}

@Preview()
@Composable
fun PreviewNotesView() {
	NotesView(
		onBack = {}
	)
}

/**
 * Creates the UI for the Notes View
 * @param notes    is the list of NoteItems
 * @param onUpdate the function in the NotesViewModel that updates the note
 * @param onDelete the function in the NotesViewModel that deletes the note
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesContent(
	notes: List<NoteItem>,
	onUpdate: (NoteItem) -> Unit,
	onDelete: (NoteItem) -> Unit,
	onBack: () -> Unit
) {
	//For the top app bar
	Scaffold(topBar = {
		TopAppBar(
			title = {
				Text(
					text = stringResource(id = R.string.notesHeading),
					style = MaterialTheme.typography.headlineLarge
				)
			},
			navigationIcon = {
				IconButton(onBack) {
					Icon(Icons.Default.ArrowBack, stringResource(R.string.nav_back))
				}
			}
		)
	}) {
		//Lists all current notes
		LazyColumn(
			Modifier
				.padding(it)
				.fillMaxWidth(),
			contentPadding = PaddingValues(16.dp),
			verticalArrangement = Arrangement.spacedBy(8.dp)
		) {
			//for each note, create its card
			items(notes) { note ->
				NotesCard(note = note, onUpdate = onUpdate, onDelete = onDelete)
			}
		}
	}


}

@Preview
@Composable
fun PreviewNotesContent() {
	val img = Image.Drawable(R.drawable.flag_icon)
	val notes = listOf(
		NoteItem("This is a comment", 0, EntityType.EVENT, img, "Event Name"),
		NoteItem("This is a comment", 0, EntityType.BUILDING, img, "Building Name"),
		NoteItem("This is a comment", 0, EntityType.NODE, img, "Node Name")
	)
	NotesContent(notes, {}, {}, {})
}