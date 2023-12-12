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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.gitlab.fsc_clam.fscwhereswhat.R


/**
 * Screen to thank users
 */
@Composable
fun ThanksScreen() {
	OnboardingScreenPage {
		Column(
			modifier = Modifier
				.padding(8.dp)
				.fillMaxSize(),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			//Developer logo
			Image(
				painter = painterResource(id = R.drawable.ic_launcher_foreground),
				contentDescription = stringResource(id = R.string.thanks_developers_logo),
				contentScale = ContentScale.Crop,
				modifier = Modifier
			)

			Text(
				text = stringResource(id = R.string.thanks_heading),
				style = MaterialTheme.typography.headlineLarge
			)

			Text(
				text = stringResource(id = R.string.thanks_body),
				style = MaterialTheme.typography.headlineMedium
			)
		}
	}
}

@Preview
@Composable
fun PreviewThanksScreen() {
	Box(
		modifier = Modifier
			.fillMaxSize()
			.paint(
				painterResource(id = R.drawable.welcome_screen_background),
				contentScale = ContentScale.FillBounds
			)
	) {
		ThanksScreen()
	}
}