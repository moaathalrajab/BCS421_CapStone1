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

package io.gitlab.fsc_clam.fscwhereswhat.ui.reminders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import io.gitlab.fsc_clam.fscwhereswhat.R
import io.gitlab.fsc_clam.fscwhereswhat.model.local.ReminderItem
import io.gitlab.fsc_clam.fscwhereswhat.model.local.ReminderTime
import java.net.URL

/**
 * Designs a single reminder with Event Image, Event Name, Time of Event, and Date of event.
 * Also includes a dropdown menu for editing the time of the reminder and/or deleting the reminder
 * @param reminder to display
 * @param onDelete is called to delete reminder
 * @param onUpdate updates when user would like to be reminded of event
 */
@Composable
fun ReminderCard(reminder: ReminderItem, onDelete: () -> Unit, onUpdate: (ReminderTime) -> Unit) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(10.dp),
		shape = MaterialTheme.shapes.medium,
		//backgroundColor = MaterialTheme.colors.surface
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(8.dp)
			) {
				AsyncImage(
					reminder.imageURL,
					contentDescription = "Event Image",
					modifier = Modifier
						.heightIn(max = 72.dp)
						.aspectRatio(1f)
						.fillMaxWidth(.25f),
					contentScale = ContentScale.Fit,
				)
				Column(
					modifier = Modifier.fillMaxWidth(.75f)

				) {
					Text(
						text = reminder.eventName,
						modifier = Modifier.padding(bottom = 4.dp),
						fontSize = 25.sp,
						fontWeight = FontWeight.Bold
					)
					Text(
						text = stringResource(
							id = when (reminder.remind) {
								ReminderTime.START -> R.string.remind_start
								ReminderTime.HALF_HOUR_BEFORE -> R.string.remind_half_hour
								ReminderTime.HOUR_BEFORE -> R.string.remind_hour
								ReminderTime.TWO_HOUR_BEFORE -> R.string.remind_two_hour
							}
						)
					)
					Text(
						"Current Date Here",
						style = MaterialTheme.typography.labelMedium
					)
				}
			}
			Column {
				Box(
					modifier = Modifier
				) {
					var expanded by remember {
						mutableStateOf(false)
					}
					IconButton(onClick = { expanded = true }) {
						Icon(
							imageVector = Icons.Default.Edit,
							contentDescription = stringResource(id = R.string.edit)
						)
					}
					ReminderCardDropDownMenu(expanded, { expanded = false }, onUpdate)
				}
				IconButton(
					onClick = onDelete // ignore for now

				) {
					Icon(
						imageVector = Icons.Default.Delete,
						contentDescription = stringResource(id = R.string.delete)
					)

				}
			}
		}
	}
}

/**
 * Drop down Menu for [ReminderCard]
 */
@Composable
fun ReminderCardDropDownMenu(
	expanded: Boolean, onDismissRequest: () -> Unit, onUpdate: (ReminderTime) -> Unit
) {
	DropdownMenu(
		expanded = expanded, onDismissRequest = onDismissRequest
	) {
		DropdownMenuItem(text = { Text("Remind when event starts") }, onClick = {
			onUpdate(ReminderTime.START) // Set reminder to START
			onDismissRequest.invoke()
		})
		DropdownMenuItem(text = { Text("Remind 30 minutes before event") }, onClick = {
			onUpdate(ReminderTime.HALF_HOUR_BEFORE) // Set reminder to HALF_HOUR_BEFORE
			onDismissRequest.invoke()
		})
		DropdownMenuItem(text = { Text("Remind 1 hour before event") }, onClick = {
			onUpdate(ReminderTime.HOUR_BEFORE) // Set reminder to HOUR_BEFORE
			onDismissRequest.invoke()
		})
		DropdownMenuItem(text = { Text("Remind 2 hours before event") }, onClick = {
			onUpdate(ReminderTime.TWO_HOUR_BEFORE) // Set reminder to TWO_HOUR_BEFORE
			onDismissRequest.invoke()
		})
	}
}

@Preview
@Composable
fun PreviewReminderCard() {
	ReminderCard(reminder = ReminderItem(
		eventId = 1,
		eventName = "Event Name",
		imageURL = URL("https://tse1.mm.bing.net/th?id=OIP.OZQP0Ud2cFFmyo6yphrd1QAAAA&pid=Api"),
		remind = ReminderTime.HALF_HOUR_BEFORE,
		date = "Event date"
	), onDelete = {

	}, onUpdate = {

	})
}
