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

package io.gitlab.fsc_clam.fscwhereswhat.ui.search

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.gitlab.fsc_clam.fscwhereswhat.R

@Composable
fun SearchView() {
	//holds the current user input
	var userQuery by remember { mutableStateOf("") }

	Card {
		Column(
			modifier = Modifier
				.padding(12.dp)
				.fillMaxSize(),
			verticalArrangement = Arrangement.spacedBy(8.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			//The Search Bar
			Card(
				shape = CircleShape,
				modifier = Modifier
					.fillMaxWidth(.8f)
					.border(2.dp, Color.Black, CircleShape),
				elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
			) {
				Row(
					verticalAlignment = Alignment.CenterVertically
				) {
					Icon(
						Icons.Default.Search,
						stringResource(id = R.string.search_bar_label),
						modifier = Modifier.padding(horizontal = 8.dp)
					)
					TextField(
						value = userQuery,
						onValueChange = { userQuery = it },
						label = { Text(text = stringResource(id = R.string.search_bar_label)) }
					)

				}
			}
		}
	}
}