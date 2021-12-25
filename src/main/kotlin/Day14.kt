package io.github.jangalinski.kata.aoc2021

fun main() {
  fun read(test: Boolean = false): Pair<String, Map<String, Char>> {
    val (h, t) = readInputString("Day14" + if (test) "_test" else "").split("\n\n")

    val m = t.lines().filterNot { it.isEmpty() }.map {
      val (k, v) = it.split(" -> ")
      k.trim() to v.trim().first()
    }

    return h to m.toMap()
  }

  fun String.count() = toList().groupingBy { it }.eachCount()
  fun String.bucketPairs() = windowed(2).groupingBy { it }.eachCount().toSortedMap()
  fun Pair<String, Map<String, Char>>.initCharCount(): Map<Char, Int> {
    val distinct: Set<Char> = first.toSet() + second.entries.flatMap {
      (it.key.toList() + it.value)
    }.distinct().toSet()

    return distinct.fold(this.first.count().toMutableMap()) { m, c ->
      m.putIfAbsent(c, 0)
      m
    }
  }

  fun part1(input: Pair<String, Map<String, Char>>, steps: Int = 10): Int {
    val charCount = input.initCharCount().toMutableMap()
    val rules: Map<String, Char> = input.second.map { e -> e.key to e.value }.toMap()
    val bucketPairs = input.first.bucketPairs()

    println("charCount=$charCount")
    println("rules=$rules")
    println("bucketPairs=$bucketPairs")



    return 0
  }


//  fun part11(input: Pair<String, Map<String, String>>, steps: Int = 10): Int {
//    val allChars
//
//    val charCount: MutableMap<Char, Int> = input.first.toList().groupingBy { it }.eachCount().toMutableMap()
//      .apply {
//        allChars.forEach {
//          this.computeIfAbsent(it) { 0 }
//        }
//      }
//
//
//    val rules: Map<String, Char> = input.second.map { e -> e.key to e.value.first() }.toMap()
//
//    val initialBuckets: Map<String, Int> = input.first.windowed(2).groupingBy { it }.eachCount()
//
//    var tmp = initialBuckets
//    println("bfore : $tmp")
//
//    repeat(steps) {
//      tmp = tmp.entries.fold(mutableMapOf<String, Int>()) { m, cu ->
//        if (rules.containsKey(cu.key)) {
//          val char = rules[cu.key]!!
//          charCount.compute(char) { _, c -> c!! + cu.value }
//
//          m.compute("${cu.key[0]}$char") { _, c -> if (c == null) 1 else c + cu.value }
//          m.compute("$char${cu.key[1]}") { _, c -> if (c == null) 1 else c + cu.value }
//        }
//        m
//      }
//      println("step $it: $tmp - $charCount")
//    }
//
//    println(charCount)
//
//    println(input.second)
//    println(initialBuckets)
//
//
//    return 0
//  }

  val input = read(true)

  println(part1(input,1))


//  println("NNCB" to "NNCB".count())
//  println("NCNBCHB" to "NCNBCHB".count())
//  println("NBCCNBBBCBHCB" to "NBCCNBBBCBHCB".count())
//  println("NBBBCNCCNBBNBNBBCHBHHBCHB" to "NBBBCNCCNBBNBNBBCHBHHBCHB".count())
//  println("NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB" to "NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB".count())
}
