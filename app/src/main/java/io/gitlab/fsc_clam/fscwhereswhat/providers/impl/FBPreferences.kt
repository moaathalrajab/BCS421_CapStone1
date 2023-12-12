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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.gitlab.fsc_clam.fscwhereswhat.model.local.EntityType
import io.gitlab.fsc_clam.fscwhereswhat.providers.base.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Implementation of Firebase Interface Functions
 */
class FBPreferences : Firebase {

	/**
	 * Set/Update the color in firebase
	 * the KEY will be an index value associated with a node type
	 * the VALUE will be the hex value for the color
	 */
	override suspend fun setColor(user: String, type: String, color: Int) {
		val fb = FirebaseDatabase.getInstance().reference
		val userDir: DatabaseReference = fb.child("userData/$user")

		userDir.child(type).setValue(color)
	}

	/**
	 * Retrieve a flow list of the colors in firebase
	 */
	override fun getColor(user: String): Flow<Map<String, Int>> {
		return callbackFlow {
			val fb = FirebaseDatabase.getInstance().reference
			val userDir: DatabaseReference = fb.child("userData/$user")

			val listener = userDir.addValueEventListener(object : ValueEventListener {
				override fun onDataChange(dataSnapshot: DataSnapshot) {
					//val colors = dataSnapshot.getValue<Map<String, String>>()
					val colors: MutableMap<String, Int> = mutableMapOf(
						EntityType.BUILDING.name to EntityType.BUILDING.defaultColor,
						EntityType.NODE.name to EntityType.NODE.defaultColor,
						EntityType.EVENT.name to EntityType.EVENT.defaultColor,
					)

					for (dataValue in dataSnapshot.children) {
						colors[dataValue.key.toString()] = (dataValue.value as Long).toInt()
					}

					trySend(colors)
				}

				override fun onCancelled(databaseError: DatabaseError) {
					Log.d("getColor:onCancelled", databaseError.toException().toString())
				}
			})

			awaitClose {
				userDir.removeEventListener(listener)
			}
		}
	}

	/**
	 * Binding point for Implementation getter
	 */
	companion object
}