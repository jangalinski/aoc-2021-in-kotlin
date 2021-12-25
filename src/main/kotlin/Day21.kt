package io.github.jangalinski.kata.aoc2021

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

interface Day21DieRolls : () -> List<Triple<Int, Int, Int>>

class Day21D100(var count: Int = 0) : Day21DieRolls, Iterator<Int> {
  private val next = AtomicInteger(1)

  override fun invoke(): List<Triple<Int, Int, Int>> = listOf(Triple(next(), next(), next()))

  override fun hasNext(): Boolean = true

  override fun next(): Int {
    val result = next.getAndIncrement() % 100
    count += 1

    return if (result == 0) {
      100
    } else {
      result
    }
  }
}

class Day21QuantumDie : Day21DieRolls {
  // all possible results of the quantom die
  private val rolls = listOf(
    Triple(1, 1, 1),
    Triple(1, 1, 2),
    Triple(1, 1, 3),
    Triple(1, 2, 1),
    Triple(1, 2, 2),
    Triple(1, 2, 3),
    Triple(1, 3, 1),
    Triple(1, 3, 2),
    Triple(1, 3, 3),
    Triple(2, 1, 1),
    Triple(2, 1, 2),
    Triple(2, 1, 3),
    Triple(2, 2, 1),
    Triple(2, 2, 2),
    Triple(2, 2, 3),
    Triple(2, 3, 1),
    Triple(2, 3, 2),
    Triple(2, 3, 3),
    Triple(3, 1, 1),
    Triple(3, 1, 2),
    Triple(3, 1, 3),
    Triple(3, 2, 1),
    Triple(3, 2, 2),
    Triple(3, 2, 3),
    Triple(3, 3, 1),
    Triple(3, 3, 2),
    Triple(3, 3, 3),
  )

  override fun invoke(): List<Triple<Int, Int, Int>> = rolls
}

data class Day21Player(val name: Int, val pos: Int, val points: Int) {

  fun takeSteps(rolls: Triple<Int, Int, Int>): Day21Player {
    val sum = rolls.first + rolls.second + rolls.third
    val newPos = ((pos + sum) % 10).let {
      if (it % 10 == 0) 10 else it
    }

    return copy(pos = newPos, points = points + newPos)
  }
}

sealed interface Day21Result {
  fun winner(): Int = -1
  fun next(): List<Day21Result> = listOf()
}

data class Day21Winner(val name: Int, val loserPoints: Int) : Day21Result {
  override fun winner(): Int = name
}

data class Day21Game(
  val activePlayer: Day21Player,
  val passivePlayer: Day21Player,
  val dieRolls: Day21DieRolls,
  val winAt: Int
) : Day21Result {

  override fun next(): List<Day21Result> {
    val allDieRolls = dieRolls()

    return allDieRolls.map {
      val activeSteps = activePlayer.takeSteps(it)

      if (activeSteps.points >= winAt) {
        Day21Winner(activeSteps.name, passivePlayer.points)
      } else {
        copy(activePlayer = passivePlayer, passivePlayer = activeSteps)
      }
    }
  }


}

fun main() {
  val test = 4 to 8
  val prod = 6 to 4


  fun part1(sp1: Int, sp2: Int): Int {
    val die = Day21D100()
    var result: Day21Result = Day21Game(
      activePlayer = Day21Player(1, sp1, 0),
      passivePlayer = Day21Player(2, sp2, 0),
      dieRolls = die,
      winAt = 1000
    )

    while (result is Day21Game) {
      result = result.next().first()
    }

    return (result as Day21Winner).loserPoints * die.count
  }

  fun part2(sp1: Int, sp2: Int): Long {
    val count: MutableList<Long> = mutableListOf(0L, 0L, 0L)

    var games: List<Day21Result> = listOf(
      Day21Game(
        activePlayer = Day21Player(1, sp1, 0),
        passivePlayer = Day21Player(1, sp2, 0),
        dieRolls = Day21QuantumDie(),
        winAt = 21
      )
    )

    while (games.isNotEmpty()) {
      val allGames = games.flatMap { it.next() }

      allGames.filterIsInstance<Day21Winner>().forEach {
        count[it.winner()] = count[it.winner()] + 1
      }

      games = allGames.filterIsInstance<Day21Game>()
    }


    return count.maxOrNull()!!
  }

  println(part2(4, 8))
//  println(part1(6, 4))


}


