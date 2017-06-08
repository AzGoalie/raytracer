package com.trashcob

import javafx.application.Application
import tornadofx.App

class RaytracerApp : App(RaytracedImage::class)

fun main(args: Array<String>) = Application.launch(RaytracerApp::class.java, *args)
