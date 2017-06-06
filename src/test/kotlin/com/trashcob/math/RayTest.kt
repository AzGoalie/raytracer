package com.trashcob.math

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class RayTest {
    @Test
    fun testPointInTime() {
        val position = Vec3(1f)
        val direction = Vec3(-1f)

        val ray = Ray(position, direction)
        val point = Vec3(.5f)

        assertThat(ray.pointInTime(0.5f), equalTo(point))
    }
}
