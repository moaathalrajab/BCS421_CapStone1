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

package io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base

import androidx.lifecycle.ViewModel
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Filter
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Pinpoint
import io.gitlab.fsc_clam.fscwhereswhat.model.local.User
import kotlinx.coroutines.flow.StateFlow

abstract class MapViewModel: ViewModel() {
	abstract val user: StateFlow<User?>
	abstract val query: StateFlow<String?>
	abstract val activeFilter: StateFlow<Filter?>
	abstract val pinpoints: StateFlow<List<Pinpoint>>
	abstract val longitude: StateFlow<Float>
	abstract val latitude: StateFlow<Float>

	abstract fun setActiveFilter(filter: Filter?)

	abstract fun setFocus(pinpoint: Pinpoint)


}