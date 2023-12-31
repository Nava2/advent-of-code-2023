package net.navatwo.adventofcode2023.day6

import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution
import kotlin.math.ceil
import kotlin.math.floor

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
    // r = record
    // d = distance past record
    // t = max time
    // h = hold time
    //
    // (r + d) = (t - h) * h
    // (r + d) = -h^2 + t * h
    //
    // solving this equation for d = 0, then using the spaces between the roots computes the number
    // of values that are winners.
    // d = -h^2 + t * h - r
    val (first, last) = findRoots(-1, time.value, -record.value)

    // need to round first *down* and last *up* to get the correct value as the result could
    // be a decimal value for each root
    return floor(last).toLong() - ceil(first).toLong() + 1
  }

  /**
   * Find the roots of the quadratic equation.
   */
  private fun findRoots(a: Long, b: Long, c: Long): Pair<Double, Double> {
    val sqrt = Math.sqrt((b * b - 4 * a * c).toDouble())
    val root1 = (-b + sqrt) / (2 * a)
    val root2 = (-b - sqrt) / (2 * a)
    return minOf(root1, root2) to maxOf(root1, root2)
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
