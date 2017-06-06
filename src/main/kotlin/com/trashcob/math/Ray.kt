package com.trashcob.math

data class Ray(val origin: Vec3, val direction: Vec3) {
    fun pointInTime(t: Float): Vec3 {
        return origin + (t * direction)
    }
}
