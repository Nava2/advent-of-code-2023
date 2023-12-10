package net.navatwo.adventofcode2023.day9

import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution

sealed class Day9Solution : Solution<List<Day9Solution.History>> {
  data object Part1 : Day9Solution() {
    override fun solve(input: List<History>): ComputedResult {
      // TODO Implement
      return ComputedResult.Simple(32L)
    }
  }

  override fun parse(lines: Sequence<String>): List<History> {
    return lines
      .map { line ->
        History(
          line.splitToSequence(' ')
            .map { it.toLong() }
            .map { History.Value(it) }
            .toList()
        )
      }
      .toList()
  }

  @JvmInline
  value class History(val values: List<Value>) {
    @JvmInline
    value class Value(val value: Long)
  }
}
