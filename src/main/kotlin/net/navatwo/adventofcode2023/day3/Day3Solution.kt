package net.navatwo.adventofcode2023.day3

import net.navatwo.adventofcode2023.Coord
import net.navatwo.adventofcode2023.coords
import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution
import net.navatwo.adventofcode2023.get
import net.navatwo.adventofcode2023.getOrNull
import net.navatwo.adventofcode2023.set

sealed class Day3Solution : Solution<Day3Solution.EngineSchematic> {
  data object Part1 : Day3Solution() {
    override fun solve(input: EngineSchematic): ComputedResult {
      val dp = Grid(
        grid = input.grid.mapTo(ArrayList(input.grid.size)) { row ->
          row.mapTo(ArrayList(row.size)) { DPCoord.Uninitialized }
        },
      )

      var result = 0L
      for (coord in input.grid.coords()) {
        when (dp[coord]) {
          DPCoord.Uninitialized -> {
            val initialized = initialize(input, dp, coord)
            if (initialized is DPCoord.Symbol) {
              result += sumSymbolPartNumbers(input, dp, coord)
            }
          }
          DPCoord.Symbol -> {
            result += sumSymbolPartNumbers(input, dp, coord)
          }
          DPCoord.Empty -> Unit
          is DPCoord.PartNumber -> Unit
        }
      }

      return ComputedResult.Simple(result)
    }

    private fun initialize(schematic: EngineSchematic, dp: Grid, coord: Coord): DPCoord {
      val char = schematic.grid[coord].char
      when {
        char == '.' -> dp[coord] = DPCoord.Empty
        char.isDigit() -> {
          computePartNumberForCoord(schematic, dp, coord)
        }
        // symbol
        else -> {
          dp[coord] = DPCoord.Symbol
        }
      }

      return dp[coord]
    }

    private fun computePartNumberForCoord(
      schematic: EngineSchematic,
      dp: Grid,
      coord: Coord,
    ) {
      var searchCoord = coord.copy(x = coord.x - 1)

      while (true) {
        val potentialPartNumber = schematic.getOrNull(searchCoord)
        if (potentialPartNumber == null || !potentialPartNumber.char.isDigit()) {
          searchCoord = searchCoord.copy(x = searchCoord.x + 1)
          break
        }

        searchCoord = searchCoord.copy(x = searchCoord.x - 1)
      }

      val firstPartNumberCoord = searchCoord

      var partNumber = 0
      while (true) {
        val potentialPartNumber = schematic.getOrNull(searchCoord)
        if (potentialPartNumber == null || !potentialPartNumber.char.isDigit()) {
          break
        }

        partNumber = partNumber * 10 + potentialPartNumber.char.digitToInt()
        searchCoord = searchCoord.copy(x = searchCoord.x + 1)
      }

      val lastPartNumberCoord = searchCoord.copy(x = searchCoord.x - 1)

      for (x in firstPartNumberCoord.x..lastPartNumberCoord.x) {
        val dpCoord = coord.copy(x = x)
        dp[dpCoord] = DPCoord.PartNumber(partNumber, firstPartNumberCoord, lastPartNumberCoord)
      }
    }

    private fun sumSymbolPartNumbers(schematic: EngineSchematic, dp: Grid, coord: Coord): Long {
      var result = 0L

      val visitedCoords = mutableSetOf<Coord>()
      for (neighbourCoord in coord.neighbours()) {
        // avoid double adds
        if (!visitedCoords.add(neighbourCoord)) {
          continue
        }

        val dpCoord = dp[neighbourCoord]
        val initializedNeighbour = if (dpCoord is DPCoord.Uninitialized) {
          initialize(schematic, dp, neighbourCoord)
        } else {
          dpCoord
        }

        if (initializedNeighbour is DPCoord.PartNumber) {
          result += initializedNeighbour.partNumber.toLong()

          // avoid adding the same part number multiple times
          for (x in initializedNeighbour.firstCoord.x..initializedNeighbour.lastCoord.x) {
            visitedCoords.add(neighbourCoord.copy(x = x))
          }
        }
      }

      return result
    }

    @JvmInline
    value class Grid(private val grid: MutableList<MutableList<DPCoord>>) {
      operator fun get(coord: Coord): DPCoord = grid[coord]
      operator fun set(coord: Coord, value: DPCoord) {
        grid[coord] = value
      }
    }
  }

  sealed interface DPCoord {

    data object Uninitialized : DPCoord
    data object Empty : DPCoord
    data class PartNumber(val partNumber: Int, val firstCoord: Coord, val lastCoord: Coord) : DPCoord
    data object Symbol : DPCoord
  }

  override fun parse(lines: Sequence<String>): EngineSchematic {
    val grid = lines
      .map { line ->
        line.map { EngineSchematic.Value(it) }
      }
      .toList()

    return EngineSchematic(grid)
  }

  @JvmInline
  value class EngineSchematic(
    val grid: List<List<Value>>
  ) {
    operator fun get(coord: Coord): Value = grid[coord]
    fun getOrNull(coord: Coord): Value? = grid.getOrNull(coord)

    @JvmInline
    value class Value(val char: Char)
  }
}
