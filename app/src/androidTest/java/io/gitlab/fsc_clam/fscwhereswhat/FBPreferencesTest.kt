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

import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.providers.impl.FBPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.UUID

class FBPreferencesTest {
	private lateinit var fbp: FBPreferences
	private lateinit var userId: String

	@Before
	fun setUp() {
		fbp = FBPreferences()
		userId = UUID.randomUUID().toString()
	}

	/**
	 * Test that colors can be inserted into the database correctly
	 */
	@Test
	fun setColorTest() {
		runBlocking {
			EntityType.entries.forEach { type ->
				fbp.setColor(userId, type.name, type.defaultColor)
			}
		}
	}

	/**
	 * Test that colors can be retrieved from database correctly
	 */
	@Test
	fun getColorTest() {
		val colors: Flow<Map<String, Int>> = fbp.getColor(userId)
		val info = runBlocking { colors.first() }
		println(info)
	}

	@Test
	fun colorIntegrationTest() {
		// Expected values
		val colors: Map<String, Int> =
			EntityType.entries.associate { it.toString() to it.defaultColor }

		// Set upstream with values
		runBlocking {
			colors.forEach { (type, color) ->
				fbp.setColor(userId, type, color)
			}
		}

		// Get the values
		val colorFlow: Flow<Map<String, Int>> = fbp.getColor(userId)

		// Assert they are true
		runBlocking {
			assert(colorFlow.first() == colors)
		}
	}
}