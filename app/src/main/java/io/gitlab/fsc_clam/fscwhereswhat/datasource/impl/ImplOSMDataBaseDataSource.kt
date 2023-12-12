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

package io.gitlab.fsc_clam.fscwhereswhat.datasource.impl

import android.app.Application
import io.gitlab.fsc_clam.fscwhereswhat.database.AppDatabase
import io.gitlab.fsc_clam.fscwhereswhat.datasource.base.OSMDataBaseDataSource
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBOSMNode
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBOSMNodeTag
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBOSMWay
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBOSMWayTag
import io.gitlab.fsc_clam.fscwhereswhat.model.local.NodeType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.OSMEntity
import io.gitlab.fsc_clam.fscwhereswhat.model.local.OpeningHours
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Token
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlin.math.abs
import kotlin.math.sqrt

class ImplOSMDataBaseDataSource(application: Application) : OSMDataBaseDataSource {
	/**
	 * Dao connections for OSM Nodes & Ways
	 */
	private val dbOSMNode = AppDatabase.get(application).OSMNodeDao
	private val dbOSMNodeTag = AppDatabase.get(application).OSMNodeTagDao
	private val dbOSMWay = AppDatabase.get(application).OSMWayDao
	private val dbOSMWayTag = AppDatabase.get(application).OSMWayTagDao
	private val dbOSMWayReference = AppDatabase.get(application).OSMWayReferenceDao

	/**
	 * Percentage used to define distances used in [queryNearby] and [getNear]
	 */
	private val queryDistance = 0.003

	override suspend fun query(token: Token): Flow<List<OSMEntity>> {
		return flow {
			val queriedEntities = mutableListOf<OSMEntity>()

			dbOSMNode.getAll().forEach { node ->
				dbOSMNodeTag.getAllByNode(node.id).forEach { nodeTag ->
					if (token.compareValue(nodeTag.value))
						queriedEntities.add(node.toModel())
				}
			}

			dbOSMWayReference.getAll().forEach {
				dbOSMWayTag.getByParent(it.id).forEach { wayTag ->
					if (token.compareValue(wayTag.value))
						queriedEntities.add(dbOSMWay.getById(it.id).toModel())
				}
			}

			emit(queriedEntities)
		}
	}

	override suspend fun queryNearby(latitude: Double, longitude: Double): Flow<List<OSMEntity>> {
		val minLat = latitude - (latitude * queryDistance)
		val minLong = longitude - (longitude * queryDistance)
		val maxLat = latitude + (latitude * queryDistance)
		val maxLong = longitude + (longitude * queryDistance)

		return flow {
			emit(
				dbOSMNode.getAll().filter { node ->
					node.lat > minLat && node.long > minLong && node.lat < maxLat && node.long < maxLong
				}.map { it.toModel() })

		}.combine(flow {
			emit(
				dbOSMWay.getAll().filter { way ->
					dbOSMWayTag.get(way.id, "lat")!!.value.toDouble() > minLat &&
							dbOSMWayTag.get(way.id, "long")!!.value.toDouble() > minLong &&
							dbOSMWayTag.get(way.id, "lat")!!.value.toDouble() < maxLat &&
							dbOSMWayTag.get(way.id, "long")!!.value.toDouble() < maxLong
				}.map { it.toModel() })
		})
		{ a, b ->
			a + b
		}
	}

	override suspend fun get(id: Long): OSMEntity? {
		return dbOSMNode.getById(id).toModel()
	}

	private suspend fun updateWayTag(entityId: Long, key: String, newValue: String) {
		val tag = dbOSMWayTag.get(entityId, key)

		if (tag == null) {
			dbOSMWayTag.insert(
				DBOSMWayTag(
					entityId,
					key,
					newValue
				)
			)
		} else {
			dbOSMWayTag.update(
				tag.copy(value = newValue)
			)
		}
	}

	private suspend fun updateNodeTag(entityId: Long, key: String, newValue: String) {
		val tag = dbOSMNodeTag.get(entityId, key)

		if (tag == null) {
			dbOSMNodeTag.insert(
				DBOSMNodeTag(
					entityId,
					key,
					newValue
				)
			)
		} else {
			dbOSMNodeTag.update(
				tag.copy(value = newValue)
			)
		}
	}

