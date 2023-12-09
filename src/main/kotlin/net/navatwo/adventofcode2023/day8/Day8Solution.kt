package net.navatwo.adventofcode2023.day8

import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution

sealed class Day8Solution : Solution<List<Day8Solution.Input>> {
  data object Part1 : Day8Solution() {
    override fun solve(input: List<Input>): ComputedResult {
      // TODO Implement
      return ComputedResult.Simple(32L)
    }
  }

  override fun parse(lines: Sequence<String>): List<Input> {
    return lines.map { Input(it) }.toList()
  }

  @JvmInline
  value class Input(val value: String)
}
