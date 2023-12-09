package net.navatwo.adventofcode2023.day6

import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution

sealed class Day6Solution : Solution<List<Day6Solution.Race>> {
  data object Part1 : Day6Solution() {
    override fun solve(input: List<Race>): ComputedResult {
      val winnersPerRace = input.asSequence().map { computeNumberOfWinners(it) }

      val multiple = winnersPerRace.fold(1L, Long::times)
      return ComputedResult.Simple(multiple)
    }

    private fun computeNumberOfWinners(race: Race): Long {
      var result = 0L

      for (holdTime in 1 until race.time.value) {
        val distanceTravelled = computeDistanceTravelled(TimeMs(holdTime), race.time)
        if (distanceTravelled.value > race.distance.value) {
          result += 1
        }
      }

      return result
    }

    private fun computeDistanceTravelled(holdTime: TimeMs, raceTime: TimeMs): DistanceMM {
      if (holdTime.value >= raceTime.value) {
        return DistanceMM(0)
      }

      val speed = holdTime.value
      val distanceTravelledMM = (raceTime.value - holdTime.value) * speed
      return DistanceMM(distanceTravelledMM)
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
