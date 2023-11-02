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

import java.net.URL

/**
 * UI Data Holder for Search
 */
sealed interface EntityItem{
	/**
	 * A unique identifier for the entity
	 */
	val id: Long
	/**
	 * To display in the listing
	 * User identifiable for the name
	 */
	val name: String
	/**
	 * For the event, building, node image
	 */
	val image: Image

	/**
	 * Represents a building
	 */
	data class Building(
		override val id: Long,
		override val name: String,
		override val image: Image,
	): EntityItem

	/**
	 * Represents any given item
	 * Ex. water fountain
	 */

	data class Node(
		override val id: Long,
		override val name: String,
		override val image: Image,
	): EntityItem

	/**
	 * @param clubName name of the club
	 * @param banner to show event image
	 */
	data class Event(
		override val id: Long,
		override val name: String,
		override val image: Image,
		val clubName: String,
		val banner: URL,
	): EntityItem
}
