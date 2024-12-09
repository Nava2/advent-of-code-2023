package net.navatwo.adventofcode2024.day1

import kotlin.math.absoluteValue
import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution

sealed class Day1Solution : Solution<Day1Solution.Input> {
  data object Part1 : Day1Solution() {
    override fun solve(input: Input): ComputedResult {
      val leftSorted = input.left.sorted()
      val rightSorted = input.right.sorted()

      return ComputedResult.Simple(
        leftSorted.asSequence()
          .zip(rightSorted.asSequence()) { l, r -> (l - r).absoluteValue }
          .sum()
      )
    }
  }

  override fun parse(lines: Sequence<String>): Input {
    val left = mutableListOf<Long>()
    val right = mutableListOf<Long>()
    for ((l, r) in lines.filter { it.isNotBlank() }.map { l -> l.split(" ").filter { it.isNotBlank() } }) {
      left.add(l.toLong())
      right.add(r.toLong())
    }

    return Input(left, right)
  }

  data class Input(val left: List<Long>, val right: List<Long>)
}
