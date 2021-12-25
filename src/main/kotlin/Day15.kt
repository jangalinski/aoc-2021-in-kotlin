package io.github.jangalinski.kata.aoc2021

import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.Krids.ORIGIN
import io.toolisticon.lib.krid.get
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.CellValue

object Day15 {

  fun Krid<Int>.minPath(start:Cell, cache:Map<Cell, Int> = emptyMap()) : Int {
    val br = Cell(this.width -1, this.height -1)
    if (start == br) {
      return this.get(start)
    }
    TODO()
  }


}

fun main() {
  fun read(test: Boolean = false): Krid<Int> = readInputString("Day15" + if (test) "_test" else "").intKrid()

  fun part1(krid: Krid<Int>): Int {

    val cache = mutableMapOf<Cell, Int>()

    return 0
  }


  val input = read(true)

  println(read(true))
}
