package com.trashcob.materials

import com.trashcob.math.Ray
import com.trashcob.math.Vec3
import com.trashcob.shapes.HitRecord
import java.util.*

val random = Random()

data class Dielectric(val refractionIndex: Float) : Material {
    override fun scatter(rayIn: Ray, hitRecord: HitRecord): MaterialHitRecord {
        val reflected = reflect(rayIn.direction, hitRecord.normal)
        val attenuation = Vec3(1.0f, 1.0f, 1.0f)

        val outwardNormal: Vec3
        val niOverNt: Float

        val cosine: Float
        if (rayIn.direction.dot(hitRecord.normal) > 0) {
            outwardNormal = -hitRecord.normal
            niOverNt = refractionIndex
            cosine = refractionIndex * rayIn.direction.dot(hitRecord.normal) / rayIn.direction.length()
        } else {
            outwardNormal = hitRecord.normal
            niOverNt = 1.0f / refractionIndex
            cosine = -rayIn.direction.dot(hitRecord.normal) / rayIn.direction.length()
        }

        val refractResult = refract(rayIn.direction, outwardNormal, niOverNt)
        val reflectionProbability: Float
        if (refractResult.hit) {
            reflectionProbability = schlick(cosine, refractionIndex)
        } else {
            reflectionProbability = 1.0f
        }

        if (random.nextFloat() < reflectionProbability) {
            return MaterialHitRecord(attenuation, Ray(hitRecord.point, reflected), true)
        } else {
            return MaterialHitRecord(attenuation, Ray(hitRecord.point, refractResult.refracted), true)
        }
    }
}