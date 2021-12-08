package io.github.jangalinski.kata.aoc2021.week1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class GenerateParenthesisCombinationsTests {

  @Test
  fun `test isvalid`() {
    assertTrue(isValid("()"))
    assertTrue(isValid("(())"))
    assertTrue(isValid("()()"))
    assertFalse(isValid("())("))
  }

  @Test
  fun mutations() {
    val p = "abc".permute()
    assertEquals(listOf(
      "abc",
      "acb",
      "bac",
      "bca",
      "cab",
      "cba",
    ), p.sorted())

    assertEquals(listOf(
      "(())",
      "()()",
      "())(",
      ")(()",
      ")()(",
      "))((",
    ), "(())".permute().distinct().sorted())


  }

  @Test
  fun `test 1`() {
    assertEquals(listOf("()"), generateParenthesisCombinations(1))

  }

  @Test
  fun `test 2`() {
    assertEquals(listOf("(())", "()()"), generateParenthesisCombinations(2).sorted())
  }

  @Test
  fun `test 3`() {
    assertEquals(
      listOf("((()))", "(()())", "(())()", "()(())", "()()()"),
      generateParenthesisCombinations(3).sorted()
    )

  }

  @Test
  fun `test 4`() {
    assertEquals(
      listOf(
        "(((())))", "((()()))", "((())())", "((()))()", "(()(()))", "(()()())", "(()())()",
        "(())(())", "(())()()", "()((()))", "()(()())", "()(())()", "()()(())", "()()()()"
      ),
      generateParenthesisCombinations(4).sorted()
    )

  }

  @Test
  fun `test 5`() {
    assertEquals(
      listOf(
        "((((()))))", "(((()())))", "(((())()))", "(((()))())", "(((())))()", "((()(())))",
        "((()()()))", "((()())())", "((()()))()", "((())(()))", "((())()())", "((())())()",
        "((()))(())", "((()))()()", "(()((())))", "(()(()()))", "(()(())())", "(()(()))()",
        "(()()(()))", "(()()()())", "(()()())()", "(()())(())", "(()())()()", "(())((()))",
        "(())(()())", "(())(())()", "(())()(())", "(())()()()", "()(((())))", "()((()()))",
        "()((())())", "()((()))()", "()(()(()))", "()(()()())", "()(()())()", "()(())(())",
        "()(())()()", "()()((()))", "()()(()())", "()()(())()", "()()()(())", "()()()()()"
      ),
      generateParenthesisCombinations(5).sorted()
    )
  }

  @Test
  fun `test 10`() {
    assertEquals(16796, generateParenthesisCombinations(10).size)
  }
}
