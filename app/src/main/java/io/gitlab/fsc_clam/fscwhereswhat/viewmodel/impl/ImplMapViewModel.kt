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

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.viewModelScope
import io.gitlab.fsc_clam.fscwhereswhat.common.FSC_LAT
import io.gitlab.fsc_clam.fscwhereswhat.common.FSC_LOG
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Pinpoint
import io.gitlab.fsc_clam.fscwhereswhat.model.local.User
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.LocationRepository
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.FakeOSMRepo
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.FakePrefRepo
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.FakeRamCentralRepo
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.FakeUserRepo
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.ImplLocationRepository.Companion.get
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.MapViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ImplMapViewModel(application: Application) : MapViewModel(application) {
	private val userRepo = FakeUserRepo()
	private val prefRepo = FakePrefRepo()
	private val osmRepo = FakeOSMRepo()
	private val ramCentralRepo = FakeRamCentralRepo()
	private val locationRepo = LocationRepository.get(application)

	override val user: StateFlow<User?> by lazy {
		userRepo.user
	}

	override val query: MutableStateFlow<String?> = MutableStateFlow(null)

	override val activeFilter: MutableStateFlow<EntityType?> = MutableStateFlow(null)

	override val focus: MutableStateFlow<Pinpoint?> = MutableStateFlow(null)

	override val pinpoints: StateFlow<List<Pinpoint>> = MutableStateFlow(
		listOf(
			Pinpoint(
				40.75175,
				-73.42902,
				0,
				0,
				EntityType.NODE,
				false
			)
		)
	).combine(activeFilter) { list, filter ->
		if (filter == null)
			list
		else list.filter { it.type == filter }
	}.combine(focus) { list, focus ->
		// Don't display other pin points if we have a focus
		if (focus == null)
			list
		else list.filter { it.id == focus.id }
	}.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

	override val longitude: StateFlow<Double> =
		locationRepo.longitude.stateIn(viewModelScope, SharingStarted.Eagerly, FSC_LOG)

	override val latitude: StateFlow<Double> =
		locationRepo.latitude.stateIn(viewModelScope, SharingStarted.Eagerly, FSC_LAT)

	override val buildingColor: StateFlow<Int> by lazy {
		prefRepo.getColor(EntityType.BUILDING).stateIn(
			viewModelScope,
			SharingStarted.Eagerly, Color.BLACK
		)
	}
	override val eventColor: StateFlow<Int> by lazy {
		prefRepo.getColor(EntityType.EVENT).stateIn(
			viewModelScope,
			SharingStarted.Eagerly, Color.BLACK
		)
	}

	override val nodeColor: StateFlow<Int> by lazy {
		prefRepo.getColor(EntityType.NODE).stateIn(
			viewModelScope,
			SharingStarted.Eagerly, Color.BLACK
		)
	}

	override fun setActiveFilter(filter: EntityType?) {
		activeFilter.value = filter
	}

	override fun setFocus(pinpoint: Pinpoint?) {
		focus.value = pinpoint
	}
}