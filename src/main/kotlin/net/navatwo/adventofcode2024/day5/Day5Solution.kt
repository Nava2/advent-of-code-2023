package net.navatwo.adventofcode2024.day5

import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution
import net.navatwo.adventofcode2024.day5.Day5Solution.Part2.computeOrderingMap
import java.util.stream.Collectors.toSet

sealed class Day5Solution : Solution<Day5Solution.Input> {
  data object Part1 : Day5Solution() {
    override fun solve(input: Input): ComputedResult {
      val pageOrdering = input.computeOrderingMap()

      // pageOrdering[pageN] = set of pages that must come before pageN
      val orderedUpdates = input.manualUpdates.asSequence()
        .filter { update ->
          update.pageUpdates.asSequence().withIndex().all { (idx, pageN) ->
            val predecessors = pageOrdering.getOrElse(pageN) { setOf() }
            predecessors.containsAll(update.pageUpdates.subList(0, idx))
          }
        }
        .map { update ->
          update.pageUpdates[update.pageUpdates.size / 2]
        }

      return ComputedResult.Simple(orderedUpdates.sum())
    }
  }

  data object Part2 : Day5Solution() {
    override fun solve(input: Input): ComputedResult {
      val pageOrdering = input.computeOrderingMap()

      val orderedUpdates = input.manualUpdates.asSequence()
        .map { update ->
          val allowedPages = update.pageUpdates.toSet()

          val previousPages = mutableMapOf<Long, Set<Long>>()
          fun computePrevious(pageN: Long): Set<Long> {
            val existing = previousPages[pageN]
            if (existing != null) return existing

            val predecessors = pageOrdering[pageN]?.intersect(allowedPages)
            if (predecessors == null) {
              previousPages[pageN] = setOf()
              return setOf()
            }

            return (predecessors + predecessors.flatMap { computePrevious(it) })
              .also { previousPages[pageN] = it }
          }

          val ordered = update.pageUpdates.sortedWith { a, b ->
            if (a == b) {
              0
            } else if (b in computePrevious(a)) {
              1
            } else {
              -1
            }
          }

          if (ordered == update.pageUpdates) {
            0
          } else {
            ordered[ordered.size / 2]
          }
        }

      // pageOrdering[pageN] = set of pages that must come before pageN


      return ComputedResult.Simple(orderedUpdates.sum())
    }
  }

  protected fun Input.computeOrderingMap(): Map<Long, Set<Long>> {
    return orderRules.fold(mutableMapOf<Long, MutableSet<Long>>()) { acc, ordering ->
      acc.computeIfAbsent(ordering.pageY) { mutableSetOf() }.add(ordering.pageX)
      acc
    }
      .mapValues { (_, v) -> v.toSortedSet() }
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
