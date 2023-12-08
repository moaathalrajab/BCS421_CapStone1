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

package io.gitlab.fsc_clam.fscwhereswhat.providers

import android.util.Log
import io.gitlab.fsc_clam.fscwhereswhat.BuildConfig
import okhttp3.OkHttpClient

private var _okHttpClient: OkHttpClient? = null

/**
 * OkHttpClient to use in the application
 */
val okHttpClient: OkHttpClient
	@Synchronized
	get() {
		if (_okHttpClient == null) {
			_okHttpClient = OkHttpClient.Builder()
				.addInterceptor { chain ->
					// Get the request
					val request = chain.request()

					// Log the request URL
					if (BuildConfig.DEBUG) {
						Log.v("OkHTTPClient", request.url.toString())
					}

					// proceed on the chain (returns as last line)
					chain.proceed(request)
				}
				.build()
		}

		return _okHttpClient!!
	}