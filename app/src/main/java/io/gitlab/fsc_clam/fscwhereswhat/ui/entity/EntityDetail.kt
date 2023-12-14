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

package io.gitlab.fsc_clam.fscwhereswhat.ui.entity

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import io.gitlab.fsc_clam.fscwhereswhat.R
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType.EVENT
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Image
import io.gitlab.fsc_clam.fscwhereswhat.model.local.OpeningHours
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.EntityDetailViewModel
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.impl.ImplEntityViewModel
import kotlinx.coroutines.launch

@Composable
fun EntityDetailView() {
	val viewModel: EntityDetailViewModel = viewModel<ImplEntityViewModel>()
	val name by viewModel.name.collectAsState()
	val note by viewModel.note.collectAsState()
	val url by viewModel.url.collectAsState()
	val image by viewModel.image.collectAsState()
	val oh by viewModel.openingHours.collectAsState()
	val description by viewModel.description.collectAsState()
	val type by viewModel.type.collectAsState()
	val nodeType by viewModel.nodeType.collectAsState()
	val instructions by viewModel.instructions.collectAsState()
	val hasRSVP by viewModel.hasRSVP.collectAsState()
	val hasReminder by viewModel.hasReminder.collectAsState()
	val reminder by viewModel.reminderTime.collectAsState()

	val scope = rememberCoroutineScope()

	if (type != null) {
		EntityDetailContent(
			name,
			type!!,
			hasRSVP,
			url.toString(),
			oh,
			note,
			image,
			updateNote = {
				scope.launch {
					viewModel.setNote(it)
				}
			},
		)
	}
}

@Composable
fun EntityDetailContent(
	name: String,

	type: EntityType,

	// Event
	hasRSVP: Boolean,

	url: String?,

	// OSM
	oh: List<OpeningHours>,

	notes: String?,
	image: Image,
	updateNote: (String) -> Unit
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(horizontal = 8.dp),
	) {
		Row(
			Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Row(verticalAlignment = Alignment.CenterVertically) {
				when (image) {
					is Image.Asset -> {
						AsyncImage(
							model = image.path,
							"Icon"
						)
					}

					is Image.Drawable -> {
						Image(painterResource(image.drawable), "Icon")
					}
				}

				Text(name, style = MaterialTheme.typography.titleLarge)
			}

			Row(verticalAlignment = Alignment.CenterVertically) {
				if (type == EVENT) {
					IconButton(
						onClick = {
							// TODO reminders
						}
					) {
						Icon(painterResource(R.drawable.notification_bell), null)
					}
				}

				IconButton(
					onClick = {
						// TODO share
					}
				) {
					Icon(Icons.Default.Share, null)
				}
			}
		}

		if (type != EVENT && oh.isNotEmpty()) {
			OpeningHoursChart(oh)
		}

		if (type == EVENT && hasRSVP) {
			val context = LocalContext.current
			Button(
				onClick = {
					context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url.toString())))
				},
				modifier = Modifier.fillMaxWidth()
			) {
				Text("RSVP Now!")
			}
		}

		OutlinedTextField(
			notes ?: "",
			onValueChange = updateNote,
			modifier = Modifier
				.fillMaxWidth()
				.fillMaxSize(.5f),
			placeholder = {
				Text("Enter a note here")
			},
			label = {
				Text("Notes")
			},
		)
	}
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OpeningHoursChart(
	ohs: List<OpeningHours>
) {
	Card {
		Column(
			verticalArrangement = Arrangement.spacedBy(4.dp),
			modifier = Modifier.padding(8.dp)
		) {
			Text(
				"Opening Hours",
				Modifier.fillMaxWidth(),
				style = MaterialTheme.typography.titleMedium
			)

			ohs.forEach { oh ->
				Column(Modifier.fillMaxWidth()) {
					Text("Open ${oh.startHour}:${oh.startMinute} till ${oh.endHour}:${oh.endMinute} on...")

					FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
						if (oh.sunday)
							Text("Sunday")
						if (oh.monday)
							Text("Monday")
						if (oh.tuesday)
							Text("Tuesday")
						if (oh.wednesday)
							Text("Wednesday")
						if (oh.thursday)
							Text("Thursday")
						if (oh.friday)
							Text("Friday")
						if (oh.saturday)
							Text("Saturday")
					}
				}

				Divider()
			}
		}
	}
}

@Preview
@Composable
fun PreviewEntityDetail() {
	Surface {
		EntityDetailContent(
			name = "Test",
			type = EVENT,
			hasRSVP = true,
			url = "google.com",
			oh = listOf(OpeningHours.everyDay),
			notes = null,
			image = Image.Drawable(R.drawable.baseline_error_24)
		) {}
	}
}