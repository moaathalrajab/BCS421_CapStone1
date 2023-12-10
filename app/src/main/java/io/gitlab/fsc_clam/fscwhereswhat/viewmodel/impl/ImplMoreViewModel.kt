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
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.PreferencesRepository
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.ImplPreferencesRepository.Companion.get
import io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base.MoreViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Implementation of the MoreViewModel class, functionality related to more/settings.
 */
class ImplMoreViewModel(application: Application) : MoreViewModel(application) {
	override val exceptions = MutableSharedFlow<Throwable>()
	override val cacheStatus = MutableSharedFlow<Boolean>()

	private val repo = PreferencesRepository.get(application)

	override val buildingColor: StateFlow<Int> by lazy {
		repo.getColor(EntityType.BUILDING).stateIn(
			viewModelScope,
			SharingStarted.Eagerly, Color.RED
		)
	}

	override val eventColor: StateFlow<Int> by lazy {
		repo.getColor(EntityType.EVENT).stateIn(
			viewModelScope,
			SharingStarted.Eagerly, Color.RED
		)
	}

	override val utilityColor: StateFlow<Int> by lazy {
		repo.getColor(EntityType.NODE).stateIn(
			viewModelScope,
			SharingStarted.Eagerly, Color.RED
		)
	}

	override fun setBuildingColor(color: Int) {
		viewModelScope.launch {
			repo.setColor(EntityType.BUILDING, color)
		}
	}

	override fun setEventColor(color: Int) {
		viewModelScope.launch {
			repo.setColor(EntityType.EVENT, color)
		}
	}

	override fun setUtilityColor(color: Int) {
		viewModelScope.launch {
			repo.setColor(EntityType.NODE, color)
		}
	}

	/**
	 * Clearing the cache
	 */

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






