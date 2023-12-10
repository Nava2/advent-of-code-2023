package net.navatwo.adventofcode2023.day10

import net.navatwo.adventofcode2023.Coord
import net.navatwo.adventofcode2023.Graph
import net.navatwo.adventofcode2023.Grid
import net.navatwo.adventofcode2023.LongGrid
import net.navatwo.adventofcode2023.coords
import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution

sealed class Day10Solution : Solution<Day10Solution.PipeLayout> {
  data object Part1 : Day10Solution() {
    private const val UNVISITED_VALUE = -1L

    override fun solve(input: PipeLayout): ComputedResult {
      val graph = input.graph
      val tiles = input.tiles

      val startNode = tiles.coords().first { tiles[it] == Tile.Start }

      val solveGrid = LongGrid.maskFor(tiles, UNVISITED_VALUE)

      val queue = ArrayDeque<Coord>()
      queue.add(startNode)

      var maxDistance = 0L

      // BFS: Starting at startNode, visit each neighbour from graph and add it to the queue, storing the
      // distance travelled from start in solveGrid
      while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        val currentDistance = solveGrid[current]
        if (currentDistance != UNVISITED_VALUE) {
          continue
        }

        var newDistance = 0L

        for (neighbour in graph[current]) {
          newDistance = maxOf(solveGrid[neighbour] + 1, newDistance)
          queue.add(neighbour)
        }

        solveGrid[current] = newDistance

        maxDistance = maxOf(maxDistance, newDistance)
      }

      return ComputedResult.Simple(maxDistance)
    }
  }

  override fun parse(lines: Sequence<String>): PipeLayout {
    val tiles = Grid(
      lines.filter { it.isNotBlank() }
        .map { line ->
          line.mapTo(mutableListOf()) { Tile.fromChar(it) }
        }
        .toMutableList()
    )

    val graph = Graph.create(tiles.coords().asIterable()) { coord ->
      tiles[coord].connectedNeighbours(coord)
        .filter { neighbour ->
          val tile = tiles.getOrNull(neighbour) ?: return@filter false
          coord in tile.connectedNeighbours(neighbour)
        }
    }

    return PipeLayout(tiles, graph)
  }

  data class PipeLayout(
    val tiles: Grid<Tile>,
    val graph: Graph<Coord>,
  )

  enum class Tile(val char: Char, private val visualChar: Char) {
    Vertical('|', '|') {
      override fun connectedNeighbours(coord: Coord): List<Coord> {
        return listOf(coord.up(), coord.down())
      }
    },

    Horizontal('-', '─') {
      override fun connectedNeighbours(coord: Coord): List<Coord> {
        return listOf(coord.left(), coord.right())
      }
    },

    NENinety('L', '└') {
      override fun connectedNeighbours(coord: Coord): List<Coord> {
        return listOf(coord.up(), coord.right())
      }
    },

    NWNinety('J', '┘') {
      override fun connectedNeighbours(coord: Coord): List<Coord> {
        return listOf(coord.up(), coord.left())
      }
    },

    SWNinety('7', '┐') {
      override fun connectedNeighbours(coord: Coord): List<Coord> {
        return listOf(coord.down(), coord.left())
      }
    },

    SENinety('F', '┌') {
      override fun connectedNeighbours(coord: Coord): List<Coord> {
        return listOf(coord.down(), coord.right())
      }
    },

    Ground('.', ' ') {
      override fun connectedNeighbours(coord: Coord): List<Coord> = listOf()
    },

    Start('S', 'S') {
      override fun connectedNeighbours(coord: Coord): List<Coord> {
        return listOf(coord.up(), coord.down(), coord.left(), coord.right())
      }
    },
    ;

    abstract fun connectedNeighbours(coord: Coord): List<Coord>

    override fun toString(): String = visualChar.toString()

    companion object {
      private val charToTile = entries.associateBy { it.char }
      fun fromChar(char: Char): Tile {
        return charToTile.getValue(char)
      }
    }
  }
}
