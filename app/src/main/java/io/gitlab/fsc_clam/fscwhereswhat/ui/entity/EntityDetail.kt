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
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import io.gitlab.fsc_clam.fscwhereswhat.R
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType.EVENT
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Image
import io.gitlab.fsc_clam.fscwhereswhat.model.local.NodeType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.OpeningHours
import io.gitlab.fsc_clam.fscwhereswhat.model.local.ReminderTime
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.EntityDetailViewModel
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.impl.ImplEntityViewModel
import kotlinx.coroutines.launch
import java.net.URL

@Preview
@Composable
fun PreviewEntityDetail() {
	Surface {
		EntityDetailContent(
			name = "Test",
			type = EVENT,
			hasRSVP = true,
			url = URL("https://google.com"),
			oh = listOf(OpeningHours.everyDay),
			notes = null,
			image = Image.Drawable(R.drawable.baseline_error_24),
			{},
			"",
			null,
			"",
			false,
			ReminderTime.HALF_HOUR_BEFORE,
			setReminder = {},
			removeReminder = {}
		)
	}
}

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
			name = name,
			notes = note,
			url = url,
			image = image,
			oh = oh,
			description = description,
			type = type!!,
			nodeType = nodeType,
			instructions = instructions,
			hasRSVP = hasRSVP,
			hasReminder = hasReminder,
			reminder = reminder,
			updateNote = {
				scope.launch {
					viewModel.setNote(it)
				}
			},
			setReminder = viewModel::setReminderTime,
			removeReminder = viewModel::deleteReminder
		)
	} else {
		Log.d("Compose", "No type")
	}
}

/**
 * TODO figure out [nodeType] usage
 */
@Composable
fun EntityDetailContent(
	name: String,

	type: EntityType,

	// Event
	hasRSVP: Boolean,

	url: URL?,

	// OSM
	oh: List<OpeningHours>,

	notes: String?,
	image: Image,
	updateNote: (String) -> Unit,
	description: String?,
	nodeType: NodeType?,
	instructions: String?,
	hasReminder: Boolean,
	reminder: ReminderTime?,

	setReminder: (ReminderTime) -> Unit,
	removeReminder: () -> Unit
) {
	val context = LocalContext.current
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(horizontal = 8.dp),
	) {
		// TopBar
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
							"Icon",
							modifier = Modifier.size(64.dp)
						)
					}

					is Image.Drawable -> {
						Image(
							painterResource(image.drawable), "Icon",
							modifier = Modifier.size(64.dp)
						)
					}
				}

				Text(name, style = MaterialTheme.typography.titleLarge)
			}

			Row(verticalAlignment = Alignment.CenterVertically) {
				if (type == EVENT) {
					var isDropDownVisible by remember { mutableStateOf(false) }
					IconButton(
						onClick = {
							isDropDownVisible = true
						}
					) {
						Icon(painterResource(R.drawable.notification_bell), null)
					}

					ReminderEdit(
						isDropDownVisible,
						onDismiss = {
							isDropDownVisible = false
						},
						onDelete = {
							removeReminder()
							isDropDownVisible = false
						},
						onUpdate = {
							setReminder(it)
							isDropDownVisible = false
						}
					)
				}

				IconButton(
					onClick = {
						val sendIntent: Intent = Intent().apply {
							action = Intent.ACTION_SEND
							putExtra(Intent.EXTRA_TEXT, url)
							this.type = "text/*"
						}
						context.startActivity(Intent.createChooser(sendIntent, "Share"))
					}
				) {
					Icon(Icons.Default.Share, null)
				}
			}
		}

		if (description != null) {
			OutlinedTextField(
				description,
				onValueChange = {},
				readOnly = true,
				modifier = Modifier.fillMaxWidth()
			)
		}

		// Opening hours
		if (type != EVENT && oh.isNotEmpty()) {
			OpeningHoursChart(oh)
		}

		// RSVP button
		if (type == EVENT && hasRSVP) {
			Button(
				onClick = {
					context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url.toString())))
				},
				modifier = Modifier.fillMaxWidth()
			) {
				Text("RSVP Now!")
			}

			if (instructions != null) {
				OutlinedTextField(
					instructions,
					onValueChange = {},
					readOnly = true,
					modifier = Modifier.fillMaxWidth()
				)
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

@Composable
fun ReminderEdit(
	showDropDown: Boolean,
	onDismiss: () -> Unit,
	onDelete: () -> Unit,
	onUpdate: (ReminderTime) -> Unit
) {
	DropdownMenu(showDropDown, onDismiss) {
		DropdownMenuItem(
			text = {
				Text("At Start")
			},
			onClick = {
				onUpdate(ReminderTime.START)
			}
		)
		DropdownMenuItem(
			text = {
				Text("Half an hour before")
			},
			onClick = {
				onUpdate(ReminderTime.HALF_HOUR_BEFORE)
			}
		)
		DropdownMenuItem(
			text = {
				Text("Two Hours Before")
			},
			onClick = {
				onUpdate(ReminderTime.TWO_HOUR_BEFORE)
			}
		)
		DropdownMenuItem(
			text = {
				Text("An hour before")
			},
			onClick = {
				onUpdate(ReminderTime.TWO_HOUR_BEFORE)
			}
		)
		DropdownMenuItem(
			text = {
				Text(stringResource(R.string.delete), color = Color.Red)
			},
			onClick = onDelete
		)
	}
}