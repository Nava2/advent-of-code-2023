package net.navatwo.adventofcode2023.day6

import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution

sealed class Day6Solution : Solution<List<Day6Solution.Race>> {
  data object Part1 : Day6Solution() {
    override fun solve(input: List<Race>): ComputedResult {
      // TODO Implement
      return ComputedResult.Simple(32L)
    }
  }

  override fun parse(lines: Sequence<String>): List<Race> {
    val (times, distances) = lines
      .map { line ->
        line.substringAfter(':')
          .trim()
          .splitToSequence(' ')
          .filter { it.isNotBlank() }
          .map { it.toLong() }
          .toList()
      }
      .toList()

    return times.zip(distances) { time, distance ->
      Race(TimeMs(time), DistanceMM(distance))
    }
  }

  @JvmInline
  value class TimeMs(val value: Long)

  @JvmInline
  value class DistanceMM(val value: Long)

  data class Race(val time: TimeMs, val distance: DistanceMM)
}
