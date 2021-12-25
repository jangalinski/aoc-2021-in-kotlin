package io.github.jangalinski.kata.aoc2021

import java.lang.NumberFormatException
import java.util.*

sealed interface Snailfish {
  companion object {
    fun split(string: String): Pair<String, String> {
      println(string)
      val s = string.removePrefix("[").removeSuffix("]")
      if (s.isBlank()) {
        return "" to ""
      }

      println("splitting: $s")

      if (!s.contains("[")) {
        val (a, b) = s.split(",")
        return a to b
      }

      val index = s.foldIndexed(-1) { index, count, char ->
        if (count == 0) {
          index
        } else if (char == '[') {
          if (count == -1) 1 else count + 1
        } else if (char == ']') {
          count - 1
        } else {
          count
        }
      }

      return s.substring(0, index) to s.substring(index + 1)
    }

    fun parse(line: String): SnailfishNumber {
      fun sub(s: String): Snailfish? {
        if (s.isBlank()) {
          return null
        }

        val num = try {
          s.toInt()
        } catch (e: NumberFormatException) {
          -1
        }
        if (num > -1) {
          return RegularNumber(num)
        }

        val (a, b) = split(s)

        val sa: Snailfish? = sub(a)
        val sb: Snailfish? = sub(b)

        return sa to sb
      }

      return sub(line) as SnailfishNumber
    }

    infix fun Int.to(snailfish: Snailfish): SnailfishNumber = RegularNumber(this) to snailfish

    infix fun Int.to(num: Int): SnailfishNumber = SnailfishNumber(this, num)

    infix fun Snailfish?.to(other: Snailfish?): Snailfish? = when {
      this == null && other == null -> null
      this == null && other != null -> other
      this != null && other == null -> this
      else -> this!! to other!!
    }
  }

  infix fun to(that: Snailfish): SnailfishNumber = SnailfishNumber(
    this, that
  )

  infix fun to(that: Int): SnailfishNumber = SnailfishNumber(
    this, RegularNumber(that)
  )

}


data class SnailfishNumber(
  val left: Snailfish,
  val right: Snailfish
) : Snailfish {

  constructor(left: Int, right: Int) : this(RegularNumber(left), RegularNumber(right))

  override fun toString() = "[$left,$right]"
}

@JvmInline
value class RegularNumber(val value: Int) : Snailfish {
  override fun toString() = "$value"
}



