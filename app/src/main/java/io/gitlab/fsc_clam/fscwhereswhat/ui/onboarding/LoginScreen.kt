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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.gitlab.fsc_clam.fscwhereswhat.R
import io.gitlab.fsc_clam.fscwhereswhat.ui.theme.bodyFont
import io.gitlab.fsc_clam.fscwhereswhat.ui.theme.headFont
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.LoginViewModel
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.impl.ImplLoginViewModel

/**
 * Screen to let users log in with Google
 */
@Composable
fun LoginScreen() {
	val loginViewModel: LoginViewModel = viewModel<ImplLoginViewModel>()
	//Creates background
	Box(
		modifier = with(Modifier) {
			fillMaxSize()
				.paint(
					painterResource(id = R.drawable.welcome_screen_background),
					contentScale = ContentScale.FillBounds
				)
		})
	{
		//Creates white box content container
		Box(
			modifier = Modifier
				.align(Alignment.Center)
				.fillMaxWidth(.9f)
				.fillMaxHeight(.8f)
				.background(Color.White)
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.wrapContentSize(),
				verticalArrangement = Arrangement.Top,
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				//Heading
				Text(
					text = stringResource(id = R.string.login_heading),
					fontFamily = headFont,
					fontWeight = FontWeight.Bold,
					fontStyle = FontStyle.Normal,
					fontSize = 48.sp,
					textAlign = TextAlign.Center
				)
				//App logo
				Image(
					painter = painterResource(id = R.drawable.wheres_what_logo),
					contentDescription = stringResource(id = R.string.app_logo_description),
					contentScale = ContentScale.Crop,
					modifier = Modifier
						.padding(vertical = 15.dp)
						.size(280.dp)
						.clip(RoundedCornerShape(25))
				)
				//Subheading
				Text(
					text = stringResource(id = R.string.login_welcome),
					fontFamily = headFont,
					fontWeight = FontWeight.Normal,
					fontStyle = FontStyle.Italic,
					fontSize = 40.sp,
					textAlign = TextAlign.Center
				)
				//Ask users to login
				Text(
					text = stringResource(id = R.string.login_screen_body),
					fontFamily = bodyFont,
					fontWeight = FontWeight.Normal,
					fontSize = 20.sp,
					textAlign = TextAlign.Center,
					modifier = Modifier.padding(bottom = 30.dp)
				)
				//Clickable image
				//May convert to IconButton
				Image(
					painter = painterResource(id = R.drawable.google_signin),
					contentDescription = stringResource(id = R.string.login_button_description),
					modifier = Modifier
						.wrapContentSize()
						.clickable(onClick = {
							TODO("Login with Google and send to loginViewModel")
						}
						)
				)
			}
		}
	}
}

@Preview
@Composable
fun PreviewLoginScreen() {
	LoginScreen()
}