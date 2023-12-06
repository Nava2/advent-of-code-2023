package net.navatwo.adventofcode2023.day5

import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution

sealed class Day5Solution : Solution<Day5Solution.Almanac> {
  data object Part1 : Day5Solution() {
    override fun solve(input: Almanac): ComputedResult {
      TODO()
    }
  }

  override fun parse(lines: Sequence<String>): Almanac {
    val seeds = mutableListOf<Almanac.Seed>()
    val entriesBySourceType = mutableMapOf<Almanac.Type, MutableList<Almanac.Entry>>()
    var currentPair: Pair<Almanac.Type, Almanac.Type>? = null
    for (line in lines.filter { it.isNotBlank() }) {
      when {
        seeds.isEmpty() -> {
          val seedValues = line.subSequence("seeds: ".length, line.length)
            .splitToSequence(' ')
          seeds.addAll(
            seedValues
              .map { it.trim().toInt() }
              .map { Almanac.Seed(it) },
          )
        }

        currentPair == null || !line.first().isDigit() -> {
          val (sourceTypeString, destTypeString) = line.subSequence(0, line.length - " map:".length)
            .split("-to-", limit = 2)
          val sourceType = Almanac.Type.lowerValueOf(sourceTypeString)
          val destType = Almanac.Type.lowerValueOf(destTypeString)
          currentPair = sourceType to destType
        }

        else -> {
          val (sourceType, destType) = currentPair
          val (destStart, sourceStart, length) = line.split(' ', limit = 3).map { it.toInt() }
          entriesBySourceType.compute(sourceType) { _, entries ->
            val entry = Almanac.Entry(
              sourceType = sourceType,
              sourceRange = sourceStart..(sourceStart + length),
              destType = destType,
              destRange = destStart..(destStart + length),
            )

            if (entries == null) {
              mutableListOf(entry)
            } else {
              entries.add(entry)
              entries
            }
          }
        }
      }
    }

    return Almanac(seeds = seeds, entriesBySourceType = entriesBySourceType)
  }

  private fun parseInts(line: String): Set<Int> {
    return line.splitToSequence(' ')
      .filter { it.isNotBlank() }
      .map { it.toInt() }
      .toSet()
  }

  data class Almanac(
    val seeds: List<Seed>,
    val entriesBySourceType: Map<Type, List<Entry>>,
  ) {
    data class Entry(
      val sourceType: Type,
      val sourceRange: IntRange,
      val destType: Type,
      val destRange: IntRange,
    )

    enum class Type {
      Seed,
      Soil,
      Fertilizer,
      Water,
      Light,
      Temperature,
      Humidity,
      Location,
      ;

      companion object {
        private val lowered = entries.associateBy { it.name.lowercase() }

        fun lowerValueOf(name: String): Type {
          return lowered.getValue(name)
        }
      }
    }

    @JvmInline
    value class Seed(val id: Int)
  }
}
