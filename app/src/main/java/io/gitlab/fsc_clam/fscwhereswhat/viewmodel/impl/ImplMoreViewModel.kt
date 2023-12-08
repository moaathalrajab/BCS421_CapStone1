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

package io.gitlab.fsc_clam.fscwhereswhat.viewmodel.impl

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.viewModelScope
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.MoreViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Implementation of the MoreViewModel class, functionality related to more/settings.
 */
class ImplMoreViewModel(application: Application) : MoreViewModel(application) {
	override val exceptions = MutableSharedFlow<Throwable>()
	override val cacheStatus = MutableSharedFlow<Boolean>()

	/**
	 * A [StateFlow] representing the color associated with buildings.
	 */
	override val buildingColor: StateFlow<Color>
		get() = TODO("Not yet implemented")

	/**
	 * A [StateFlow] representing the color associated with events.
	 * Not yet implemented.
	 */
	override val eventColor: StateFlow<Color>
		get() = TODO("Not yet implemented")

	/**
	 * A [StateFlow] representing the color associated with utilities.
	 * Not yet implemented.
	 */
	override val utilityColor: StateFlow<Color>
		get() = TODO("Not yet implemented")

	/**
	 * Set the color associated with buildings.
	 * @param color The color to be set.
	 */
	override fun setBuildingColor(color: Color) {
		TODO("Not yet implemented")
	}

	/**
	 * Set the color associated with events.
	 * @param color The color to be set.
	 */
	override fun setEventColor(color: Color) {
		TODO("Not yet implemented")
	}

	/**
	 * Set the color associated with utilities.
	 * @param color The color to be set.
	 */
	override fun setUtilityColor(color: Color) {
		TODO("Not yet implemented")
	}

	override fun clearCache() {
		viewModelScope.launch(Dispatchers.IO) {
			val context = getApplication<Application>()
			try {
				val cacheDir = context.cacheDir
				val cacheFiles = cacheDir.listFiles()
				val internalComplete =
					cacheFiles?.map { file ->
						file.delete()
					}
						?.all { it } ?: false
				// clear the external cache directory (maybe not necessary)
				val externalCacheDir = context.externalCacheDir
				val externalCacheFiles = externalCacheDir?.listFiles()
				val externalComplete =
					externalCacheFiles?.map { file ->
						file.delete()
					}
						?.all { it } ?: false
				cacheStatus.emit(internalComplete && externalComplete)
			} catch (e: Exception) {
				e.printStackTrace()
				exceptions.emit(e)
			}
		}
	}
}






