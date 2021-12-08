package io.github.jangalinski.kata.aoc2021.week1

sealed class JsonElement
data class JsonObject(val fields: Map<String, JsonElement>) : JsonElement() {
  constructor(vararg fields: Pair<String, JsonElement>) : this(fields.toMap())
}

data class JsonArray(val elements: List<JsonElement>) : JsonElement() {
  constructor(vararg elements: JsonElement) : this(elements.toList())
}

data class JsonNumber(val value: Double) : JsonElement()
data class JsonString(val value: String) : JsonElement()
data class JsonBoolean(val value: Boolean) : JsonElement()
object JsonNull : JsonElement()

fun JsonElement.stringify(): String = when (this) {
  is JsonObject -> this.fields.map {
    """"${it.key}":${it.value.stringify()}"""
  }.joinToString(prefix = "{", postfix = "}", separator = ",")
  is JsonNull -> "null"
  is JsonNumber ->  {
    val string = this.value.toString()
    if (string.endsWith(".0"))
      string.replace(".0", "")
    else
      string
  }
  is JsonBoolean -> this.value.toString()
  is JsonString -> """"${this.value}""""
  is JsonArray -> this.elements.map { it.stringify() }.joinToString(prefix = "[", postfix = "]", separator = ",")
}


