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

package io.gitlab.fsc_clam.fscwhereswhat.providers.impl

import android.util.Log
import com.google.firebase.database.*
import io.gitlab.fsc_clam.fscwhereswhat.providers.base.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

/**
 * Implementation of Firebase Interface Functions
 */
class FBPreferences: Firebase {

	/**
	 * Set/Update the color in firebase
	 * the KEY will be an index value associated with a node type
	 * the VALUE will be the hex value for the color
	 */
	override suspend fun setColor(user: String, colorSet: Map<String, String>) {
		val fb = FirebaseDatabase.getInstance().reference
		val userDir: DatabaseReference = fb.child("userData/$user")

		colorSet.forEach {item ->
			userDir.child( item.key ).setValue( item.value )
		}
	}

	/**
	 * Retrieve a flow list of the colors in firebase
	 */
	override fun getColor(user: String): Flow<Map<String, String>> {
		return callbackFlow {
			val fb = FirebaseDatabase.getInstance().reference
			val userDir: DatabaseReference = fb.child("userData/$user")

			val listener = userDir.addValueEventListener(object: ValueEventListener {
				override fun onDataChange(dataSnapshot: DataSnapshot) {
					//val colors = dataSnapshot.getValue<Map<String, String>>()
					val colors: MutableMap<String, String> = mutableMapOf()

					for (dataValue in dataSnapshot.children)
					{
						colors[dataValue.key.toString()] = dataValue.value.toString()
					}

					trySend(colors)
				}

				override fun onCancelled(databaseError: DatabaseError) {
					Log.d( "getColor:onCancelled", databaseError.toException().toString() )
				}
			})

			awaitClose() {
				userDir.removeEventListener(listener)
			}
		}
	}

	/**
	 * Binding point for Implementation getter
	 */
	companion object
}