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
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import tornadofx.View
import tornadofx.plusAssign
import java.lang.Math.sqrt
import java.util.*

data class Pixel(val x: Int, val y: Int, val color: Color)

class RaytracedImage : View() {
    val width = 1200
    val height = 800
    val samples = 10

    override val root = VBox()

    init {
        val image = WritableImage(width, height)
        root += ImageView(image)

        runAsync {
            createImage(samples, image)
        }
    }

    fun createImage(samples: Int, image: WritableImage) {
        val width = image.width.toInt()
        val height = image.height.toInt()

        val lookFrom = Vec3(13f, 2f, 3f)
        val lookAt = Vec3(0f, 0f, 0f)
        val distToFocus = 10f
        val aperture = 0.1f
        val camera = Camera(lookFrom, lookAt, Vec3(0f, 1f, 0f), 20f, width / height.toFloat(), aperture, distToFocus)

        val shapes = createRandomScene()

        val random = Random()

        for (y in 0 until height) {
            launch(CommonPool) {
                val rowOfPixels = arrayListOf<Pixel>()
                for (x in 0 until width) {

                    var color = Vec3()
                    for (i in 0 until samples) {
                        val u = (x + random.nextFloat()) / width.toFloat()
                        val v = (y + random.nextFloat()) / height.toFloat()

                        val ray = camera.getRay(u, v)
                        color += color(ray, 0, shapes)
                    }

                    val pixel = Pixel(x, height - 1 - y, gammaCorrection(color))
                    rowOfPixels.add(pixel)
                }

                Platform.runLater {
                    rowOfPixels.forEach { image.pixelWriter.setColor(it.x, it.y, it.color) }
                }
            }
        }
    }

    fun createShapes(): List<Hitable> {
        return listOf<Hitable>(
                Sphere(Vec3(0f, 0f, -1f), 0.5f, Lambertian(Vec3(0.1f, 0.2f, 0.5f))),
                Sphere(Vec3(0f, -100.5f, -1f), 100f, Lambertian(Vec3(0.8f, 0.8f, 0.0f))),
                Sphere(Vec3(1f, 0f, -1f), 0.5f, Metal(Vec3(0.8f, 0.6f, 0.2f), 0.0f)),
                Sphere(Vec3(-1f, 0f, -1f), 0.5f, Dielectric(1.5f)),
                Sphere(Vec3(-1f, 0f, -1f), -0.45f, Dielectric(1.5f)))
    }

    fun createRandomScene(): List<Hitable> {
        val n = 500
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
        if ((hitRecord.hit) and (depth < 50)) {
            val materialHit = hitRecord.material.scatter(ray, hitRecord)
            if (materialHit.shouldScatter) {
                return materialHit.attenuation * color(materialHit.scatterRay, depth + 1, world)
            } else {
                return Vec3()
            }

        } else {
            val unitDirection = ray.direction.unit()
            val time = 0.5f * (unitDirection.y + 1.0f)
            return (1.0f - time) * Vec3(1.0f) + time * Vec3(0.5f, 0.7f, 1.0f)
        }
    }
}


fun randomInUnitSphere(): Vec3 {
    val random = Random()
    var p: Vec3
    do {
        p = 2.0f * Vec3(random.nextFloat(), random.nextFloat(), random.nextFloat()) - Vec3(1f)
    } while (p.dot(p) >= 1.0f)

    return p
}
