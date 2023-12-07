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
import okhttp3.HttpUrl
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.MapTileIndex
import kotlin.math.atan
import kotlin.math.pow
import kotlin.math.sinh

private const val style = "clpi9vo3b00n701o91pugfmeh"

object MapBoxXYTileSource : XYTileSource(
	"FSCWheresWhatMap",
	13,
	20,
	256,
	"",
	arrayOf("https://api.mapbox.com/styles/v1/arachas/clpi9vo3b00n701o91pugfmeh/tiles/")
) {
	override fun getTileURLString(pMapTileIndex: Long): String {
		val source = super.getTileURLString(pMapTileIndex)
		Log.d("MapBox", source)
		return source + "?access_token=${BuildConfig.mapboxAPI}"
	}
}

object MapBoxTileSource : OnlineTileSourceBase(
	"MapBox",
	13,
	20,
	256,
	".png",
	null
) {
	override fun getTileURLString(tileIndex: Long): String {
		val zoom = MapTileIndex.getZoom(tileIndex)
		val x = MapTileIndex.getX(tileIndex)
		val y = MapTileIndex.getY(tileIndex)

		val n = 2.0.pow(zoom.toDouble())
		val lon_deg = x / n * 360.0 - 180.0
		val lat_rad = atan(sinh(Math.PI * (1 - 2 * y / n)))
		val lat_deg = lat_rad * 180.0 / Math.PI


		val bearing = 0

//https://api.mapbox.com/styles/v1/arachas/clpi9vo3b00n701o91pugfmeh/static/-73.4295,40.7515,17.55,0/300x200?
		val tileURL = HttpUrl.Builder()
			.scheme("https")
			.host("api.mapbox.com")
			.addPathSegments("styles/v1/")
			.addPathSegment("arachas") // map profile
			.addPathSegment(style) // style to work with
			.addPathSegment("static")
			// These have to be together
			.addPathSegment("$lon_deg,$lat_deg,$zoom,$bearing")
			.addPathSegment("${tileSizePixels}x$tileSizePixels")
			.addPathSegment("$y")
			.build()
		Log.d("MapBox", tileURL.toString())

		return tileURL.newBuilder()
			.addQueryParameter("access_token", BuildConfig.mapboxAPI)
			.build()
			.toString()
	}
}