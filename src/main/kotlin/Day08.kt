package io.github.jangalinski.kata.aoc2021

const val sample = "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf"

private fun String.parseSegments() = trim().split(" ").map { it.trim().toList().sorted().joinToString(separator = "") }
private fun String.parseLine(): Pair<List<String>, List<String>> {
  val (digits, result) = split("|")
  return digits.parseSegments().sortedBy { it.length } to result.parseSegments()
}

private operator fun String.minus(s: String) = this.toList() - s.toList().toSet()
private fun String.hasLength(l: Int) = l == length
private fun String.containsAll(s: String) = this.toSet().containsAll(s.toSet())

private fun analyse(line: Pair<List<String>, List<String>>): Int {
  data class Solution(
    val num2string: MutableList<String?> = List<String?>(10) { null }.toMutableList(),
    val string2num: MutableMap<String, Int> = mutableMapOf(),
    var unsolved: MutableList<String> = line.first.toMutableList(),
    val digits: List<String> = line.second
  ) {
    fun solve(p: Pair<String, Int>) {
      // store by index
      num2string[p.second] = p.first
      // store by key
      string2num[p.first] = p.second
      // remove solved from list
      unsolved.remove(p.first)
    }

    fun number() = digits.map { string2num[it]!! }.joinToString(separator = "").toInt()
  }

  val solution = Solution().apply {
    // captain obvious, input is sorted by length
    // 1 is the only 2-seg digit
    solve(line.first[0] to 1)
    // 7 is the only 3-seg digit
    solve(line.first[1] to 7)
    // 4 is the only 4-seg digit
    solve(line.first[2] to 4)
    // 8 is the only 7-seg digit
    solve(line.first[9] to 8)
  }

  with(solution) {
    // 6: only 6-seg number that does not contain 1
    solve(unsolved.filter { it.hasLength(6) }.filterNot { it.containsAll(num2string[1]!!) }.single() to 6)
    // 3: only 5-seg number that contains 1
    solve(unsolved.filter { it.hasLength(5) }.single { it.containsAll(num2string[1]!!) } to 3)
    // 9: only 6-seg number that contains 4
    solve(unsolved.filter { it.hasLength(6) }.single { it.containsAll(num2string[4]!!) } to 9)
    // 0: last 6-seg
    solve(unsolved.single { it.hasLength(6) } to 0)
    // 5: of the 2 remaining numbers, this is contained in 6
    solve(unsolved.single{ num2string[6]!!.containsAll(it) } to 5)
    // 2: is the last one
    solve(unsolved.single() to 2)
  }

  return solution.number()
}

fun main() {

  fun part1(input: List<String>): Int = input.map { it.split("|")[1] }
    .flatMap { it.split(" ") }
    .map { it.trim() }
    .map { it.length }
    .count { it == 2 || it == 4 || it == 3 || it == 7 }

  fun part2(input: List<String>): Int {
    return input.map { it.parseLine() }.sumOf { analyse(it) }
  }

  println(part1(readInput("Day08")))
  println(part2(readInput("Day08")))
}
