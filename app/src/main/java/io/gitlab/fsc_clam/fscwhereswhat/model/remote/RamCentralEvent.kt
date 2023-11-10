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

package io.gitlab.fsc_clam.fscwhereswhat.model.remote

@Deprecated("API mismatch")
data class RamCentralEvent(
	val eventId :Int?,
	val eventName :String?,
	val organizationId :Int?,
	val organizationName :String?,
	val startDateTime :String?,
	val endDateTime :String?,
	val externalId :String?,
	val locationExternalId :String?,
	val locationId :Int?,
	val locationName :String?,
	val otherLocation :String?,
	val onlineLocation :String?,
	val instructions :String?,
	val address :String?,
	val addressStreet1 :String?,
	val addressStreet2 :String?,
	val addressCity :String?,
	val addressStateProvince :String?,
	val addressZipPostal :String?,
	val description :String?,
	val accessCode :String?,
	val eventUrl :String?,
	val flyerUrl :String?,
	val theme :String?,
	val thumbnailUrl :String?,
	val typeId :Int?,
	val typeName :String?,
	val rsvpOption :String?,
	val rsvpMaxSpotsAllowed :Int?,
	val allowSelfReportAttendance :String?,
	val allowOnTranscript :String?,
	val submittedOn :String?,
	val submittedByUserId :Int?,
	val submittedByUsername :String?,
	val submittedByUserCampusEmail :String?,
	val approvedOn :String?,
	val approvedByUserId :Int?,
	val approvedByUsername :String?,
	val approvedByUserCampusEmail :String?,
	val status :String?,
	val categories :List<RamCentralCategory>?,
	val customFields :List<RamCentralCustomField>?,
	val hosts :List<RamCentralOrganizationSlim>?
)
