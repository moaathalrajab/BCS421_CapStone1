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
import io.gitlab.fsc_clam.fscwhereswhat.common.utils.fuzz
import io.gitlab.fsc_clam.fscwhereswhat.database.AppDatabase
import io.gitlab.fsc_clam.fscwhereswhat.datasource.base.OSMDataBaseDataSource
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBOSMBuilding
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBOSMBuildingOH
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBOSMNode
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBOSMNodeOH
import io.gitlab.fsc_clam.fscwhereswhat.model.local.OSMEntity
import io.gitlab.fsc_clam.fscwhereswhat.model.local.OpeningHours
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Token
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class ImplOSMDataBaseDataSource(application: Application) : OSMDataBaseDataSource {
	/**
	 * Dao connections for OSM Nodes & Ways
	 */
	private val dbOSMNode = AppDatabase.get(application).osmNodeDao
	private val dbOSMNodeOH = AppDatabase.get(application).osmNodeOHDao
	private val dbOSMWay = AppDatabase.get(application).osmBuildingDao
	private val dbOSMWayOH = AppDatabase.get(application).osmBuildingOHDao

	/**
	 * Percentage used to define distances used in [queryNearby] and [getNear]
	 */
	private val queryDistance = 0.003

	override suspend fun query(token: Token): Flow<List<OSMEntity>> {
		val nodeFlow = dbOSMNode.getAllFlow().map { list ->
			list.filter { node ->
				token.compareValue(node.description) ||
						token.compareValue(node.name)
			}.map {
				it.toModel()
			}
		}

		val wayFlow = dbOSMWay.getAllFlow().map { list ->
			list.filter { node ->
				token.compareValue(node.description) ||
						token.compareValue(node.name)
			}.map {
				it.toModel()
			}
		}

		return nodeFlow.combine(wayFlow) { a, b -> a + b }
	}

	override suspend fun queryNearby(latitude: Double, longitude: Double): Flow<List<OSMEntity>> {
		val (minLat, maxLat) = latitude.fuzz(queryDistance)
		val (minLong, maxLong) = longitude.fuzz(queryDistance)

		val nodeFlow = dbOSMNode.getAllFlow().map { list ->
			list.filter { node ->
				node.lat > minLat && node.long > minLong && node.lat < maxLat && node.long < maxLong
			}.map { it.toModel() }
		}

		val buildingFlow = dbOSMWay.getAllFlow().map { list ->
			list.filter { node ->
				node.lat > minLat && node.long > minLong && node.lat < maxLat && node.long < maxLong
			}.map { it.toModel() }
		}

		return nodeFlow.combine(buildingFlow) { a, b -> a + b }
	}

	override suspend fun get(id: Long): OSMEntity? {
		return dbOSMNode.getById(id)?.toModel() ?: dbOSMWay.getById(id)?.toModel()
	}


	override suspend fun update(entities: List<OSMEntity>) {
		entities.forEach { entity ->
			when (entity) {
				is OSMEntity.Building -> {
					val db = dbOSMWay.getById(entity.id)
					if (db != null) {
						dbOSMWay.update(
							db.copy(
								lat = entity.lat,
								long = entity.long,
								name = entity.name,
								description = entity.description,
								hasWater = entity.hasWater,
								hasFood = entity.hasFood
							)
						)
					} else {
						dbOSMWay.insert(
							DBOSMBuilding(
								entity.id,
								entity.lat,
								entity.long,
								entity.name,
								entity.description,
								entity.hasWater,
								entity.hasFood
							)
						)
					}
				}

				is OSMEntity.Node -> {
					val db = dbOSMNode.getById(entity.id)
					if (db != null) {
						dbOSMNode.update(
							db.copy(
								lat = entity.lat,
								long = entity.long,
								name = entity.name,
								description = entity.description,
								nodeType = entity.nodeType
							)
						)
					} else {
						dbOSMNode.insert(
							DBOSMNode(
								entity.id,
								entity.lat,
								entity.long,
								entity.name,
								entity.description,
								entity.nodeType
							)
						)
					}
				}
			}
		}
	}

	override suspend fun getLikeName(name: String): List<OSMEntity> {
		val nodes = dbOSMNode.getAll().filter { it.name.contains(name, true) }.map { it.toModel() }
		val ways = dbOSMWay.getAll().filter { it.name.contains(name, true) }.map { it.toModel() }

		return nodes + ways
	}

	private val nearDistance = .0005

	override suspend fun getNear(latitude: Double, longitude: Double): OSMEntity? {

		val nodePair = dbOSMNode.getAll().map { node ->
			// Calculate average difference in distance
			node to ((node.lat - latitude) + (node.long - longitude)) / 2
		}.minByOrNull { it.second }

		val buildingPair = dbOSMWay.getAll().map { way ->
			// Calculate average difference in distance
			way to ((way.lat - latitude) + (way.long - longitude)) / 2
		}.minByOrNull { it.second } // the first one is the closest

		return when {
			nodePair == null && buildingPair == null -> null

			nodePair != null && buildingPair == null -> nodePair.first.toModel()

			nodePair == null && buildingPair != null -> buildingPair.first.toModel()

			nodePair!!.second > buildingPair!!.second -> nodePair.first.toModel()

			else -> buildingPair.first.toModel()
		}
	}

	override fun getAll(): Flow<List<OSMEntity>> =
		dbOSMNode.getAllFlow().map { list -> list.map { it.toModel() } }
			.combine(dbOSMWay.getAllFlow().map { list -> list.map { it.toModel() } }) { a, b ->
				a + b
			}

	/**
	 * Convert db to model
	 */

	private suspend fun DBOSMBuilding.toModel() =
		OSMEntity.Building(
			id = id,
			lat = lat,
			long = long,
			name = name,
			description = description,
			hasFood = hasFood,
			hasWater = hasWater,
			hours = dbOSMWayOH.getAllByParent(id).map { it.toModel() }
		)

	private suspend fun DBOSMNode.toModel() =
		OSMEntity.Node(
			id = id,
			lat = lat,
			long = long,
			name = name,
			description = description,
			nodeType = nodeType,
			hours = dbOSMNodeOH.getAllByParent(id).map { it.toModel() }
		)

	private fun DBOSMBuildingOH.toModel() =
		OpeningHours(
			monday,
			tuesday,
			wednesday,
			thursday,
			friday,
			saturday,
			sunday,
			startHour,
			startMinute,
			endHour,
			endMinute
		)

	private fun DBOSMNodeOH.toModel() =
		OpeningHours(
			monday,
			tuesday,
			wednesday,
			thursday,
			friday,
			saturday,
			sunday,
			startHour,
			startMinute,
			endHour,
			endMinute
		)

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