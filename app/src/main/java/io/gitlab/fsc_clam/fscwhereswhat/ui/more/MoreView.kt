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

package io.gitlab.fsc_clam.fscwhereswhat.ui.more

import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.gitlab.fsc_clam.fscwhereswhat.BuildConfig
import io.gitlab.fsc_clam.fscwhereswhat.R
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.ui.onboarding.PinpointColorItem
import io.gitlab.fsc_clam.fscwhereswhat.ui.theme.FSCWheresWhatTheme
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.MoreViewModel
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.impl.ImplMoreViewModel

//@Preview
@Composable
fun MoreView(
	navToNotes: () -> Unit,
	navToReminders: () -> Unit,
	onBack: () -> Unit
) {
	val viewModel: MoreViewModel = viewModel<ImplMoreViewModel>()
	val exception by viewModel.exceptions.collectAsState(initial = null)
	val cacheStatus by viewModel.cacheStatus.collectAsState(initial = null)
	val eventColor by viewModel.eventColor.collectAsState()
	val buildingColor by viewModel.buildingColor.collectAsState()
	val nodeColor by viewModel.nodeColor.collectAsState()

	val snackbarHostState = remember { SnackbarHostState() }
	LaunchedEffect(key1 = exception) {
		if (exception != null) {
			snackbarHostState.showSnackbar(exception!!.message ?: "Unknown Error")
		}
	}
	LaunchedEffect(key1 = cacheStatus) {
		if (cacheStatus != null) {
			if (cacheStatus!!) {
				snackbarHostState.showSnackbar("Cache Successfully Completed")
			} else {

			}
		}
	}

	FSCWheresWhatTheme {
		MoreContent(
			navToNotes = navToNotes,
			navToReminders = navToReminders,
			clearCacheLogic = viewModel::clearCache,
			snackbarHostState = snackbarHostState,
			onBack = onBack,
			eventColor = eventColor,
			buildingColor = buildingColor,
			nodeColor = nodeColor,
			setEventColor = viewModel::setEventColor,
			setBuildingColor = viewModel::setBuildingColor,
			setNodeColor = viewModel::setNodeColor
		)
	}
}

@Preview
@Composable
fun PreviewMoreContent() {
	MoreContent(
		navToNotes = {},
		navToReminders = {},
		clearCacheLogic = {},
		snackbarHostState = SnackbarHostState(),
		onBack = {},
		eventColor = EntityType.EVENT.defaultColor,
		buildingColor = EntityType.BUILDING.defaultColor,
		nodeColor = EntityType.NODE.defaultColor,
		setBuildingColor = {},
		setEventColor = {},
		setNodeColor = {}
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreContent(
	navToNotes: () -> Unit,
	navToReminders: () -> Unit,
	clearCacheLogic: () -> Unit,
	snackbarHostState: SnackbarHostState,
	onBack: () -> Unit,

	eventColor: Int,
	buildingColor: Int,
	nodeColor: Int,
	setEventColor: (Int) -> Unit,
	setBuildingColor: (Int) -> Unit,
	setNodeColor: (Int) -> Unit,
) {

	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text(text = "More View") // TODO: Subject to change
				},
				navigationIcon = {
					IconButton(
						onClick = onBack
					) {
						Icon(Icons.Default.ArrowBack, stringResource(R.string.nav_back))
					}
				}
			)
		},
		snackbarHost = { SnackbarHost(snackbarHostState) },
	) {
		val scroll = rememberScrollState()
		Column(
			modifier = Modifier
				.padding(it)
				.verticalScroll(scroll),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy(8.dp),
		) {
			NavigationItems(navToReminders, navToNotes)

			OptionsUI(
				eventColor,
				buildingColor,
				nodeColor,
				setEventColor,
				setBuildingColor,
				setNodeColor
			)

			AuthorsCard()

			ClearCacheCard(clearCacheLogic)

			Text(
				text = "Version ${BuildConfig.VERSION_NAME}",
				style = MaterialTheme.typography.bodySmall
			)
		}
	}
}

