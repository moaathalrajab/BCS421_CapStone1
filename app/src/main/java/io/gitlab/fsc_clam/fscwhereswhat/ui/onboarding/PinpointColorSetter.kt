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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.drawColorIndicator
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import io.gitlab.fsc_clam.fscwhereswhat.R
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.ui.theme.headFont
import io.gitlab.fsc_clam.fscwhereswhat.ui.theme.primaryColor

@Composable
fun PinpointColorItem(
	name: String,
	img: Painter,
	imgDescription: String,
	color: Color,
	onColorSelected: (Color) -> Unit,
) {
	val controller = rememberColorPickerController()
	var hexCode by remember { mutableStateOf("") }

	var isDialogVisible by remember {
		mutableStateOf(false)
	}

	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween,
		modifier = Modifier.fillMaxWidth()
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
		) {
			Image(
				painter = img,
				contentDescription = imgDescription,
				modifier = Modifier.requiredSize(50.dp),
			)
			Text(
				text = name,
				fontFamily = headFont,
				fontWeight = FontWeight.Bold,
				fontStyle = FontStyle.Normal,
				fontSize = 32.sp,
				textAlign = TextAlign.Center,
				modifier = Modifier.padding(start = 16.dp)
			)
		}

		//Will set dialog visible
		IconButton(
			onClick = {
				isDialogVisible = true
			},
		) {
			Icon(
				Icons.Default.Edit,
				stringResource(id = R.string.edit),
				Modifier.background(color)
			)
		}
	}

	if (isDialogVisible) {
		Dialog(onDismissRequest = { isDialogVisible = false }) {
			var textColor by remember { mutableStateOf(Color.Transparent) }
			Column(
				Modifier
					.background(textColor)
					.fillMaxHeight(.6f),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(
					text = name,
					fontFamily = headFont,
					fontWeight = FontWeight.Bold,
					fontStyle = FontStyle.Normal,
					fontSize = 32.sp,
					textAlign = TextAlign.Center,
				)
				HsvColorPicker(
					modifier = Modifier
						.padding(5.dp)
						.weight(1f),
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
					initialColor = Color.Black,
				)
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.background(Color.White),
					horizontalArrangement = Arrangement.End,
					verticalAlignment = Alignment.CenterVertically
				) {
					TextButton(onClick = { isDialogVisible = false }) {
						Text(text = stringResource(id = android.R.string.cancel))
					}
					TextButton(onClick = {
						onColorSelected(textColor)
						isDialogVisible = false
					}) {
						Text(text = stringResource(id = R.string.options_confirm_button))
					}
				}
			}
		}
	}
}

@Preview
@Composable
fun PreviewPinpointSetting() {
	Surface {
		PinpointColorItem(
			name = "EVENT",
			img = painterResource(id = R.drawable.flag_icon),
			imgDescription = stringResource(
				id = R.string.explanation_event_img,
			),
			Color.Red,
			onColorSelected = {}
		)
	}

}


