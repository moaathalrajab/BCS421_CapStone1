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

package io.gitlab.fsc_clam.fscwhereswhat.model.local

/**
 * OSM Opening Hours for a node
 * Days are if opening hours are applied on those days
 * @param startHour in what hour is opened
 * @param startMinute in what minute is opened
 * @param endHour in what hour is closed
 * @param endMinute in what minute is closed
 */
data class OpeningHours(
	val monday: Boolean,
	val tuesday: Boolean,
	val wednesday: Boolean,
	val thursday: Boolean,
	val friday: Boolean,
	val saturday: Boolean,
	val sunday: Boolean,
	val startHour: Int,
	val startMinute: Int,
	val endHour: Int,
	val endMinute: Int
) {
	companion object {

		private lateinit var hours: MutableList<OpeningHours>

		/**
		 * Converts the OSM hours string into the opening hours object
		 */
		fun setHours(value: String): List<OpeningHours> {
			val daySet = mutableListOf<String>()
			var str = ""
			value.forEach {
				if (it == ';') {
					daySet.add(str)
					str = ""
				}
				str += it;
			}

			daySet.forEach { set ->
				str = ""
				val isOpen = mutableListOf<Boolean>(false, false, false, false, false, false, false)
				var startHr = 0
				var startMin = 0
				var endHr = 0
				var endMin = 0
				if (set.contains("Mo"))
					isOpen[0] = true

				if (set.contains("Tu"))
					isOpen[1] = true

				if (set.contains("We"))
					isOpen[2] = true

				if (set.contains("Th"))
					isOpen[3] = true

				if (set.contains("Fr"))
					isOpen[4] = true

				if (set.contains("Sa"))
					isOpen[5] = true

				if (set.contains("Su"))
					isOpen[6] = true

				set.forEach { char ->
					if (char.isDigit())
						str += char
					if (str.length == 8) {
						startHr = str.substring(0, 1).toInt()
						startMin = str.substring(2, 3).toInt()
						endHr = str.substring(4, 5).toInt()
						endMin = str.substring(6, 7).toInt()
					}
				}

				hours.add(
					OpeningHours(
						isOpen[0], isOpen[1], isOpen[2], isOpen[3],
						isOpen[4], isOpen[5], isOpen[6], startHr, startMin, endHr, endMin
					)
				)
			}

			return hours
		}

		val everyDay = OpeningHours(
			monday = true,
			tuesday = true,
			wednesday = true,
			thursday = true,
			friday = true,
			saturday = true,
			sunday = true,
			startHour = 0,
			startMinute = 0,
			endHour = 24,
			endMinute = 0
		)
	}
}
