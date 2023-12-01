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

package io.gitlab.fsc_clam.fscwhereswhat.repo.impl

import io.gitlab.fsc_clam.fscwhereswhat.model.local.Pinpoint
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.MapViewFocusRepository
import kotlinx.coroutines.flow.MutableStateFlow

class ImplMapViewFocusRepository: MapViewFocusRepository {
	override val focus = MutableStateFlow(null as Pinpoint?)

	override fun setFocus(pinpoint: Pinpoint) {
		focus.value = pinpoint
	}

	companion object {
		private var repo: ImplMapViewFocusRepository? = null

		/**
		 * Get the implementation of [MapViewFocusRepository]
		 */
		@Synchronized
		fun MapViewFocusRepository.Companion.get(): MapViewFocusRepository {
			if (repo == null) {
				repo = ImplMapViewFocusRepository()
			}

			return repo!!
		}
	}
}