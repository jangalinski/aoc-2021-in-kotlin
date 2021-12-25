package io.github.jangalinski.kata.aoc2021

import java.util.concurrent.atomic.AtomicLong

data class Day24ALU(
  val input: List<Int>,
  val state: Map<String, Int> = mapOf("w" to 0, "x" to 0, "y" to 0, "z" to 0)
) {
  operator fun get(key: String): Int = state[key] ?: key.toInt()

  operator fun set(key: String, value: Int): Day24ALU = copy(state = state.toMutableMap().apply { put(key, value) })

  val w by state
  val x by state
  val y by state
  val z by state

  val valid = z == 0
}

//In all of these instructions, a and b are placeholders; a will always be the variable where the result of the operation is stored (one of w, x, y, or z), while b can be either a variable or a number. Numbers can be positive or negative, but will always be integers.
typealias Day24Instruction = (Day24ALU) -> Day24ALU


fun main() {
  fun parse(line: String): Day24Instruction {
    val (cmd, params) = line.trim().split(" ").map { it.trim() }.let {
      it.first() to it.drop(1)
    }
    return when (cmd) {
      //add a b - Add the value of a to the value of b, then store the result in variable a.
      "add" -> { alu ->
        val (a, b) = params
        alu.set(a, alu[a] + alu[b])
      }
      //mul a b - Multiply the value of a by the value of b, then store the result in variable a.
      "mul" -> { alu ->
        val (a, b) = params
        alu.set(a, alu[a] * alu[b])
      }
      //div a b - Divide the value of a by the value of b, truncate the result to an integer, then store the result in variable a. (Here, "truncate" means to round the value toward zero.)
      "div" -> { alu ->
        val (a, b) = params
        alu.set(a, alu[a] / alu[b])
      }
      //mod a b - Divide the value of a by the value of b, then store the remainder in variable a. (This is also called the modulo operation.)
      "mod" -> { alu ->
        val (a, b) = params
        alu.set(a, alu[a] % alu[b])
      }
      //eql a b - If the value of a and b are equal, then store the value 1 in variable a. Otherwise, store the value 0 in variable a.
      "eql" -> { alu ->
        val (a, b) = params
        alu.set(a, if (alu[a] == alu[b]) 1 else 0)
      }
      //inp a - Read an input value and write it to variable a.
      else -> { alu ->
        val input = alu.input.first()

        alu.copy(input = alu.input.drop(1)).set(params.first(), input)
      }
    }


  }

  fun List<Day24Instruction>.run(alu: Day24ALU) = fold(alu) { c, f -> f(c) }

  fun part1(prog: List<Day24Instruction>): Long {
    val max = AtomicLong(100_000_000_000_000L)
    var alu = Day24ALU(emptyList()).set("z", 1)

    while (!alu.valid) {
      val string = max.decrementAndGet().toString()
      if (string.contains('0')) {
        println("skipping: $string")
        continue
      } else {
        if (max.get() % 100 == 0L) {
          println("-> ${max.get()}")
        }
        val input = string.split("").filterNot { it.isBlank() }.map { it.toInt() }

        alu = prog.run(Day24ALU(input))
      }
    }

    return max.get()
  }

  val instructions = readInput("Day24").filterNot { it.isBlank() }.map { parse(it) }

  part1(instructions)

}
