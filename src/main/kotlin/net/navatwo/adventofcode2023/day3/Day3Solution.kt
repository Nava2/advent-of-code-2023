package net.navatwo.adventofcode2023.day3

import net.navatwo.adventofcode2023.Coord
import net.navatwo.adventofcode2023.Grid
import net.navatwo.adventofcode2023.coords
import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution
import net.navatwo.adventofcode2023.get
import net.navatwo.adventofcode2023.getOrNull

sealed class Day3Solution : Solution<Day3Solution.EngineSchematic> {
  abstract class SolveContext<DP_COORD : Any>(
    private val schematic: EngineSchematic,
    private val uninitialized: DP_COORD,
    private val symbol: DP_COORD,
    private val empty: DP_COORD,
  ) {

    // Lazy due to needing uninitialized, etc.
    protected val dp: Grid<DP_COORD> by lazy(LazyThreadSafetyMode.NONE) {
      Grid(
        grid = schematic.grid.mapTo(ArrayList(schematic.grid.size)) { row ->
          row.mapTo(ArrayList(row.size)) { uninitialized }
        },
      )
    }


    fun solve(): Long {
      var result = 0L

      for (coord in schematic.grid.coords()) {
        result += visitCoord(coord)
      }

      return result
    }

    protected abstract fun visitCoord(
      coord: Coord,
    ): Long

    protected abstract fun partNumber(
      value: Int,
      firstCoord: Coord,
      lastCoord: Coord,
    ): DP_COORD

    protected open fun initializeGear(coord: Coord): DP_COORD = symbol

    protected fun initialize(coord: Coord): DP_COORD {
      val char = schematic.grid[coord].char
      when {
        char == '.' -> dp[coord] = empty
        char.isDigit() -> {
          computePartNumberForCoord(coord)
        }

        char == '*' -> dp[coord] = initializeGear(coord)
        // symbol
        else -> {
          dp[coord] = symbol
        }
      }

      return dp[coord]
    }

    protected fun initializeNeighbours(coord: Coord) {
      coord.forEachNeighbour { x, y ->
        if (dp[x, y] is Part2.DPCoord.Uninitialized) {
          val neighbourCoord = Coord(x, y)
          initialize(neighbourCoord)
        }
      }
    }

    private fun computePartNumberForCoord(
      coord: Coord,
    ) {
      val searchCoordY = coord.y
      var searchCoordX = coord.x - 1

      while (true) {
        val potentialPartNumber = schematic.getOrNull(searchCoordX, searchCoordY)
        if (potentialPartNumber == null || !potentialPartNumber.char.isDigit()) {
          searchCoordX += 1
          break
        }

        searchCoordX -= 1
      }

      val firstPartNumberCoord = Coord(searchCoordX, searchCoordY)

      var partNumber = 0
      while (true) {
        val potentialPartNumber = schematic.getOrNull(searchCoordX, searchCoordY)
        if (potentialPartNumber == null || !potentialPartNumber.char.isDigit()) {
          break
        }

        partNumber = partNumber * 10 + potentialPartNumber.char.digitToInt()
        searchCoordX += 1
      }

      val lastPartNumberCoord = Coord(searchCoordX - 1, searchCoordY)

      for (x in firstPartNumberCoord.x..lastPartNumberCoord.x) {
        dp[x, searchCoordY] = partNumber(partNumber, firstPartNumberCoord, lastPartNumberCoord)
      }
    }
  }

  data object Part1 : Day3Solution() {
    override fun solve(input: EngineSchematic): ComputedResult {
      val solveContext = Part1SolveContext(input)

      return ComputedResult.Simple(solveContext.solve())
    }

    sealed interface DPCoord {
      data object Uninitialized : DPCoord
      data object Empty : DPCoord
      data class PartNumber(val partNumber: Int, val firstCoord: Coord, val lastCoord: Coord) : DPCoord
      data object Symbol : DPCoord
    }

