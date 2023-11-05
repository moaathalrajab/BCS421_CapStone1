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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.gitlab.fsc_clam.fscwhereswhat.R
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Image

/**
 * Screen to explain the layout of the mop
 */
@Composable
fun ExplanationScreen(){
	val headFont = FontFamily(
		Font(R.font.lobster_two_regular, FontWeight.Normal),
		Font(R.font.lobster_two_bold, FontWeight.Bold),
		Font(R.font.lobster_two_italic, FontWeight.Normal, FontStyle.Italic),
		Font(R.font.lobster_two_bold_italic, FontWeight.Bold, FontStyle.Italic)
	)
	val bodyFont = FontFamily(
		Font(R.font.lilitaone_regular, FontWeight.Normal)
	)
	val logo = Image.Drawable(R.drawable.wheres_what_logo)
	val background = Image.Drawable(R.drawable.welcome_screen_background)

	val entityExplanations = listOf(EntityType.EVENT, EntityType.BUILDING, EntityType.NODE)
	Box(
		modifier = with(Modifier) {
			fillMaxSize()
				.paint(
					painterResource(id = background.drawable),
					contentScale = ContentScale.FillBounds
				)
		}) {
		Box(modifier = Modifier
			.align(Alignment.Center)
			.fillMaxWidth(.9f)
			.fillMaxHeight(.9f)
			.background(Color.White)){
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.wrapContentSize(),
				verticalArrangement = Arrangement.Top,
				horizontalAlignment = Alignment.CenterHorizontally
			){
				Image(
					painter = painterResource(id = logo.drawable),
					contentDescription = stringResource(id = R.string.app_logo_description),
					contentScale = ContentScale.Crop,
					modifier = Modifier
						.padding(vertical = 15.dp)
						.size(320.dp)
						.clip(RoundedCornerShape(25))
				)
				Column(
					modifier = Modifier
						.fillMaxSize(),
					verticalArrangement = Arrangement.Top,
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					Text(
						text = stringResource(id = R.string.headline),
						fontFamily = headFont,
						fontWeight = FontWeight.Bold,
						fontStyle = FontStyle.Italic,
						fontSize = 40.sp,
					)
					Text(
						text = stringResource(id = R.string.explanation_body),
						fontFamily = bodyFont,
						fontWeight = FontWeight.Normal,
						fontStyle = FontStyle.Normal,
						fontSize = 20.sp,
						textAlign = TextAlign.Center,
					)

					Divider(Modifier.padding(vertical = 15.dp), thickness = 2.dp, color = Color.Black)

					entityExplanations.forEach {type ->
						EntityExplanations(type = type)
					}
				}
			}
		}
	}
}

@Preview
@Composable
fun PreviewExplanationScreen(){
	ExplanationScreen()
}

@Composable
fun EntityExplanations(type: EntityType){
	val eventImg = Image.Drawable(R.drawable.flag_icon)
	val buildingImg = Image.Drawable(R.drawable.building_icon)
	val nodeImg = Image.Drawable(R.drawable.node_icon)
	val explanationText = when(type){
		EntityType.EVENT -> stringResource(id = R.string.explanation_event)
		EntityType.BUILDING -> stringResource(id = R.string.explanation_building)
		EntityType.NODE -> stringResource(id = R.string.explanation_node)
	}
	val img = when(type){
		EntityType.EVENT -> painterResource(id = eventImg.drawable)
		EntityType.BUILDING -> painterResource(id = buildingImg.drawable)
		EntityType.NODE -> painterResource(id = nodeImg.drawable)
	}
	val imgDescription = when(type){
		EntityType.EVENT -> stringResource(id = R.string.explanation_event_img)
		EntityType.BUILDING -> stringResource(id = R.string.explanation_building_img)
		EntityType.NODE -> stringResource(id = R.string.explanation_node_img)
	}
	val explanationFont = FontFamily(
		Font(R.font.lilitaone_regular, FontWeight.Normal)
	)
	Box(
		Modifier
			.fillMaxWidth()
			.background(Color.White)) {
		Text(
			text = explanationText,
			fontFamily = explanationFont,
			fontWeight = FontWeight.Normal,
			fontStyle = FontStyle.Normal,
			fontSize = 12.sp,
			modifier = Modifier
				.fillMaxWidth(.8f)
				.padding(bottom = 15.dp)
				.offset(x = 5.dp)
				.align(Alignment.CenterStart)
		)

		Image(
			painter = img,
			contentDescription = imgDescription,
			modifier = Modifier
				.align(Alignment.TopEnd)
				.requiredSize(50.dp)
			)
		Spacer(Modifier.padding(vertical = 30.dp))
	}
}

@Preview
@Composable
fun PreviewEnttiyExplanations(){
	Surface(Modifier.fillMaxSize()) {
		Column {
			EntityExplanations(type = EntityType.EVENT)
			EntityExplanations(type = EntityType.BUILDING)
			EntityExplanations(type = EntityType.NODE)
		}
	}
}