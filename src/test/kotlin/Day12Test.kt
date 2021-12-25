package io.github.jangalinski.kata.aoc2021

import org.junit.jupiter.api.Test


internal class Day12Test {

  @Test
  internal fun `read simple`() {
    val input = Day12.read("""
      start-A
      start-b
      A-c
      A-b
      b-d
      A-end
      b-end
    """.trimIndent())


  }


}
