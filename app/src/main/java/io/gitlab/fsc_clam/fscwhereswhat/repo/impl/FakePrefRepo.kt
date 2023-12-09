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

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakePrefRepo : PreferencesRepository {

	private val colors: MutableMap<EntityType, Int> = mutableMapOf(
		EntityType.BUILDING to Color.Black.toArgb(),
		EntityType.EVENT to Color.Black.toArgb(),
		EntityType.NODE to Color.Black.toArgb()
	)

	override fun getColor(type: EntityType): Flow<Int> {
		// Create an immutable flow that emits the current color for the specified entity type
		val colorFlow = flow {
			emit(colors[type] ?: Color.Black.toArgb())
		}
		return colorFlow
	}


	override suspend fun setColor(type: EntityType, color: Int) {
		colors[type] = color
	}
}