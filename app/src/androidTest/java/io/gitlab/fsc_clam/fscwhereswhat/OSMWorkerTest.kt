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
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.await
import androidx.work.impl.utils.taskexecutor.WorkManagerTaskExecutor
import com.google.common.util.concurrent.ListenableFuture
import io.gitlab.fsc_clam.fscwhereswhat.worker.OSMWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.UUID
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

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
		val ls = object : ListenableFuture<Void> {
			override fun cancel(mayInterruptIfRunning: Boolean): Boolean = true

			override fun isCancelled(): Boolean = false

			override fun isDone(): Boolean = false

			override fun get(): Nothing? = null

			override fun get(timeout: Long, unit: TimeUnit?): Nothing? = null

			override fun addListener(listener: Runnable, executor: Executor) {
			}

		}
		val worker = OSMWorker(
			instrumentationContext,
			WorkerParameters(
				UUID.randomUUID(),
				Data.EMPTY,
				emptyList(),
				WorkerParameters.RuntimeExtras(),
				0,
				0,
				{ },
				WorkManagerTaskExecutor(Dispatchers.IO.asExecutor()),
				WorkerFactory.getDefaultWorkerFactory(),
				{ context, id, data -> ls },
				{ context, id, foregroundInfo -> ls }
			)
		)


		runBlocking {
			worker.doWork()
		}
	}
}