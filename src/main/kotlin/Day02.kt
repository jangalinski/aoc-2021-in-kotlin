package io.github.jangalinski.kata.aoc2021

import io.toolisticon.lib.krid.Krids.ORIGIN
import io.toolisticon.lib.krid.model.step.CompositeStep
import io.toolisticon.lib.krid.model.step.CoordinatesStep
import io.toolisticon.lib.krid.model.step.Direction
import io.toolisticon.lib.krid.model.step.DirectionStep

/**
 * https://adventofcode.com/2021/day/2
 */
fun main() {
  // reading the input as [DirectionStep] from krid-lib
  fun readInputAsSteps(name: String): List<DirectionStep> = readInput(name).map { it.trim() }
    .map { it.replace("forward", "right") }
    .map {
      val (d, v) = it.split(" ").map { it.trim() }
      Direction.valueOf(d.uppercase())(v.toInt())
    }


  // apply a composite of all steps to (0,0) cell and multiply x,y coordinates
  fun part1(steps: List<DirectionStep>): Int = CompositeStep(steps)(ORIGIN).multiply()

  // fold on Pair(Cell, Aim)
  fun part2(steps: List<DirectionStep>): Int = steps.fold(ORIGIN to CoordinatesStep(ORIGIN)) { (cell, aim), step ->
    when (step.direction) {
      // when forward: compositeStep of current + n-times aim
      Direction.RIGHT -> (step + aim * step.number)(cell) to aim
      // else: increase/decrease aim
      else -> cell to aim + step
    }
  }.first.multiply()

  val input = readInputAsSteps("Day02")

  println(part1(input))
  println(part2(input))
}
