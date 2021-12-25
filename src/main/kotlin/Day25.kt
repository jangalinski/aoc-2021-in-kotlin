package io.github.jangalinski.kata.aoc2021

import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid.ascii
import io.toolisticon.lib.krid.get
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.CellValue
import io.toolisticon.lib.krid.model.step.Direction

/**
 * During a single step, the east-facing herd moves first, then the south-facing herd moves.
 */
data class Day25Krid(val krid: Krid<Day25>, val steps: Int = 0, private val moved: Boolean = true) : Iterator<Day25Krid> {
  companion object {
    fun parse(string: String): Day25Krid = Day25Krid(Krids.krid(string = string, emptyElement = Day25.Empty) { Day25.fromChar(it) })

    fun parse(test: Boolean = false): Day25Krid = parse(readInputString("Day25" + if (test) "_test" else "").trim())

    fun Krid<Day25>.toAscii() = this.ascii { it.toChar() }
  }

  fun isEmpty(cell: Cell) = krid[cell] == Day25.Empty

  fun toAscii() = krid.toAscii()

  override fun hasNext(): Boolean = moved

  fun find(cucumber: Day25.Cucumber): Sequence<CellValue<Day25.Cucumber>> = krid.cellValues()
    .filter { it.value == cucumber }
    .map { cell(it.cell, it.value as Day25.Cucumber) }

  override fun next(): Day25Krid {
    fun cucumbers(type: Day25.Cucumber, krid: Krid<Day25>): List<CellValue<Day25>> = find(type)
      .map { it to it.value.move(krid, it.cell) }
      .filter { it.second != null }
      .map { it.first to it.second!! }
      .flatMap {
        val list = mutableListOf<CellValue<Day25>>()
        list.add(CellValue(it.first.cell, Day25.Empty))
        list.add(CellValue(it.second, type))
        list
      }.toList()

    var newKrid = krid

    val easts = cucumbers(Day25.Cucumber.EAST, newKrid)
    newKrid += easts


    val souths = cucumbers(Day25.Cucumber.SOUTH, newKrid)
    newKrid += souths

    val moved = easts.isNotEmpty() || souths.isNotEmpty()

    return copy(krid = newKrid, moved = moved, steps = steps + 1)
  }
}


sealed interface Day25 {
  companion object {
    fun fromChar(char: Char) = when (char) {
      '>' -> Cucumber.EAST
      'v' -> Cucumber.SOUTH
      else -> Empty
    }

    fun part1(string: String): Int {
      var krid = Day25Krid.parse(string)

      while (krid.hasNext()) {
        krid = krid.next()
      }

      return krid.steps
    }
  }

  fun toChar(): Char

  enum class Cucumber : Day25 {
    EAST {
      override fun move(krid: Krid<Day25>, cell: Cell): Cell? {
        val next = Direction.RIGHT(cell).let {
          if (krid.dimension.isInBounds(it)) {
            it
          } else {
            Cell(0, it.y)
          }
        }
        return if (krid.get(next) == Empty) next else null
      }

      override fun toChar(): Char = '>'
    },
    SOUTH {
      override fun move(krid: Krid<Day25>, cell: Cell): Cell? {
        val next = Direction.DOWN(cell).let {
          if (krid.dimension.isInBounds(it)) {
            it
          } else {
            Cell(it.x, 0)
          }
        }
        return if (krid.get(next) == Empty) next else null
      }

      override fun toChar(): Char = 'v'
    };

    abstract fun move(krid: Krid<Day25>, cell: Cell): Cell?
  }

  object Empty : Day25 {
    override fun toChar() = '.'

    override fun toString() = "EMPTY"
  }
}


fun main() {
  val input = readInputString("Day25")

  println(Day25.part1(input))
}
