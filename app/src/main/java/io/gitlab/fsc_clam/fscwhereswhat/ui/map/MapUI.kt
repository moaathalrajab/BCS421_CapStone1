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

package io.gitlab.fsc_clam.fscwhereswhat.ui.map

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.gitlab.fsc_clam.fscwhereswhat.R
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Image
import io.gitlab.fsc_clam.fscwhereswhat.model.local.User
import io.gitlab.fsc_clam.fscwhereswhat.ui.theme.FSCWheresWhatTheme

/**
 * Creates the UI for Map Content
 * @param activeFilter is the current filter selected
 * @param buildingColor is the color of building pinpoints from preferences repo
 * @param eventColor is the color of event pinpoints from preferences repo
 * @param nodeColor is the color of node pinpoints from preferences repo
 * @param setActiveFilter will change the active filter to selected filter type from viewmodel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapUI(
	user: User?,
	activeFilter: EntityType?,
	buildingColor: Int,
	eventColor: Int,
	nodeColor: Int,
	setActiveFilter: (EntityType?) -> Unit,
) {
	FSCWheresWhatTheme {
		//holds the current user input
		var userQuery by remember { mutableStateOf("") }
		//state of showing search bar
		var isSearchVisible by remember { mutableStateOf(false) }
		Box(
			modifier = Modifier.fillMaxSize()
		) {
			//Icon of the Account
			//WIP should include functionality to login
			IconButton(
				modifier = Modifier
					.padding(12.dp)
					.align(Alignment.TopStart),
				onClick = { /*TODO*/ }
			) {
				if (user != null) {
					AsyncImage(
						model = user.image,
						contentDescription = stringResource(id = R.string.account_icon)
					)
				} else {
					Icon(
						Icons.Filled.AccountCircle,
						contentDescription = stringResource(id = R.string.account_icon),
						modifier = Modifier.size(50.dp)
					)
				}
			}
			//Holds the search bar and filter buttons
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.align(Alignment.BottomStart),
				verticalArrangement = Arrangement.Bottom,
				horizontalAlignment = Alignment.Start
			) {

				//Creates the filter buttons
				Row(
					modifier = Modifier
						.padding(horizontal = 12.dp)
						.fillMaxWidth(),
					horizontalArrangement = Arrangement.spacedBy(8.dp),
					verticalAlignment = Alignment.CenterVertically
				) {
					when (activeFilter) {
						//if activeFilter is building, the only selected filterButton is building
						EntityType.BUILDING -> {
							FilterButtons(
								filter = EntityType.BUILDING,
								img = Image.Drawable(R.drawable.building_icon),
								pinPointColor = buildingColor,
								isSelected = true,
								onSelected = {
									setActiveFilter(it)
								}
							)
							FilterButtons(
								filter = EntityType.EVENT,
								img = Image.Drawable(R.drawable.flag_icon),
								pinPointColor = eventColor,
								false,
								onSelected = { setActiveFilter(it) }
							)
							FilterButtons(
								filter = EntityType.NODE,
								img = Image.Drawable(R.drawable.node_icon),
								pinPointColor = nodeColor,
								false,
								onSelected = { setActiveFilter(it) }
							)
						}
						//if activeFilter is event, the only selected filterButton is event
						EntityType.EVENT -> {
							FilterButtons(
								filter = EntityType.BUILDING,
								img = Image.Drawable(R.drawable.building_icon),
								pinPointColor = buildingColor,
								isSelected = false,
								onSelected = {
									setActiveFilter(it)
								}
							)
							FilterButtons(
								filter = EntityType.EVENT,
								img = Image.Drawable(R.drawable.flag_icon),
								pinPointColor = eventColor,
								isSelected = true,
								onSelected = { setActiveFilter(it) }
							)
							FilterButtons(
								filter = EntityType.NODE,
								img = Image.Drawable(R.drawable.node_icon),
								pinPointColor = nodeColor,
								isSelected = false,
								onSelected = { setActiveFilter(it) }
							)
						}
						//if activeFilter is node, the only selected filterButton is node
						EntityType.NODE -> {
							FilterButtons(
								filter = EntityType.BUILDING,
								img = Image.Drawable(R.drawable.building_icon),
								pinPointColor = buildingColor,
								isSelected = false,
								onSelected = {
									setActiveFilter(it)
								}
							)
							FilterButtons(
								filter = EntityType.EVENT,
								img = Image.Drawable(R.drawable.flag_icon),
								pinPointColor = eventColor,
								isSelected = false,
								onSelected = { setActiveFilter(it) }
							)
							FilterButtons(
								filter = EntityType.NODE,
								img = Image.Drawable(R.drawable.node_icon),
								pinPointColor = nodeColor,
								isSelected = true,
								onSelected = { setActiveFilter(it) }
							)
						}
						//if activeFilter is null, the  selected filterButton is all the filterButtons
						else -> {
							FilterButtons(
								filter = EntityType.BUILDING,
								img = Image.Drawable(R.drawable.building_icon),
								pinPointColor = buildingColor,
								isSelected = true,
								onSelected = { setActiveFilter(it) }
							)
							FilterButtons(
								filter = EntityType.EVENT,
								img = Image.Drawable(R.drawable.flag_icon),
								pinPointColor = eventColor,
								isSelected = true,
								onSelected = { setActiveFilter(it) }
							)
							FilterButtons(
								filter = EntityType.NODE,
								img = Image.Drawable(R.drawable.node_icon),
								pinPointColor = nodeColor,
								isSelected = true,
								onSelected = { setActiveFilter(it) }
							)
						}
					}
				}
				//bottom bar holds search and more button
				Row(
					modifier = Modifier
						.padding(8.dp)
						.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceAround,
					verticalAlignment = Alignment.CenterVertically
				) {
					//search bar on click expands to show search view
					Card(
						shape = CircleShape,
						modifier = Modifier
							.fillMaxWidth(.8f)
							.requiredHeight(35.dp)
							.clickable {
								isSearchVisible = true
							},
						elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
					) {
						Row(
							modifier = Modifier
								.fillMaxSize(),
							verticalAlignment = Alignment.CenterVertically,
						) {
							Icon(
								Icons.Default.Search,
								stringResource(id = R.string.search_bar_label),
								modifier = Modifier.padding(horizontal = 8.dp)
							)
							Text(text = let {
								if (userQuery.isNotEmpty())
									userQuery
								else
									stringResource(id = R.string.search_bar_label)
							}
							)
						}
					}
					//The more button
					Card {
						IconButton(
							onClick = { /*TODO*/ },
							modifier = Modifier
								.size(35.dp)
						) {
							Icon(
								painter = painterResource(id = R.drawable.baseline_more_horiz_24),
								contentDescription = stringResource(id = R.string.more_button_desc),
							)
						}
					}
				}
			}
		}
		//placeholder for search view
		if (isSearchVisible) {
			ModalBottomSheet(onDismissRequest = { isSearchVisible = false }) {
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
		}
	}

}

