package net.navatwo.adventofcode2023.day1

import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution

sealed class Day1Solution : Solution<List<Day1Solution.CalibrationLine>> {
  data object Part1 : Day1Solution() {
    override fun solve(input: List<CalibrationLine>): ComputedResult {
      return ComputedResult.Simple(input.sumOf { parse(it.line) })
    }

    private fun parse(line: String): Int {
      val mostSig = (0..line.lastIndex)
        .firstNotNullOf { i ->
          val ch = line[i]
          if (ch.isDigit()) ch.digitToInt() else null
        }
      val leastSig = (line.lastIndex downTo 0)
        .firstNotNullOf { i ->
          val ch = line[i]
          if (ch.isDigit()) ch.digitToInt() else null
        }

      return mostSig * 10 + leastSig
    }
  }

  data object Part2 : Day1Solution() {
    override fun solve(input: List<CalibrationLine>): ComputedResult {
      return ComputedResult.Simple(input.sumOf { parse(it.line) })
    }

    private fun parse(line: String): Int {
      val mostSig = (0..line.lastIndex)
        .firstNotNullOf { i ->
          tryParseChar(line, i)
        }
      val leastSig = (line.lastIndex downTo 0)
        .firstNotNullOf { i ->
          tryParseChar(line, i)
        }

      return mostSig * 10 + leastSig
    }
  }

  override fun parse(lines: Sequence<String>): List<CalibrationLine> {
    return lines.map { CalibrationLine(it) }.toList()
  }

  @JvmInline
  value class CalibrationLine(val line: String)
}

private data class Node(
  var value: Int? = null,
  val children: MutableMap<Char, Node> = mutableMapOf(),
) {
  fun find(input: CharSequence): Int? {
    var node: Node = this
    for (c in input) {
      val nextNode = node.children[c]

      // null node => no child
      // non-null node with value => found a word
      if (nextNode == null || nextNode.value != null) {
        return nextNode?.value
      }

      node = nextNode
    }

    return node.value
  }
}

private val numbers = mapOf(
  "one" to 1,
  "two" to 2,
  "three" to 3,
  "four" to 4,
  "five" to 5,
  "six" to 6,
  "seven" to 7,
  "eight" to 8,
  "nine" to 9,
)

private val maxNumberSize = numbers.keys.maxOf { it.length }

private fun buildTrie(): Node {
  val rootNode = Node()
  for ((word, value) in numbers) {
    var node = rootNode
    for (c in word) {
      node = node.children.getOrPut(c) { Node() }
    }
    node.value = value
  }

  return rootNode
}

private val numberTrie = buildTrie()

private fun tryParseChar(line: String, index: Int): Int? {
  val char = line[index]
  if (char.isDigit()) return char.digitToInt()

  val numberValue = line.subSequence(index, minOf(index + maxNumberSize, line.length))
  return numberTrie.find(numberValue)
}
