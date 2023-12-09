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

package io.gitlab.fsc_clam.fscwhereswhat.repo.impl

import android.app.Application
import io.gitlab.fsc_clam.fscwhereswhat.database.AppDatabase
import io.gitlab.fsc_clam.fscwhereswhat.model.database.DBReminder
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Reminder
import io.gitlab.fsc_clam.fscwhereswhat.repo.base.ReminderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ImplReminderRepository(application: Application) : ReminderRepository {
	private val db = AppDatabase.get(application).reminderDao

	override fun getReminder(eventId: Long): Flow<Reminder?> =
		db.getById(eventId)
			.map { it?.toModel() }
			.flowOn(Dispatchers.IO)

	override fun getAllReminders(): Flow<List<Reminder>> =
		db.getAllFlow()
			.map { dbReminders -> dbReminders.map { it.toModel() } }
			.flowOn(Dispatchers.IO)

	override suspend fun updateReminder(reminder: Reminder) =
		withContext(Dispatchers.IO) {
			db.update(reminder.toDB())
		}

	override suspend fun deleteReminder(reminder: Reminder) =
		withContext(Dispatchers.IO) {
			db.delete(reminder.toDB())
		}

	override suspend fun createReminder(reminder: Reminder) =
		withContext(Dispatchers.IO) {
			db.insert(reminder.toDB())
		}

	/**
	 * Convert model to db
	 */
	private fun Reminder.toDB() = DBReminder(eventId, remind)

	/**
	 * Convert db to model
	 */
	private fun DBReminder.toModel() = Reminder(eventId, remind)

	companion object {
		private var repo: ImplReminderRepository? = null

		/**
		 * Get the implementation of [ReminderRepository]
		 */
		@Synchronized
		fun ReminderRepository.Companion.get(
			application: Application
		): ReminderRepository {
			if (repo == null) {
				repo = ImplReminderRepository(application)
			}

			return repo!!
		}
	}
}