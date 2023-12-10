package net.navatwo.adventofcode2023.day9

import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution

sealed class Day9Solution : Solution<List<Day9Solution.History>> {
  data object Part1 : Day9Solution() {
    override fun solve(input: List<History>): ComputedResult {
      val predictions = input.map { history ->
        var rowHistory = history.values.map { it.value }

        val lastElementStack = ArrayDeque<Long>()

        var constDiff: Long? = null
        do {
          // compute the differences between each value and its next
          val differences = rowHistory.zipWithNext { a, b -> b - a }

          lastElementStack.addLast(rowHistory.last())
          rowHistory = differences

          val differenceCandidate = differences.first()
          if (differences.all { it == differenceCandidate }) {
            // if all differences are the same, we have found the linear regression
            // and can compute the next value
            constDiff = differenceCandidate
          }
        } while (rowHistory.isNotEmpty() && constDiff == null)

        // Iterate the stack of last elements and walk back up adding the prediction to the previous one
        var prediction = constDiff!!
        while (lastElementStack.isNotEmpty()) {
          val lastElement = lastElementStack.removeLast()
          prediction += lastElement
        }

        prediction
      }

      return ComputedResult.Simple(predictions.reduce(Long::plus))
    }
  }

  override fun parse(lines: Sequence<String>): List<History> {
    return lines
      .filter { it.isNotBlank() }
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
