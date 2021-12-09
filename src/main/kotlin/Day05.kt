package io.github.jangalinski.kata.aoc2021

import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid.ascii
import io.toolisticon.lib.krid.model.AddKrid
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.CellValue
import io.toolisticon.lib.krid.model.step.CompositeStep
import io.toolisticon.lib.krid.model.step.CoordinatesStep
import io.toolisticon.lib.krid.model.step.DirectionStep
import io.toolisticon.lib.krid.toAddKrid
import kotlin.math.max

fun main() {
  fun List<Pair<Cell, Cell>>.emptyKrid(): Krid<Int> = map { max(it.first.x, it.second.x) to max(it.first.y, it.second.y) }
    .fold(0 to 0) { (x, y), cur -> max(x, cur.first) to max(y, cur.second) }
    .let {
      krid(it.first + 1, it.second + 1, 0)
    }

  fun Pair<Cell, Cell>.vector(): Pair<Cell, DirectionStep> {
    val move = cell(second.x - first.x, second.y - first.y)

    val step = CompositeStep(move, true).single() as DirectionStep

    return first to step
  }

  fun read(test: Boolean = false): List<Pair<Cell, Cell>> {
    val regex = """(\d+),(\d+) -> (\d+),(\d+)""".toRegex()
    fun String.parseLine(): Pair<Cell, Cell> {
      val (p1, p2, p3, p4) = regex.find(this)!!.destructured
      return cell(p1.trim().toInt(), p2.trim().toInt()) to cell(p3.trim().toInt(), p4.trim().toInt())
    }
    return readInput("Day05" + if (test) "_test" else "").map { it.parseLine() }
  }

  data class Venture(var krid: Krid<Int>) {

    operator fun plus(param: Pair<Cell, Krid<Int>>) {
      plus(param.second.toAddKrid(param.first) { o, n -> o + n })
    }

    operator fun plus(a: AddKrid<Int>) {
      krid = krid.plus(a)
    }

    fun count() = krid.cellValues().map { it.value }.count { it > 1 }

    fun ascii() = krid.ascii {
      when {
        it == 0 -> '.'
        it > 1 -> '*'
        else -> '1'
      }
    }
  }


  fun part1(moves: List<Pair<Cell, Cell>>): Int {
    val k = Venture(moves.emptyKrid())

    val cols = moves.filter { it.first.x == it.second.x }.map { if (it.first.y < it.second.y) it else it.second to it.first }
      .map { it.first to krid(width = 1, height = it.second.y - it.first.y + 1, emptyElement = 0) { _, _ -> 1 } }

    val rows = moves.filter { it.first.y == it.second.y }.map { if (it.first.x < it.second.x) it else it.second to it.first }
      .map { it.first to krid(width = it.second.x - it.first.x + 1, height = 1, emptyElement = 0) { _, _ -> 1 } }

    (cols + rows).forEach { k.plus(it) }

    return k.count()
  }

  fun part2(moves: List<Pair<Cell, Cell>>): Int {
    val vectors = moves.map { it.vector() }
      // build beamss of cellValue(1)
      .map { v ->
        v.second.direction.beam(v.first, true)
          .take(v.second.number + 1)
          .map { CellValue(it, 1) }
          .toList()
      }
      // build a krid that just contains the beams
      .map {
        moves.emptyKrid() + it
      }
      // reduce all krids by adding up cellValues
      .fold(moves.emptyKrid()) { r, c ->
        r + c.toAddKrid { o, n -> o + n }
      }

    // count all values > 1
    return vectors.cellValues().count { it.value > 1 }
  }


  fun part22(moves: List<Pair<Cell, Cell>>): Int  = moves.map { it.vector() }
      .flatMap {
        it.second.direction.beam(it.first, true)
          .take(it.second.number + 1)
          .toList()
      }.groupingBy { it }.eachCount().toList()
      .count { it.second > 1 }


  println(part22(read(true)))
  println(part22(read(false))) // wrong
}

