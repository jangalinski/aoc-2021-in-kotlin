package io.github.jangalinski.kata.aoc2021

import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.model.Cell

const val test = """
2199943210
3987894921
9856789892
8767896789
9899965678
"""

fun main() {
  fun read(s: String): Krid<Int> = Krids.krid(s, 0) { it.toString().toInt() }

  fun part1(k: Krid<Int>) = k.cellValues()
    .map { cell -> cell.value to k.orthogonalAdjacentCellValues(cell.cell).map { it.value }.minOf { it } }
    .filter { it.first < it.second }
    .map { it.first + 1 }
    .sum()

  fun part2(k: Krid<Int>): Int {
    fun Cell.orthogonalWithoutNine() = k.orthogonalAdjacentCellValues(this).filter { it.value < 9 }.map { it.cell }

    // keep track of all cells that are already part of a basin
    var usedCells = setOf<Cell>()
    // store the sizes of basins
    var basinSizes = listOf<Int>()

    // start with a cell (a neighbor of nine)
    fun recursiveBasin(cell: Cell, basin: Set<Cell> = emptySet()): Set<Cell> {
      // if already used: wrong direction, we would recreate the same basin
      if (usedCells.contains(cell)) {
        return emptySet()
      }

      // continue: current cell is part of basin
      var b = basin + cell
      // all non-nine-neighbors are potentially also part of this basin, but only if they are not already in it
      val neighbors = cell.orthogonalWithoutNine().filterNot { basin.contains(it) }.forEach {
        // so we traverse and add
        b = b + recursiveBasin(it, b)
      }

      // once we found all: return a Set with all cells part of this basin
      return b
    }

    // the starting candidates (walls)
    val adjacentOfNines = k.cellValues().filter { it.value == 9 }.map { it.cell }.flatMap { it.orthogonalWithoutNine() }.toSet()

    adjacentOfNines.forEach {
      val candidate = recursiveBasin(it)

      if (candidate.isNotEmpty()) {
        usedCells += candidate
        basinSizes += candidate.size
      }
    }

    return basinSizes.sortedDescending().take(3).fold(1) { p, c -> p * c }
  }

  val input = readInputString("Day09")
  // Part1
  //println(part1(read(test)))
  //println(part1(read(input)))

  println(part2(read(test)))
  println(part2(read(input)))
}
