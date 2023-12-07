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

import io.gitlab.fsc_clam.fscwhereswhat.BuildConfig
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.MapTileIndex

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
	"MapBox", 13, 20, 256, ".png",
	arrayOf("https://api.mapbox.com/styles/v1/arachas/$style/static/$-73.4295,40.7515,")
) {
	override fun getTileURLString(tileIndex: Long): String {
		return baseUrl +
				"/${MapTileIndex.getZoom(tileIndex)}" +
				",0" +
				"/300x200" +
				"?access_token=${BuildConfig.mapboxAPI}"
	}

}