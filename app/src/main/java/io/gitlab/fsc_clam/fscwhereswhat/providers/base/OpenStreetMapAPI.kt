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

import io.gitlab.fsc_clam.fscwhereswhat.model.remote.OSMResponse
import io.gitlab.fsc_clam.fscwhereswhat.model.remote.OSMType

/**
 * Defines functions to interface with OpenStreetMaps API
 */
interface OpenStreetMapAPI {

	/**
	 * Get information about an element and a brief detailing of its child elements.
	 */
	suspend fun getElement(type: OSMType, id: Long): OSMResponse

	/**
	 * Get an element with all information of its child elements.
	 */
	suspend fun getFullElement(type: OSMType, id: Long): OSMResponse

	/**
	 * Get elements in a given area/box.
	 *
	 * Documentation borrowed from OSM wiki.
	 *
	 * @param left is the longitude of the west side of the bounding box.
	 * @param bottom is the latitude of the south side of the bounding box.
	 * @param right is the longitude of the north side of the bounding box.
	 * @param top is the latitude of the east side of the bounding box.
	 *
	 * @see <a href="https://wiki.openstreetmap.org/wiki/API_v0.6#Retrieving_map_data_by_bounding_box:_GET_/api/0.6/map">Retrieving map data by bounding box</a>
	 */
	suspend fun getElementsInBox(
		left: Float,
		bottom: Float,
		right: Float,
		top: Float,
	): OSMResponse

	/**
	 * Binding point for Implementation getter
	 */
	companion object
}