@Preview
@Composable
fun PreviewMapUI() {
	Surface {
		MapUI(
			null,
			null,
			Color.Red.toArgb(),
			Color.Red.toArgb(),
			Color.Red.toArgb(),
			{}
		)
	}
}

/**
 * Creates individual filter buttons for each Entity Type
 * @param filter is the entity type
 * @param img is the drawable for the icon
 * @param pinPointColor is the color for the entity type
 * @param isSelected is if the button is the active filter
 * @param onSelected is the function called when pressed to change active filter
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterButtons(
	filter: EntityType,
	img: Image.Drawable,
	pinPointColor: Int,
	isSelected: Boolean,
	onSelected: (EntityType?) -> Unit
) {
	FilterChip(
		selected = isSelected,
		onClick = { onSelected(filter) },
		label = {
			Text(
				text = filter.toString(),
				style = MaterialTheme.typography.bodyMedium,
				color = Color.Black
			)
		},
		leadingIcon = {
			Icon(
				painter = painterResource(id = img.drawable),
				contentDescription = "",
				tint = Color(pinPointColor),
				modifier = Modifier.size(25.dp)
			)
		}
	)

}

@Preview
@Composable
fun PreviewFilterButtons() {
	Row {
		FilterButtons(
			filter = EntityType.BUILDING,
			img = Image.Drawable(R.drawable.building_icon),
			Color.Red.toArgb(),
			true,
			{}
		)
		FilterButtons(
			filter = EntityType.EVENT,
			img = Image.Drawable(R.drawable.flag_icon),
			Color.Red.toArgb(),
			false,
			{}
		)
	}
}