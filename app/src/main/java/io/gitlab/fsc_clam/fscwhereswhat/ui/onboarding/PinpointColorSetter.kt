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

import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.drawColorIndicator
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import io.gitlab.fsc_clam.fscwhereswhat.R
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Image
import io.gitlab.fsc_clam.fscwhereswhat.ui.theme.primaryColor
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.impl.ImplOptionsViewModel

@Composable
fun PinpointColorSetter(type: EntityType,){
	val controller = rememberColorPickerController()
	var hexCode by remember { mutableStateOf("") }
	//the current compose Color selected
	var textColor by remember { mutableStateOf(Color.Transparent) }
	val headFont = FontFamily(
		Font(R.font.lilitaone_regular)
	)
	val eventImg = Image.Drawable(R.drawable.flag_icon)
	val buildingImg = Image.Drawable(R.drawable.building_icon)
	val nodeImg = Image.Drawable(R.drawable.node_icon)
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

	val optionsViewModel: ImplOptionsViewModel = viewModel()

	Column(horizontalAlignment = Alignment.CenterHorizontally) {
		Text(
			text = type.toString(),
			fontFamily = headFont,
			fontWeight = FontWeight.Bold,
			fontStyle = FontStyle.Normal,
			fontSize = 32.sp,
			textAlign = TextAlign.Center,
			modifier = Modifier
		)

		Box(modifier = Modifier.weight(1f)) {
			HsvColorPicker(
				modifier = Modifier
					.padding(5.dp),
				controller = controller,
				drawOnPosSelected = {
					drawColorIndicator(
						controller.selectedPoint.value,
						controller.selectedColor.value,
					)
				},
				onColorChanged = { colorEnvelope ->
					hexCode = colorEnvelope.hexCode
					textColor = colorEnvelope.color
				},
				initialColor = Color.Red,
			)
		}


		AlphaSlider(
			modifier = Modifier
				.fillMaxWidth()
				.padding(10.dp)
				.height(35.dp)
				.align(Alignment.CenterHorizontally),
			controller = controller,
		)

		BrightnessSlider(
			modifier = Modifier
				.fillMaxWidth()
				.padding(10.dp)
				.height(35.dp)
				.align(Alignment.CenterHorizontally),
			controller = controller,
		)

		Text(
			text = "#$hexCode",
			color = Color.Black,
			fontSize = 16.sp,
			fontWeight = FontWeight.Bold,
			modifier = Modifier.align(Alignment.CenterHorizontally),
		)

		Row(
			Modifier
				.padding(bottom = 15.dp)
				.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceAround,
			verticalAlignment = Alignment.CenterVertically
			) {
			AlphaTile(
				modifier = Modifier
					.size(80.dp)
					.clip(RoundedCornerShape(6.dp)),
				controller = controller,
			)

			Image(
				painter = img,
				contentDescription = imgDescription,
				modifier = Modifier.requiredSize(50.dp),
				colorFilter = ColorFilter.tint(textColor)
			)
			//Will set color of EntityType to current textColor
			Button(
				onClick = {
					val color = textColor.toArgb()
					when (type) {
						EntityType.EVENT -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
							optionsViewModel.setEventColor(android.graphics.Color.valueOf(color))
						}

						EntityType.BUILDING -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
							optionsViewModel.setBuildingColor(android.graphics.Color.valueOf(color))
						}
						//Only setting color for Building and Event
						EntityType.NODE -> {}
					}
				},
				colors = ButtonDefaults.buttonColors(primaryColor)

				) {
				Text(text = stringResource(id = R.string.options_confirm_button), color = Color.Black)
			}
		}
	}
}

@Preview
@Composable
fun PreviewPinpointSetting(){
	val img = Image.Drawable(R.drawable.wheres_what_logo)
	val controller = rememberColorPickerController()
	PinpointColorSetter(type = EntityType.EVENT)
}