package org.aboutus.project.features.earth.domain

import androidx.compose.ui.geometry.Offset
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object Projection3D {

    private fun Float.toRadians(): Float = this * (PI.toFloat() / 180f)

    fun latLngToVector3D(latLng: LatLng, radius: Float): Point3D {
        val latRad = latLng.latitude.toRadians()
        val lonRad = latLng.longitude.toRadians()

        val x = radius * cos(latRad) * sin(lonRad)
        val y = -radius * sin(latRad)
        val z = radius * cos(latRad) * cos(lonRad)

        return Point3D(x, y, z)
    }

    fun rotatePoint(point: Point3D, rotXDeg: Float, rotYDeg: Float): Point3D {
        val radX = rotXDeg.toRadians()
        val radY = rotYDeg.toRadians()

        // Rotation around Y axis (Yaw - افقي)
        val x1 = point.x * cos(radY) + point.z * sin(radY)
        val z1 = -point.x * sin(radY) + point.z * cos(radY)

        // Rotation around X axis (Pitch - رأسي)
        val y2 = point.y * cos(radX) - z1 * sin(radX)
        val z2 = point.y * sin(radX) + z1 * cos(radX)

        return Point3D(x1, y2, z2)
    }

    fun projectToScreen(
        point: Point3D,
        centerX: Float,
        centerY: Float,
        cameraDistance: Float = 1000f
    ): Offset {
        val scale = cameraDistance / (cameraDistance + point.z)
        return Offset(
            x = centerX + (point.x * scale),
            y = centerY + (point.y * scale)
        )
    }
}