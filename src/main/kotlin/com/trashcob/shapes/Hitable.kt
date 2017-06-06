package com.trashcob.shapes

import com.trashcob.materials.EmptyMaterial
import com.trashcob.materials.Material
import com.trashcob.math.Ray
import com.trashcob.math.Vec3

data class HitRecord(val time: Float = 0f, val point: Vec3 = Vec3(),
                     val normal: Vec3 = Vec3(), val material: Material = EmptyMaterial(),
                     val hit: Boolean)

interface Hitable {
    fun hit(ray: Ray, tMin: Float, tMax: Float): HitRecord
}

fun Collection<Hitable>.hit(ray: Ray, tMin: Float, tMax: Float): HitRecord {
    var closestHitTime = tMax
    var hit: HitRecord = HitRecord(hit = false)

    for (hitable in this) {
        val hitRecord = hitable.hit(ray, tMin, closestHitTime)
        if (hitRecord.hit) {
            closestHitTime = hitRecord.time
            hit = hitRecord
        }
    }

    return hit
}