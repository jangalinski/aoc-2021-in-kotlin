package io.github.jangalinski.kata.aoc2021

import io.github.jangalinski.kata.aoc2021.strings.HexString
import io.github.jangalinski.kata.aoc2021.strings.toHexString

object Day16 {

  @JvmInline
  value class BinaryString(val value: String) {
    companion object {
      val EMPTY = BinaryString("")
      operator fun invoke(value: String?): BinaryString = if (value != null) BinaryString(value) else EMPTY
    }

    // return the next type (substring(3,6).toInt(2) without modifying the value
    fun nextType(): Int {
      return value.substring(3, 6).toInt(2)
    }

    fun readNextPacket(): Triple<Int, Int, BinaryString> {
      val (version, r1) = splitAtIndex(3)
      val (type, r2) = r1.splitAtIndex(3)

      return Triple(version.toInt(), type.toInt(), r2)
    }


    operator fun plus(s: BinaryString) = BinaryString(value + s.value)

    init {
      val allowed = setOf('0', '1')
      val diff = (value.toSet() - allowed).sorted()
      require(diff.isEmpty()) { "no valid binaryString, contains: $diff" }
    }

    fun nextPac1ket(): Triple<Int, Int, BinaryString> {
      val (version, r1) = splitAtIndex(3)
      val (type, r2) = r1.splitAtIndex(3)

      return Triple(version.toInt(), type.toInt(), r2)
    }

    // true if rest of String can be ignored (empty or all zero)
    fun ignore() = this.isEmpty() || !value.contains("1")

    fun toInt() = value.toInt(2)

    fun isEmpty() = value.isEmpty()

    fun splitAtIndex(index: Int): Pair<BinaryString, BinaryString> {
      require(index >= 0) { "string='$this', index=$index" }

      return if (value.isEmpty())
        EMPTY to EMPTY
      else
        BinaryString(value.substring(0, index)) to BinaryString(value.substring(index))
    }


    fun readBoolean(): Pair<Boolean, BinaryString> {
      val (b, rest) = this.splitAtIndex(1)

      return (b.value == "1") to rest
    }


  }

  sealed interface Packet {
    companion object {

      fun parse(bits: BinaryString): List<Packet> {
        val result = mutableListOf<Packet>()
        var remaining = bits

        while (!remaining.ignore()) {
          val type = bits.nextType()

          if (type == 4) {
            val (lit, r1) = Literal.readNext(remaining)
            result.add(lit)
            remaining = r1
          } else {
            val (op, r2) = Operator.readNext(remaining)

            result.add(op)
            remaining = r2
          }
        }

        return result.toList()
      }

      fun parse(hex: HexString) = parse(hex.toBinaryString())
    }

    val version: Int
    val type: Int
  }

  data class Literal(
    override val version: Int,
    override val type: Int,
    val value: Int
  ) : Packet {
    companion object {
      fun readNext(binaryString: BinaryString): Pair<Literal, BinaryString> {
        val next = binaryString.readNextPacket()
        require(next.second == 4)
        val (value, rest) = readLiteral(next.third)
        return Literal(version = next.first, type = next.second, value = value) to rest
      }

      private fun readLiteral(bits: BinaryString): Pair<Int, BinaryString> {
        var end = false
        var remainder = bits
        var value = BinaryString.EMPTY
        while (!end) {
          val fiveBits = remainder.splitAtIndex(5)
          val (shouldEnd, digits) = fiveBits.first.readBoolean()
          value += digits
          end = !shouldEnd
          remainder = fiveBits.second
        }
        return value.toInt() to remainder
      }
    }
  }

  data class Operator(
    override val version: Int,
    override val type: Int,
    val size: Int,
    val subPackets: List<Packet> = emptyList()
  ) : Packet {
    companion object {
      fun readOperator(binaryString: BinaryString) : Pair<Operator, BinaryString> {
        val (version, type, rest) = binaryString.readNextPacket()
        require(type != 4)
        val (size, r1) = Operator.readSize(rest)

        return Operator(version, type, size) to r1
      }


      fun readNext(binaryString: BinaryString): Pair<Operator, BinaryString> {
        val (op, rest) = readOperator(binaryString)

        return if (!rest.ignore())
          op to rest
        else
          op to BinaryString.EMPTY
      }

      private fun readSize(binaryString: BinaryString): Pair<Int, BinaryString> {
        val (sizeTypeBit, r1) = binaryString.readBoolean()
        val (sizeBits, r2) = r1.splitAtIndex(if (sizeTypeBit) 11 else 15)
        val size = sizeBits.toInt()
        return size to r2
      }
    }

    fun add(string: BinaryString): Pair<Operator, BinaryString> {
      return if (size == subPackets.size) {
        this to string
      } else {
        var result = this.copy()
        var rem = string

        while (result.size != result.subPackets.size) {
          val type = rem.nextType()
          if (type == 4) {
            val (lit, r) = Literal.readNext(binaryString = string)
            result = result.copy(subPackets = subPackets + lit)
            rem = r
          } else {
            val (no, r2) = Operator.readNext(rem)
            result = result.copy(subPackets = subPackets + no)
            rem = r2
          }
        }
        result to rem
      }
    }
  }
}

fun main() {
  val hexInput = readInputString("Day16").trim().toHexString()


  println(Day16.Packet.Companion.parse("A0016C880162017C3686B18A3D4780".toHexString()))


}
