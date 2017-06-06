package com.trashcob

import com.trashcob.math.Ray
import com.trashcob.math.Vec3
import com.trashcob.math.times

data class Camera(val lowerLeftCorner: Vec3 = Vec3(-2.0f, -1.0f, -1.0f),
                  val horizontal: Vec3 = Vec3(4.0f, 0.0f, 0.0f),
                  val vertical: Vec3 = Vec3(0.0f, 2.0f, 0.0f),
                  val origin: Vec3 = Vec3()) {

    fun getRay(u: Float, v: Float): Ray {
        return Ray(origin,
                lowerLeftCorner + u * horizontal + v * vertical - origin)
    }
}