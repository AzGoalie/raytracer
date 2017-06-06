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
import javafx.application.Application
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import tornadofx.App
import tornadofx.View
import tornadofx.plusAssign
import tornadofx.vbox
import java.lang.Math.sqrt
import java.util.*

val SCALE = 4
val WIDTH = 200 * SCALE
val HEIGHT = 100 * SCALE
val SAMPLES = 100

fun simpleBackground(width: Int, height: Int): Image {
    val image = WritableImage(width, height)

    val camera = Camera()
    val shapes = listOf<Hitable>(
            Sphere(Vec3(0f, 0f, -1f), 0.5f, Lambertian(Vec3(0.1f, 0.2f, 0.5f))),
            Sphere(Vec3(0f, -100.5f, -1f), 100f, Lambertian(Vec3(0.8f, 0.8f, 0.0f))),
            Sphere(Vec3(1f, 0f, -1f), 0.5f, Metal(Vec3(0.8f, 0.6f, 0.2f), 0.0f)),
            Sphere(Vec3(-1f, 0f, -1f), 0.5f, Dielectric(1.5f)))

    val random = Random()

    for (y in 0 until height) {
        for (x in 0 until width) {
            var color = Vec3()

            for (i in 0 until SAMPLES) {
                val u = (x + random.nextFloat()) / width.toFloat()
                val v = (y + random.nextFloat()) / height.toFloat()

                val ray = camera.getRay(u, v)
                color += color(ray, 0, shapes)
            }

            val pixelColor = Color(sqrt(color.x.toDouble() / SAMPLES),
                    sqrt(color.y.toDouble() / SAMPLES),
                    sqrt(color.z.toDouble() / SAMPLES),
                    1.0)
            image.pixelWriter.setColor(x, height - 1 - y, pixelColor)
        }
    }

    return image
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

fun randomInUnitSphere(): Vec3 {
    val random = Random()
    var p: Vec3
    do {
        p = 2.0f * Vec3(random.nextFloat(), random.nextFloat(), random.nextFloat()) - Vec3(1f)
    } while (p.dot(p) >= 1.0f)

    return p
}

class RaytracerView : View() {
    override val root = vbox()

    init {
        with(root) {
            val image = simpleBackground(WIDTH, HEIGHT)
            this += ImageView(image)
        }
    }
}

class RaytracerApp : App(RaytracerView::class)

fun main(args: Array<String>) {
    Application.launch(RaytracerApp::class.java, *args)
}