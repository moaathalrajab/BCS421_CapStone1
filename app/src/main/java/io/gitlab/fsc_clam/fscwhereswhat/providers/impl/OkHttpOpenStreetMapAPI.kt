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

import io.gitlab.fsc_clam.fscwhereswhat.common.OSM_API_URL
import io.gitlab.fsc_clam.fscwhereswhat.common.ext.executeAsync
import io.gitlab.fsc_clam.fscwhereswhat.model.remote.OSMResponse
import io.gitlab.fsc_clam.fscwhereswhat.model.remote.OSMType
import io.gitlab.fsc_clam.fscwhereswhat.providers.base.OpenStreetMapAPI
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream

/**
 * Implementation of [OpenStreetMapAPI] using OkHttp
 */
class OkHttpOpenStreetMapAPI(
	private val okHttpClient: OkHttpClient
) : OpenStreetMapAPI {
	companion object {
		/**
		 * Custom JSON parser for the OSM api
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
	private suspend fun getByteStream(url: String): InputStream {
		val request = Request.Builder()
			.url(url)
			.header("Accept", "application/json") // Request JSON instead of XML
			.build()
		val call = okHttpClient.newCall(request)
		val response = call.executeAsync()
		return response.body!!.byteStream()
	}

	@OptIn(ExperimentalSerializationApi::class)
	override suspend fun getElement(type: OSMType, id: Long): OSMResponse =
		parser.decodeFromStream(
			getByteStream("$OSM_API_URL/$type/${id}")
		)

	@OptIn(ExperimentalSerializationApi::class)
	override suspend fun getFullElement(type: OSMType, id: Long): OSMResponse {
		if (type == OSMType.NODE) throw IllegalArgumentException("Only supports nodes and relations")

		return parser.decodeFromStream(
			getByteStream("$OSM_API_URL/$type/${id}/full")
		)
	}

	@OptIn(ExperimentalSerializationApi::class)
	override suspend fun getElementsInBox(
		left: Float,
		bottom: Float,
		right: Float,
		top: Float,
	): OSMResponse =
		parser.decodeFromStream(
			getByteStream("$OSM_API_URL/map?bbox=$left,$bottom,$right,$top")
		)
}