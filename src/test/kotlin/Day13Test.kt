package io.github.jangalinski.kata.aoc2021

import io.toolisticon.lib.krid.Krids
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class Day13Test {

  @Test
  fun `fold vertical`() {
    val fold = Day13.Y(1)
    val bk = Krids.krid(width = 3, height = 3, emptyElement = false) { x, y -> x == y }

    val f = fold.fold(bk)

    assertThat(f.width).isEqualTo(3)
    assertThat(f.height).isEqualTo(1)

    assertThat(f.toAscii()).isEqualTo("#.#")
  }


  @Test
  fun `fold horizontal`() {
    val fold = Day13.X(1)
    val bk = Krids.krid(width = 3, height = 3, emptyElement = false) { x, y -> x == y }

    val f = fold.fold(bk)

    assertThat(f.width).isEqualTo(1)
    assertThat(f.height).isEqualTo(3)

    assertThat(f.toAscii()).isEqualTo("""
      #
      .
      #
    """.trimIndent())
  }
}
