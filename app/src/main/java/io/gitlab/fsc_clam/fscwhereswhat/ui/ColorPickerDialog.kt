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

package io.gitlab.fsc_clam.fscwhereswhat.ui

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.drawColorIndicator
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import io.gitlab.fsc_clam.fscwhereswhat.ui.theme.headFont

@Composable
fun colorPicker (name: String, onDismissRequest: () -> Unit, onColorSelected: (Color) -> Unit){

	//controls the Color Picker
	val controller = rememberColorPickerController()
	var hexCode by remember { mutableStateOf("") }

	Dialog(onDismissRequest ) {
		var textColor by remember { mutableStateOf(Color.Transparent) }
		//Background of dialog changes to match with currently selected color
		Column(
			Modifier
				.background(textColor)
				.fillMaxHeight(.6f),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			//EntityName as header
			Text(
				text = name,
				fontFamily = headFont,
				fontWeight = FontWeight.Bold,
				fontStyle = FontStyle.Normal,
				fontSize = 32.sp,
				textAlign = TextAlign.Center,
			)
			//Color picker
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
			//Contains two buttons to either confirm choice or exit dialog
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.background(Color.White),
				horizontalArrangement = Arrangement.End,
				verticalAlignment = Alignment.CenterVertically
			) {
				//Exits dialog
				TextButton(onDismissRequest ) {
					Text(text = stringResource(id = R.string.cancel))
				}
				//Confirms color choice
				TextButton(onClick = {
					onColorSelected(textColor)
					onDismissRequest()
				}) {
					Text(text = stringResource(id = io.gitlab.fsc_clam.fscwhereswhat.R.string.options_confirm_button))
				}
			}
		}
	}

}