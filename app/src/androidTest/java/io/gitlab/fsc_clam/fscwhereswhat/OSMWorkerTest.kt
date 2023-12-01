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

import android.content.Context
import androidx.compose.animation.core.updateTransition
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.await
import io.gitlab.fsc_clam.fscwhereswhat.worker.OSMWorker
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class OSMWorkerTest {
	lateinit var instrumentationContext: Context

	@Before
	fun setup() {
		instrumentationContext = InstrumentationRegistry.getInstrumentation().context
	}

	@Test
	fun test() {
		val uploadWorkRequest =
			OneTimeWorkRequestBuilder<OSMWorker>()
				.setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
				.build()

		runBlocking {
			val manager = WorkManager
				.getInstance(instrumentationContext)

			manager.enqueueUniqueWork("test", ExistingWorkPolicy.REPLACE, uploadWorkRequest).await()

			// TODO await worker to finish
			//manager.getWorkInfoByIdFlow(uploadWorkRequest.id).map {}.first { it == WorkInfo.State. }
		}
	}
}