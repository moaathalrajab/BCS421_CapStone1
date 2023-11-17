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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import io.gitlab.fsc_clam.fscwhereswhat.R
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Image
import io.gitlab.fsc_clam.fscwhereswhat.model.local.NoteItem

/**
 * Creates individual cards for a NoteItem
 */
@Composable
fun NotesCard(note: NoteItem, onUpdate: (NoteItem) -> Unit, onDelete: (NoteItem) -> Unit) {
	//holds state if user is editing
	var isEditingVisible by remember { mutableStateOf(false) }
	Card() {
		Row(
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.fillMaxWidth()
		) {

			Row(Modifier.fillMaxWidth(.8f)) {
				//Image of the Entity
				AsyncImage(
					model = note.image,
					contentDescription = "",
					modifier = Modifier
						.size(90.dp)
				)
				Column(
					Modifier
						.padding(vertical = 8.dp),
					verticalArrangement = Arrangement.spacedBy(4.dp)
				) {
					Row() {
						//TODO("Change color from preferences repo)
						Box(
							modifier = Modifier.size(24.dp),
							contentAlignment = Alignment.CenterStart
						) {
							//Creates corresponding Image for EntityType
							when (note.type) {
								EntityType.EVENT -> Image(
									painter = painterResource(id = R.drawable.flag_icon),
									contentDescription = stringResource(
										id = R.string.explanation_event_img
									),
								)

								EntityType.BUILDING -> Image(
									painter = painterResource(id = R.drawable.building_icon),
									contentDescription = stringResource(
										id = R.string.explanation_building_img
									),
								)

								EntityType.NODE -> Image(
									painter = painterResource(id = R.drawable.node_icon),
									contentDescription = stringResource(
										id = R.string.explanation_node_img
									)
								)
							}

						}
						//Name of the Entity
						Text(
							text = note.referenceName,
							modifier = Modifier
								.padding(start = 10.dp),
							style = MaterialTheme.typography.headlineSmall,
							fontWeight = FontWeight.Bold,
							overflow = TextOverflow.Ellipsis
						)
					}
					//The comment about the entity
					Text(
						text = note.comment,
						modifier = Modifier.padding(bottom = 4.dp)
					)

				}
			}

			Column(Modifier.padding(8.dp)) {
				//Edit Icon Button
				//On Click, the user is in edit mode
				IconButton(onClick = { isEditingVisible = !isEditingVisible })
				{
					Icon(Icons.Filled.Edit, "")
				}
				//Delete Icon Button
				//On click, sends to ViewModel to delete this note
				IconButton(onClick = { onDelete(note.copy()) }) {
					Icon(Icons.Filled.Delete, "")
				}
			}
		}
	}
	//if user is in edit mode, opens a Dialog to allow users to edit their comment
	if (isEditingVisible) {
		Dialog(
			onDismissRequest = { isEditingVisible = false },
		) {
			//holds state of the comment
			var comment by remember { mutableStateOf(note.comment) }
			Card(
				modifier = Modifier
					.fillMaxHeight(.5f)
			) {
				//Name of Entity
				Text(
					text = stringResource(id = R.string.editComment),
					style = MaterialTheme.typography.headlineSmall,
					overflow = TextOverflow.Ellipsis,
					modifier = Modifier.padding(8.dp)
				)
				//Textfield of comment
				OutlinedTextField(
					value = comment,
					onValueChange = { comment = it },
					modifier = Modifier.weight(1f).padding(8.dp).fillMaxWidth()
				)
				Row(
					Modifier
						.fillMaxWidth(),
					horizontalArrangement = Arrangement.End
				) {
					//Exits dialog
					TextButton(onClick = { isEditingVisible = false }) {
						Text(text = stringResource(id = android.R.string.cancel))
					}
					//Confirms  edit
					//On click, sends to NotesViewModel to update notes
					TextButton(onClick = {
						isEditingVisible = false
						onUpdate(note.copy(comment = comment))
					}) {
						Text(text = stringResource(id = R.string.options_confirm_button))
					}
				}
			}
		}
	}
}

@Preview
@Composable
fun PreviewNotesCard() {
	val img = Image.Drawable(R.drawable.flag_icon)
	val notes = listOf(
		NoteItem("This is a comment", 0, EntityType.EVENT, img, "Event Name"),
		NoteItem("This is a comment", 0, EntityType.BUILDING, img, "Building Name"),
		NoteItem("This is a comment", 0, EntityType.NODE, img, "Node Name")
	)
	Column(
		verticalArrangement = Arrangement.spacedBy(8.dp),
		modifier = Modifier.fillMaxSize()
	) {
		NotesCard(notes[0], {}, {})
		NotesCard(notes[1], {}, {})
		NotesCard(notes[2], {}, {})
	}

}