    private class Part1SolveContext(schematic: EngineSchematic) : SolveContext<DPCoord>(
      schematic = schematic,
      uninitialized = DPCoord.Uninitialized,
      empty = DPCoord.Empty,
      symbol = DPCoord.Symbol,
    ) {
      override fun partNumber(value: Int, firstCoord: Coord, lastCoord: Coord): DPCoord {
        return DPCoord.PartNumber(value, firstCoord, lastCoord)
      }

      override fun visitCoord(
        coord: Coord,
      ): Long {
        return when (dp[coord]) {
          DPCoord.Uninitialized -> {
            val initialized = initialize(coord)
            if (initialized is DPCoord.Symbol) {
              sumSymbolPartNumbers(coord)
            } else {
              0L
            }
          }

          DPCoord.Symbol -> {
            sumSymbolPartNumbers(coord)
          }

          DPCoord.Empty, is DPCoord.PartNumber -> 0L
        }
      }

      private fun sumSymbolPartNumbers(coord: Coord): Long {
        initializeNeighbours(coord)

        var result = 0L

        val visitedCoords = mutableSetOf<Coord>()
        coord.forEachNeighbour { x, y ->
          val neighbourCoord = Coord(x, y)
          // avoid double adds
          if (!visitedCoords.add(neighbourCoord)) {
            return@forEachNeighbour
          }

          val dpCoord = dp[neighbourCoord]
          if (dpCoord is DPCoord.PartNumber) {
            result += dpCoord.partNumber.toLong()

            // avoid adding the same part number multiple times
            for (cx in dpCoord.firstCoord.x..dpCoord.lastCoord.x) {
              visitedCoords.add(neighbourCoord.copy(x = cx))
            }
          }
        }

        return result
      }
    }
  }

  data object Part2 : Day3Solution() {
    override fun solve(input: EngineSchematic): ComputedResult {
      val solveContext = Part2SolveContext(input)

      return ComputedResult.Simple(solveContext.solve())
    }

    sealed interface DPCoord {

      data object Uninitialized : DPCoord
      data object Empty : DPCoord
      data class PartNumber(val partNumber: Int, val firstCoord: Coord, val lastCoord: Coord) : DPCoord
      data object Symbol : DPCoord
      data object Gear : DPCoord
    }

    private class Part2SolveContext(schematic: EngineSchematic) : SolveContext<DPCoord>(
      schematic = schematic,
      uninitialized = DPCoord.Uninitialized,
      empty = DPCoord.Empty,
      symbol = DPCoord.Symbol,
    ) {

      override fun partNumber(value: Int, firstCoord: Coord, lastCoord: Coord): DPCoord {
        return DPCoord.PartNumber(value, firstCoord, lastCoord)
      }

      override fun initializeGear(coord: Coord): DPCoord {
        initializeNeighbours(coord)

        var partNumberCount = 0
        val visitedCoords = mutableSetOf<Coord>()
        coord.forEachNeighbour { x, y ->
          val neighbourCoord = Coord(x, y)
          if (!visitedCoords.add(neighbourCoord)) return@forEachNeighbour

          val partNumber = dp[x, y]
          if (partNumber is DPCoord.PartNumber) {
            partNumberCount += 1

            // avoid adding the same part number multiple times
            for (cx in partNumber.firstCoord.x..partNumber.lastCoord.x) {
              visitedCoords.add(neighbourCoord.copy(x = cx))
            }
          }
        }

        return if (partNumberCount == 2) {
          DPCoord.Gear
        } else {
          DPCoord.Symbol
        }
      }

      override fun visitCoord(coord: Coord): Long {
        return when (val dpCoord = dp[coord]) {
          DPCoord.Uninitialized -> {
            val initialized = initialize(coord)
            if (initialized is DPCoord.Gear) {
              computeGearRatio(initialized, coord)
            } else {
              0L
            }
          }

          is DPCoord.Gear -> {
            computeGearRatio(dpCoord, coord)
          }

          else -> 0L
        }
      }

      private fun computeGearRatio(
        @Suppress("UNUSED_PARAMETER") // witness
        gear: DPCoord.Gear,
        coord: Coord,
      ): Long {
        var result = 1L

        val visitedCoords = mutableSetOf<Coord>()
        coord.forEachNeighbour { x, y ->
          val neighbourCoord = Coord(x, y)
          // avoid double adds
          if (!visitedCoords.add(neighbourCoord)) {
            return@forEachNeighbour
          }

          val dpCoord = dp[neighbourCoord]
          if (dpCoord is DPCoord.PartNumber) {
            result *= dpCoord.partNumber.toLong()

            // avoid adding the same part number multiple times
            for (cx in dpCoord.firstCoord.x..dpCoord.lastCoord.x) {
              visitedCoords.add(neighbourCoord.copy(x = cx))
            }
          }
        }

        return result
      }
    }
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
    fun getOrNull(x: Int, y: Int): Value? = grid.getOrNull(y)?.getOrNull(x)

    @JvmInline
    value class Value(val char: Char)
  }
}
