package net.navatwo.adventofcode2023.day4

import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution

sealed class Day4Solution : Solution<List<Day4Solution.Card>> {
  data object Part1 : Day4Solution() {
    override fun solve(input: List<Card>): ComputedResult {
      var result = 0L

      for (card in input) {
        val winningNumbersMatched = card.winningNumbers.count {
          it in card.playableNumbers
        }

        if (winningNumbersMatched >= 1) {
          // 2^N
          val score = 1 shl (winningNumbersMatched - 1)
          result += score
        }
      }

      return ComputedResult.Simple(result)
    }
  }

  override fun parse(lines: Sequence<String>): List<Card> {
    return lines
      .filter { it.isNotBlank() }
      .map { line ->
        val withoutPrefix = line.substring("Card ".length)
        val idString = withoutPrefix.substring(0, withoutPrefix.indexOf(':'))
        val id = idString.trimStart().toInt()

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

  private fun parseInts(line: String): Set<Int> {
    return line.splitToSequence(' ')
      .filter { it.isNotBlank() }
      .map { it.toInt() }
      .toSet()
  }

  data class Card(
    val id: Int,
    val winningNumbers: Set<Int>,
    val playableNumbers: Set<Int>,
  )
}
