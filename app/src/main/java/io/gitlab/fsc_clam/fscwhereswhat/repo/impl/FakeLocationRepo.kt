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

import io.gitlab.fsc_clam.fscwhereswhat.common.FSC_LAT
import io.gitlab.fsc_clam.fscwhereswhat.common.FSC_LOG
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeLocationRepo : LocationRepository {

	override val longitude: MutableStateFlow<Double> = MutableStateFlow(FSC_LOG)

	override val latitude: MutableStateFlow<Double> = MutableStateFlow(FSC_LAT)

	override val bearing: Flow<Float> = MutableStateFlow(0f)

//	init {
//		GlobalScope.launch {
//			delay(5000)
//			while (true) {
//				Log.d("fakelocrepo", "${latitude.value}")
//				latitude.value += .0001
//				delay(2000)
//			}
//		}
//	}
}