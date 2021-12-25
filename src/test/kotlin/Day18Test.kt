package io.github.jangalinski.kata.aoc2021

import io.github.jangalinski.kata.aoc2021.Snailfish.Companion.to
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.Arguments.of as arg
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class Day18Test {
  companion object {

    @JvmStatic
    fun `parse snailfish parameters`(): Stream<Arguments> = Stream.of(
      arg("[1,2]", (1 to 2)),
      arg("[[1,2],3]", (1 to 2) to 3),
      arg("[9,[8,7]]", 9 to  (8 to 7)),
      arg("[[1,9],[8,5]]", (1 to 9) to (8  to 5)),
      arg("[[[[1,2],[3,4]],[[5,6],[7,8]]],9]",
        (((1 to 2) to (3 to 4)) to ((5 to 6) to (7 to 8))) to 9
      ),
      arg("[" +
        "[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]" +
        "]",
        1 to 3
      )
    )
  }

  @Test
  fun `to string`() {
    val r1 = RegularNumber(1) to 2
    assertThat(r1.toString()).isEqualTo("[1,2]")

    assertThat((r1 to 3).toString()).isEqualTo("[[1,2],3]")

  }

  @ParameterizedTest
  @MethodSource(value = ["parse snailfish parameters"])
  fun `parse snailfish`(string: String, expected: Snailfish) {
    assertThat(expected.toString())
      .describedAs("toString matches")
      .isEqualTo(string)

    assertThat(Snailfish.parse(string)).isEqualTo(expected)
  }

  @ParameterizedTest
  @CsvSource(value = [
    "[1,2];1;2",
    "[[1,2],3];[1,2];3",
    "[[[[1,2],[3,4]],[[5,6],[7,8]]],9];[[[1,2],[3,4]],[[5,6],[7,8]]];9",
    "[9,[8,7]];9;[8,7]",
  ], delimiter = ';')
  fun `split string in parts`(input:String, first: String, second:String) {
    assertThat(Snailfish.split(input)).isEqualTo(first to second)
  }


}
