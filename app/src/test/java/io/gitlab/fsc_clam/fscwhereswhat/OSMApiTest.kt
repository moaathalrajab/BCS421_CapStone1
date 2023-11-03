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

package io.gitlab.fsc_clam.fscwhereswhat

import io.gitlab.fsc_clam.fscwhereswhat.model.remote.OSMType
import io.gitlab.fsc_clam.fscwhereswhat.providers.base.OpenStreetMapAPI
import io.gitlab.fsc_clam.fscwhereswhat.providers.impl.OkHttpOpenStreetMapAPI
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Test

/**
 * Test the [OpenStreetMapAPI]
 */
class OSMApiTest {

	/**
	 * [OpenStreetMapAPI] implementation
	 */
	private lateinit var api: OpenStreetMapAPI

	@Before
	fun setup() {
		val client = OkHttpClient.Builder().addInterceptor {
			val request = it.request()
			println(request.url)
			it.proceed(request)
		}.build()

		api = OkHttpOpenStreetMapAPI(client)
	}

	/**
	 * Test that a WAY retrieves as intended
	 */
	@Test
	fun wayTest() {
		val info = runBlocking {
			api.getElement(OSMType.WAY, 49332856) // FSC Library
		}
		println(info)
	}

	/**
	 * Test that a "full" WAY retrieves as intended
	 */
	@Test
	fun fullWayTest() {
		val info = runBlocking {
			api.getFullElement(OSMType.WAY, 49332856) // FSC Library
		}
		println(info)
	}

	/**
	 * Test that a RELATION retrieves as intended
	 */
	@Test
	fun relationTest() {
		val info = runBlocking {
			api.getElement(OSMType.RELATION, 13076771) // Farmingdale State College
		}
		println(info)
	}

	/**
	 * Test that a "full" RELATION retrieves as intended
	 */
	@Test
	fun fullRelationTest() {
		val info = runBlocking {
			api.getFullElement(OSMType.RELATION, 13076771) //  Farmingdale State College
		}
		println(info)
	}

	/**
	 * Test that a NODE retrieves as intended
	 */
	@Test
	fun nodeTest() {
		val info = runBlocking {
			api.getElement(OSMType.NODE, 9986792342L) // Books n' Beans Cafe
		}
		println(info)
	}

	/**
	 * Test retrieval of items in a box.
	 */
	@Test
	fun boundingTest() {
		val info = runBlocking {
			api.getElementsInBox(
				left = -73.42767f,
				bottom = 40.7524f,
				right = -73.42052f,
				top = 40.75352f
			) // Random box
		}
		println(info)
	}
}