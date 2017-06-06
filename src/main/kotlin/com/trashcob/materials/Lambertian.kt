package com.trashcob.materials

import com.trashcob.math.Ray
import com.trashcob.math.Vec3
import com.trashcob.randomInUnitSphere
import com.trashcob.shapes.HitRecord

data class Lambertian(val albedo: Vec3) : Material {
    override fun scatter(rayIn: Ray, hitRecord: HitRecord): MaterialHitRecord {
        val target = hitRecord.point + hitRecord.normal + randomInUnitSphere()
        val scatterRay = Ray(hitRecord.point, target - hitRecord.point)

        return MaterialHitRecord(albedo, scatterRay, true)
    }
}
