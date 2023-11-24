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

import io.gitlab.fsc_clam.fscwhereswhat.repo.base.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow

class ImplLocationRepository : LocationRepository {
	override val longitude = MutableStateFlow(-73.42942148736103)
	override val latitude = MutableStateFlow(40.75145183660949)
	override fun setLongitude(longitude: Double) {
		this.longitude.value = longitude
	}

	override fun setLatitude(latitude: Double) {
		this.latitude.value = latitude
	}

	companion object {
		private var repo: ImplLocationRepository? = null

		/**
		 * Get implementation of ImplLocationRepository
		 */
		@Synchronized
		fun LocationRepository.Companion.get(): LocationRepository {
			if (repo == null)
				repo = ImplLocationRepository()

			return repo!!
		}
	}
}