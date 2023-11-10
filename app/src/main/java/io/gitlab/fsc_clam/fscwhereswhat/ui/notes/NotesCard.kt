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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.gitlab.fsc_clam.fscwhereswhat.R
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Image
import io.gitlab.fsc_clam.fscwhereswhat.model.local.NoteItem

@Composable
fun NotesCard(note: NoteItem, onUpdate: (NoteItem) -> Unit, onDelete: (NoteItem) -> Unit) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
	) {
		Row(
			horizontalArrangement = Arrangement.SpaceAround
		) {
			AsyncImage(
				model = note.image,
				contentDescription = "",
				modifier = Modifier
					.requiredSize(90.dp)
					.padding(start = 5.dp)
			)

			Column(
				Modifier
					.fillMaxWidth(.8f),
				verticalArrangement = Arrangement.Top
			) {
				Box(
					Modifier.fillMaxWidth(.85f)
				) {
					Text(
						text = note.referenceName,
						modifier = Modifier
							.padding(end = 10.dp)
							.align(Alignment.CenterStart),
						style = MaterialTheme.typography.headlineSmall,
						overflow = TextOverflow.Ellipsis
					)
					//change color from preferences repo
					when (note.type) {
						EntityType.EVENT -> Image(
							painter = painterResource(id = R.drawable.flag_icon),
							contentDescription = stringResource(
								id = R.string.explanation_event_img
							),
							modifier = Modifier.align(Alignment.CenterEnd).requiredSize(35.dp)
						)

						EntityType.BUILDING -> Image(
							painter = painterResource(id = R.drawable.building_icon),
							contentDescription = stringResource(
								id = R.string.explanation_building_img
							),
							modifier = Modifier.align(Alignment.CenterEnd).requiredSize(35.dp)
						)

						EntityType.NODE -> Image(
							painter = painterResource(id = R.drawable.node_icon),
							contentDescription = stringResource(
								id = R.string.explanation_node_img
							),
							modifier = Modifier.align(Alignment.CenterEnd).requiredSize(35.dp)
						)
					}
				}

				Text(
					text = note.comment,
					style = MaterialTheme.typography.bodyMedium
				)


			}
			Column(verticalArrangement = Arrangement.SpaceEvenly) {
				IconButton(onClick = { onUpdate }) {
					Icon(Icons.Filled.Edit, "")
				}
				IconButton(onClick = { onDelete }) {
					Icon(Icons.Filled.Delete, "")
				}
			}
		}
	}
}

@Preview
@Composable
fun PreviewNotesCard() {
	val img = Image.Drawable(R.drawable.flag_icon)
	val note = NoteItem("This is a comment", 0, EntityType.EVENT, img, "Event Name")
	NotesCard(note, {}, {})
}