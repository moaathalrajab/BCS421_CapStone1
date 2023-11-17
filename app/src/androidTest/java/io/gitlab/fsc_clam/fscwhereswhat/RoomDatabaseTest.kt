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

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import io.gitlab.fsc_clam.fscwhereswhat.database.*
import io.gitlab.fsc_clam.fscwhereswhat.model.database.*
import io.gitlab.fsc_clam.fscwhereswhat.model.local.*
import kotlinx.coroutines.*
import org.hamcrest.collection.IsIterableContainingInOrder.contains
import org.junit.*

import org.junit.runner.RunWith

import org.junit.Assert.*
import java.util.concurrent.*

/**
 * Database tests for the defined room database
 *
 */
@RunWith(AndroidJUnit4::class)
@SmallTest
class RoomDatabaseTest {

	private lateinit var database: AppDatabase
	private lateinit var noteDao: NoteDao

	@Before
	fun setupDatabase() {
		database = Room.inMemoryDatabaseBuilder(
			ApplicationProvider.getApplicationContext(),
			AppDatabase::class.java
		).build()

		noteDao = database.noteDao
	}
	@After
	fun closeDatabase() {
		database.close()
	}

	@Test
	fun insertEvent_returnsTrue() = runBlocking {
		val note = DBNote("I LOVE NOTES!!!", 0, EntityType.BUILDING)
		noteDao.insert(note)

		val latch = CountDownLatch(1)
		val job = async(Dispatchers.IO) {
			noteDao.getAllFlow().collect {
				contains(note)
				latch.countDown()
			}
		}
		latch.await()
		job.cancelAndJoin()
	}
}