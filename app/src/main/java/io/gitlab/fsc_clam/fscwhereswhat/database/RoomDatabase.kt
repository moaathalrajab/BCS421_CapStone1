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
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBEvent
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBNote
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBOSMBuilding
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBOSMBuildingOH
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBOSMNode
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBOSMNodeOH
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBReminder

@Database(
	version = 1,
	entities = [
		DBReminder::class,
		DBNote::class,
		DBEvent::class,
		DBOSMNode::class,
		DBOSMNodeOH::class,
		DBOSMBuilding::class,
		DBOSMBuildingOH::class
	]
)
@TypeConverters(TypeConversion::class)
abstract class AppDatabase : RoomDatabase() {
	abstract val reminderDao: ReminderDao
	abstract val noteDao: NoteDao
	abstract val eventDao: EventDao
	abstract val osmNodeDao: OSMNodeDao
	abstract val osmNodeOHDao: OSMNodeOHDao
	abstract val osmBuildingDao: OSMBuildingDao
	abstract val osmBuildingOHDao: OSMBuildingOHDao

	companion object {
		@Volatile
		private var instance: AppDatabase? = null

		fun get(context: Context): AppDatabase {
			synchronized(AppDatabase::class) {
				if (instance == null) {
					instance = Room.databaseBuilder(
						context.applicationContext,
						AppDatabase::class.java,
						"whereswhat.db"
					).build()
				}
			}
			return instance!!
		}
	}
}
