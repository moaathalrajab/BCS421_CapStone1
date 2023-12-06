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

package io.gitlab.fsc_clam.fscwhereswhat.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.gitlab.fsc_clam.fscwhereswhat.ui.notes.NotesView
import io.gitlab.fsc_clam.fscwhereswhat.ui.onboarding.OnboardingView
import io.gitlab.fsc_clam.fscwhereswhat.ui.reminders.RemindersView
import io.gitlab.fsc_clam.fscwhereswhat.ui.theme.FSCWheresWhatTheme

@Preview
@Composable
fun MainView() {
	FSCWheresWhatTheme {
		MainContent()
	}
}

@Composable
fun MainContent() {
	val navController = rememberNavController()

	NavHost(
		navController = navController,
		startDestination = "onboarding",
	) {
		composable("onboarding") {
			OnboardingView()
		}
		composable("notes") {
			NotesView()
		}
		composable("reminders") {
			RemindersView()
		}
		composable("map") {
		}
	}
}