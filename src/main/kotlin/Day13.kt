package io.github.jangalinski.kata.aoc2021

import io.github.jangalinski.kata.aoc2021.Day13.Fold
import io.github.jangalinski.kata.aoc2021.Day13.X
import io.github.jangalinski.kata.aoc2021.Day13.Y
import io.toolisticon.lib.krid.*
import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid.model.Cell


object Day13 {

  fun read(test: Boolean = false): Pair<Krid<Boolean>, List<Fold>> {
    // fold along y=7
    val regex = """fold along (\w+)=(\d+)""".toRegex()
    val (points, foldStrings) = readInputString("Day13" + if (test) "_test" else "").split("\n\n")

    val cells = points.lines().filterNot { it.isEmpty() }.map {
      val (x, y) = it.trim().split(",")
      Cell(x.trim().toInt(), y.trim().toInt())
    }

    val folds = foldStrings.lines().filterNot { it.isEmpty() }
      .map {
        val (dir, num) = regex.find(it)!!.destructured
        with(num.trim().toInt()) {
          if (dir.trim() == "x") X(this) else Y(this)
        }
      }

    val maxX: Int = folds.filter { it is X }.maxOf { it.index }
    val maxY: Int = folds.filter { it is Y }.maxOf { it.index }

    val krid = Krids.krid(width = maxX * 2 + 1, height = maxY * 2 + 1, emptyElement = false) { x, y ->
      cells.contains(cell(x, y))
    }

    return krid to folds
  }

  sealed class Fold(open val index: Int) {
    abstract fun fold(krid: Krid<Boolean>): Krid<Boolean>
  }

  data class X(override val index: Int) : Fold(index) {

    override fun fold(krid: Krid<Boolean>): Krid<Boolean> {
      require(krid.width == index * 2 + 1)

      val left = krid.subKrid(
        cell(0, 0),
        cell(index - 1, krid.height - 1)
      )
      val right = krid.subKrid(
        cell(index + 1, 0),
        cell(index + 1, krid.height - 1)
      ).flipHorizontal()

      return left + right.toAddKrid { o,n -> o || n}
    }
  }

  data class Y(override val index: Int) : Fold(index) {
    override fun fold(krid: Krid<Boolean>): Krid<Boolean> {
      require(krid.height == index * 2 + 1)

      val up: Krid<Boolean> = krid.subKrid(
        cell(0, 0),
        cell(krid.width - 1, index - 1)
      )

      val bottom: Krid<Boolean> = krid.subKrid(
        cell(0, index + 1),
        cell(krid.width - 1, krid.height - 1)
      ).flipVertical()

      return up + bottom.toAddKrid { o, n -> o || n }
    }
  }


}


fun main() {


  fun part1(krid: Krid<Boolean>, fold: Fold): Int = fold.fold(krid).cellValues().count { it.value }

  val input = Day13.read()

  println(part1(input.first, input.second.first()))
}
