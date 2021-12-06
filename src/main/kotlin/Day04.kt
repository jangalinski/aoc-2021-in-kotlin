package io.github.jangalinski.kata.aoc2021

import io.toolisticon.lib.krid.Krid
import io.toolisticon.lib.krid.Krids
import io.toolisticon.lib.krid.plus


data class BingoNumber(val number: Int, val checked: Boolean = false)

data class Bingo(var krid: Krid<BingoNumber>) {
  companion object {
    fun readAll(boards: List<String>): List<Bingo> = boards.map { read(it) }

    fun read(board: String): Bingo {

      val n: List<List<BingoNumber>> = board.lines().map {
        it.split(" ")
          .filter { it.isNotBlank() }
          .map { it.trim().toInt() }
          .map { BingoNumber(it) }
      }

      return Bingo(Krids.krid(n, BingoNumber(-1)))
    }
  }

  fun isWinning() = krid.columns().any {
    it.values.all { it.checked }
  } || krid.rows().any {
    it.values.all { it.checked }
  }

  fun check(num:Int) {
    krid.iterator().asSequence().find { it.value.number == num }?.let {
      krid = krid.plus(it.copy(value = it.value.copy(checked = true)))
    }
  }

  fun sumUnchecked(): Int = krid.iterator().asSequence().map { it.value }.filterNot { it.checked }.sumOf { it.number }

}

fun main() {
  val input = readInputString("Day04_test").split("\n\n").map { it.trim() }
  val numbers = input.first().split(",").map { it.trim() }.map { it.toInt() }

  var boards = Bingo.readAll(input.drop(1))

  fun List<Bingo>.check(number:Int) = this.forEach { it.check(number) }

  fun List<Bingo>.winner() = this.find { it.isWinning() }

  fun Pair<Int,Bingo>.score() = first * second.sumUnchecked()

  fun part1() : Int {
    var b = boards
    var res : Pair<Int,Bingo>? = null
    for (n in numbers) {
      b.check(n)
      val winner = b.winner()
      if (winner != null) {
        res = n to winner
        break
      }
    }
    return res!!.score()
  }

  fun part2() : Int {
    var b = boards

    var res : Pair<Int,Bingo>? = null
    for (n in numbers) {
      b.check(n)
      val winner = b.winner()
      if (winner != null) {
        b = b -  winner
        print("removed winner: $b")
      }
      if (b.size == 1) {
        res = n to b.first()
      }
    }

    println("r: $res")
    return res!!.score()
  }


//  println(part1())
  println(part2())
}



