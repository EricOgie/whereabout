package com.tees.s3186984.whereabout.componets


import android.Manifest
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.maps.android.compose.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.maps.android.compose.Circle
import com.tees.s3186984.whereabout.wutils.ADDRESS_RESOLUTION_ERROR
import com.tees.s3186984.whereabout.wutils.Helpers
import com.tees.s3186984.whereabout.wutils.MapInteractivityState

/**
 * A composable function that embed and displays a Google Map with location-based interactivity.
 *
 * This composable:
 * - Requests the user's location permission and retrieves their current location upon approval.
 * - Displays a Google Map centered on the user's current location (if available).
 * - Allows users to interact with the map and fetch address details for their location when they click
 *   the "My Location" button.
 * - Notifies the parent composable of changes in the state of location resolution via the
 *   `handleLocationPinClick` callback.
 *
 * @param modifier Modifier for styling or layout adjustments.
 * @param handleLocationPinClick A callback function to notify the parent composable about the
 *                                state of location resolution. This function emits a
 *                                [MapInteractivityState], which can represent:
 *                                - `Resolving`: Indicates that the app is currently resolving the user's address.
 *                                - `MapData`: Contains resolved address data, represented as a
 *                                  `Pair<String, List<String>>`, where the first value is the formatted address
 *                                  and the second is a list of additional address details.
 *                                - `Error`: Represents an error state with a message (e.g., failure to resolve address).
 *
 * The flow of emitted values via `handleLocationPinClick`:
 * 1. **Resolving**: When the user clicks the "My Location" button, this state is emitted to notify the parent
 *    that the app is attempting to resolve the user's address.
 * 2. **MapData**: If the address resolution is successful, this state is emitted with the resolved address details.
 * 3. **Error**: If the address resolution fails, this state is emitted with an appropriate error message.
 *
 * Usage:
 * ```
 * GoogleMapView(
 *     modifier = Modifier.fillMaxSize(),
 *     handleLocationPinClick = { mapInteractivityState ->
 *
 *     }
 * )
 * ```
 *
 * @see MapInteractivityState
 */
@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    handleLocationPinClick: (MapInteractivityState) -> Unit
)
{

    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var hasLocationPermission by remember { mutableStateOf(false) }

    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 10000)
        .setMinUpdateIntervalMillis(5000)
        .setMaxUpdateDelayMillis(10000)
        .setWaitForAccurateLocation(false) // setting to false so we can prioritize user's device battery
        .build()


    // Ask user for their location permission
    RequestPermissionHandler(Manifest.permission.ACCESS_FINE_LOCATION) { isGranted ->
        hasLocationPermission = isGranted
    }

    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    currentLocation = LatLng(location.latitude, location.longitude)
                }
            }
        }
    }



    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            try {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            } catch (e: SecurityException) {
                Log.d("GoogleMapView", "Location permission not granted", e)
            }
        } else {
            // If permission is revoked, stop location updates
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }


    // Create a camera position state
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation ?: LatLng(0.0, 0.0), 15f)
    }

    LaunchedEffect(currentLocation) {
        currentLocation?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            myLocationButtonEnabled = true,
            mapToolbarEnabled = false,
            zoomControlsEnabled = false
        ),
        properties = MapProperties(isMyLocationEnabled = hasLocationPermission),

        onMyLocationClick = { myLocation ->
            // Notify parent that the location is being resolved
            handleLocationPinClick(MapInteractivityState.Resolving)
            val latLng = LatLng(myLocation.latitude, myLocation.longitude)
            Helpers.resolveAddress(latLng, context){ addressLookUpResult ->
                // Update state based on resolution result
                if (addressLookUpResult != null){
                    handleLocationPinClick(
                        MapInteractivityState.MapData(addressLookUpResult)
                    )
                }else{
                    handleLocationPinClick(MapInteractivityState.Error(ADDRESS_RESOLUTION_ERROR))
                }
            }
        },
    ){
        currentLocation?.let {
            Circle(
                center = it,
                radius = 50.0,
                fillColor = Color(0x550000FF),
                strokeColor = Color.Blue.copy(0.1f),
                strokeWidth = 2f
            )
        }
    }
}
