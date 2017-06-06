package com.trashcob.math

import java.lang.Math.sqrt

operator fun Float.times(b: Vec3): Vec3 {
    return b * this
}

data class Vec3(val x: Float = 0f, val y: Float = 0f, val z: Float = 0f) {

    constructor(scalar: Float) : this(scalar, scalar, scalar)

    operator fun plus(b: Vec3): Vec3 {
        return Vec3(x + b.x, y + b.y, z + b.z)
    }

    operator fun minus(b: Vec3): Vec3 {
        return Vec3(x - b.x, y - b.y, z - b.z)
    }

    operator fun unaryMinus(): Vec3 {
        return Vec3(-x, -y, -z)
    }

    operator fun times(b: Vec3): Vec3 {
        return Vec3(x * b.x, y * b.y, z * b.z)
    }

    operator fun times(b: Float): Vec3 {
        return Vec3(x * b, y * b, z * b)
    }

    operator fun div(b: Vec3): Vec3 {
        return Vec3(x / b.x, y / b.y, z / b.z)
    }

    operator fun div(b: Float): Vec3 {
        return Vec3(x / b, y / b, z / b)
    }

    fun length(): Float {
        return sqrt((x * x
                + y * y
                + z * z).toDouble())
                .toFloat()
    }

    fun squaredLength(): Float {
        return (x * x) + (y * y) + (z * z)
    }

    fun dot(b: Vec3): Float {
        return (x * b.x) + (y * b.y) + (z * b.z)
    }

    fun cross(b: Vec3): Vec3 {
        return Vec3(
                x = (y * b.z) - (z * b.y),
                y = (z * b.x) - (x * b.z),
                z = (x * b.y) - (y * b.x)
        )
    }

    fun unit(): Vec3 {
        return this / length()
    }
}
