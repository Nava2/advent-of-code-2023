package net.navatwo.adventofcode2023.day5

import com.github.nava2.interval_tree.Interval
import com.github.nava2.interval_tree.IntervalTree
import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution
import java.util.Objects

sealed class Day5Solution : Solution<Day5Solution.Almanac> {
  data object Part1 : Day5Solution() {
    override fun solve(input: Almanac): ComputedResult {
      val result = input.seeds.minOf { seed ->
        input.computeSeedLocation(seed)
      }

      return ComputedResult.Simple(result)
    }

    private fun Almanac.computeSeedLocation(seed: Almanac.Seed): Long {
      var value = seed.id
      var mapping: Almanac.Mapping? = mappingsBySourceType.getValue(Almanac.Type.Seed)
      while (mapping != null) {
        value = mapping.map(value)
        mapping = mappingsBySourceType[mapping.destType]
      }

      return value
    }
  }

  data object Part2 : Day5Solution() {
    override fun solve(input: Almanac): ComputedResult {
      val seeds = (0 until input.seeds.size step 2)
        .asSequence()
        .map { i ->
          input.seeds[i] to input.seeds[i + 1].id
        }
        .map { (seed, range) ->
          Almanac.Entry(
            sourceRange = seed.id until (seed.id + range),
            destRange = seed.id until (seed.id + range),
          )
        }

      val result = seeds.minOf { seed ->
        input.computeMinimumSeedLocation(seed)
      }

      return ComputedResult.Simple(result)
    }

    private fun Almanac.computeMinimumSeedLocation(interval: Almanac.Entry): Long {
      var mapping: Almanac.Mapping? = mappingsBySourceType.getValue(Almanac.Type.Seed)

      var intervals = listOf<Interval>(interval)
      while (mapping != null) {
        intervals = computeNextIntervals(mapping, intervals)
        mapping = mappingsBySourceType[mapping.destType]
      }

      return intervals.minOf { it.start }
    }

    private fun computeNextIntervals(mapping: Almanac.Mapping, intervals: List<Interval>): List<Interval> {
      val next = intervals.flatMap { mapping.mapRange(it) }
      check(next.sumOf { it.length } == intervals.sumOf { it.length }) {
        "wat: input: $intervals, next: $next"
      }
      return next
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
              .map { it.trim().toLong() }
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
                sourceRange = sourceStart until (sourceStart + length),
                destRange = destStart until (destStart + length),
              )
            )

            nextMapping
          }
        }
      }
    }

    return Almanac(seeds = seeds, mappingsBySourceType = mappingsBySourceType)
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

      fun mapRange(interval: Interval): Sequence<Interval> {
        val overlappers = intervalTree.overlappers(interval)
        return sequence {
          var value = interval.start
          for (overlapper in overlappers) {
            if (overlapper.start > value) {
              yield(Interval.Simple(value, overlapper.start - value))
            }

            val truncated = overlapper.intersect(interval)
              ?: error("wat")

            val destOffset = overlapper.destRange.first - overlapper.sourceRange.first
            val destStart = truncated.start + destOffset
            yield(
              Interval.Simple(destStart, truncated.length)
            )

            value = truncated.endExclusive
          }

          if (value != interval.endExclusive) {
            yield(Interval.Simple(value, interval.endExclusive - value))
          }
        }
      }

      override fun toString(): String {
        return "Mapping(sourceType=$sourceType, destType=$destType, intervalTree=${intervalTree.toList()})"
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
    value class Seed(val id: Long)
  }
}

fun Interval.intersect(other: Interval): Interval? {
  if (start > other.endInclusive || endInclusive < other.start) {
    return null
  }

  val truncatedStart = maxOf(start, other.start)
  val truncatedEndExclusive = minOf(endExclusive, other.endExclusive)
  val truncatedLength = truncatedEndExclusive - truncatedStart
  return Interval.Simple(truncatedStart, truncatedLength)
}
