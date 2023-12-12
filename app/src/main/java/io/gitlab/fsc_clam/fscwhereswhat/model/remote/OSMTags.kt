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

package io.gitlab.fsc_clam.fscwhereswhat.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Instead of using a "Map<String,String>" for tags, we can use this data structure instead.
 *
 * Contains all the relevant information to the application.
 *
 * @param emergency For SOS phones on campus
 * 	https://wiki.openstreetmap.org/wiki/Key:emergency
 * @param shop For shops on campus
 *  https://wiki.openstreetmap.org/wiki/Key:shop
 */
@Serializable
data class OSMTags(
	@SerialName("addr:city")
	val city: String? = null,
	@SerialName("addr:housenumber")
	val houseNumber: Int? = null,
	@SerialName("addr:postcode")
	val postalCode: String? = null,
	@SerialName("addr:state")
	val state: String? = null,
	@SerialName("addr:street")
	val street: String? = null,
	@SerialName("addr:unit")
	val unit: String? = null,
	val amenity: String? = null,
	val building: String? = null,
	val name: String? = null,
	@SerialName("social_facility")
	val socialFacility: String? = null,
	val emergency: String? = null,
	val shop: String? = null
)