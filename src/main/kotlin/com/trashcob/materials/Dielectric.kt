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

        when {
            rayIn.direction.dot(hitRecord.normal) > 0 -> {
                outwardNormal = -hitRecord.normal
                niOverNt = refractionIndex
                cosine = refractionIndex * rayIn.direction.dot(hitRecord.normal) / rayIn.direction.length()
            }
            else -> {
                outwardNormal = hitRecord.normal
                niOverNt = 1.0f / refractionIndex
                cosine = -rayIn.direction.dot(hitRecord.normal) / rayIn.direction.length()
            }
        }

        val refractResult = refract(rayIn.direction, outwardNormal, niOverNt)
        val reflectionProbability = refractResult?.let { schlick(cosine, refractionIndex) } ?: 1.0f

        when (random.nextFloat()) {
            in 0f..reflectionProbability -> return MaterialHitRecord(attenuation, Ray(hitRecord.point, reflected))
            else -> return MaterialHitRecord(attenuation, Ray(hitRecord.point, refractResult?.refracted ?: Vec3()))
        }
    }
}