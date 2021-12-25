package io.github.jangalinski.kata.aoc2021

import io.github.jangalinski.kata.aoc2021.Day16.BinaryString
import io.github.jangalinski.kata.aoc2021.strings.toHexString
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource


internal class Day16Test {

  @Test
  fun `valid hexString`() {
    assertThatThrownBy { "ABCDEF0123456789xY".toHexString() }
      .isInstanceOf(IllegalArgumentException::class.java)
      .hasMessageContaining("no valid hexString, contains: [Y, x]")
  }

  @Test
  fun `valid binaryString`() {
    assertThatThrownBy { BinaryString("a10001b12") }
      .isInstanceOf(IllegalArgumentException::class.java)
      .hasMessageContaining("no valid binaryString, contains: [2, a, b]")
  }

  @Test
  fun `hexString to binaryString`() {
    val hex = "D2FE28".toHexString()

    val binary = hex.toBinaryString()

    assertThat(binary.value)
      .isEqualTo("110100101111111000101000")
  }

  @Test
  internal fun `next packet - delete`() {
    assertThat("D2FE28".toHexString().toBinaryString().nextPac1ket())
      .isEqualTo(Triple(6,4, BinaryString("101111111000101000")))
  }





  @ParameterizedTest
  @CsvSource(value=[
    ", true",
    "00010, false",
    "0000, true",
  ])
  fun `ignore rest`(input:String?, expeceted:Boolean) {
    assertThat(BinaryString(input).ignore()).isEqualTo(expeceted)
  }

  @Test
  fun `literal p`() {
    val binary = "D2FE28".toHexString().toBinaryString()

    val result = Day16.Literal.readNext(binary)

    assertThat(result.first).isEqualTo(Day16.Literal(6,4,2021))
    assertThat(result.second.ignore()).isTrue
  }

  @Test
  fun `next type`() {
    assertThat("D2FE28".toHexString().toBinaryString().nextType())
      .isEqualTo(4)
    assertThat("8A004A801A8002F478".toHexString().toBinaryString().nextType())
      .isNotEqualTo(4)
  }

  @Test
   fun `read operator A0016C880162017C3686B18A3D4780`() {
    val input = "A0016C880162017C3686B18A3D4780"
    val hex = input.toHexString()
    val binary = hex.toBinaryString().value.windowed(4)


    val (op, rest) = Day16.Operator.readOperator("A0016C880162017C3686B18A3D4780".toHexString().toBinaryString())

    println(op)
  }

  @Test
  fun `read operator`() {
    val bits = BinaryString("010110100000000001")

    val (op, r) = Day16.Operator.readNext(bits)
    assertThat(op.version).isEqualTo(2)
    assertThat(op.type).isEqualTo(6)
    assertThat(op.size).isEqualTo(1)
    assertThat(op.subPackets).isEmpty()
    assertThat(r.ignore()).isTrue

    val b2 = "8A004A801A8002F478".toHexString().toBinaryString()
    println("8A004A801A8002F478".toHexString().toBinaryString())

    val (o1,r1) = Day16.Operator.readOperator(b2)
    assertThat(o1.version).isEqualTo(4)
    assertThat(o1.type).isEqualTo(2)
    assertThat(o1.size).isEqualTo(1)
    assertThat(o1.subPackets).isEmpty()
    assertThat(r1.ignore()).isFalse()

  }
}
