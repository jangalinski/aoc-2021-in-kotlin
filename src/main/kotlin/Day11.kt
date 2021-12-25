package io.github.jangalinski.kata.aoc2021

import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid.ascii
import io.toolisticon.lib.krid.model.CellValue
import io.toolisticon.lib.krid.toAddKrid

private val day11 = mapOf(
  "prod" to """
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
""",
  "test" to """
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
)

object Day11 {

  fun krid(input: String): Krid<Int> = krid(input, 0) { it.toString().toInt() }

}

fun main() {
  fun Krid<Int>.step(): Pair<Krid<Int>, Int> {

    // add 1 to each
    var k = this + krid(this.width, this.height, 0) { x, y -> 1 }
      .toAddKrid { o, n -> o + n }

    var tens: List<CellValue<Int>> = k.cellValues().filter { it.value == 10 }.toList()
    while (tens.isNotEmpty()) {
      val c: Map<CellValue<Int>, Int> = tens.flatMap { k.adjacentCellValues(it.cell).distinct() + it }
        .map { it.copy(value = it.value + 1) }.groupingBy { it }.eachCount()

      var adjacents: List<CellValue<Int>> = c.entries.map { (k, v) -> k.copy(value = k.value + v - 1) }
      k = k + adjacents
      tens = k.cellValues().filter { it.value == 10 }.toList()
    }

    // set all flashed to 0
    val flashed = k.cellValues().filter { it.value > 9 }.map { it.copy(value = 0) }.toList()
    k += flashed

    return k to flashed.size
  }

  fun String?.toKrid(): Krid<Int> = krid(this!!, 0) { it.toString().toInt() }

  fun part1(krid: Krid<Int>): Int {
    var k = krid
    var c = 0

    repeat(100) {
      k.step().let {
        k = it.first
        c += it.second
      }
    }

    return c
  }


  //println(day11["test"].toKrid().step().first.step().first.ascii ())

  println(part1(day11["test"].toKrid()))
}
