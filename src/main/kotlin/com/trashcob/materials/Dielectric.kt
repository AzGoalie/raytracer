package com.trashcob.materials

import com.trashcob.math.Ray
import com.trashcob.math.Vec3
import com.trashcob.shapes.HitRecord

data class Dielectric(val refractionIndex: Float) : Material {
    override fun scatter(rayIn: Ray, hitRecord: HitRecord): MaterialHitRecord {
        val reflected = reflect(rayIn.direction, hitRecord.normal)
        val attenuation = Vec3(1.0f, 1.0f, 1.0f)

        val outwardNormal: Vec3
        val niOverNt: Float

        if (rayIn.direction.dot(hitRecord.normal) > 0) {
            outwardNormal = -hitRecord.normal
            niOverNt = refractionIndex
        } else {
            outwardNormal = hitRecord.normal
            niOverNt = 1.0f / refractionIndex
        }

        val refractResult = refract(rayIn.direction, outwardNormal, niOverNt)
        if (refractResult.hit) {
            return MaterialHitRecord(attenuation, Ray(hitRecord.point, refractResult.refracted), true)
        } else {
            return MaterialHitRecord(attenuation, Ray(hitRecord.point, reflected), true)
        }
    }
}