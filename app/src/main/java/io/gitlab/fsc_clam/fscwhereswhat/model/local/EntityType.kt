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

package io.gitlab.fsc_clam.fscwhereswhat.model.local

import android.graphics.Color

/**
 * EntityType are constants for the three fundamental types of data we are working with
 */
enum class EntityType(val defaultColor: Int) {
	/**
	 * Represents a building
	 */
	BUILDING(Color.CYAN),

	/**
	 * Represents an event
	 */
	EVENT(Color.GREEN),

	/**
	 * Represents utilities/amenities
	 * Ex. water fountain
	 */
	NODE(Color.RED),
}