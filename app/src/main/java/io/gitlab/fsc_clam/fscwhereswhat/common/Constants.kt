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

package io.gitlab.fsc_clam.fscwhereswhat.common

import okhttp3.HttpUrl.Companion.toHttpUrl

/**
 * URL to OSM API
 */
const val OSM_API_URL = "https://openstreetmap.org/api/0.6/"

/**
 * URL to FSC API
 */
val RC_API_URL = "https://farmingdale.campuslabs.com/engage/api/".toHttpUrl()

const val FSC_LAT = 40.7515
const val FSC_LOG = -73.4295
