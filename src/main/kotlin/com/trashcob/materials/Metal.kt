package com.trashcob.materials

import com.trashcob.math.Ray
import com.trashcob.math.Vec3
import com.trashcob.math.times
import com.trashcob.shapes.HitRecord

data class Metal(val albedo: Vec3, val fuzzy: Float) : Material {

    init {
        if ((0.0f < fuzzy) and (fuzzy > 1.0f)) throw IllegalArgumentException("fuzzy must be between 0.0f and 1.0f inclusive")
    }

    override fun scatter(rayIn: Ray, hitRecord: HitRecord): MaterialHitRecord {
        val reflected = reflect(rayIn.direction.unit(), hitRecord.normal)
        val scatterRay = Ray(hitRecord.point, reflected + fuzzy * randomInUnitSphere())
        return MaterialHitRecord(albedo, scatterRay)
    }
}