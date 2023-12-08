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

package io.gitlab.fsc_clam.fscwhereswhat.repo.base

import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining operations for interacting with the color preferences repository
 */
interface PreferencesRepository {

	/**
	 * Get a color from the preferences repository
	 *
	 * @param type is the EntityType in which the related color is being requested
	 */
	fun getColor(type: EntityType): Flow<Int>

	/**
	 * Set a color in the preferences repository
	 *
	 * @param type is the particular EntityType being changed
	 * @param color is the new color for the EntityType
	 */
	suspend fun setColor(type: EntityType, color: Int)

	/**
	 * Binding point for Implementation getter
	 */
	companion object
}