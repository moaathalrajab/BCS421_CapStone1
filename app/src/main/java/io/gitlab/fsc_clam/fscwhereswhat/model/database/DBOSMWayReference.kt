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

import androidx.room.*

/**
 * This is a reference between a OSM Way and its child nodes
 * @param id is the id of the way
 * @param nodeId is the id of the associated child node
 */
@Entity(
	tableName = "osm_way_reference",
	foreignKeys = [ForeignKey(DBOSMWay::class, ["id"], ["id"], ForeignKey.CASCADE),
					ForeignKey(DBOSMNode::class, ["id"], ["nodeId"], ForeignKey.CASCADE)],
	primaryKeys = ["id", "nodeId"]
)
data class DBOSMWayReference(
	val id: Long,
	val nodeId: Long,
)