package net.navatwo.adventofcode2023.day4

import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution

sealed class Day4Solution : Solution<List<Day4Solution.Card>> {
  data object Part1 : Day4Solution() {
    override fun solve(input: List<Card>): ComputedResult {
      var result = 0L

      for (card in input) {
        val winningNumbersMatched = card.computeWinningNumbers()

        if (winningNumbersMatched >= 1) {
          // 2^N
          val score = 1 shl (winningNumbersMatched - 1)
          result += score
        }
      }

      return ComputedResult.Simple(result)
    }
  }

  data object Part2 : Day4Solution() {
    override fun solve(input: List<Card>): ComputedResult {
      val winningNumbersPerCard = input.associateWith { it.computeWinningNumbers() }

      // do a DFS of the winning cards
      // compute the number of winning cards in reverse order, then sum them up

      val winningCardsPerId = input.associateTo(HashMap(input.size)) { it.id to 1L }
      for (card in input.reversed()) {
        val winningNumbers = winningNumbersPerCard.getValue(card)
        winningCardsPerId.computeIfPresent(card.id) { id, value ->
          value + (1..winningNumbers).sumOf { winningCardsPerId.getValue(it + id) }
        }
      }

      val result = winningCardsPerId.values.sum()

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
  ) {
    fun computeWinningNumbers(): Int = winningNumbers.count { it in playableNumbers }
  }
}
