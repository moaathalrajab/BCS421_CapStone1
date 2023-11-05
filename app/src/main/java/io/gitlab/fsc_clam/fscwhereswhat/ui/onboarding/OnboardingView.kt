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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.gitlab.fsc_clam.fscwhereswhat.R
import kotlinx.coroutines.launch

/**
 * Overall Onboarding View, containing a Horizontal Pager to switch to each Onboarding Screen
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingView(){
	val pagerState = rememberPagerState(
		initialPage = 0,
		initialPageOffsetFraction = 0f
	) {
		5
	}
	val state = rememberCoroutineScope()

	Scaffold(
		bottomBar = {
			BottomAppBar(
				actions = {
					IconButton(onClick = {
						state.launch {
							pagerState.scrollToPage(pagerState.currentPage - 1)
						}
					},
						) {
						Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(id = R.string.arrow_backward))
					}
					Row(
						Modifier
							.wrapContentHeight()
							.fillMaxWidth(.85f)
							.padding(bottom = 8.dp),
						horizontalArrangement = Arrangement.Center,
					) {
						repeat(pagerState.pageCount) { iteration ->
							val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
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
						Icon(Icons.Filled.ArrowForward, contentDescription = stringResource(id = R.string.arrow_forward))
					}
				},
			)

		}
	) {
		HorizontalPager(
			modifier = Modifier.padding(it),
			state = pagerState,
		) { page ->
			when (page) {
				0 -> WelcomeScreen()
				1 -> ExplanationScreen()
				2 -> LoginScreen()
				3 -> OptionsScreen()
				4 -> ThanksScreen()
			}
		}
	}

}

@Preview
@Composable
fun PreviewOnboardingView(){
	OnboardingView()
}
