package io.github.jangalinski.kata.aoc2021

import java.util.*

object Day12 {

  data class Node(val value:String, val children: List<Node> = emptyList()) {

  }

  fun read(string: String) = string.trim().lines()
    .map { it.trim() }
    .map {
      val (n1, n2) = it.split("-")
      n1 to n2
    }

  fun parse(list: List<Pair<String,String>>) : Node {
    val tmp = list.toMutableList()
    var node = Node("start")



    return node
  }

}

fun main() {

}
