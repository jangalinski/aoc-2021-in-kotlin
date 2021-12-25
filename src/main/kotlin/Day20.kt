package io.github.jangalinski.kata.aoc2021

import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid.get
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.CellValue
import io.toolisticon.lib.krid.model.adjacent
import io.toolisticon.lib.krid.model.step.Direction.*
import io.toolisticon.lib.krid.toAddKrid

data class Day20(
  val algorithm: List<Boolean>,
  val image: Krid<Boolean>,
  val outerValue: Boolean
) {
  companion object {

    fun read(test: Boolean=false): Day20 {
      val (a, k) = readInputString("Day20" + if (test) "_test" else "")
        .split("\n\n")
        .map { it.trim() }
        .filterNot { it.isBlank() }

      val algorithm = a.toList().map { it == '#' }
      val krid = Krids.krid(k, false) { it == '#' }


      return Day20(algorithm = algorithm, image = krid.wrap(false), outerValue = false)
    }

    fun Krid<Boolean>.wrap(outerValue: Boolean): Krid<Boolean> {
      val outer = Krids.krid(width = this.width + 4, height = this.height + 4, emptyElement = false) { _, _ ->
        outerValue
      }

      return outer + this.toAddKrid(cell(2, 2))
    }
  }

  fun lit() = image.cellValues().count{ it.value}

  fun adjacentToInt(cell: Cell): Int {
    val nineCells = cell.adjacent(UP_LEFT, UP, UP_RIGHT, LEFT) + cell + cell.adjacent(RIGHT, DOWN_LEFT, DOWN, DOWN_RIGHT)
    val cellValues = nineCells.map { if (image.dimension.isInBounds(it)) image[it] else outerValue }

    return nineCells.map { if (image.dimension.isInBounds(it)) image[it] else outerValue }.joinToString(separator = "") { if (it) "1" else "0" }.toInt(2)
  }

  fun next(): Day20 {
    val nextValues: List<CellValue<Boolean>> = image.cells().map {
      it to adjacentToInt(it)
    }.map { CellValue(it.first.x, it.first.y, algorithm[it.second]) }.toList()

    val nk = image + nextValues
    val nout =  nk[cell(0,0)]


    return copy(
      image = nk.wrap(nout),
      outerValue = nout
    )
  }

  override fun toString(): String = """
      algorithm: ${algorithm.size}

      image:
${image.toAscii()}
      outer: $outerValue
    """.trimIndent()
}

fun main() {
  var input = Day20.read()

  repeat(50) {
    input = input.next()
  }

  println(input.lit())
}
