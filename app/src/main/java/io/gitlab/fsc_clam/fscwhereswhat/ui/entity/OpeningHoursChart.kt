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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.gitlab.fsc_clam.fscwhereswhat.model.local.OpeningHours

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