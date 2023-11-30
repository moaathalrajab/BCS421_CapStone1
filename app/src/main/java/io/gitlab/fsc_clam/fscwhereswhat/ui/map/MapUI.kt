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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.gitlab.fsc_clam.fscwhereswhat.R
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Image

/**
 * Creates the UI for Map Content
 * @param activeFilter is the current filter selected
 * @param buildingColor is the color of building pinpoints from preferences repo
 * @param eventColor is the color of event pinpoints from preferences repo
 * @param nodeColor is the color of node pinpoints from preferences repo
 * @param setActiveFilter will change the active filter to selected filter type from viewmodel
 */
@Composable
fun MapUI(
	activeFilter: EntityType?,
	buildingColor: Int,
	eventColor: Int,
	nodeColor: Int,
	setActiveFilter: (EntityType?) -> Unit,
) {
	Box(
		modifier = Modifier.fillMaxSize()
	) {
		IconButton(
			modifier = Modifier
				.padding(12.dp)
				.align(Alignment.TopStart),
			onClick = { /*TODO*/ }
		) {
			Icon(
				Icons.Filled.AccountCircle,
				contentDescription = stringResource(id = R.string.account_icon),
				modifier = Modifier.size(50.dp)
			)
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
				horizontalArrangement = Arrangement.spacedBy(8.dp,),
				verticalAlignment = Alignment.CenterVertically
			) {
				when (activeFilter) {
					//if activeFilter is building, the only selected filterButton is building
					EntityType.BUILDING -> {
						FilterButtons(
							filter = EntityType.BUILDING,
							img = Image.Drawable(R.drawable.building_icon),
							pinPointColor = buildingColor,
							true,
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
							false,
							onSelected = {
								setActiveFilter(it)
							}
						)
						FilterButtons(
							filter = EntityType.EVENT,
							img = Image.Drawable(R.drawable.flag_icon),
							pinPointColor = eventColor,
							true,
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
					//if activeFilter is node, the only selected filterButton is node
					EntityType.NODE -> {
						FilterButtons(
							filter = EntityType.BUILDING,
							img = Image.Drawable(R.drawable.building_icon),
							pinPointColor = buildingColor,
							false,
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
							true,
							onSelected = { setActiveFilter(it) }
						)
					}
					//if activeFilter is null, the  selected filterButton is all the filterButtons
					else -> {
						FilterButtons(
							filter = EntityType.BUILDING,
							img = Image.Drawable(R.drawable.building_icon),
							pinPointColor = buildingColor,
							true,
							onSelected = { setActiveFilter(it) }
						)
						FilterButtons(
							filter = EntityType.EVENT,
							img = Image.Drawable(R.drawable.flag_icon),
							pinPointColor = eventColor,
							true,
							onSelected = { setActiveFilter(it) }
						)
						FilterButtons(
							filter = EntityType.NODE,
							img = Image.Drawable(R.drawable.node_icon),
							pinPointColor = nodeColor,
							true,
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
				Card(
					shape = CircleShape,
					modifier = Modifier
						.fillMaxWidth(.8f)
						.clickable {

						},
					elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
				) {
					Row(
						verticalAlignment = Alignment.CenterVertically
					) {
						IconButton(onClick = {}) {
							Icon(Icons.Default.Search, "Search")
						}
						Text(text = stringResource(id = R.string.search_bar_label))
					}
				}
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
}

@Preview
@Composable
fun PreviewMapUI() {
	Surface {
		MapUI(
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
				color = Color.let {
					if(isSelected)
						Color.Black
					else
						Color.White
				}
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