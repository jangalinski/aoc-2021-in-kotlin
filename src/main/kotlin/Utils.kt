package io.github.jangalinski.kata

import io.toolisticon.lib.krid.model.Cell
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest



fun readInputString(name:String) = File("src/main/resources", "$name.txt").readText()

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src/main/resources", "$name.txt").readLines()

fun readInputAsInt(name:String) = readInput(name).map { it.trim() }.map { it.toInt() }

fun Pair<Int, Int>.sum() = this.first + this.second

fun Cell.multiply() = this.x * this.y

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)
