package net.navatwo.adventofcode2023.template

import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution

sealed class TemplateSolution : Solution<List<TemplateSolution.Input>> {
  data object Part1 : TemplateSolution() {
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
