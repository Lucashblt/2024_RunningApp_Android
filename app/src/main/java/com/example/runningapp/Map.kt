package com.example.runningapp

import android.Manifest
import android.annotation.SuppressLint
import android.view.MotionEvent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.toArgb
import androidx.compose.foundation.layout.size
import androidx.navigation.NavHostController
import org.osmdroid.config.Configuration
import org.osmdroid.views.overlay.Overlay


@Composable
fun MapViewComposable(boxHeight: Dp, locations: List<GeoPoint>, currentLocation : GeoPoint) {
    val context = LocalContext.current

    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(20.0)
        }
    }

    DisposableEffect(Unit) {
        mapView.onResume()
        onDispose {
            mapView.onPause()
            mapView.onDetach()
        }
    }

    if (locations.isNotEmpty()) {
        val polyline = Polyline().apply {
            setPoints(locations)
        }
        mapView.overlays.add(polyline)
        mapView.controller.setCenter(locations.last())
    } else {
        mapView.controller.setCenter(currentLocation)
    }

    AndroidView(
        factory = {
            mapView
        },
        modifier = Modifier
            .height(boxHeight)
            .fillMaxWidth()
    )
}

@Composable
fun RouteMapComposable(profile : Boolean, route : String?, navController : NavHostController, routeData: String) {
    val context = LocalContext.current

    Configuration.getInstance().load(context, androidx.preference.PreferenceManager.getDefaultSharedPreferences(context))

    val mapView = remember {
        if (!profile) {
            MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(19.0)
            }
        } else {
            MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(false)
                controller.setZoom(17.0)
                setBuiltInZoomControls(false)
                isClickable = false
            }
        }
    }

    if (profile){
        val overlay = object : Overlay() {
            override fun onTouchEvent(event: MotionEvent, mapView: MapView): Boolean {
                navController.navigate("$route")
                return true
            }
        }
        mapView.overlayManager.add(overlay)
    }

    val points: List<GeoPoint> = Gson().fromJson(routeData, object : TypeToken<List<GeoPoint>>() {}.type)

    if (points.isNotEmpty()) {
        val polyline = Polyline().apply {
            setPoints(points)
            outlinePaint.color = Color.Blue.toArgb()
            outlinePaint.strokeWidth = 5f
        }
        mapView.overlayManager.add(polyline)
        mapView.controller.setCenter(points.last())
    }

    AndroidView(
        factory = { mapView },
        modifier = if (!profile) {
            Modifier
                .size(250.dp)
        } else {
            Modifier
        }
    ) { map ->
        map.onResume()
        map.controller.setCenter(if (points.isNotEmpty()) points.last() else GeoPoint(0.0, 0.0))
    }
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun RequestLocationPermissions(onPermissionGranted: @Composable () -> Unit) {
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    LaunchedEffect(Unit) {
        permissionsState.launchMultiplePermissionRequest()
    }

    when {
        permissionsState.allPermissionsGranted -> {
            onPermissionGranted()
        }
        else -> {
            Text(text = "Location permissions are required for this app")
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun TrackLocationUpdates(onLocationUpdate: (GeoPoint) -> Unit) {
    val context = LocalContext.current
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    val locationRequest = LocationRequest.Builder(2500L)
        .setMinUpdateIntervalMillis(5000L)
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .build()

    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.forEach { location ->
                    onLocationUpdate(GeoPoint(location.latitude, location.longitude))
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                onLocationUpdate(GeoPoint(it.latitude, it.longitude))
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    DisposableEffect(Unit) {
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
        onDispose {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }
}