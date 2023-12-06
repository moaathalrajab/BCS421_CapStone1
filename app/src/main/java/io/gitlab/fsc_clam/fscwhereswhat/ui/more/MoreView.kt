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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.gitlab.fsc_clam.fscwhereswhat.R
import io.gitlab.fsc_clam.fscwhereswhat.ui.onboarding.PinpointColorItem
import io.gitlab.fsc_clam.fscwhereswhat.ui.theme.FSCWheresWhatTheme
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.MoreViewModel
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.impl.ImplMoreViewModel


@Composable
fun MoreView(
	navToNotes: () -> Unit, navToReminders: () -> Unit
) {
	val viewModel: MoreViewModel = viewModel<ImplMoreViewModel>()
	val exception by viewModel.exceptions.collectAsState(initial = null)
	val cacheStatus by viewModel.cacheStatus.collectAsState(initial = null)

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
			navToNotes,
			navToReminders,
			clearCacheLogic = viewModel::clearCache,
			snackbarHostState //
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
		snackbarHostState = SnackbarHostState()
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreContent(
	navToNotes: () -> Unit,
	navToReminders: () -> Unit,
	clearCacheLogic: () -> Unit,
	snackbarHostState: SnackbarHostState
) {

	Scaffold(topBar = {
		TopAppBar(title = {
			Text(text = "More View") // TODO: Subject to change
		})
	}, snackbarHost = { SnackbarHost(snackbarHostState) }
	) {
		Column(modifier = Modifier.padding(it)) {
			// Reminder Card
			Card(
				onClick = {
					navToReminders
				}, modifier = Modifier
					.padding(8.dp)
					.fillMaxWidth()
			) {
				Row(
					modifier = Modifier
						.padding(vertical = 10.dp, horizontal = 14.dp)
						.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Box(
						modifier = Modifier.size(34.dp)
					) {
						Icon(
							painter = painterResource(id = R.drawable.notification_bell),
							contentDescription = "",
							modifier = Modifier.padding(8.dp)
						)
					}
					Row(
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.Start
					) {

						Column(
							verticalArrangement = Arrangement.Center
						) {
							Text(
								text = "Reminders",
								fontSize = 14.sp,
								modifier = Modifier.align(Alignment.CenterHorizontally)
							)
							Text(
								text = "View and edit reminders",
								fontSize = 10.sp,
								modifier = Modifier.align(Alignment.CenterHorizontally)
							)

						}

					}
					Box(
						modifier = Modifier.size(16.dp)
					)
				}
			}

			//  NOTES VIEW
			Card(
				onClick = {
					navToNotes
				}, modifier = Modifier
					.padding(8.dp)
					.fillMaxWidth()
			) {
				Row(
					modifier = Modifier
						.padding(vertical = 10.dp, horizontal = 14.dp)
						.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Box(
						modifier = Modifier.size(34.dp)
					) {
						Icon(
							painter = painterResource(id = R.drawable.note_icon),
							contentDescription = "",
							modifier = Modifier.padding(8.dp)
						)
					}
					Row(
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.Start
					) {

						Column(
							verticalArrangement = Arrangement.Center
						) {
							Text(
								text = "Notes",
								fontSize = 14.sp,
								modifier = Modifier.align(Alignment.CenterHorizontally)
							)
							Text(
								text = "View and edit notes",
								fontSize = 10.sp,
								modifier = Modifier.align(Alignment.CenterHorizontally)
							)

						}

					}
					Box(
						modifier = Modifier.size(16.dp)
					)
				}
			}

			OptionsUI(Color.Black.toArgb(), Color.Black.toArgb(), Color.Black.toArgb())
			Spacer(modifier = Modifier.height(100.dp))
			ProfileCard()
			ClearCacheCard(clearCacheLogic)
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
fun OptionsUI(eventColor: Int, buildingColor: Int, utilityColor: Int) {
	Column(
		modifier = Modifier
			.padding(horizontal = 14.dp)
			.padding(top = 10.dp)
	) {
		Text(
			text = "Make it your own",
			fontSize = 14.sp,
			modifier = Modifier.padding(vertical = 8.dp)
		)
		Customize(eventColor, buildingColor, utilityColor)
	}
}

@ExperimentalMaterial3Api
@Composable
fun Customize(eventColor: Int, buildingColor: Int, utilityColor: Int) {
	var isOptionsVisible by remember {
		mutableStateOf(false)
	}

	Card(
		modifier = Modifier
			.padding(bottom = 10.dp)
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
					img = painterResource(id = R.drawable.flag_icon),
					imgDescription = stringResource(
						id = R.string.explanation_event_img,
					),
					Color(eventColor),
					onColorSelected = {
						//setEventColor(it.toArgb())
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
						//setBuildingColor(it.toArgb())
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
					img = painterResource(id = R.drawable.node_icon),
					imgDescription = stringResource(
						id = R.string.explanation_node_img
					),
					Color(utilityColor),
					onColorSelected = {
						//setUtilityColor(it.toArgb())
					})
			}
		}
	}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCard() {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.height(175.dp)
			.padding(10.dp)
			.padding(8.dp),
	) {
		Row(
			modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.Top
		) {
			Column(
				modifier = Modifier.weight(1f),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(
					text = "Check us out!", fontSize = 16.sp, fontWeight = FontWeight.Bold
				)
			}
		}
		Row(
			modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.Bottom
		) {
			Column(
				modifier = Modifier
					.weight(1f)
					.height(60.dp)
					.fillMaxWidth(),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally,
			) {
				Row(
					modifier = Modifier
						.padding(top = 8.dp)
						.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceEvenly,
					verticalAlignment = Alignment.Bottom
				) {
					Column(horizontalAlignment = Alignment.CenterHorizontally) {
						// Image or Icon
						// Text underneath

						Text(
							text = "Rahim Ahkter", fontSize = 8.sp, textAlign = TextAlign.Center
						)
					}

					Column(horizontalAlignment = Alignment.CenterHorizontally) {
						// Image or Icon composable
						// Text underneath

						Text(
							text = "Harvey Tseng", fontSize = 8.sp, textAlign = TextAlign.Center
						)
					}

					Column(horizontalAlignment = Alignment.CenterHorizontally) {
						// Image or Icon composable
						// Text underneath

						Text(
							text = "Aaron Tabuteau", fontSize = 8.sp, textAlign = TextAlign.Center
						)
					}

					Column(horizontalAlignment = Alignment.CenterHorizontally) {
						// Icon 4
						// Image or Icon composable
						// Text underneath

						Text(
							text = "Olivia Sanfilippo",
							fontSize = 8.sp,
							textAlign = TextAlign.Center
						)
					}
				}
			}
		}
		Row(
			modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.Bottom
		) {
			Column(
				modifier = Modifier.weight(1f),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally,
			) {
				Text(
					text = "Team CLAM \nVersion #1.0", fontSize = 8.sp, fontWeight = FontWeight.Bold
				)
			}
		}
	}
}





