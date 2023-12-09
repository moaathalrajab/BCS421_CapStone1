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

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
fun OnboardingView(
	onFinish: () -> Unit
) {
	val pagerState = rememberPagerState(
		initialPage = 0,
		initialPageOffsetFraction = 0f
	) {
		5
	}
	val snackbarState = remember { SnackbarHostState() }
	val state = rememberCoroutineScope()
	Scaffold(
		bottomBar = {
			//Creates the bottom bar which holds two icon buttons for navigation and a page indicator
			BottomAppBar(
				actions = {
					//Create box for alignment
					Box(Modifier.fillMaxSize()) {
						//Button to return to previous page
						IconButton(
							modifier = Modifier.align(Alignment.CenterStart),
							onClick = {
								state.launch {
									pagerState.scrollToPage(pagerState.currentPage - 1)
								}
							},
						) {
							//if on WelcomeScreen, hide arrow backwards
							if (pagerState.currentPage != 0) {
								Icon(
									Icons.Filled.ArrowBack,
									contentDescription = stringResource(id = R.string.arrow_backward)
								)
							}
						}
						//Creates the page indicator
						Row(
							Modifier
								.wrapContentHeight()
								.align(Alignment.Center)
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
						//Button to move to next page
						IconButton(
							modifier = Modifier.align(Alignment.CenterEnd),
							onClick = {
								state.launch {
									//if on last page should navigate to MapView
									if (pagerState.currentPage == pagerState.pageCount - 1) {
										Log.d("X Clicked", "Exit Onboard")
									}
									//else move to next screen on OnboardingView
									else {
										pagerState.scrollToPage(pagerState.currentPage + 1)
									}
								}
							}
						)
						{
							//if moving to next last page, make icon into an X
							if (pagerState.currentPage == pagerState.pageCount - 1) {
								Icon(
									Icons.Filled.Close,
									contentDescription = stringResource(id = R.string.close_button)
								)
							} else {
								//else keep arrow forward
								Icon(
									Icons.Filled.ArrowForward,
									contentDescription = stringResource(id = R.string.arrow_forward)
								)
							}
						}
					}
				}

			)
		},
		snackbarHost = {
			SnackbarHost(snackbarState)
		}
	) {
		//Allows users to swap to each screen when horizontally swiped
		HorizontalPager(
			modifier = Modifier
				.padding(it)
				.fillMaxSize()
				//Creates background for the OnboardingView
				.paint(
					painterResource(id = R.drawable.welcome_screen_background),
					contentScale = ContentScale.FillBounds
				),
			state = pagerState,
		) { page ->
			when (page) {
				0 -> WelcomeScreen()
				1 -> ExplanationScreen()
				2 -> LoginScreen(
					showSnackBar = snackbarState::showSnackbar
				)
				3 -> OptionsScreen()
				4 -> ThanksScreen(onFinish)
			}
		}
	}

}

@Preview
@Composable
fun PreviewOnboardingView() {
	OnboardingView(
		onFinish = {}
	)
}
