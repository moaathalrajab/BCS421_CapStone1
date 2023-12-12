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

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.gitlab.fsc_clam.fscwhereswhat.R

data class SearchItem(val name: String, val description: String) // example data class for show
@Preview
@Composable
fun SearchViewPreview() {
	val searchItem = SearchItem(name = "Item 1", description = "Description for Item 1")

	Column(
			modifier = Modifier
			.fillMaxSize()
			.padding(16.dp)
			.background(color = Color.DarkGray)
	) {
		SearchView()
		SearchViewList()
		SearchViewItem(searchItem)
	}
}

@Composable
fun SearchView() {
	//holds the current user input
	var userQuery by remember { mutableStateOf("") }

	Card {
		Column(
			modifier = Modifier
				.padding(12.dp)
				.fillMaxWidth(),
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

@Composable
fun SearchViewList() {
	val items = List(10) { index -> // EXAMPLES
		SearchItem(name = "Item $index", description = "Description for Item $index")
	}

	LazyColumn(contentPadding = PaddingValues(16.dp)) {
		items.forEach { item ->
			item {
				SearchViewItem(item)
			}
		}
	}
}

@Composable
fun SearchViewItem(item: SearchItem) {
	val scrollState = rememberScrollState()
	Card(
		modifier = Modifier
			.padding(10.dp)
			.wrapContentSize()
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.height(100.dp),
			verticalAlignment = Alignment.Top,
			horizontalArrangement = Arrangement.spacedBy(15.dp)
		) {
			Image(
				painter = painterResource(id = R.drawable.ic_launcher_foreground),
				contentDescription = null,
				modifier = Modifier.size(115.dp)
			)
			Column(
				modifier = Modifier.padding(12.dp)
					.verticalScroll(scrollState),
				verticalArrangement = Arrangement.spacedBy(20.dp)
			)
			{
				Text(
					text = item.name
				)
				Text(text = item.description)
			}
		}
	}
}
