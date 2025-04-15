package com.coalminesoftware.rickandmorty.data

import com.google.common.truth.Truth.assertThat
import com.google.gson.GsonBuilder
import org.junit.Test
import java.time.Instant

class InstantTypeAdapterTest {
    @Test
    fun deserialize_validIsoString_returnsCorrectInstant() {
        val gson = GsonBuilder()
            .registerTypeAdapter(Instant::class.java, InstantTypeAdapter())
            .create()

        val instantAsQuotedString = "\"2017-11-04T20:03:34.737Z\""

        assertThat(gson.fromJson(instantAsQuotedString, Instant::class.java))
            .isEqualTo(Instant.parse("2017-11-04T20:03:34.737Z"))
    }

    @Test
    fun serialize_instant_returnsIsoFormattedString() {
        val gson = GsonBuilder()
            .registerTypeAdapter(Instant::class.java, InstantTypeAdapter())
            .create()

        assertThat(gson.toJson(Instant.parse("2023-01-01T12:00:00Z")))
            .isEqualTo("\"2023-01-01T12:00:00Z\"")
    }
}
