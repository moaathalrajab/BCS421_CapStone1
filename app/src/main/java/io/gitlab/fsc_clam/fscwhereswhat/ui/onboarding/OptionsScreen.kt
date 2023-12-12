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

package io.gitlab.fsc_clam.fscwhereswhat.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.gitlab.fsc_clam.fscwhereswhat.R
import io.gitlab.fsc_clam.fscwhereswhat.ui.theme.bodyFont
import io.gitlab.fsc_clam.fscwhereswhat.ui.theme.headFont
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.OptionsViewModel
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.impl.ImplOptionsViewModel

/**
 * Screen to let users set pinpoint colors
 */
@Composable
fun OptionsScreen() {
	val optionsViewModel: OptionsViewModel = viewModel<ImplOptionsViewModel>()
	val eventColor by optionsViewModel.eventColor.collectAsState()
	val buildingColor by optionsViewModel.buildingColor.collectAsState()
	val utilityColor by optionsViewModel.utilityColor.collectAsState()
	OptionsContent(
		eventColor = eventColor,
		buildingColor = buildingColor,
		utilityColor = utilityColor,
		setEventColor = optionsViewModel::setEventColor,
		setBuildingColor = optionsViewModel::setBuildingColor,
		setUtilityColor = optionsViewModel::setUtilityColor
	)

}

/**
 * Content for the Options Screen
 * @param eventColor is passed from the optionsViewModel
 * @param buildingColor is passed from the optionsViewModel
 * @param utilityColor is passed from the optionsViewModel
 * @param setEventColor is called to the optionsViewModel
 * @param setBuildingColor is called to the optionsViewModel
 * @param setUtilityColor is called to the optionsViewModel
 */
@Composable
fun OptionsContent(
	eventColor: Int,
	buildingColor: Int,
	utilityColor: Int,
	setEventColor: (Int) -> Unit,
	setBuildingColor: (Int) -> Unit,
	setUtilityColor: (Int) -> Unit
) {
	Column(
		modifier = Modifier
			.padding(16.dp)
			.fillMaxSize()
			.background(Color.White)
			.verticalScroll(rememberScrollState()),
		verticalArrangement = Arrangement.spacedBy(
			16.dp,
			alignment = Alignment.CenterVertically
		),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		//This column only contains the headings
		Column(
			modifier = Modifier.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally

		) {
			//Heading
			Text(
				text = stringResource(id = R.string.options_heading),
				fontFamily = headFont,
				fontWeight = FontWeight.Bold,
				fontStyle = FontStyle.Normal,
				fontSize = 48.sp,
				textAlign = TextAlign.Center
			)
			//Subheading
			Text(
				text = stringResource(id = R.string.options_subheading),
				fontFamily = bodyFont,
				fontWeight = FontWeight.Normal,
				fontStyle = FontStyle.Normal,
				fontSize = 16.sp,
				textAlign = TextAlign.Center
			)
		}
		//App logo
		Image(
			painter = painterResource(id = R.drawable.ic_launcher_foreground),
			contentDescription = stringResource(id = R.string.app_logo_description),
			contentScale = ContentScale.Crop,
			modifier = Modifier
				.padding(vertical = 15.dp)
				.size(280.dp)
				.clip(RoundedCornerShape(25))
		)
		//This column contains the three color setters for each EntityType
		Column(
			modifier = Modifier.fillMaxWidth(),
			verticalArrangement = Arrangement.spacedBy(
				16.dp,
				alignment = Alignment.CenterVertically
			)
		) {
			//Lets users set colors for events
			PinpointColorItem(
				name = stringResource(id = R.string.options_events_label),
				img = painterResource(id = R.drawable.event),
				imgDescription = stringResource(
					id = R.string.explanation_event_img,
				),
				Color(eventColor),
				onColorSelected = {
					setEventColor(it.toArgb())
				}
			)
			//Lets users set colors for buildings
			PinpointColorItem(
				name = stringResource(id = R.string.options_buildings_label),
				img = painterResource(id = R.drawable.building_icon),
				imgDescription = stringResource(
					id = R.string.explanation_building_img
				),
				Color(buildingColor),
				onColorSelected = {
					setBuildingColor(it.toArgb())
				}
			)
			//Lets users set colors for utilities
			PinpointColorItem(
				name = stringResource(id = R.string.options_utilities_label),
				img = painterResource(id = R.drawable.node),
				imgDescription = stringResource(
					id = R.string.explanation_node_img
				),
				Color(utilityColor),
				onColorSelected = {
					setUtilityColor(it.toArgb())
				}
			)
		}

	}
}

@Preview
@Composable
fun PreviewOptionsContent() {
	//Creates background
	Box(
		modifier = Modifier
			.fillMaxSize()
			.paint(
				painterResource(id = R.drawable.welcome_screen_background),
				contentScale = ContentScale.FillBounds
			)
	) {
		OptionsContent(50, 50, 50, {}, {}, {})
	}
}

