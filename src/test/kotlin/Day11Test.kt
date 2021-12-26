package io.github.jangalinski.kata.aoc2021

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import org.junit.jupiter.params.provider.Arguments.of as arg


internal class Day11Test {
  companion object {

    @JvmStatic
    fun `test parameters`() = Stream.of(
      arg(
        """
        5483143223
        2745854711
        5264556173
        6141336146
        6357385478
        4167524645
        2176841721
        6882881134
        4846848554
        5283751526
      """.trimIndent(),
        1,
        """
        6594254334
        3856965822
        6375667284
        7252447257
        7468496589
        5278635756
        3287952832
        7993992245
        5957959665
        6394862637
      """.trimIndent(), 0
      ),
      arg(
        """
        6594254334
3856965822
6375667284
7252447257
7468496589
5278635756
3287952832
7993992245
5957959665
6394862637
      """.trimIndent(),
        1,
        """
        8807476555
5089087054
8597889608
8485769600
8700908800
6600088989
6800005943
0000007456
9000000876
8700006848
      """.trimIndent(), 35
      ),
      arg(
        Day11Input.TEST.string,
        10,
        """
        0481112976
0031112009
0041112504
0081111406
0099111306
0093511233
0442361130
5532252350
0532250600
0032240000
      """.trimIndent(), 204
      ),
      arg(
        Day11Input.TEST.string,
        100,
        """
        0397666866
0749766918
0053976933
0004297822
0004229892
0053222877
0532222966
9322228966
7922286866
6789998766
      """.trimIndent(), 1656
      ),

      )
  }

  @ParameterizedTest
  @MethodSource(value = ["test parameters"])
  internal fun `iterate test`(input: String, steps: Int, expected: String, flashes: Int) {
    val start = Day11(input)

    val stepN = start.steps(steps)

    assertThat(stepN.ascii()).isEqualTo(expected.trim())
    assertThat(stepN.flashes).isEqualTo(flashes)
  }

  @Test
  fun `small 0-1`() {
    val krid = Day11Input.SAMPLE.krid()

    println(krid.ascii())
    println(krid)

    val step1 = krid.next()

    assertThat(step1.ascii()).isEqualTo(
      """
      34543
      40004
      50005
      40004
      34543
    """.trimIndent()
    )

    assertThat(step1.flashes).isEqualTo(9)
    assertThat(step1.steps).isEqualTo(1)
  }
}
