package com.tees.s3186984.whereabout.wutils

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import android.Manifest
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.tees.s3186984.whereabout.model.LocationAddress

class Helpers(){

    companion object {

        /**
         * Validates an email address format.
         *
         * @param email The email address to validate.
         * @return `true` if the email matches the expected pattern; `false` otherwise.
         */
        fun isValidEmail(email: String): Boolean {
            val emailPattern = Regex(
                "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
            )
            return emailPattern.matches(email)
        }


        /**
         * Validates a password for minimum length 8 and presence of a special character.
         *
         * @param password The password to validate.
         * @return `true` if the password meets the criteria; `false` otherwise.
         */
        fun isValidPassword(password: String): Boolean {
            val minLength = 8
            val specialCharPattern = Regex("[^A-Za-z0-9]")
            return password.length >= minLength && specialCharPattern.containsMatchIn(password)
        }


        /**
         * Validates a full name, allowing only letters and spaces, within a length range.
         *
         * @param fullName The full name to validate.
         * @return `true` if the full name meets the criteria; `false` otherwise.
         */
        fun isValidFullName(fullName: String): Boolean {
            return fullName.isNotBlank() &&
                    fullName.all { it.isLetter() || it.isWhitespace() } &&
                    fullName.length in 3..20
        }

        /**
         * Validates a phrase within a length range.
         *
         * @param phrase The security phrase to validate.
         * @return `true` if the phrase meets the criteria; `false` otherwise.
         */
        fun isValidPhrase(phrase: String): Boolean {
            return phrase.isNotBlank() &&
                    phrase.length in 5..100
        }


        fun capitalizeWords(input: String): String {
            return input.split(" ")
                .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
        }


        fun resolveAddress(latLng: LatLng, context: Context, onResolve: (LocationAddress?) -> Unit){
            try {
                val geocoder = Geocoder(context)
                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0].getAddressLine(0)
                    // get Nearby landmarks
                    nearByLandMarks(context){ landmarks ->

                        if (landmarks.isNullOrEmpty()){
                            val locationAdd = LocationAddress(address = address, landmarks = emptyList<Place>())
                            onResolve(locationAdd)
                            return@nearByLandMarks
                        }

                        val firstTwoLandMarks = landmarks.take(2)
                        val result = LocationAddress(address = address, landmarks = firstTwoLandMarks)
                        onResolve(result)
                    }

                } else {
                    onResolve(null)
                }
            } catch (e: Exception) {
                Log.d("Resolve-Address", "${FETCHING_ERROR} Error: ${e.message}")
                onResolve(null)
            }
        }


        fun nearByLandMarks(context: Context, onResult: (List<Place>?) -> Unit) {
            val appInfo = context.packageManager
                .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)

            val googleMapKey = appInfo.metaData.getString(GOOGLE_API_KEY)

            if (!Places.isInitialized()) {
                Places.initialize(context, googleMapKey!!)
            }

            try {
                val placeClient = Places.createClient(context)

                val requestPLaceFields = listOf(
                    Place.Field.NAME,
                    Place.Field.ADDRESS
                )

                val request = FindCurrentPlaceRequest.newInstance(requestPLaceFields)

                if ( ContextCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    placeClient.findCurrentPlace(request)
                        .addOnSuccessListener { response ->
                            val places = response.placeLikelihoods.map { it.place }
                            onResult(places)
                        }
                        .addOnFailureListener { exception ->
                            onResult(null)
                            Log.d("FetchLandmarks", "$FETCHING_ERROR landmarks: ${exception.message}")
                        }
                } else {
                    onResult(null)
                    Log.d("FetchLandmarks", LOCATION_PERMISSION_ERROR)
                }

            }catch (e: Exception){
                onResult(null)
                Log.d("LANDMARK", "resolveAddress EXCEPTYION: ${e.message}")
            }
        }

    }


}





