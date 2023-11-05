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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

/**
 * Screen to let users set pinpoint colors
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OptionsScreen() {
	val headFont = FontFamily(
		Font(R.font.lilitaone_regular)
	)
	val bodyFont = FontFamily(
		Font(R.font.roboto_regular, FontWeight.Normal)
	)
	val background = Image.Drawable(R.drawable.welcome_screen_background)

	Box(
		modifier = with(Modifier) {
			fillMaxSize()
				.paint(
					painterResource(id = background.drawable),
					contentScale = ContentScale.FillBounds
				)
		}
	)
	{
		Box(
			modifier = Modifier
				.align(Alignment.Center)
				.fillMaxWidth(.9f)
				.fillMaxHeight(.8f)
				.background(Color.White)
		)
		{
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.wrapContentSize()
					.offset(y = (30).dp),
				verticalArrangement = Arrangement.Top,
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(
					text = stringResource(id = R.string.options_heading),
					fontFamily = headFont,
					fontWeight = FontWeight.Bold,
					fontStyle = FontStyle.Normal,
					fontSize = 48.sp,
					textAlign = TextAlign.Center
				)
				Text(
					text = stringResource(id = R.string.options_subheading),
					fontFamily = bodyFont,
					fontWeight = FontWeight.Normal,
					fontStyle = FontStyle.Normal,
					fontSize = 16.sp,
					textAlign = TextAlign.Center
				)
				Divider(Modifier.padding(vertical = 30.dp), thickness = 2.dp, color = Color.Black)

				val pagerState = rememberPagerState(
					initialPage = 0,
					initialPageOffsetFraction = 0f
				) {
					2
				}
				val state = rememberCoroutineScope()
				Scaffold(
					topBar = {
						CenterAlignedTopAppBar(
							title = { Text(text = "") },
							actions = {
								IconButton(
									onClick = {
										state.launch {
											pagerState.scrollToPage(pagerState.currentPage - 1)
										}
									},
								) {
									Icon(
										Icons.Filled.ArrowBack,
										contentDescription = stringResource(id = R.string.arrow_backward)
									)
								}
								Row(
									Modifier
										.wrapContentHeight()
										.fillMaxWidth(.85f)
										.padding(bottom = 8.dp),
									horizontalArrangement = Arrangement.Center,
								) {
									repeat(pagerState.pageCount) { iteration ->
										val color =
											if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
										Box(
											modifier = Modifier
												.padding(2.dp)
												.clip(CircleShape)
												.background(color)
												.size(16.dp)
										)
									}
								}
								IconButton(onClick = {
									state.launch {
										pagerState.scrollToPage(pagerState.currentPage + 1)
									}
								}
								)
								{
									Icon(
										Icons.Filled.ArrowForward,
										contentDescription = stringResource(id = R.string.arrow_forward)
									)
								}
							}
						)
					},
				) {
					HorizontalPager(
						modifier = Modifier.padding(it),
						state = pagerState,
					) { page ->
						when (page) {
							0 -> PinpointColorSetter(
								type = EntityType.EVENT,
							)

							1 -> PinpointColorSetter(
								type = EntityType.BUILDING,
							)
						}
					}
				}

			}
		}
	}
}

@Preview
@Composable
fun PreviewOptionsScreen() {
	OptionsScreen()
}

