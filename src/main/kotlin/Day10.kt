package io.github.jangalinski.kata.aoc2021

import java.util.*

private sealed interface TestResult
private data class Corrupt(val char:String) : TestResult
private data class Incomplete(val stack: Stack<String>) : TestResult
private object Correct : TestResult

private val points = mapOf(
  ")" to 3,
  "]" to 57,
  "}" to 1197,
  ">" to 25137,
)


fun main() {
  fun String.analyse() : TestResult {
    val stack= Stack<String>()

    this.split("").filterNot { it.isEmpty() }.forEach {
      when(it) {
        "(" -> stack.push(")")
        "<" -> stack.push(">")
        "[" -> stack.push("]")
        "{" -> stack.push("}")
        else -> if (stack.isEmpty() || stack.pop() != it) {
          return Corrupt(it)
        }
      }
    }

    return if (stack.isEmpty()) Correct else Incomplete(stack)
  }

  fun read(test:Boolean): List<List<Char>> {
    return readInput("Day10" + if(test) "_test" else "").map { it.trim().toList() }
  }

  fun List<Char>.count(): Map<Char, Int> = this.groupingBy { it }.eachCount()
  fun List<Char>.isCorrupted(): Boolean {
    val c = count()
    return c['(']?:0 == c[')']?:0 && c['<']?:0 == c['>']?:0 && c['[']?:0 == c[']']?:0 && c['{']?:0 == c['}']?:0
  }

  fun part1(lines : List<String>): Int = lines.map { it to it.analyse() }
    .filter { it.second is Corrupt }
    .map { it.first to it.second as Corrupt }
    .sumOf { points[it.second.char]!! }

  fun part2(lines: List<String>) : Long {
    val analysis: List<Pair<Pair<String, List<String>>, Long>> = lines
      // each line with TestResult
      .asSequence()
      .map { it to it.analyse() }
      // keep only Incomplete
      .filter { it.second is Incomplete }
      .map { it.first to it.second as Incomplete }
      // map to reversed stack
      .map { it.first to it.second.stack.reversed() }
      .map { it to it.second.fold(0L) { r,c -> 5 * r + when(c) {
        ")" -> 1L
        "]" -> 2L
        "}" -> 3L
        else -> 4L
      }  } }
      .toList()

    val scores = analysis.map { it.second }.sorted()

    println(scores)
    val middle = scores.lastIndex / 2
println(middle)


    return scores[middle]
  }


  val test = readInput("Day10_test")
  val input = readInput("Day10")


  println(part1(test))
  //println(part1(input))

  println(part2(test))
  println(part2(input))

}
