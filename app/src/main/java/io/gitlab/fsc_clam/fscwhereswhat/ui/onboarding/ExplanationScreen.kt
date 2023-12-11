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
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.gitlab.fsc_clam.fscwhereswhat.R
import io.gitlab.fsc_clam.fscwhereswhat.ui.theme.bodyFont
import io.gitlab.fsc_clam.fscwhereswhat.ui.theme.headFont

/**
 * Screen to explain the layout of the mop
 */
@Composable
fun ExplanationScreen() {
		Column(
			modifier = Modifier
				.padding(12.dp)
				.background(Color.White)
				.fillMaxSize()
				.verticalScroll(rememberScrollState()),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			//App Logo
			Image(
				painter = painterResource(id = R.drawable.wheres_what_logo),
				contentDescription = stringResource(id = R.string.app_logo_description),
				contentScale = ContentScale.Crop,
				modifier = Modifier
					.padding(vertical = 15.dp)
					.size(280.dp)
					.clip(RoundedCornerShape(25))
			)
			//Header
			Text(
				text = stringResource(id = R.string.headline),
				fontFamily = headFont,
				fontWeight = FontWeight.Bold,
				fontStyle = FontStyle.Italic,
				fontSize = 36.sp,
			)
			//Subheading
			Text(
				text = stringResource(id = R.string.explanation_body),
				fontFamily = bodyFont,
				fontWeight = FontWeight.Normal,
				fontStyle = FontStyle.Normal,
				fontSize = 20.sp,
				textAlign = TextAlign.Center,
			)

			Column(
				modifier = Modifier
					.fillMaxSize(),
				verticalArrangement = Arrangement.SpaceEvenly
			) {
				//Explains Event
				EntityExplanations(
					explanationText = stringResource(id = R.string.explanation_event),
					img = painterResource(
						id = R.drawable.flag_icon
					),
					imgDescription = stringResource(id = R.string.explanation_event_img)
				)
				//Explains Buildings
				EntityExplanations(
					explanationText = stringResource(id = R.string.explanation_building),
					img = painterResource(
						id = R.drawable.building_icon
					),
					imgDescription = stringResource(id = R.string.explanation_building_img)
				)
				//Explains nodes(utilities)
				EntityExplanations(
					explanationText = stringResource(id = R.string.explanation_node),
					img = painterResource(
						id = R.drawable.node_icon
					),
					imgDescription = stringResource(id = R.string.explanation_node_img)
				)
			}
		}
	}

@Preview
@Composable
fun PreviewExplanationScreen() {
	//Creates background
	Box(
		modifier = Modifier
			.fillMaxSize()
			.paint(
				painterResource(id = R.drawable.welcome_screen_background),
				contentScale = ContentScale.FillBounds
			)
	) {
		ExplanationScreen()
	}

}

/**
 * Creates explanation for the each EntityType - Event, Building, Node
 * @param explanationText is the text explaining how each what each EntityType is and how they are represented
 * @param img is the image of the pinpoint for each EntityType
 * @param imgDescription is the content description
 */
@Composable
fun EntityExplanations(explanationText: String, img: Painter, imgDescription: String) {
	Box(
		Modifier
			.fillMaxWidth()
			.background(Color.White)
	) {
		Text(
			text = explanationText,
			fontFamily = bodyFont,
			fontWeight = FontWeight.Normal,
			fontStyle = FontStyle.Normal,
			fontSize = 16.sp,
			modifier = Modifier
				.fillMaxWidth(.8f)
				.padding(bottom = 10.dp, start = 5.dp)
				.align(Alignment.CenterStart)
		)

		Image(
			painter = img,
			contentDescription = imgDescription,
			modifier = Modifier
				.padding(end = 5.dp)
				.fillMaxWidth(.2f)
				.align(Alignment.CenterEnd)
				.requiredSize(35.dp)
		)

	}
}

@Preview
@Composable
fun PreviewEntityExplanations() {
	Surface {
		EntityExplanations(
			explanationText = stringResource(id = R.string.explanation_event),
			img = painterResource(
				id = R.drawable.flag_icon
			),
			imgDescription = stringResource(id = R.string.explanation_event_img)
		)
	}
}