package net.navatwo.adventofcode2023.day3

import net.navatwo.adventofcode2023.Coord
import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution

sealed class Day3Solution : Solution<Day3Solution.EngineSchematic> {
  data object Part1 : Day3Solution() {
    override fun solve(input: EngineSchematic): ComputedResult {
      TODO()
    }
  }

  override fun parse(lines: Sequence<String>): EngineSchematic {
    val grid = lines.withIndex()
      .map { (y, line) ->
        line.asSequence()
          .withIndex()
          .map { (x, char) ->
            EngineCoord(Coord(x, y), char)
          }
          .toList()
      }
      .toList()

    return EngineSchematic(grid)
  }

  data class EngineSchematic(
    val grid: List<List<EngineCoord>>
  )

  data class EngineCoord(val coord: Coord, val value: Char)
}
