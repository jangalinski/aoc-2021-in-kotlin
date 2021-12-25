package io.github.jangalinski.kata.aoc2021

import io.github.jangalinski.kata.aoc2021.Result.HIT
import io.github.jangalinski.kata.aoc2021.Result.OVER
import io.toolisticon.lib.krid.model.Cell
import io.toolisticon.lib.krid.model.toCell
import kotlin.Boolean
import kotlin.Int
import kotlin.Pair
import kotlin.Triple
import kotlin.let
import kotlin.to

private enum class Result { NEXT, HIT, OVER }

fun main() {
  data class TargetZone(val x1: Int, val x2: Int, val y1: Int, val y2: Int) {
    fun checkX(x: Int) = if (x > x2)
      OVER
    else if (x < x1)
      Result.NEXT
    else
      HIT

    fun checkY(y: Int) = if (y < y1)
      OVER
    else if (y > y2)
      Result.NEXT
    else
      HIT
  }

  data class Probe(val position: Cell, val velocity: Pair<Int, Int>) {

    fun next() = copy(
      position = position + velocity.toCell(),
      velocity = (if (velocity.first == 0) 0 else velocity.first - 1) to velocity.second - 1
    )

    fun checkNext(target: TargetZone): Triple<Probe, Result, Result> {
      val next = next()
      return Triple(next, target.checkX(next.position.x), target.checkY(next.position.y))
    }
  }

  fun shootSequence(velocity: Pair<Int, Int>, target: TargetZone): Sequence<Triple<Probe, Result, Result>> = generateSequence(
    Triple(Probe(Cell(0, 0), velocity), Result.NEXT, Result.NEXT)
  ) {
    it.first.checkNext(target)
  }

  fun shoot(velocity: Pair<Int, Int>, target: TargetZone): Boolean = shootSequence(velocity, target)
    .takeWhile { (_, x, y) ->
      x != OVER && y != OVER
    }.last().let { (_, x, y) -> x == HIT && y == HIT }

  data class Y(val pos: Int, val velocity: Int) {
    fun next() = copy(pos = pos + velocity, velocity = velocity - 1)


  }


  fun findMaxY(target: IntRange, velocity: Int): Int? {
    var y = Y(0, velocity)
    var max = 0
    var hit = false
    var overshoot = false

    while (!hit && !overshoot) {
      if (y.pos > max)
        max = y.pos

      hit = target.contains(y.pos)
      overshoot = y.pos < target.first
      y = y.next()
    }

    return if (overshoot) null else max
  }

  fun yValues(velocity: Int) = sequence<Pair<Y, Int>> {
    var y = Y(0, velocity)

  }

  fun part1(range: IntRange): Int = generateSequence(1) { it + 1 }
    .map { findMaxY(target = range, velocity = it) }
    .take(1000)
    .filterNotNull()
    .maxOf { it }

  fun part2(target: TargetZone): Int {
    val result = mutableSetOf<Pair<Int, Int>>()

    for (y in target.y1 - 2 until 100) {
      for (x in 1 until target.x2 + 2) {
        val velocity = x to y
        if (shoot(velocity, target)) {
          result.add(velocity)
        }
      }
    }

    println(result)
    return result.size
  }


  val r1 = TargetZone(20, 30, -10, -5)
  val r2 = TargetZone(201, 230, -99, -65)

  println(r1)
  println(r2)

  //println(shoot(10 to -10, r1))

  println(part2(r2))

  val all = """
    23,-10  25,-9   27,-5   29,-6   22,-6   21,-7   9,0     27,-7   24,-5
    25,-7   26,-6   25,-5   6,8     11,-2   20,-5   29,-10  6,3     28,-7
    8,0     30,-6   29,-8   20,-10  6,7     6,4     6,1     14,-4   21,-6
    26,-10  7,-1    7,7     8,-1    21,-9   6,2     20,-7   30,-10  14,-3
    20,-8   13,-2   7,3     28,-8   29,-9   15,-3   22,-5   26,-8   25,-8
    25,-6   15,-4   9,-2    15,-2   12,-2   28,-9   12,-3   24,-6   23,-7
    25,-10  7,8     11,-3   26,-7   7,1     23,-9   6,0     22,-10  27,-6
    8,1     22,-8   13,-4   7,6     28,-6   11,-4   12,-4   26,-9   7,4
    24,-10  23,-8   30,-8   7,0     9,-1    10,-1   26,-5   22,-9   6,5
    7,5     23,-6   28,-10  10,-2   11,-1   20,-9   14,-2   29,-7   13,-3
    23,-5   24,-8   27,-9   30,-7   28,-5   21,-10  7,9     6,6     21,-5
    27,-10  7,2     30,-9   21,-8   22,-7   24,-9   20,-6   6,9     29,-5
    8,-2    27,-8   30,-5   24,-7
  """.trimIndent().lines().flatMap { it.split(" ") }.map { it.trim() }.filterNot { it.isBlank() }
    .map {
      val (x, y) = it.split(",")
      x.toInt() to y.toInt()
    }


//  println(all.map { it to shoot(it, r1) }.filter { !it.second })
//
//  shootSequence(6 to 8, r1).take(20).forEach { println(it) }
}
