package io.github.jangalinski.kata.aoc2021

fun main() {

  fun Triple<Int, List<Long>, Long> .next(): Triple<Int, MutableList<Long>, Long> {
    val day = first + 1
    val breeding = this.second.first()
    val fish = this.second.drop(1).toMutableList()
    fish.add(breeding)
    fish[6] = fish[6] + breeding

    return Triple(day, fish, fish.sum())
  }

  fun List<Int>.init(day:Int=0) = groupingBy { it }.eachCount().let { count ->
    val list = List(9) { 0L }.toMutableList()
    (0..8).forEach {
        index -> list[index] = count[index]?.toLong()?:0L
    }
    Triple(day, list.toList(), list.sum())
  }


  fun part1(init: List<Int>, days:Int = 80)  : Long {
    var state : Triple<Int, List<Long>, Long> = init.init()

    repeat(days) {
      state = state.next()
      println(state)
    }


    return state.third
  }


  """
    Initial state: 3,4,3,1,2
    After  1 day:  2,3,2,0,1
    After  2 days: 1,2,1,6,0,8
    After  3 days: 0,1,0,5,6,7,8
    After  4 days: 6,0,6,4,5,6,7,8,8
    After  5 days: 5,6,5,3,4,5,6,7,7,8
    After  6 days: 4,5,4,2,3,4,5,6,6,7
    After  7 days: 3,4,3,1,2,3,4,5,5,6
    After  8 days: 2,3,2,0,1,2,3,4,4,5
    After  9 days: 1,2,1,6,0,1,2,3,3,4,8
    After 10 days: 0,1,0,5,6,0,1,2,2,3,7,8
    After 11 days: 6,0,6,4,5,6,0,1,1,2,6,7,8,8,8
    After 12 days: 5,6,5,3,4,5,6,0,0,1,5,6,7,7,7,8,8
    After 13 days: 4,5,4,2,3,4,5,6,6,0,4,5,6,6,6,7,7,8,8
    After 14 days: 3,4,3,1,2,3,4,5,5,6,3,4,5,5,5,6,6,7,7,8
    After 15 days: 2,3,2,0,1,2,3,4,4,5,2,3,4,4,4,5,5,6,6,7
    After 16 days: 1,2,1,6,0,1,2,3,3,4,1,2,3,3,3,4,4,5,5,6,8
    After 17 days: 0,1,0,5,6,0,1,2,2,3,0,1,2,2,2,3,3,4,4,5,7,8
    After 18 days: 6,0,6,4,5,6,0,1,1,2,6,0,1,1,1,2,2,3,3,4,6,7,8,8,8,8
  """.trimIndent().lines().mapIndexed { i,it -> i to it.split(": ")[1].split(",").map { it.trim().toInt() } }
    .map {  it.second.init(it.first) }
    .forEach {
      println(it)
    }


  val input = readInputString("Day06").split(",").map { it.trim() }.map { it.toInt() }
  println(input)

  println(part1(input, 256))
}
