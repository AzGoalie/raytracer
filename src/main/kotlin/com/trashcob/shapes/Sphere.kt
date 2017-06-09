package com.trashcob.shapes

import com.trashcob.materials.Material
import com.trashcob.math.Ray
import com.trashcob.math.Vec3
import java.lang.Math.sqrt

data class Sphere(val center: Vec3, val radius: Float, val material: Material) : Hitable {

    override fun hit(ray: Ray, tMin: Float, tMax: Float): HitRecord? {
        val oc = ray.origin - center
        val a = ray.direction.dot(ray.direction)
        val b = oc.dot(ray.direction)
        val c = oc.dot(oc) - radius * radius
        val discriminant = b * b - a * c

        if (discriminant > 0) {
            var temp = (-b - sqrt((b * b - a * c).toDouble()).toFloat()) / a
            if ((temp > tMin) and (temp < tMax)) {
                return HitRecord(
                        time = temp,
                        point = ray.pointInTime(temp),
                        normal = (ray.pointInTime(temp) - center) / radius,
                        material = material
                )
            }

            temp = (-b + sqrt((b * b - a * c).toDouble()).toFloat()) / a
            if ((temp > tMin) and (temp < tMax)) {
                return HitRecord(
                        time = temp,
                        point = ray.pointInTime(temp),
                        normal = (ray.pointInTime(temp) - center) / radius,
                        material = material
                )
            }
        }

        return null
    }
}