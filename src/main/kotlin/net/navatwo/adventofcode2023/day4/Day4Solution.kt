package net.navatwo.adventofcode2023.day4

import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution

sealed class Day4Solution : Solution<List<Day4Solution.Card>> {
  data object Part1 : Day4Solution() {
    override fun solve(input: List<Card>): ComputedResult {
      TODO()
    }
  }

  override fun parse(lines: Sequence<String>): List<Card> {
    return lines
      .map { line ->
        val withoutPrefix = line.substring("Cart ".length)
        val idString = withoutPrefix.substring(0, withoutPrefix.indexOf(':'))
        val id = idString.toInt()

        val (winningNumbersString, playableNumbersString) = withoutPrefix
          .subSequence(idString.length + 2, withoutPrefix.length)
          .split(" | ", limit = 2)

        Card(
          id = id,
          winningNumbers = parseInts(winningNumbersString),
          playableNumbers = parseInts(playableNumbersString),
        )
      }
      .toList()
  }

  private fun parseInts(line: String): List<Int> {
    return line.splitToSequence(' ')
      .filter { it.isNotBlank() }
      .map { it.toInt() }
      .toList()
  }

  data class Card(
    val id: Int,
    val winningNumbers: List<Int>,
    val playableNumbers: List<Int>,
  )
}
