package com.trashcob

import com.trashcob.math.Ray
import com.trashcob.math.Vec3
import com.trashcob.math.times
import java.util.*


data class Camera(val lookFrom: Vec3, val lookAt: Vec3, val up: Vec3,
                  val vFov: Float, val aspectRatio: Float, val aperture: Float,
                  val focusDist: Float) {
    private val lensRadius = aperture / 2

    private val theta = vFov * Math.PI / 180
    private val halfHeight = Math.tan(theta / 2).toFloat()
    private val halfWidth = aspectRatio * halfHeight

    private val w = (lookFrom - lookAt).unit()
    private val u = up.cross(w).unit()
    private val v = w.cross(u)

    private val lowerLeftCorner = lookFrom - halfWidth * focusDist * u - halfHeight * focusDist * v - focusDist * w
    private val horizontal = 2 * halfWidth * focusDist * u
    private val vertical = 2 * halfHeight * focusDist * v

    private val random = Random()

    fun getRay(s: Float, t: Float): Ray {
        val rd = lensRadius * randomVecInUnitDisk()
        val offset = u * rd.x + v * rd.y
        return Ray(lookFrom + offset, lowerLeftCorner + s * horizontal + t * vertical - lookFrom - offset)
    }

    fun randomVecInUnitDisk(): Vec3 {
        var p: Vec3

        do {
            p = 2.0f * Vec3(random.nextFloat(), random.nextFloat(), 0f) - Vec3(1f, 1f, 0f)
        } while (p.dot(p) >= 1.0f)

        return p
    }
}