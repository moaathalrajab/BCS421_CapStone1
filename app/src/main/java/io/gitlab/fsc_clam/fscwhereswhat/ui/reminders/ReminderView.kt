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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
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
import io.gitlab.fsc_clam.fscwhereswhat.model.local.ReminderItem
import io.gitlab.fsc_clam.fscwhereswhat.model.local.ReminderTime
import io.gitlab.fsc_clam.fscwhereswhat.ui.theme.FSCWheresWhatTheme
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.RemindersViewModel
import java.net.URL

/**
 * Displays a list of reminders with, event image, event name, date, time.
 * There are options to edit and delete each reminder
 */
@Composable
fun ReminderView() {
	val viewModel: RemindersViewModel = viewModel()
	val reminders by viewModel.reminders.collectAsState()
	FSCWheresWhatTheme {
		RemindersContent(
			reminders,
			viewModel::deleteReminder,
			viewModel::updateReminderTime
		)
	}
}

/**
 * Display a scaffold with a title.
 * Scaffolds content is a lazy column with [ReminderCard] children
 * @param reminders is the list of items to display
 * @param onDelete is called to delete a reminder
 * @param onUpdate is called to update a reminder time
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindersContent(
	reminders: List<ReminderItem>,
	onDelete: (ReminderItem) -> Unit,
	onUpdate: (Int, ReminderTime) -> Unit
) {
	Scaffold(
		topBar = {
			TopAppBar(
				//backgroundColor = MaterialTheme.colors.primary,
				title = { Text(stringResource(R.string.title_reminders)) }
			)
		},
	) {
		LazyColumn(
			modifier = Modifier
				.padding(it)
				.fillMaxWidth(),
			contentPadding = PaddingValues(16.dp)
		) {

			items(reminders) { reminder ->
				ReminderCard(
					reminder,
					onDelete = { onDelete(reminder) },
					onUpdate = { onUpdate(reminder.eventId, it) } // Getting reminder time
				)
			}
		}
	}
}

@Preview
@Composable
fun PreviewReminder() {
	RemindersContent(
		listOf(
			ReminderItem(
				eventId = 1,
				eventName = "Event Name",
				imageURL = URL("https://tse1.mm.bing.net/th?id=OIP.OZQP0Ud2cFFmyo6yphrd1QAAAA&pid=Api"),
				remind = ReminderTime.HALF_HOUR_BEFORE,
				date = "Event date"
			),
			ReminderItem(
				eventId = 1,
				eventName = "Event Name",
				imageURL = URL("https://tse1.mm.bing.net/th?id=OIP.OZQP0Ud2cFFmyo6yphrd1QAAAA&pid=Api"),
				remind = ReminderTime.HALF_HOUR_BEFORE,
				date = "Event date"
			),
			ReminderItem(
				eventId = 1,
				eventName = "Event Name",
				imageURL = URL("https://tse1.mm.bing.net/th?id=OIP.OZQP0Ud2cFFmyo6yphrd1QAAAA&pid=Api"),
				remind = ReminderTime.HALF_HOUR_BEFORE,
				date = "Event date"
			),
			ReminderItem(
				eventId = 1,
				eventName = "Event Name",
				imageURL = URL("https://tse1.mm.bing.net/th?id=OIP.OZQP0Ud2cFFmyo6yphrd1QAAAA&pid=Api"),
				remind = ReminderTime.HALF_HOUR_BEFORE,
				date = "Event date"
			),
			ReminderItem(
				eventId = 1,
				eventName = "Event Name",
				imageURL = URL("https://tse1.mm.bing.net/th?id=OIP.OZQP0Ud2cFFmyo6yphrd1QAAAA&pid=Api"),
				remind = ReminderTime.HALF_HOUR_BEFORE,
				date = "Event date"
			),
			ReminderItem(
				eventId = 1,
				eventName = "Event Name",
				imageURL = URL("https://tse1.mm.bing.net/th?id=OIP.OZQP0Ud2cFFmyo6yphrd1QAAAA&pid=Api"),
				remind = ReminderTime.HALF_HOUR_BEFORE,
				date = "Event date"
			),
			ReminderItem(
				eventId = 1,
				eventName = "Event Name",
				imageURL = URL("https://tse1.mm.bing.net/th?id=OIP.OZQP0Ud2cFFmyo6yphrd1QAAAA&pid=Api"),
				remind = ReminderTime.HALF_HOUR_BEFORE,
				date = "Event date"
			)
		),
		{}, { _, _ -> }
	)
}

