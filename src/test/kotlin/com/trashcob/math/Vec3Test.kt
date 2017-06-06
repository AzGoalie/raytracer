package com.trashcob.math

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.lang.Math.sqrt

class Vec3Test {
    @Test
    fun additionTest() {
        val a = Vec3()
        val b = Vec3(1f, 2f, 3f)

        assertThat(a + b, equalTo(b))
    }

    @Test
    fun subtractionTest() {
        val a = Vec3()
        val b = Vec3(1f, 2f, 3f)
        val c = Vec3(-1f, -2f, -3f)

        assertThat(a - b, equalTo(c))
    }

    @Test
    fun multiplicationTest() {
        val a = Vec3(1f, 2f, 3f)
        val b = Vec3(2f)
        val c = Vec3(2f, 4f, 6f)

        assertThat(a * b, equalTo(c))
    }

    @Test
    fun scalarMultiplicationTest() {
        val a = Vec3(1f)
        val b = -2f
        val c = Vec3(-2f)

        assertThat(a * b, equalTo(c))
    }

    @Test
    fun divisionTest() {
        val a = Vec3(2f, 4f, 6f)
        val b = Vec3(2f)
        val c = Vec3(1f, 2f, 3f)

        assertThat(a / b, equalTo(c))
    }

    @Test
    fun scalarDivision() {
        val a = Vec3(2f, 4f, 6f)
        val b = 2f
        val c = Vec3(1f, 2f, 3f)

        assertThat(a / b, equalTo(c))
    }

    @Test
    fun negativeTest() {
        val a = Vec3(1f)
        val b = Vec3(-1f)

        assertThat(-a, equalTo(b))
    }

    @Test
    fun lengthTest() {
        val a = Vec3(1f, 2f, 3f)
        val length = sqrt(((a.x * a.x) + (a.y * a.y) + (a.z * a.z)).toDouble()).toFloat()

        assertThat(a.length(), equalTo(length))
    }

    @Test
    fun squaredLengthTest() {
        val a = Vec3(1f, 2f, 3f)
        val squaredLength = (a.x * a.x) + (a.y * a.y) + (a.z * a.z)

        assertThat(a.squaredLength(), equalTo(squaredLength))
    }

    @Test
    fun unitTest() {
        val a = Vec3(1f)
        val length = a.length()

        assertThat(a.unit(), equalTo(a / length))
    }

    @Test
    fun dotProductTest() {
        val a = Vec3(2f)
        val b = Vec3(3f)
        val c = 18f

        assertThat(a.dot(b), equalTo(c))
    }

    @Test
    fun crossProductTest() {
        val a = Vec3(2f, 3f, 4f)
        val b = Vec3(5f, 6f, 7f)
        val c = Vec3(-3f, 6f, -3f)

        assertThat(a.cross(b), equalTo(c))
    }
}