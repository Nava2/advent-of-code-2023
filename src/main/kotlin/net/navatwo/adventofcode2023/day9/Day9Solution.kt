package net.navatwo.adventofcode2023.day9

import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution

sealed class Day9Solution : Solution<List<Day9Solution.History>> {
  data object Part1 : Day9Solution() {
    override fun solve(input: List<History>): ComputedResult {
      val predictions = input.map { history ->
        val rowHistory = history.values.map { it.value }

        val lastElementStack = ArrayDeque<Long>()

        val constDiff = computeLinearRegression(rowHistory, lastElementStack = lastElementStack)

        // Iterate the stack of last elements and walk back up adding the prediction to the previous one
        var prediction = constDiff
        while (lastElementStack.isNotEmpty()) {
          val lastElement = lastElementStack.removeLast()
          prediction += lastElement
        }

        prediction
      }

      return ComputedResult.Simple(predictions.reduce(Long::plus))
    }
  }

  protected fun computeLinearRegression(
    initialHistory: List<Long>,
    firstElementStack: ArrayDeque<Long>? = null,
    lastElementStack: ArrayDeque<Long>? = null,
  ): Long {
    var history = initialHistory
    var constDiff: Long? = null

    do {
      // compute the differences between each value and its next
      val differences = history.zipWithNext { a, b -> b - a }

      firstElementStack?.addLast(history.first())
      lastElementStack?.addLast(history.last())

      history = differences

      val differenceCandidate = differences.first()
      if (differences.all { it == differenceCandidate }) {
        // if all differences are the same, we have found the linear regression
        // and can compute the next value
        constDiff = differenceCandidate
      }
    } while (constDiff == null)

    return constDiff
  }

  data object Part2 : Day9Solution() {
    override fun solve(input: List<History>): ComputedResult {
      val predictions = input.map { history ->
        val firstElementStack = ArrayDeque<Long>()

        val constDiff = computeLinearRegression(history.values.map { it.value }, firstElementStack = firstElementStack)

        // Iterate the stack of last elements and walk back up adding the prediction to the previous one
        var prediction = constDiff
        while (firstElementStack.isNotEmpty()) {
          val lastElement = firstElementStack.removeLast()
          prediction = lastElement - prediction
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
