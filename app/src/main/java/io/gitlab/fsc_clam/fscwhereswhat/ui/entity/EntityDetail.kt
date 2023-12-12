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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.gitlab.fsc_clam.fscwhereswhat.R
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType.EVENT
import io.gitlab.fsc_clam.fscwhereswhat.model.local.OpeningHours
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.EntityViewModel
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.impl.ImplEntityViewModel

@Composable
fun EntityDetailView() {
	val viewModel: EntityViewModel = viewModel<ImplEntityViewModel>()
	val notes by viewModel.notes.collectAsState()
	val reminder by viewModel.reminder.collectAsState()
	val type by viewModel.type.collectAsState()
	val name by viewModel.name.collectAsState()
	val oh by viewModel.oh.collectAsState()
	val hasRSVP by viewModel.hasRSVP.collectAsState()
	val url by viewModel.url.collectAsState()
	val imageURL by viewModel.imageURL.collectAsState()

	if (type != null) {
		EntityDetailContent(
			name,
			type!!,
			hasRSVP,
			url,
			oh,
			notes,
			updateNote = viewModel::updaterNote
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
	updateNote: (String) -> Unit
) {
	Column(
		modifier = Modifier.fillMaxSize(),
	) {
		Row(
			Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(name, style = MaterialTheme.typography.titleLarge)

			Row {
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
			notes = null,
			updateNote = {},
			type = EVENT,
			oh = listOf(OpeningHours.everyDay),
			hasRSVP = true,
			url = "google.com"
		)
	}
}