package org.aboutus.project.features.earth.domain


data class LatLng(
    val latitude: Float,  // -90 to 90
    val longitude: Float  // -180 to 180
)

data class Point3D(
    val x: Float,
    val y: Float,
    val z: Float
)

data class Country(
    val id: String,
    val name: String,
    val boundary: List<LatLng> = emptyList(),
    val points3D: List<Point3D> = emptyList()
)