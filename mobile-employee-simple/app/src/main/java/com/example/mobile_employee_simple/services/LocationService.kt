package com.example.mobile_employee_simple.services

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

class LocationService(private val context: Context) {
    
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    
    /**
     * 检查位置权限
     */
    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    /**
     * 检查粗略位置权限
     */
    fun hasCoarseLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    /**
     * 检查GPS是否启用
     */
    fun isGpsEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
    
    /**
     * 检查网络定位是否启用
     */
    fun isNetworkLocationEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    
    /**
     * 获取最后已知位置
     */
    fun getLastKnownLocation(): Location? {
        if (!hasLocationPermission()) return null
        
        var bestLocation: Location? = null
        var bestAccuracy = Float.MAX_VALUE
        var bestTime = 0L
        
        // 尝试从GPS获取位置
        if (isGpsEnabled()) {
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let { location ->
                if (location.accuracy < bestAccuracy || location.time > bestTime) {
                    bestLocation = location
                    bestAccuracy = location.accuracy
                    bestTime = location.time
                }
            }
        }
        
        // 尝试从网络获取位置
        if (isNetworkLocationEnabled()) {
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)?.let { location ->
                if (location.accuracy < bestAccuracy || location.time > bestTime) {
                    bestLocation = location
                    bestAccuracy = location.accuracy
                    bestTime = location.time
                }
            }
        }
        
        return bestLocation
    }
    
    /**
     * 请求位置更新
     */
    fun requestLocationUpdates(
        minTimeMs: Long = 10000, // 10秒
        minDistanceM: Float = 10f, // 10米
        onLocationChanged: (Location) -> Unit
    ) {
        if (!hasLocationPermission()) return
        
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                onLocationChanged(location)
            }
            
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
        
        // 请求GPS位置更新
        if (isGpsEnabled()) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTimeMs,
                minDistanceM,
                locationListener
            )
        }
        
        // 请求网络位置更新
        if (isNetworkLocationEnabled()) {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTimeMs,
                minDistanceM,
                locationListener
            )
        }
    }
    
    /**
     * 停止位置更新
     */
    fun removeLocationUpdates(locationListener: LocationListener) {
        locationManager.removeUpdates(locationListener)
    }
    
    /**
     * 计算两点间距离（米）
     */
    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0]
    }
    
    /**
     * 格式化位置信息
     */
    fun formatLocation(location: Location): String {
        return "纬度: ${location.latitude}, 经度: ${location.longitude}, 精度: ${location.accuracy}米"
    }
    
    /**
     * 获取位置Flow
     */
    fun getLocationFlow(
        minTimeMs: Long = 10000,
        minDistanceM: Float = 10f
    ): Flow<Location> = callbackFlow {
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                trySend(location)
            }
            
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
        
        if (hasLocationPermission()) {
            if (isGpsEnabled()) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTimeMs,
                    minDistanceM,
                    locationListener
                )
            }
            
            if (isNetworkLocationEnabled()) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTimeMs,
                    minDistanceM,
                    locationListener
                )
            }
        }
        
        awaitClose {
            locationManager.removeUpdates(locationListener)
        }
    }
} 