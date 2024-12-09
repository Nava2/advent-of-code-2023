package net.navatwo.adventofcode2024.day5

import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution

sealed class Day5Solution : Solution<Day5Solution.Input> {
  data object Part1 : Day5Solution() {
    override fun solve(input: Input): ComputedResult {
      TODO()
    }
  }

  override fun parse(lines: Sequence<String>): Input {
    val pageOrderingRules = mutableListOf<Ordering>()
    val pageUpdates = mutableListOf<ManualUpdate>()

    var isPages = true
    for (line in lines) {
      if (line.isBlank()) {
        isPages = false
        continue
      }

      if (isPages) {
        pageOrderingRules += line.split("|")
          .map { it.trim() }
          .let { (x, y) -> Ordering(x.toLong(), y.toLong()) }
      } else {
        pageUpdates += line.split(",")
          .map { it.trim().toLong() }
          .let { ManualUpdate(it) }
      }
    }

    return Input(pageOrderingRules, pageUpdates)
  }

  data class Ordering(val pageX: Long, val pageY: Long)
  data class ManualUpdate(val pageUpdates: List<Long>)

  data class Input(
    val orderRules: List<Ordering>,
    val manualUpdates: List<ManualUpdate>,
  )
}
