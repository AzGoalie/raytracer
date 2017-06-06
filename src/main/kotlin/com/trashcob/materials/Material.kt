package com.trashcob.materials

import com.trashcob.math.Ray
import com.trashcob.math.Vec3
import com.trashcob.math.times
import com.trashcob.shapes.HitRecord
import java.lang.Math.sqrt

data class MaterialHitRecord(val attenuation: Vec3, val scatterRay: Ray, val shouldScatter: Boolean)

interface Material {
    fun scatter(rayIn: Ray, hitRecord: HitRecord): MaterialHitRecord
}

fun reflect(vector: Vec3, normal: Vec3): Vec3 {
    return vector - 2 * vector.dot(normal) * normal
}

data class RefractResult(val hit: Boolean, val refracted: Vec3)

fun refract(vector: Vec3, normal: Vec3, niOverNt: Float): RefractResult {
    val unitVector = vector.unit()
    val dt = unitVector.dot(normal)
    val discriminant = 1.0f - niOverNt * niOverNt * (1f - dt * dt)
    if (discriminant > 0) {
        return RefractResult(true,
                niOverNt * (unitVector - normal * dt) - normal * sqrt(discriminant.toDouble()).toFloat())
    }
    return RefractResult(false, Vec3())
}