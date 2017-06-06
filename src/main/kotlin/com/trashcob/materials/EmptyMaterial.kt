package com.trashcob.materials

import com.trashcob.math.Ray
import com.trashcob.math.Vec3
import com.trashcob.shapes.HitRecord

class EmptyMaterial : Material {
    override fun scatter(rayIn: Ray, hitRecord: HitRecord): MaterialHitRecord {
        return MaterialHitRecord(Vec3(), Ray(Vec3(), Vec3()), false)
    }
}