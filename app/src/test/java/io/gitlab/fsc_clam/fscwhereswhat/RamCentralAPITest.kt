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

import io.gitlab.fsc_clam.fscwhereswhat.providers.base.RamCentralAPI
import io.gitlab.fsc_clam.fscwhereswhat.providers.impl.OkHttpRamCentralAPI
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Test

/**
 * Test the [RamCentralAPI]
 */
class RamCentralAPITest {

	/**
	 * [RamCentralAPI] implementation
	 */
	private lateinit var api: RamCentralAPI

	@Before
	fun setup() {
		val client = OkHttpClient.Builder().addInterceptor {
			val request = it.request()
			println(request.url)
			it.proceed(request)
		}.build()

		api = OkHttpRamCentralAPI(client)
	}

	/**
	 * Test a generic search
	 */
	@Test
	fun search() {
		/*
		https://farmingdale.campuslabs.com/engage/api/discovery/event/search
		?endsAfter=2023-11-10T16:27:26-05:00
		&orderByField=endsOn
		&orderByDirection=ascending
		&status=Approved
		&take=15&query=
		 */
		val api = runBlocking {
			api.search(
				endsAfter = "2023-11-10T16:27:26-05:00",
				orderByField = RamCentralAPI.OrderByField.ENDS_ON,
				orderByDirection = RamCentralAPI.OrderByDirection.ASCENDING,
				status = RamCentralAPI.Status.APPROVED,
				take = 5,
				query = ""
			)
		}

		println(api)
	}

	//9439482
	/**
	 * Test a given event
	 */
	@Test
	fun getEvent() {
		val api = runBlocking {
			api.getEvent(9439482)
		}

		println(api)
	}

	/**
	 * Test to get organizations for the above event
	 */
	@Test
	fun getOrganizationsForEvent() {
		val api = runBlocking {
			api.getOrganizationsForEvent(9439482)
		}

		println(api)
	}
}