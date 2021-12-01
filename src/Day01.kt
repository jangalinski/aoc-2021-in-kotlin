fun main() {
  fun part1(input: List<Int>): Int = input.fold(Int.MAX_VALUE to 0) { p, c ->
    c to if (c > p.first) p.second + 1 else p.second
  }.second

  fun sum3(input:List<Int>) = sequence<Int>{
    var sumA = mutableListOf<Int>()
    var sumB = mutableListOf<Int>()
    var sumC = mutableListOf<Int>()
    var cA =0
    var cB =-1
    var cC =-2

    input.forEachIndexed { i,num ->
      if (cA in 0..2) {
        sumA += num
      }
      if (cB in 0..2) {
        sumB += num
      }
      if (cC in 0..2) {
        sumC += num
      }
      cA++
      cB++
      cC++
      if (sumA.size == 3) {
        yield(sumA.sum())
        cA = 0
        sumA.clear()
      }
      if (sumB.size == 3) {
        cB = 0
        yield(sumB.sum())
        sumB.clear()
      }
      if (sumC.size == 3) {
        cC = 0
        yield(sumC.sum())
        sumC.clear()
      }
    }
  }.toList()

  fun part2(input: List<Int>): Int {
    return part1(sum3(input))
  }
//
//      199  A
//      200  A B
//      208  A B C
//      210    B C D
//      200      C D E
//      207        D E F
//      240          E F G
//      269            F G H
//      260              G H I
//      263                H I J


  // test if implementation meets criteria from the description, like:
  val testInput: List<Int> = readInput("Day01_test").map { it.trim() }.map { it.toInt() }
  println("i: $testInput")
  println("s: ${sum3(testInput).toList()}")
  println(part2(testInput))


  val input = readInput("Day01").map { it.trim() }.map { it.toInt() }
   println(part2(input))
}
