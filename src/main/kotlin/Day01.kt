package io.github.jangalinski.kata

fun main() {

  // fold on pair of last value and increaseCount, then take just the count
  fun part1(input: List<Int>): Int = input.fold(Int.MAX_VALUE to 0) { (last, count), next ->
    if (next > last)
      next to count + 1
    else
      next to count
  }.second

  fun part2(input: List<Int>): Int {
    // zip list twice with head moved by 1 and 2.
    // zip only takes entries where all three lists have a value.
    val tripleFold: List<Int> = input
      .zip(input.drop(1))
      .map { it.sum() }
      .zip(input.drop(2))
      .map { it.sum() }

    return part1(tripleFold)
  }

  fun solve(name: String) {
    println("$name:")
    val input: List<Int> = readInputAsInt(name)
    println("part1: ${part1(input)}")
    println("part2: ${part2(input)}")
  }

  solve("Day01_test")
  solve("Day01")
}
