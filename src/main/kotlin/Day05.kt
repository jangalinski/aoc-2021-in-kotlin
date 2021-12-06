package io.github.jangalinski.kata.aoc2021

import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids.cell
import io.toolisticon.lib.krid.Krids.krid
import io.toolisticon.lib.krid.ascii
import io.toolisticon.lib.krid.model.AddKrid
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.toAddKrid
import kotlin.math.max

fun main() {
  val regex = """(\d+),(\d+) -> (\d+),(\d+)""".toRegex()
  fun String.parseLine(): Pair<Cell, Cell> {
    val (p1, p2, p3, p4) = regex.find(this)!!.destructured
    return cell(p1.toInt(), p2.toInt()) to cell(p3.toInt(), p4.toInt())
  }

  data class Venture(var krid: Krid<Int>) {

    operator fun plus(param: Pair<Cell, Krid<Int>>) {
      plus(param.second.toAddKrid(param.first) { o, n -> o + n })
    }

    operator fun plus(a: AddKrid<Int>) {
      krid = krid.plus(a)
    }

    fun count() = krid.iterator().asSequence().map { it.value }.count { it > 1 }

    fun ascii() = krid.ascii { if (it == 0) '.' else if (it > 1) '*' else '1' }
  }

  fun List<Pair<Cell, Cell>>.emptyKrid(): Venture = map { max(it.first.x, it.second.x) to max(it.first.y, it.second.y) }
    .fold(0 to 0) { (x, y), cur -> max(x, cur.first) to max(y, cur.second) }
    .let {
      Venture(krid(it.first + 1, it.second + 1, 0))
    }

  fun part2(moves: List<Pair<Cell, Cell>>): Int {
    val k = moves.emptyKrid()
    val p1 = mutableListOf<Pair<Cell, Cell>>()
    val p2 = mutableListOf<Pair<Cell, Cell>>()
    val p3 = mutableListOf<Pair<Cell, Cell>>()

    moves.forEach {
      if (it.first.x == it.second.x && it.first.y != it.second.y) {
        p1.add(it)
      } else if (it.first.y == it.second.y && it.first.x != it.second.x) {
        p2.add(it)
      } else {
        p3.add(it)
      }
    }

    fun toAddKrid(p: Pair<Cell, Cell>, init: (Int, Int) -> Int): AddKrid<Int> {
      val ps = if (p.first < p.second) {
        p
      } else {
        p.second to p.first
      }

println(ps)
      val k = krid(
        width = 1 + ps.second.x - ps.first.x,
        height = 1 + ps.second.y - ps.first.y,
        emptyElement = 0,
        initialize = init
      )

      return k.toAddKrid(offset = ps.first) { o, n -> o + n }
    }
    println("p3: $p3")

    val a1 = p1.map { toAddKrid(it) { _, _ -> 1 } }
    val a2 = p2.map { toAddKrid(it) { _, _ -> 1 } }
    val a3 = p3.map { toAddKrid(it) { x,y-> if (x==y) 1 else 0 } }


    println("p1: $a1")
    println("p2: $a2")
    println("p3: $p3")

//    val cols: List<Pair<Cell, Krid<Int>>> = moves.filter { it.first.x == it.second.x }.map { if (it.first.y < it.second.y) it else it.second to it.first }
//      .map {
//        it.first to krid(
//          width = 1,
//          height = it.second.y - it.first.y + 1,
//          emptyElement = 0
//        ) { _, _ -> 1 }
//      }
//
//    val rows = moves.filter { it.first.y == it.second.y }.map { if (it.first.x < it.second.x) it else it.second to it.first }
//      .map { it.first to krid(
//        width = it.second.x - it.first.x + 1,
//        height = 1,
//        emptyElement = 0) { _, _ -> 1 }
//      }
//
//    val diagonals = moves.filter { it.first.y != it.second.y && it.first.x != it.second.x }.map { if (it.first > it.second) it else it.second to it.first }
//      .map {
//        it.first to krid(
//          width = it.second.x - it.first.x + 1,
//          height = it.second.y - it.first.y + 1,
//          emptyElement = 0
//        ) { x, y -> if (x == y) 1 else 0 }
//      }

    (a1 + a2).forEach { k.plus(it) }
    println(k.ascii())

    return k.count()
  }

  fun part1(moves: List<Pair<Cell, Cell>>): Int {
    val k = moves.emptyKrid()

    val cols = moves.filter { it.first.x == it.second.x }.map { if (it.first.y < it.second.y) it else it.second to it.first }
      .map { it.first to krid(width = 1, height = it.second.y - it.first.y + 1, emptyElement = 0) { _, _ -> 1 } }

    val rows = moves.filter { it.first.y == it.second.y }.map { if (it.first.x < it.second.x) it else it.second to it.first }
      .map { it.first to krid(width = it.second.x - it.first.x + 1, height = 1, emptyElement = 0) { _, _ -> 1 } }

    (cols + rows).forEach { k.plus(it) }

    println(k.ascii())

    return k.count()
  }

  val moves: List<Pair<Cell, Cell>> = readInput("Day05_test").map { it.parseLine() }



  println(part2(moves))
  //println("dimension: \n${moves.emptyKrid().ascii()}")


}
