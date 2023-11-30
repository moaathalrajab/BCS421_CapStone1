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

package io.gitlab.fsc_clam.fscwhereswhat.viewmodel.impl

import androidx.lifecycle.viewModelScope
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Pinpoint
import io.gitlab.fsc_clam.fscwhereswhat.model.local.User
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.FakeLocationRepo
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.FakeOSMRepo
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.FakePrefRepo
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.FakeRamCentralRepo
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.FakeUserRepo
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.MapViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ImplMapViewModel() : MapViewModel() {
	private val userRepo = FakeUserRepo()
	private val prefRepo = FakePrefRepo()
	private val osmRepo = FakeOSMRepo()
	private val ramCentralRepo = FakeRamCentralRepo()
	private val locationRepo = FakeLocationRepo()

	override val user: StateFlow<User?> by lazy {
		userRepo.user
	}

	override val query: MutableStateFlow<String?> = MutableStateFlow(null)

	override val activeFilter: MutableStateFlow<EntityType?> = MutableStateFlow(null)

	override val pinpoints: StateFlow<List<Pinpoint>> = TODO()

	override val longitude: StateFlow<Float> by lazy {
		locationRepo.longitude
	}

	override val latitude: StateFlow<Float> by lazy {
		locationRepo.latitude
	}

	//do we need this?
	val focus: MutableStateFlow<Pinpoint?> = MutableStateFlow(null)

	override fun setActiveFilter(filter: EntityType?) {
		activeFilter.value = filter
	}

	override fun setFocus(pinpoint: Pinpoint) {
		focus.value = pinpoint
	}

	//should I add this to MapViewModel?
	fun getColor(type: EntityType): Int {
		return prefRepo.getColor(type).stateIn(
			viewModelScope, SharingStarted.Eagerly, 0
		).value
	}
}