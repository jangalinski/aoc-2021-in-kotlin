package io.github.jangalinski.kata.aoc2021

import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.ascii

/**
 * https://adventofcode.com/2021/day/3#part2
 */
fun main() {
  val input: Krid<Char> = Krids.krid(readInputString("Day03"))

  fun count(krid: Krid<Char>): List<Pair<Char, Char>> = krid.columns().map { col ->
    col.values.groupingBy { it }.eachCount()
  }.map {
    // todo: why does maxOf/maxBy not work?
    if (it['1'] ?: 0 >= it['0'] ?: 0) {
      '1' to '0'
    } else {
      '0' to '1'
    }
  }

  fun filterKrid(krid: Krid<Char>, pos: Int, value: Char): Krid<Char> = Krids.krid(krid.rows().filter { it[pos] == value }, krid.emptyElement)

  fun filterKrid(krid: Krid<Char>, pos: Int, high: Boolean): Krid<Char> {
    val criteria = count(krid).map { if (high) it.first else it.second }[pos]

    return filterKrid(krid, pos, criteria)
  }


  fun part1(krid: Krid<Char>): Int {

    // counting 0 and 1 for each col
    val count: List<Pair<Char, Char>> = count(krid)


    val (a, b) = count.fold("" to "") { string, (min, max) ->
      string.first + max to string.second + min
    }
      .let {
        it.first.toInt(2) to it.second.toInt(2)
      }

    return a * b
  }

  fun part2(krid: Krid<Char>): Int {
    fun reduce(k: Krid<Char> = krid, pos: Int = 0, high: Boolean): Krid<Char> {
      return if (k.height == 1) {
        k
      } else {
        reduce(filterKrid(k, pos, high), pos + 1, high)
      }
    }

    val ox = reduce(krid,0,true).ascii().toInt(2)
    val co2 = reduce(krid,0,false).ascii().toInt(2)

    return ox * co2
  }

  println(part1(input))
  println(part2(input))
}