	override suspend fun update(entities: List<OSMEntity>) {
		entities.forEach { entity ->
			when (entity) {
				is OSMEntity.Building -> {
					updateWayTag(
						entity.id,
						"hasFood",
						if (entity.hasFood) "1" else "0"
					)

					updateWayTag(
						entity.id,
						"hasWater",
						if (entity.hasWater) "1" else "0"
					)

					updateWayTag(
						entity.id,
						"opening_hours",
						entity.hours.toString()
					)

					updateWayTag(
						entity.id,
						"description",
						entity.description
					)

					updateWayTag(
						entity.id,
						"name",
						entity.name
					)

					updateWayTag(
						entity.id,
						"lat",
						entity.lat.toString()
					)

					updateWayTag(
						entity.id,
						"long",
						entity.long.toString()
					)

					dbOSMWay.update(dbOSMWay.getById(entity.id))
				}

				is OSMEntity.Node -> {
					updateNodeTag(
						entity.id,
						"lat",
						entity.lat.toString()
					)

					updateNodeTag(
						entity.id,
						"long",
						entity.long.toString()
					)

					updateNodeTag(
						entity.id,
						"name",
						entity.name
					)

					updateNodeTag(
						entity.id,
						"description",
						entity.description
					)

					updateNodeTag(
						entity.id,
						"opening_hours",
						entity.hours.toString()
					)

					updateNodeTag(
						entity.id,
						"nodeType",
						dbOSMNodeTag.get(entity.id, "type").value
					)

					dbOSMNode.update(dbOSMNode.getById(entity.id))
				}
			}
		}
	}

	override suspend fun getLikeName(name: String): List<OSMEntity> {
		val simName = mutableListOf<OSMEntity>()

		dbOSMNode.getAll().forEach { node ->
			if ( node.toModel().name.lowercase().contains(name.lowercase()) )
				simName.add(node.toModel())
			else if (abs(name.compareTo(node.toModel().name)) < 30)
				simName.add(node.toModel())
		}

		dbOSMWay.getAll().forEach { way ->
			if ( way.toModel().name.lowercase().contains(name.lowercase()) )
				simName.add(way.toModel())
			else if (abs(name.compareTo(way.toModel().name)) < 30)
				simName.add(way.toModel())
		}

		return simName
	}

	override suspend fun getNear(latitude: Double, longitude: Double): OSMEntity? {
		val minLat = latitude - (latitude * queryDistance)
		val minLong = longitude - (longitude * queryDistance)

		var nearest: OSMEntity = dbOSMNode.getAll().first().toModel()
		var distLat = abs(nearest.lat) - abs(latitude)
		var distLong = abs(nearest.long) - abs(longitude)
		var distFlatNearest = sqrt(distLat + distLong) //distance of the (currently) closest node
		var distFlatNode: Double //distance of the current node

		dbOSMNode.getAll().forEach { node ->
			if (node.lat < minLat && node.long < minLong) {
				distLat = abs(node.lat) - abs(latitude)
				distLong = abs(node.long) - abs(longitude)
				distFlatNode = sqrt(distLat + distLong)

				if (distFlatNode < distFlatNearest) {
					nearest = node.toModel()
					distFlatNearest = distFlatNode
				}
			}
		}

		return nearest;
	}

	/**
	 * Convert db to model
	 */

	private suspend fun DBOSMWay.toModel() =
		OSMEntity.Building(
			id = id,
			lat = dbOSMWayTag.get(id, "lat")!!.value.toDouble(),
			long = dbOSMWayTag.get(id, "long")!!.value.toDouble(),
			name = dbOSMWayTag.get(id, "name")!!.value,
			description = dbOSMWayTag.get(id, "description")!!.value,
			hasFood = dbOSMWayTag.get(id, "hasFood")!!.value.toBoolean(),
			hasWater = dbOSMWayTag.get(id, "hasWater")!!.value.toBoolean(),
			hours = OpeningHours.setHours(dbOSMWayTag.get(id, "opening_hours")!!.value)
		)

	private suspend fun DBOSMNode.toModel() =
		OSMEntity.Node(
			id = id,
			lat = lat,
			long = long,
			name = dbOSMNodeTag.get(id, "name").value,
			description = dbOSMNodeTag.get(id, "description").value,
			nodeType = enumValueOf<NodeType>(dbOSMNodeTag.get(id, "type").value),
			hours = OpeningHours.setHours(dbOSMNodeTag.get(id, "opening_hours").value)
		)

	/**
	 * Convert model to db osm node
	 */
	private fun OSMEntity.toDBOSMNode() =
		DBOSMNode(
			id = id,
			lat = lat,
			long = long
		)

	/**
	 * Convert model to osm tag
	 */
	private fun OSMEntity.toDBOSMTag() {
		when (this) {
			is OSMEntity.Building -> {
				DBOSMWayTag(
					parentId = 0,
					key = "",
					value = ""
				)
			}

			is OSMEntity.Node -> {
				DBOSMNodeTag(
					nodeId = 0,
					key = "",
					value = ""
				)
			}
		}
	}

	companion object {
		private var repo: ImplOSMDataBaseDataSource? = null

		/**
		 * Get the implementation of [OSMDataBaseDataSource]
		 */
		@Synchronized
		fun OSMDataBaseDataSource.Companion.get(
			application: Application
		): OSMDataBaseDataSource {
			if (repo == null) {
				repo = ImplOSMDataBaseDataSource(application)
			}

			return repo!!
		}
	}

}