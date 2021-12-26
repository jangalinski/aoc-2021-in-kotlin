package io.github.jangalinski.kata.aoc2021

import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid.ascii
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.CellValue
import io.toolisticon.lib.krid.toAddKrid

enum class Day11Input(val string: String) {
  TEST(
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
"""
  ),
  PROD(
    """
3172537688
4566483125
6374512653
8321148885
4342747758
1362188582
7582213132
6887875268
7635112787
7242787273
"""
  ),
  SAMPLE(
    """
11111
19991
19191
19991
11111
  """
  ),
  ;

  fun krid(): Day11 = Day11(string)
}


data class Day11(
  val krid: Krid<Int>,
  val flashes: Int = 0,
  val steps: Int = 0,
  val newFlashes: Int = 0
) : Iterator<Day11> {
  constructor(string: String) : this(krid(string, 0) { it.toString().toInt() })

  fun ascii(k: Krid<Int> = krid) = k.ascii {
    if (it > 9) '*'
    else it.toString().first()
  }

  override fun toString() = """

flashes= $flashes
steps  = $steps

${ascii()}
  """.trimIndent()

  override fun hasNext() = true

  override fun next(): Day11 {
    var tmp = krid + krid(krid.width, krid.height, 0) { x, y -> 1 }
      .toAddKrid { o, n -> o + n }
    val flashed = mutableSetOf<Cell>()

    fun findFlashes(k: Krid<Int>) = k.cellValues()
      .filter { it.value > 9 }
      .filterNot { flashed.contains(it.cell) }
      .toList()

    var tens: List<CellValue<Int>> = findFlashes(tmp)

    while (tens.isNotEmpty()) {
      tens.forEach {
        flashed.add(it.cell)
        tmp += tmp.adjacentCellValues(it.cell).map { it.copy(value = it.value + 1) }
      }

      tens = findFlashes(tmp)
    }

    return copy(
      krid = tmp + tmp.cellValues()
        .filter { it.value > 9 }
        .map { it.copy(value = 0) }
        .toList(),
      flashes = flashes + flashed.size,
      steps = steps + 1,
      newFlashes = flashed.size
    )
  }

  val sync = steps > 0 && krid.dimension.size == newFlashes

  fun steps(num: Int): Day11 {
    var tmp = this
    repeat(num) {
      tmp = tmp.next()
    }
    return tmp
  }
}

fun main() {

  fun part1(input: Day11Input): Int = input.krid().steps(100).flashes

  fun part2(input: Day11Input): Int {
    var k = input.krid()

    while (!k.sync) {
      k = k.next()
    }

    return k.steps
  }

  println(part1(Day11Input.TEST))
  println(part2(Day11Input.TEST))
  println(part2(Day11Input.PROD))
//  println(part1(Day11Input.PROD))
}
