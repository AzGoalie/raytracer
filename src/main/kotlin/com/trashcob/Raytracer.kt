package com.trashcob

import com.trashcob.materials.Dielectric
import com.trashcob.materials.Lambertian
import com.trashcob.materials.Metal
import com.trashcob.math.Ray
import com.trashcob.math.Vec3
import com.trashcob.math.times
import com.trashcob.shapes.Hitable
import com.trashcob.shapes.Sphere
import com.trashcob.shapes.hit
import javafx.application.Platform
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newSingleThreadContext
import kotlinx.coroutines.experimental.run
import java.lang.Math.sqrt
import java.util.*

data class Pixel(val x: Int, val y: Int, val color: Color)

class Raytracer {

    fun createImage(samples: Int, image: WritableImage) {
        val width = image.width.toInt()
        val height = image.height.toInt()

        val camera = Camera(
                lookFrom = Vec3(13f, 2f, 3f),
                lookAt = Vec3(0f, 0f, 0f),
                up = Vec3(0f, 1f, 0f),
                vFov = 20f,
                aspectRatio = width / height.toFloat(),
                aperture = 0.1f,
                focusDist = 10f)

        val shapes = createRandomScene()

        val random = Random()

        val drawContext = newSingleThreadContext("DrawContext")
        for (y in 0 until height) {
            launch(CommonPool) {
                for (x in 0 until width) {

                    var color = Vec3()
                    for (i in 0 until samples) {
                        val u = (x + random.nextFloat()) / width.toFloat()
                        val v = (y + random.nextFloat()) / height.toFloat()

                        val ray = camera.getRay(u, v)
                        color += color(ray, 0, shapes)
                    }

                    run(drawContext) {
                        Platform.runLater {
                            image.pixelWriter.setColor(x, height - 1 - y, gammaCorrection(color))
                        }
                    }
                }
            }
        }
    }

    fun createRandomScene(): List<Hitable> {
        val listOfHitables = arrayListOf<Hitable>()
        val floor = Sphere(Vec3(0f, -1000f, 0f), 1000f, Lambertian(Vec3(0.5f, 0.5f, 0.5f)))

        listOfHitables.add(floor)

        val random = Random()
        for (a in -11 until 11) {
            for (b in -11 until 11) {
                val chooseMat = random.nextFloat()
                val center = Vec3(a + 0.9f * random.nextFloat(), 0.2f, b + 0.9f * random.nextFloat())
                if ((center - Vec3(4f, 0.2f, 0f)).length() > 0.9f) {
                    if (chooseMat < 0.8f) {
                        listOfHitables.add(Sphere(center, 0.2f, Lambertian(Vec3(random.nextFloat() * random.nextFloat(),
                                random.nextFloat() * random.nextFloat(),
                                random.nextFloat() * random.nextFloat()))))
                    } else if (chooseMat < 0.95) {
                        listOfHitables.add(Sphere(center, 0.2f, Metal(Vec3(
                                0.5f * (1 + random.nextFloat()),
                                0.5f * (1 + random.nextFloat()),
                                0.5f * random.nextFloat()), 0.5f * random.nextFloat())))
                    } else {
                        listOfHitables.add(Sphere(center, 0.2f, Dielectric(1.5f)))
                    }
                }
            }
        }

        listOfHitables.add(Sphere(Vec3(0f, 1f, 0f), 1.0f, Dielectric(1.5f)))
        listOfHitables.add(Sphere(Vec3(-4f, 1f, 0f), 1.0f, Lambertian(Vec3(0.4f, 0.2f, 0.1f))))
        listOfHitables.add(Sphere(Vec3(4f, 1f, 0f), 1.0f, Metal(Vec3(0.7f, 0.6f, 0.5f), 0.0f)))

        return listOfHitables
    }

    fun gammaCorrection(color: Vec3): Color {
        return Color(sqrt(color.x.toDouble() / samples),
                sqrt(color.y.toDouble() / samples),
                sqrt(color.z.toDouble() / samples),
                1.0)
    }

    fun color(ray: Ray, depth: Int, world: List<Hitable>): Vec3 {
        val hitRecord = world.hit(ray, 0.001f, Float.MAX_VALUE)

        when {
            hitRecord != null && (depth < 50) -> {
                val materialResult = hitRecord.material.scatter(ray, hitRecord)
                materialResult?.let { return materialResult.attenuation * color(materialResult.scatterRay, depth + 1, world) }
                        ?: return Vec3()
            }

            else -> {
                val unitDirection = ray.direction.unit()
                val time = 0.5f * (unitDirection.y + 1.0f)
                return (1.0f - time) * Vec3(1.0f) + time * Vec3(0.5f, 0.7f, 1.0f)
            }
        }
    }
}
