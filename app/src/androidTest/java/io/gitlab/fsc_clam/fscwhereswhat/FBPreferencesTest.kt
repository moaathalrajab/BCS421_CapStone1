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

import io.gitlab.fsc_clam.fscwhereswhat.providers.impl.FBPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class FBPreferencesTest {
	private lateinit var fbp: FBPreferences

	@Before
	fun setUp() {
		fbp = FBPreferences()
	}

	/**
	 * Test that colors can be inserted into the database correctly
	 */
	@Test
	fun setColorTest() {
		val info = runBlocking {
			fbp.setColor("Aaron", "0", "#c70000")
			fbp.setColor("Aaron", "1", "#009c24")
			fbp.setColor("Aaron", "2", "#4287f5")
		}

		println(info)
	}

	/**
	 * Test that colors can be retrieved from database correctly
	 */
	@Test
	fun getColorTest() {
		val info = runBlocking {
			val colors: Flow<Map<String, String>> = fbp.getColor("Aaron")
			colors.first()
		}

		println(info)
	}

	@Test
	fun colorIntegrationTest() {
		val info = runBlocking {
			val colors: Map<String, String> = mapOf("0" to "#c70000", "1" to "#009c24", "2" to "#4287f5")
			fbp.setColor("Aaron", "0", "#c70000")
			fbp.setColor("Aaron", "1", "#009c24")
			fbp.setColor("Aaron", "2", "#4287f5")
			val colorFlow: Flow<Map<String,String>> = fbp.getColor("Aaron")

			colorFlow.first() == colors
		}

		println(info)
	}
}