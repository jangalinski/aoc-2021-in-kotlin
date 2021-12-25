package io.github.jangalinski.kata.aoc2021

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import org.junit.jupiter.params.provider.Arguments.of as arg

internal class Day25Test {
  companion object {
    @JvmStatic
    fun `verify next parameters`(): Stream<Arguments> = Stream.of(
      arg(
        "...>>>>>...", "...>>>>.>.."
      )
    )
  }

  @ParameterizedTest
  @MethodSource(value = ["verify next parameters"])
  internal fun `verify next`(input: String, expected: String) {
    val krid = Day25Krid.parse(input.trim())

    assertThat(krid.next().toAscii()).isEqualTo(expected.trim())
  }
}
