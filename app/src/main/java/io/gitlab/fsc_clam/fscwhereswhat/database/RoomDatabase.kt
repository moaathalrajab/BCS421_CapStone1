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

package io.gitlab.fsc_clam.fscwhereswhat.database

import android.content.Context
import androidx.room.*
import io.gitlab.fsc_clam.fscwhereswhat.model.database.*

@Database(
	version = 1,
	entities = [
		DBReminder::class,
		DBNote::class,
		DBEvent::class,
		DBOSMNode::class,
		DBOSMNodeTag::class,
		DBOSMWay::class,
		DBOSMWayReference::class,
		DBOSMWayTag::class
	]
)
@TypeConverters(TypeConversion::class)
abstract class AppDatabase : RoomDatabase() {
	abstract val reminderDao: ReminderDao
	abstract val noteDao: NoteDao
	abstract val eventDao: EventDao
	abstract val OSMNodeDao: OSMNodeDao
	abstract val OSMNodeTagDao: OSMNodeTagDao
	abstract val OSMWayDao: OSMWayDao
	abstract val OSMWayReferenceDao: OSMWayReferenceDao
	abstract val OSMWayTagDao: OSMWayTagDao

	companion object {
		@Volatile
		private var instance: AppDatabase? = null

		fun buildDatabase(context: Context): AppDatabase? {
			if (instance == null) {
				synchronized(AppDatabase::class) {
					instance = Room.databaseBuilder(context.applicationContext,
						AppDatabase::class.java,
						"whereswhat.db").allowMainThreadQueries().build()
				}
			}
			return instance
		}
	}
}
