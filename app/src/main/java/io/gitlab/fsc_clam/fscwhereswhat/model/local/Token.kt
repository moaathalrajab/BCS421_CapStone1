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
 * For Search queries with multiple words
 * If user searches "water and food", [strings] will contain a list ["water", "food"].
 * If user searches "water or food", two tokens with the first one being "water", the second being "food"
 * If user searches "water not in campus center", token will have water, afterwards filtering out anything mentioning "campus center"
 * If user searches "water in campus center", token will have water, afterwards filtering for "campus center"
 */
data class Token(val strings: List<String>)

