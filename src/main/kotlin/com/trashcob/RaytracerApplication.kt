package com.trashcob

import javafx.application.Application
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.layout.VBox
import tornadofx.App
import tornadofx.View
import tornadofx.plusAssign

val width = 1200
val height = 800
val samples = 100

class RaytracedImage : View() {
    override val root = VBox()

    init {
        val image = WritableImage(width, height)
        root += ImageView(image)

        val raytracer = Raytracer()
        runAsync {
            raytracer.createImage(samples, image)
        }
    }

}

class RaytracerApp : App(RaytracedImage::class)

fun main(args: Array<String>) = Application.launch(RaytracerApp::class.java, *args)
