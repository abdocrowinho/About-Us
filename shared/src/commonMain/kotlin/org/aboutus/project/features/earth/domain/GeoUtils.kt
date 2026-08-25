package org.aboutus.project.features.earth.domain


object GeoUtils {


    fun getCenterLatLng(rotationX: Float, rotationY: Float): LatLng {
        var normX = rotationX % 360f
        if (normX > 180f) normX -= 360f
        if (normX < -180f) normX += 360f

        var lat = -normX
        var lon = -rotationY % 360f

        if (lat > 90f) {
            lat = 180f - lat
            lon += 180f
        } else if (lat < -90f) {
            lat = -180f - lat
            lon += 180f
        }

        if (lon > 180f) lon -= 360f
        if (lon < -180f) lon += 360f

        return LatLng(latitude = lat, longitude = lon)
    }

    fun isPointInPolygon(point: LatLng, polygon: List<LatLng>): Boolean {
        if (polygon.size < 3) return false
        var intersects = false
        var j = polygon.size - 1

        for (i in polygon.indices) {
            val pi = polygon[i]
            val pj = polygon[j]

            val cond1 = (pi.latitude > point.latitude) != (pj.latitude > point.latitude)
            if (cond1) {
                val intersectLon = (pj.longitude - pi.longitude) *
                        (point.latitude - pi.latitude) / (pj.latitude - pi.latitude) + pi.longitude
                if (point.longitude < intersectLon) {
                    intersects = !intersects
                }
            }
            j = i
        }
        return intersects
    }
}