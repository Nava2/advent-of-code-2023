package net.navatwo.adventofcode2023.day5

import com.github.nava2.interval_tree.Interval
import com.github.nava2.interval_tree.IntervalTree
import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution
import java.util.Objects

sealed class Day5Solution : Solution<Day5Solution.Almanac> {
  data object Part1 : Day5Solution() {
    override fun solve(input: Almanac): ComputedResult {
      TODO()
    }
  }

  override fun parse(lines: Sequence<String>): Almanac {
    val seeds = mutableListOf<Almanac.Seed>()
    val mappingsBySourceType = mutableMapOf<Almanac.Type, Almanac.Mapping>()
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
          val (destStart, sourceStart, length) = line.split(' ', limit = 3).map { it.toLong() }
          mappingsBySourceType.compute(sourceType) { _, mapping ->
            val nextMapping = mapping ?: Almanac.Mapping(sourceType, destType)

            nextMapping.addEntry(
              Almanac.Entry(
                sourceRange = sourceStart..(sourceStart + length),
                destRange = destStart..(destStart + length),
              )
            )

            nextMapping
          }
        }
      }
    }

    return Almanac(seeds = seeds, mappingsBySourceType = mappingsBySourceType)
  }

  private fun parseInts(line: String): Set<Int> {
    return line.splitToSequence(' ')
      .filter { it.isNotBlank() }
      .map { it.toInt() }
      .toSet()
  }

  data class Almanac(
    val seeds: List<Seed>,
    val mappingsBySourceType: Map<Type, Mapping>,
  ) {
    class Mapping(
      val sourceType: Type,
      val destType: Type,
      entries: Collection<Entry> = listOf(),
    ) {
      private val intervalTree = IntervalTree<Entry>().apply {
        insertAll(entries)
      }

      fun addEntry(entry: Entry) {
        intervalTree.insert(entry)
      }

      fun map(value: Long): Long {
        val overlapper = intervalTree.minimumOverlapper(Interval.Simple(value, 1))
        return overlapper.map { it.map(value) }.orElse(value)
      }

      override fun hashCode(): Int {
        return Objects.hash(
          sourceType,
          destType,
          intervalTree.toSet(),
        )
      }

      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Mapping) return false

        return sourceType == other.sourceType &&
          destType == other.destType &&
          intervalTree.toSet() == other.intervalTree.toSet()
      }
    }

    data class Entry(
      val sourceRange: LongRange,
      val destRange: LongRange,
    ) : Interval {
      override val start: Long = sourceRange.first
      override val endExclusive: Long = sourceRange.endExclusive

      fun map(value: Long): Long {
        return destRange.first + (value - sourceRange.first)
      }
    }

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
