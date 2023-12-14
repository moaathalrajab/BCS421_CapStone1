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

package io.gitlab.fsc_clam.fscwhereswhat.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.gitlab.fsc_clam.fscwhereswhat.model.local.NodeType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.OSMEntity
import io.gitlab.fsc_clam.fscwhereswhat.model.local.OpeningHours
import io.gitlab.fsc_clam.fscwhereswhat.model.remote.OSMElement
import io.gitlab.fsc_clam.fscwhereswhat.model.remote.OSMType
import io.gitlab.fsc_clam.fscwhereswhat.providers.base.OpenStreetMapAPI
import io.gitlab.fsc_clam.fscwhereswhat.providers.impl.OkHttpOpenStreetMapAPI
import io.gitlab.fsc_clam.fscwhereswhat.providers.okHttpClient
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.OSMRepository
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.ImplOSMRepository.Companion.get

/**
 * Downloads latest OSM data and updates database with the new data
 */
class OSMWorker(appContext: Context, params: WorkerParameters) :
	CoroutineWorker(appContext, params) {
	private val repo = OSMRepository.get(applicationContext)

	private val api: OpenStreetMapAPI = OkHttpOpenStreetMapAPI(okHttpClient)
	private val entities = ArrayList<OSMEntity>()
	private lateinit var sourceElements: List<OSMElement>

	override suspend fun doWork(): Result {
		Log.i("OSMWorker", "Starting")
		val relation = api.getFullElement(OSMType.RELATION, FSC_RELATION)
		sourceElements = relation.elements

		relation.elements.forEach { element ->
			process(element)
		}

		repo.update(entities)

		Log.i("OSMWorker", "Complete")
		return Result.success()
	}

	private suspend fun process(element: OSMElement) {
		Log.d(LOG, "Processing OSMElement(${element.id})")
		when (element.type) {
			OSMType.WAY -> {
				Log.d(LOG, "OSMElement(${element.id}) is a way")
				parseWay(element)
			}

			OSMType.RELATION -> {
				Log.d(LOG, "OSMElement(${element.id}) is a relation, ignoring")
			}

			OSMType.NODE -> {
				Log.d(LOG, "OSMElement(${element.id}) is a node")
				parseNode(element)
			}
		}
	}

	private fun parseNode(element: OSMElement) {

		if (element.tags == null) {
			Log.d(LOG, "OSMElement(${element.id}) has no tags, skipping")
			return
		}

		val nodeType: NodeType
		val name: String

		when {
			element.tags.amenity == "drinking_water" -> {
				nodeType = NodeType.WATER
				name = "Water fountain"
			}

			element.tags.amenity == "vending_machine" -> {
				nodeType = NodeType.VENDING_MACHINE
				name = "Vending Machine"
			}

			element.tags.amenity == "food_court" -> {
				nodeType = NodeType.FOOD
				name = if(element.tags.name != null)
					element.tags.name
				else
					"Food Court"
			}

			element.tags.amenity == "cafe" -> {
				nodeType = NodeType.FOOD
				name = if(element.tags.name != null)
					element.tags.name
				else
					"Cafe"
			}

			element.tags.amenity == "restaurant" -> {
				nodeType = NodeType.FOOD
				name = if(element.tags.name != null)
					element.tags.name
				else
					"Restaurant"
			}

			element.tags.shop != null -> {
				nodeType = NodeType.RETAIL
				name = if(element.tags.name != null)
					element.tags.name
				else
					"Shop"
			}

			element.tags.emergency == "phone" ->{
				nodeType = NodeType.SOS
				name = "Emergency Phone"
			}

			element.tags.amenity == "bicycle_parking" ->{
				nodeType = NodeType.BICYCLE
				name = "Bicycle Parking"
			}

			else -> {
				Log.d(LOG, "OSMElement(${element.id}) does not match any known NodeTypes")
				return
			}
		}

		entities.add(
			OSMEntity.Node(
				id = element.id,
				lat = element.lat!!,
				long = element.lon!!,
				name = name,
				description = "", // TODO OSM descriptions??
				hours = listOf(OpeningHours.everyDay),
				nodeType = nodeType
			)
		)
	}

	private suspend fun parseWay(element: OSMElement) {
		@Suppress("NullChecksToSafeCall")
		if (element.tags == null || element.tags.building == null) {
			Log.d(LOG, "OSMElement(${element.id}) is not a building")
		} else {
			Log.d(LOG, "OSMElement(${element.id}) is a building")

			Log.d(LOG, "Parsing OSMElement(${element.id}) nodes for location")
			val nodes = element.nodes.mapNotNull { nodeId -> // find data we already have
				sourceElements.find { it.id == nodeId }
			}.filter { it.lat != null && it.lon != null } // ensure no null lat / lon

			val lat = nodes.sumOf { it.lat!! } / nodes.size
			val log = nodes.sumOf { it.lon!! } / nodes.size

			entities.add(
				OSMEntity.Building(
					id = element.id,
					lat = lat,
					long = log,
					name = element.tags.name ?: "Building ${element.id}",
					description = "",
					hours = listOf(OpeningHours.everyDay),
					hasWater = true,
					hasFood = true
				)
			)
		}
	}

	companion object {
		/**
		 * Relation ID for the entirety of FSC
		 */
		private const val FSC_RELATION: Long = 8288536

		private const val LOG = "OSMWorker"
	}
}