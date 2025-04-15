package com.coalminesoftware.rickandmorty.data

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.Instant

/** GSON adapter for serializing and deserializing [Instant]s. */
class InstantTypeAdapter : JsonDeserializer<Instant>, JsonSerializer<Instant> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Instant
        = Instant.parse(json.asString)
    override fun serialize(src: Instant, typeOfSrc: Type, context: JsonSerializationContext): JsonElement
        = JsonPrimitive(src.toString())
}
