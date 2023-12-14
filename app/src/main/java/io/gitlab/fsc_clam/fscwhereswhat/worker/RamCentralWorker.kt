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

package io.gitlab.fsc_clam.fscwhereswhat.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Token
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.RamCentralRepository
import io.gitlab.fsc_clam.fscwhereswhat.repo.impl.ImplRamCentralRepository.Companion.get
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlin.time.Duration.Companion.days

class RamCentralWorker(appContext: Context, params: WorkerParameters) :
	CoroutineWorker(appContext, params) {
	private val repo = RamCentralRepository.get(applicationContext)

	override suspend fun doWork(): Result {
		Log.i(LOG, "Starting")
		repo.search(Token(emptyList()), 50).take(2).collect { }

		val currDate = System.currentTimeMillis()

		repo.getAll().first().forEach { event ->
			Log.d(LOG, "Processing Event(${event.id})")

			if ( event.endsOn.days < currDate.days )
				repo.deleteEvent(event)

		}

		Log.d(LOG, "Processing complete!")
		return Result.success()
	}

	companion object {

		private const val LOG = "RamCentralWorker"
		
	}
}