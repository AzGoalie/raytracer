package com.trashcob.materials

import com.trashcob.math.Ray
import com.trashcob.math.Vec3
import com.trashcob.math.times
import com.trashcob.shapes.HitRecord
import java.lang.Math.sqrt
import java.util.*

data class MaterialHitRecord(val attenuation: Vec3, val scatterRay: Ray)

interface Material {
    fun scatter(rayIn: Ray, hitRecord: HitRecord): MaterialHitRecord?
}

fun randomInUnitSphere(): Vec3 {
    val random = Random()
    var p: Vec3
    do {
        p = 2.0f * Vec3(random.nextFloat(), random.nextFloat(), random.nextFloat()) - Vec3(1f)
    } while (p.dot(p) >= 1.0f)

    return p
}

fun reflect(vector: Vec3, normal: Vec3): Vec3 {
    return vector - 2 * vector.dot(normal) * normal
}

data class RefractResult(val refracted: Vec3)

fun refract(vector: Vec3, normal: Vec3, niOverNt: Float): RefractResult? {
    val unitVector = vector.unit()
    val dt = unitVector.dot(normal)
    val discriminant = 1.0f - niOverNt * niOverNt * (1f - dt * dt)

    return if (discriminant > 0) RefractResult(niOverNt * (unitVector - normal * dt) - normal * sqrt(discriminant.toDouble()).toFloat())
    else null
}


fun schlick(cosine: Float, refractionIndex: Float): Float {
    var r0 = (1 - refractionIndex) / (1 + refractionIndex)
    r0 *= r0
    return r0 + (1 - r0) * Math.pow((1 - cosine).toDouble(), 5.0).toFloat()
}