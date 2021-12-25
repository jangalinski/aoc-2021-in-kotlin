package io.github.jangalinski.kata.aoc2021.strings

import io.github.jangalinski.kata.aoc2021.Day16

fun String.toHexString() = HexString(this)

@JvmInline
value class HexString(val value: String) {

  companion object {
    val MAP = mapOf(
      '0' to "0000",
      '1' to "0001",
      '2' to "0010",
      '3' to "0011",
      '4' to "0100",
      '5' to "0101",
      '6' to "0110",
      '7' to "0111",
      '8' to "1000",
      '9' to "1001",
      'A' to "1010",
      'B' to "1011",
      'C' to "1100",
      'D' to "1101",
      'E' to "1110",
      'F' to "1111",
    )
  }

  init {
    val allowed = setOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
    val diff = (value.toSet() - allowed).sorted()
    require(diff.isEmpty()) { "no valid hexString, contains: $diff" }
  }

  // Transforms a HEX String to Bit-String
  fun toBinaryString(): Day16.BinaryString = Day16.BinaryString(value.toList().map { MAP[it]!! }.joinToString(""))

}
