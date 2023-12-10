package net.navatwo.adventofcode2023.day10

import net.navatwo.adventofcode2023.Grid
import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution

sealed class Day10Solution : Solution<Grid<Day10Solution.Tile>> {
  data object Part1 : Day10Solution() {
    override fun solve(input: Grid<Tile>): ComputedResult {
      // TODO Implement
      return ComputedResult.Simple(32L)
    }
  }

  override fun parse(lines: Sequence<String>): Grid<Tile> {
    val tiles = lines.filter { it.isNotBlank() }
      .map { line ->
        line.mapTo(mutableListOf()) { Tile.fromChar(it) }
      }
      .toMutableList()

    return Grid(tiles)
  }

  @JvmInline
  value class Input(val value: String)

  enum class Tile(val char: Char) {
    Vertical('|'),
    Horizontal('-'),
    NENinety('L'),
    NWNinety('J'),
    SWNinety('7'),
    SENinety('F'),
    Ground('.'),
    Start('S'),
    ;

    companion object {
      private val charToTile = entries.associateBy { it.char }
      fun fromChar(char: Char): Tile {
        return charToTile.getValue(char)
      }
    }
  }
}
