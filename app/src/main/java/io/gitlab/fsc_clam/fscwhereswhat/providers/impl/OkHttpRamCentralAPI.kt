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

package io.gitlab.fsc_clam.fscwhereswhat.providers.impl

import io.gitlab.fsc_clam.fscwhereswhat.common.RC_API_URL
import io.gitlab.fsc_clam.fscwhereswhat.common.ext.executeAsync
import io.gitlab.fsc_clam.fscwhereswhat.model.remote.RamCentralDiscoveryEventOrganization
import io.gitlab.fsc_clam.fscwhereswhat.model.remote.RamCentralDiscoveryEventSearchResult
import io.gitlab.fsc_clam.fscwhereswhat.model.remote.RamCentralDiscoveryEvent
import io.gitlab.fsc_clam.fscwhereswhat.providers.base.RamCentralAPI
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream

class OkHttpRamCentralAPI(private val client: OkHttpClient) : RamCentralAPI {
	companion object {
		/**
		 * Custom JSON parser for the RamCentral api
		 */
		private val parser: Json by lazy {
			Json {
				ignoreUnknownKeys = true
			}
		}
	}

	/**
	 * Internal function to quickly make calls.
	 */
	private suspend fun getByteStream(url: HttpUrl): InputStream {
		val request = Request.Builder()
			.url(url)
			.header("Accept", "application/json") // Request JSON instead of XML
			.build()
		val call = client.newCall(request)
		val response = call.executeAsync()
		return response.body!!.byteStream()
	}

	@OptIn(ExperimentalSerializationApi::class)
	override suspend fun search(
		endsAfter: String,
		orderByField: RamCentralAPI.OrderByField,
		orderByDirection: RamCentralAPI.OrderByDirection,
		status: RamCentralAPI.Status,
		take: Int,
		query: String
	): RamCentralDiscoveryEventSearchResult {
		val stream = getByteStream(
			RC_API_URL.newBuilder().apply {
				addPathSegments("discovery/event/search")
				addEncodedQueryParameter("endsAfter", endsAfter)
				addQueryParameter("orderByField", orderByField.query)
				addQueryParameter("orderByDirection", orderByDirection.query)
				addQueryParameter("status", status.query)
				addQueryParameter("take", take.toString())
				addQueryParameter("query", query)
			}.build()
		)
		return parser.decodeFromStream(stream)
	}

	override suspend fun getEvent(eventId: Long): RamCentralDiscoveryEvent {
		val stream = getByteStream(
			RC_API_URL.newBuilder().apply {
				addPathSegments("discovery/event")
				addPathSegment(eventId.toString())
			}.build()
		)
		return parser.decodeFromStream(stream)
	}

	override suspend fun getOrganizationsForEvent(eventId: Long): List<RamCentralDiscoveryEventOrganization> {
		val stream = getByteStream(
			RC_API_URL.newBuilder().apply {
				addPathSegments("discovery/event")
				addPathSegment(eventId.toString())
				addPathSegment("organizations")
			}.build()
		)
		return parser.decodeFromStream(stream)
	}

}