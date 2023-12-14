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

package io.gitlab.fsc_clam.fscwhereswhat.viewmodel.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Image
import io.gitlab.fsc_clam.fscwhereswhat.model.local.NodeType
import io.gitlab.fsc_clam.fscwhereswhat.model.local.OpeningHours
import io.gitlab.fsc_clam.fscwhereswhat.model.local.ReminderTime
import kotlinx.coroutines.flow.StateFlow
import java.net.URL

/**
 * EntityDetailViewModel handles EntityDetailView when user clicks entity
 */
abstract class EntityDetailViewModel(application: Application) : AndroidViewModel(application) {
	/**
	 * Name is name of the entity
	 */
	abstract val name: StateFlow<String>

	/**
	 * Note is the comment from the user
	 */
	abstract val note: StateFlow<String?>

	/**
	 * URL is the link to RamCentral if event or the link to the OSM provided URL
	 */
	abstract val url: StateFlow<URL?>

	/**
	 * Image is the image of the eentity
	 */
	abstract val image: StateFlow<Image>

	/**
	 * ShareURL is the app specific share link to share with others
	 */
	abstract val shareURL: StateFlow<URL>

	/**
	 * @see OpeningHours
	 */
	abstract val openingHours: StateFlow<List<OpeningHours>>

	/**
	 * Description is the description of the entity
	 */
	abstract val description: StateFlow<String?>

	/**
	 * Type specifies which type is the entity - Node, Entity, Building
	 */
	abstract val type: StateFlow<EntityType?>

	/**
	 * If Entity is node, specifies which nodeType it is
	 * @see NodeType
	 */
	abstract val nodeType: StateFlow<NodeType?>

	//Events
	/**
	 * optional instructions for event
	 */
	abstract val instructions: StateFlow<String?>

	/**
	 * if event has option for RSVP
	 */
	abstract val hasRSVP: StateFlow<Boolean>

	/**
	 * if user set reminder
	 */
	abstract val hasReminder: StateFlow<Boolean>

	/**
	 * if user set reminder, how long until reminder notifies
	 */
	abstract val reminderTime: StateFlow<ReminderTime?>

	/**
	 * sets reminder time from entity detail view
	 */
	abstract fun setReminderTime(time: ReminderTime)

	/**
	 * creates note for entity from view
	 */
	abstract suspend fun setNote(newNote: String)

	/**
	 * deletes reminder
	 */
	abstract fun deleteReminder()

	/**
	 * deletes note
	 */
	abstract fun deleteNote()

}