@Composable
fun NavigationItems(
	navToReminders: () -> Unit,
	navToNotes: () -> Unit
) {
	Column {
		NavigationItem(
			"Reminders",
			"View and edit reminders",
			R.drawable.notification_bell,
			navToReminders
		)

		NavigationItem(
			"Notes",
			"View and edit notes",
			R.drawable.note_icon,
			navToNotes
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationItem(
	name: String,
	description: String,
	@DrawableRes
	drawable: Int,
	onNavigate: () -> Unit
) {
	Card(
		onClick = onNavigate,
		modifier = Modifier.fillMaxWidth(),
		shape = RectangleShape,
		colors = CardDefaults.cardColors(containerColor = Color.Transparent)
	) {
		Row(
			modifier = Modifier
				.padding(vertical = 10.dp, horizontal = 14.dp)
				.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(8.dp)
		) {
			Box(
				modifier = Modifier.size(34.dp)
			) {
				Icon(
					painter = painterResource(id = drawable),
					contentDescription = "",
					modifier = Modifier.padding(8.dp)
				)
			}

			Column(
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.Start
			) {
				Text(
					text = name,
				)
				Text(
					text = description,
				)
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClearCacheCard(clearCacheLogic: () -> Unit) {
	// Button
	Column(
		modifier = Modifier
			.padding(horizontal = 14.dp)
			.padding(top = 10.dp)
			.fillMaxWidth(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.SpaceBetween
	) {
		Card(
			onClick = clearCacheLogic,
			modifier = Modifier
				.wrapContentWidth()
				.padding(bottom = 8.dp)
				.wrapContentHeight()
		) {
			Column(
				modifier = Modifier.padding(vertical = 10.dp, horizontal = 14.dp),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(
					text = "Clear Cache", fontSize = 12.sp
				)
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsUI(
	eventColor: Int,
	buildingColor: Int,
	nodeColor: Int,
	setEventColor: (Int) -> Unit,
	setBuildingColor: (Int) -> Unit,
	setNodeColor: (Int) -> Unit,
) {
	Column(
		modifier = Modifier.padding(horizontal = 8.dp),
		verticalArrangement = Arrangement.spacedBy(4.dp)
	) {
		Text(
			text = "Make it your own",
			style = MaterialTheme.typography.titleMedium
		)

		var isOptionsVisible by remember {
			mutableStateOf(false)
		}

		Card(
			modifier = Modifier
				.fillMaxWidth()
		) {
			IconToggleButton(
				isOptionsVisible,
				onCheckedChange = { isOptionsVisible = it },
				modifier = Modifier
					.padding(vertical = 10.dp, horizontal = 14.dp)
					.fillMaxWidth(),
			) {
				Row(
					modifier = Modifier.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Box(
						modifier = Modifier.size(34.dp)
					) {
						// You can add content or other icon here
					}
					Row(
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.Start
					) {
						Column(
							verticalArrangement = Arrangement.Center
						) {
							Text(
								text = "Customize",
								fontSize = 14.sp,
								modifier = Modifier.align(Alignment.CenterHorizontally)
							)
							Text(
								text = "Change pinpoint colors",
								fontSize = 10.sp,
								modifier = Modifier.align(Alignment.CenterHorizontally)
							)
						}
					}
					Icon(
						painter = if (isOptionsVisible) {
							painterResource(id = R.drawable.down_arrow) // Use a different icon
						} else {
							painterResource(id = R.drawable.right_arrow) // Default arrow icon
						}, contentDescription = "", modifier = Modifier.size(16.dp)
					)
				}
			}
		}

		AnimatedVisibility(visible = isOptionsVisible) {
			Column {

				//Events
				Card(
					onClick = {}, modifier = Modifier
						.padding(bottom = 8.dp)
						.fillMaxWidth()
				) {
					//Lets users set colors for events
					PinpointColorItem(name = stringResource(id = R.string.options_events_label),
						img = painterResource(id = R.drawable.event),
						imgDescription = stringResource(
							id = R.string.explanation_event_img,
						),
						Color(eventColor),
						onColorSelected = {
							setEventColor(it.toArgb())
						})
				}
				//Buildings
				Card(
					onClick = {}, modifier = Modifier
						.padding(bottom = 8.dp)
						.fillMaxWidth()
				) {
					//Lets users set colors for buildings
					PinpointColorItem(name = stringResource(id = R.string.options_buildings_label),
						img = painterResource(id = R.drawable.building_icon),
						imgDescription = stringResource(
							id = R.string.explanation_building_img
						),
						Color(buildingColor),
						onColorSelected = {
							setBuildingColor(it.toArgb())
						})
				}
				// Utilities
				Card(
					onClick = {}, modifier = Modifier
						.padding(bottom = 8.dp)
						.fillMaxWidth()
				) {
					//Lets users set colors for utilities
					PinpointColorItem(name = stringResource(id = R.string.options_utilities_label),
						img = painterResource(id = R.drawable.node),
						imgDescription = stringResource(
							id = R.string.explanation_node_img
						),
						Color(nodeColor),
						onColorSelected = {
							setNodeColor(it.toArgb())
						})
				}
			}
		}
	}
}

data class Author(
	val name: String,
	@DrawableRes
	val image: Int,
	val link: Uri
)

val authors = listOf(
	Author(
		"Rahim Akhter",
		R.drawable.rahim,
		Uri.parse("https://www.linkedin.com/in/rahim-akhter-2002")
	),
	Author(
		"Harvey Tseng",
		R.drawable.harvey,
		Uri.parse("https://www.linkedin.com/in/harvey-tseng/")
	),
	Author(
		"Olivia Sanfilippo",
		R.drawable.olivia,
		Uri.parse("https://www.linkedin.com/in/olivia-sanfilippo/")
	),
	Author(
		"Aaron Tabuteau",
		R.drawable.aaron,
		Uri.parse("https://www.linkedin.com/in/atabuteau/")
	),
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AuthorsCard() {
	Column(
		Modifier
			.padding(8.dp)
			.fillMaxWidth(),
		verticalArrangement = Arrangement.spacedBy(4.dp)
	) {
		Text(
			text = stringResource(R.string.team_clam),
			style = MaterialTheme.typography.titleMedium
		)

		FlowRow(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
			verticalArrangement = Arrangement.spacedBy(8.dp)
		) {
			// Add Authors
			authors.forEach { AuthorCard(it) }
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorCard(
	author: Author
) {
	val context = LocalContext.current
	val intent = Intent(Intent.ACTION_VIEW, author.link)

	Card(
		onClick = {
			context.startActivity(intent)
		},
		modifier = Modifier
			.width(80.dp)
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Image(
				painter = painterResource(id = author.image),
				contentDescription = "",
				modifier = Modifier.fillMaxWidth()
			)

			Text(
				text = author.name,
				style = MaterialTheme.typography.labelMedium,
				textAlign = TextAlign.Center,
				modifier = Modifier.padding(4.dp)
			)
		}
	}
}