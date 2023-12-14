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

import android.app.Application
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import io.gitlab.fsc_clam.fscwhereswhat.worker.OSMWorker
import io.gitlab.fsc_clam.fscwhereswhat.worker.RamCentralWorker

class FSCWWApplication : Application() {
	override fun onCreate() {
		super.onCreate()

		// Start OSM
		WorkManager.getInstance(this)
			.enqueueUniqueWork(
				"OSM",
				ExistingWorkPolicy.KEEP,
				OneTimeWorkRequest.Companion.from(OSMWorker::class.java)
			)

		// Start RamCentralWorker
		WorkManager.getInstance(this)
			.enqueueUniqueWork(
				"RM",
				ExistingWorkPolicy.KEEP,
				OneTimeWorkRequest.Companion.from(RamCentralWorker::class.java)
			)
	}
}