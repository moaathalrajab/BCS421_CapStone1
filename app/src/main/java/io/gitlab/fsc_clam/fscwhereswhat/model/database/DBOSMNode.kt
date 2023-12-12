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

package io.gitlab.fsc_clam.fscwhereswhat.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.gitlab.fsc_clam.fscwhereswhat.model.local.NodeType

/**
 * This is an OSM node
 * @param id is the id of a specific node
 * @param lat is the latitude of the node
 * @param long is the longitude of the node
 */
@Entity(
	tableName = "osm_node"
)
data class DBOSMNode(
	@PrimaryKey
	val id: Long,
	val lat: Double,
	val long: Double,
	val name: String,
	val description: String,
	val nodeType: NodeType,
)