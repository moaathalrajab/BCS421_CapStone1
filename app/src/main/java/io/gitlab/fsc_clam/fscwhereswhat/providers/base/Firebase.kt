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

package io.gitlab.fsc_clam.fscwhereswhat.providers.base

import kotlinx.coroutines.flow.Flow

/**
 * Defines functions to interface with the FSC Wheres What Firebase Project
 */
interface Firebase {
	/**
	 * Sets color preferences in firebase
	 * The function will convert the inputted list into a map, where
	 * the KEY will be an index value associated with a node type
	 * the VALUE will be the hex value for the color
	 */
	suspend fun setColor(user: String, colorSet: Map<String, String>)

	/**
	 * Gets the color preferences from firebase
	 * The function will take the map from the database,
	 * and convert it into a flow list
	 */
	fun getColor(user: String): Flow<Map<String, String>>
}