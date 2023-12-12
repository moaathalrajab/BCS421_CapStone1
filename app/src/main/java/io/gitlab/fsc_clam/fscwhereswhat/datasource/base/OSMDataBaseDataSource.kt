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

package io.gitlab.fsc_clam.fscwhereswhat.datasource.base

import io.gitlab.fsc_clam.fscwhereswhat.model.local.OSMEntity
import io.gitlab.fsc_clam.fscwhereswhat.model.local.Token
import kotlinx.coroutines.flow.Flow

interface OSMDataBaseDataSource {
	/**
	 * Query for entities matching [token].
	 *
	 * Is a flow to observe changes.
	 */
	suspend fun query(token: Token): Flow<List<OSMEntity>>

	/**
	 * Query for entities nearby a given point
	 *
	 * Is a flow to observe changes.
	 */
	suspend fun queryNearby(latitude: Double, longitude: Double): Flow<List<OSMEntity>>

	/**
	 * Get an entity by its given entity id.
	 *
	 * May not exist.
	 */
	suspend fun get(id: Long): OSMEntity?

	/**
	 * Update with new info on entities
	 */
	suspend fun update(entities: List<OSMEntity>)

	/**
	 * Get entities with names similar to [name].
	 */
	suspend fun getLikeName(name: String): List<OSMEntity>

	/**
	 * Get the nearest OSMEntity based on a given [latitude] and [longitude].
	 *
	 * Should not return a null. But is possible if there are no OSM entities in the database.
	 */
	suspend fun getNear(latitude: Double, longitude: Double): OSMEntity?

	/**
	 * Companion object for implementation
	 */
	companion object
}