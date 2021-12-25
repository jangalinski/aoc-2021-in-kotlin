package io.github.jangalinski.kata.aoc2021

data class Day22Step(
  val x: List<Int>,
  val y: List<Int>,
  val z: List<Int>,
  val on: Boolean,
  val limits: IntRange = IntRange.EMPTY
) {
  companion object {
    fun readLine(string: String, limits: IntRange = IntRange.EMPTY): Day22Step {
      val v = """([onf]+) x=([-0-9]+)..([-0-9]+),y=([-0-9]+)..([-0-9]+),z=([-0-9]+)..([-0-9]+)""".toRegex()
        .find(string)!!.groups.map { it!!.value }.drop(1)
      val b = v.first().let { it == "on" }
      val l = v.asSequence().drop(1).map { it.trim().toInt() }.windowed(size = 2, step = 2)
        .map { it.sorted() }
        .map { IntRange(it[0], it[1]) }.toList()

      return Day22Step(Triple(l[0], l[1], l[2]), b, limits)
    }
  }

  constructor(
    ranges: Triple<IntRange, IntRange, IntRange>,
    on: Boolean,
    limits: IntRange
  ) : this(ranges.first.toList(), ranges.second.toList(), ranges.third.toList(), on, limits)

  val cubes by lazy {
    fun inRange(num: Int) = limits.isEmpty() || num in limits
    val cubes = mutableSetOf<Triple<Int, Int, Int>>()
    x.filter(::inRange).forEach { x ->
      y.filter(::inRange).forEach { y ->
        z.filter(::inRange).forEach { z ->
          cubes.add(Triple(x, y, z))
        }
      }
    }
    cubes.toSet()
  }

}

fun main() {
  fun read(suffix: String, limits: IntRange = IntRange.EMPTY): List<Day22Step> = readInput("Day22$suffix").map { Day22Step.readLine(it, limits) }

  fun cubes(ranges: Triple<List<Int>, List<Int>, List<Int>>, limits: IntRange = IntRange(-50, 50)): Set<Triple<Int, Int, Int>> {
    fun inRange(num: Int) = limits.isEmpty() || num in limits
    val cubes = mutableSetOf<Triple<Int, Int, Int>>()
    ranges.first.filter(::inRange).forEach { x ->
      ranges.second.filter(::inRange).forEach { y ->
        ranges.third.filter(::inRange).forEach { z ->
          cubes.add(Triple(x, y, z))
        }
      }
    }
    return cubes.toSet()
  }

  fun cubes(ranges: Triple<IntRange, IntRange, IntRange>, limits: IntRange = IntRange(-50, 50)): Set<Triple<Int, Int, Int>> =
    cubes(Triple(ranges.first.toList(), ranges.second.toList(), ranges.third.toList()), limits)

  fun part1(input: List<Pair<Triple<IntRange, IntRange, IntRange>, Boolean>>): Int {
    return input.fold(setOf<Triple<Int, Int, Int>>()) { set, line ->
      val cubes = cubes(line.first)
      if (line.second) {
        set + cubes
      } else {
        set - cubes
      }
    }.size
  }

  fun part2(input: List<Pair<Triple<IntRange, IntRange, IntRange>, Boolean>>): Int {


    return input.fold(setOf<Triple<Int, Int, Int>>()) { set, line ->
      val cubes = cubes(line.first, IntRange(Int.MIN_VALUE, Int.MAX_VALUE))
      if (line.second) {
        set + cubes
      } else {
        set - cubes
      }
    }.size
  }

  fun merge(a: Triple<IntRange, IntRange, IntRange>, b: Triple<IntRange, IntRange, IntRange>): Triple<List<Int>, List<Int>, List<Int>> {
    return Triple(a.first + b.first, a.second + b.second, a.third + b.third)
  }

  //println(part1(read("_test")))
}
