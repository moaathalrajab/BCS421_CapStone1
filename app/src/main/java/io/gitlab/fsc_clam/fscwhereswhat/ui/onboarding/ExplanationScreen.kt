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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.gitlab.fsc_clam.fscwhereswhat.R

/**
 * Screen to explain the layout of the mop
 */
@Composable
fun ExplanationScreen() {
	OnboardingScreenPage {
		Column(
			modifier = Modifier
				.padding(8.dp)
				.fillMaxSize(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterVertically)
		) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				//Header
				Text(
					text = stringResource(id = R.string.headline),
					style = MaterialTheme.typography.headlineLarge
				)

				//Subheading
				Text(
					text = stringResource(id = R.string.explanation_body),
					textAlign = TextAlign.Center
				)
			}

			Column(
				modifier = Modifier
					.fillMaxSize(),
				verticalArrangement = Arrangement.spacedBy(4.dp)
			) {
				//Explains Event
				ExplanationItem(
					explanationText = stringResource(id = R.string.explanation_event),
					img = painterResource(
						id = R.drawable.event
					),
					imgDescription = stringResource(id = R.string.explanation_event_img)
				)
				//Explains Buildings
				ExplanationItem(
					explanationText = stringResource(id = R.string.explanation_building),
					img = painterResource(
						id = R.drawable.building_icon
					),
					imgDescription = stringResource(id = R.string.explanation_building_img)
				)
				//Explains nodes(utilities)
				ExplanationItem(
					explanationText = stringResource(id = R.string.explanation_node),
					img = painterResource(
						id = R.drawable.node
					),
					imgDescription = stringResource(id = R.string.explanation_node_img)
				)
			}
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
 * An explanation for a given EntityType - Event, Building, Node
 *
 * @param explanationText is the text explaining how each what each EntityType is and how they are represented
 * @param img is the image of the pinpoint for each EntityType
 * @param imgDescription is the content description
 */
@Composable
fun ExplanationItem(explanationText: String, img: Painter, imgDescription: String) {
	Box {
		Row(
			Modifier
				.fillMaxWidth()
				.padding(4.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Image(
				painter = img,
				contentDescription = imgDescription,
				modifier = Modifier
					.weight(.2f)
					.size(35.dp)
			)

			Text(
				text = explanationText,
				modifier = Modifier
					.weight(.8f)
			)
		}
	}
}

@Preview
@Composable
fun PreviewEntityExplanations() {
	Surface {
		ExplanationItem(
			explanationText = stringResource(id = R.string.explanation_event),
			img = painterResource(
				id = R.drawable.event
			),
			imgDescription = stringResource(id = R.string.explanation_event_img)
		)
	}
}