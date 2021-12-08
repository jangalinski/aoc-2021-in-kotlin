package io.github.jangalinski.kata.aoc2021.week1

import java.util.*

fun String.permute(result: String = ""): List<String> =
  if (isEmpty()) listOf(result) else flatMapIndexed { i, c -> removeRange(i, i + 1).permute(result + c) }


fun isValid(s: String): Boolean {

  val stack= Stack<String>()
  s.forEach {
    when(it.toString()){
      "("->stack.push(")")
      else-> {
        if(stack.isEmpty()||stack.pop()!=it.toString()){
          return false
        }
      }
    }
  }

  return stack.isEmpty()
}


fun generateParenthesisCombinations(num: Int): List<String> = String(CharArray(num) { _ -> '('} + CharArray(num) { _ -> ')'})
  .permute()
  .distinct()
  .filter { isValid(it) }
  .sorted()
