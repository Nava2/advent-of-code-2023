package net.navatwo.adventofcode2023.day6

import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution

sealed class Day6Solution : Solution<Day6Solution.Races> {
  data object Part1 : Day6Solution() {
    override fun solve(input: Races): ComputedResult {
      val races = input.times.asSequence()
        .zip(input.records.asSequence()) { time, distance ->
          TimeMs(time.toLong()) to DistanceMM(distance.toLong())
        }
      val winnersPerRace = races.map { (time, record) -> computeNumberOfWinners(time, record) }

      val multiple = winnersPerRace.fold(1L, Long::times)
      return ComputedResult.Simple(multiple)
    }
  }

  data object Part2 : Day6Solution() {
    override fun solve(input: Races): ComputedResult {
      val time = input.times.fold(StringBuilder(), StringBuilder::append).toString().toLong()
      val record = input.records.fold(StringBuilder(), StringBuilder::append).toString().toLong()

      val result = computeNumberOfWinners(TimeMs(time), DistanceMM(record))
      return ComputedResult.Simple(result)
    }
  }

  protected fun computeNumberOfWinners(time: TimeMs, record: DistanceMM): Long {
    var result = 0L

    for (holdTime in 1 until time.value) {
      val distanceTravelled = computeDistanceTravelled(TimeMs(holdTime), time)
      if (distanceTravelled.value > record.value) {
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

  override fun parse(lines: Sequence<String>): Races {
    val (times, distances) = lines
      .map { line ->
        line.substringAfter(':')
          .splitToSequence(' ')
          .filter { it.isNotBlank() }
          .map { it.trim() }
          .toList()
      }
      .toList()

    return Races(times, distances)
  }

  @JvmInline
  value class TimeMs(val value: Long)

  @JvmInline
  value class DistanceMM(val value: Long)

  data class Races(val times: List<String>, val records: List<String>) {
    init {
      check(times.size == records.size)
    }
  }
}
