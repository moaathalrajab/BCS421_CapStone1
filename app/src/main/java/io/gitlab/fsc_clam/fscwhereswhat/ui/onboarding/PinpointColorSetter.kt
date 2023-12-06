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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.gitlab.fsc_clam.fscwhereswhat.R
import io.gitlab.fsc_clam.fscwhereswhat.ui.colorPicker

/**
 * Composable to let users set colors for each EntityType
 * @param name is the EntityType
 * @param img is the associated pinpoint for each EntityType
 * @param imgDescription is the content description for each img
 * @param color is the current color of the pinpoint
 * @param onColorSelected calls to optionsViewModel to set colors
 */
@Composable
fun PinpointColorItem(
	name: String,
	img: Painter,
	imgDescription: String,
	color: Color,
	onColorSelected: (Color) -> Unit,
) {

	var isDialogVisible by remember {
		mutableStateOf(false)
	}

	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween,
		modifier = Modifier.fillMaxWidth()
	) {
		//This row contains the img and name
		Row(
			verticalAlignment = Alignment.CenterVertically,
		) {
			//The Image of the EntityType
			Image(
				painter = img,
				contentDescription = imgDescription,
				modifier = Modifier.requiredSize(50.dp),
				colorFilter = ColorFilter.tint(color)
			)
			//The name of the EntityType
			Text(
				text = name,
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
		//Creates the dialog to let users pick colors
		colorPicker(
			name, onDismissRequest = {
				isDialogVisible = false
			},
			onColorSelected
		)
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


