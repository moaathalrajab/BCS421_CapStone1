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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.gitlab.fsc_clam.fscwhereswhat.R
import io.gitlab.fsc_clam.fscwhereswhat.ui.theme.primaryColor
import io.gitlab.fsc_clam.fscwhereswhat.ui.theme.titleFont

/**
 * Screen to thank users
 */
@Composable
fun ThanksScreen(
	onFinish: () -> Unit
) {
	BoxWithConstraints(Modifier.fillMaxSize()) {
		if(maxHeight > 480.dp)
		//Creates white box for content
		Box(
			modifier = Modifier
				.align(Alignment.Center)
				.fillMaxWidth(.8f)
				.fillMaxHeight(.7f)
				.background(Color.White)
		)
		{
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.wrapContentSize(),
				verticalArrangement = Arrangement.Top,
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				//Developer logo
				Image(
					painter = painterResource(id = R.drawable.team_clam_logo),
					contentDescription = stringResource(id = R.string.thanks_developers_logo),
					contentScale = ContentScale.Crop,
					modifier = Modifier
						.padding(top = 5.dp, bottom = 15.dp)
						.size(280.dp)
						.clip(RoundedCornerShape(25))
				)
				Text(
					text = stringResource(id = R.string.thanks_heading),
					fontFamily = titleFont,
					fontWeight = FontWeight.Bold,
					fontStyle = FontStyle.Normal,
					fontSize = 46.sp,
				)
				Text(
					text = stringResource(id = R.string.thanks_body),
					fontFamily = titleFont,
					fontWeight = FontWeight.Bold,
					fontStyle = FontStyle.Normal,
					fontSize = 38.sp,
					modifier = Modifier.padding(top = 15.dp, bottom = 25.dp)
				)
				//Button to navigate to MapView
				Button(
					onClick = onFinish,
					colors = ButtonDefaults.buttonColors(primaryColor),
				) {
					Text(
						text = stringResource(id = R.string.thanks_continue_button),
						color = Color.Black
					)

				}
			}
		}
		else{
			Row(
				modifier = Modifier
					.fillMaxSize(.9f)
					.background(Color.White)
					.align(Alignment.Center),
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				//Developer logo
				Image(
					painter = painterResource(id = R.drawable.team_clam_logo),
					contentDescription = stringResource(id = R.string.thanks_developers_logo),
					contentScale = ContentScale.Crop,
					modifier = Modifier
						.padding(top = 5.dp, bottom = 15.dp)
						.size(280.dp)
						.clip(RoundedCornerShape(25))
				)
				Column(
					modifier = Modifier.fillMaxSize(),
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.spacedBy(8.dp)
				) {
					Text(
						text = stringResource(id = R.string.thanks_heading),
						fontFamily = titleFont,
						fontWeight = FontWeight.Bold,
						fontStyle = FontStyle.Normal,
						fontSize = 46.sp,
					)
					Text(
						text = stringResource(id = R.string.thanks_body),
						fontFamily = titleFont,
						fontWeight = FontWeight.Bold,
						fontStyle = FontStyle.Normal,
						fontSize = 38.sp,
						modifier = Modifier.padding(top = 15.dp, bottom = 25.dp)
					)
					//Button to navigate to MapView
					Button(
						onClick = onFinish,
						colors = ButtonDefaults.buttonColors(primaryColor),
					) {
						Text(
							text = stringResource(id = R.string.thanks_continue_button),
							color = Color.Black
						)

					}
				}
			}
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
		ThanksScreen(
			onFinish = {}
		)
	}
}