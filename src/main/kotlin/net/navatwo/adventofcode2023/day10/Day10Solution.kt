package net.navatwo.adventofcode2023.day10

import net.navatwo.adventofcode2023.BooleanGrid
import net.navatwo.adventofcode2023.Coord
import net.navatwo.adventofcode2023.Graph
import net.navatwo.adventofcode2023.Grid
import net.navatwo.adventofcode2023.LongGrid
import net.navatwo.adventofcode2023.columnIndices
import net.navatwo.adventofcode2023.coords
import net.navatwo.adventofcode2023.forEachCoord
import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution
import net.navatwo.adventofcode2023.rowIndices

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
      graph.bfs(startNode) { current ->
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

  data object Part2 : Day10Solution() {
    override fun solve(input: PipeLayout): ComputedResult {
      val tiles = input.tiles

      val startNode = tiles.coords().first { tiles[it] == Tile.Start }

      val grownLayout = growGrid(input, startNode)

      val grownTiles = grownLayout.tiles

      val exteriorTiles = floodFillExteriorOfGrid(
        tiles = grownTiles,
        graph = grownLayout.graph,
        startNode = Coord(startNode.x * 2 + 1, startNode.y * 2 + 1),
      )

      var interiorTileCount = 0L
      exteriorTiles.forEachCoord { x, y ->
        if (x % 2 == 1 && y % 2 == 1) {
          if (!exteriorTiles[x, y]) {
            interiorTileCount += 1
          }
        }
      }

      return ComputedResult.Simple(interiorTileCount)
    }

    private fun floodFillExteriorOfGrid(
      tiles: Grid<Tile>,
      graph: Graph<Coord>,
      startNode: Coord,
    ): BooleanGrid {
      val exteriorTiles = BooleanGrid.maskFor(tiles, false)
      graph.bfs(startNode) { current ->
        exteriorTiles[current] = true
      }

      // flood fill each tile marking exteriorTiles[x, y] = true if it is an exterior tile
      fun floodFill(coord: Coord) {
        // short circuit to avoid setting up the queue
        if (exteriorTiles[coord]) return

        val queue = ArrayDeque<Coord>()
        queue.add(coord)

        val visited = mutableSetOf<Coord>()

        while (queue.isNotEmpty()) {
          val current = queue.removeFirst()
          if (!visited.add(current) || exteriorTiles[current]) continue

          exteriorTiles[current] = true

          current.forEachNeighbour { x, y ->
            val neighbour = Coord(x, y)
            if (neighbour in exteriorTiles) {
              queue.add(Coord(x, y))
            }
          }
        }
      }

      for (x in tiles.columnIndices) {
        floodFill(Coord(x, 0))
        floodFill(Coord(x, tiles.rowCount - 1))
      }

      for (y in tiles.rowIndices) {
        floodFill(Coord(0, y))
        floodFill(Coord(tiles.columnCount - 1, y))
      }

      return exteriorTiles
    }

    internal fun growGrid(layout: PipeLayout, start: Coord): PipeLayout {
      val pathCoords = mutableSetOf<Coord>()

      layout.graph.bfs(start, pathCoords::add)

      val newTiles = layout.tiles.growGridIncluding(pathCoords)

      newTiles.fillInPathGaps(pathCoords)

      val newGraph = newTiles.generateGraph()
      return PipeLayout(newTiles, newGraph)
    }

    private fun Grid<Tile>.fillInPathGaps(
      path: Set<Coord>,
    ) {
      for (pathCoord in path) {
        val newCoord = Coord(x = pathCoord.x * 2 + 1, y = pathCoord.y * 2 + 1)
        val tile = get(newCoord)

        val connectedNeighbours = tile.getMappedNeighbours(path, newCoord, pathCoord)

        for (neighbour in connectedNeighbours) {
          when {
            newCoord.y == neighbour.y -> {
              set(neighbour, Tile.Horizontal)
            }

            newCoord.x == neighbour.x -> {
              set(neighbour, Tile.Vertical)
            }
          }
        }
      }
    }

    private fun Tile.getMappedNeighbours(
      path: Set<Coord>,
      newCoord: Coord,
      pathCoord: Coord,
    ): Sequence<Coord> = if (this == Tile.Start) {
      // The start condition has connections to _every_ tile its around, it means it can
      // try to connect when we grow it, so special case it.
      connectedNeighbours(newCoord)
        .filter { new ->
          when {
            new == newCoord.up() && pathCoord.up() in path -> true
            new == newCoord.down() && pathCoord.down() in path -> true
            new == newCoord.left() && pathCoord.left() in path -> true
            new == newCoord.right() && pathCoord.right() in path -> true
            else -> false
          }
        }
    } else {
      connectedNeighbours(newCoord)
    }

    private fun Grid<Tile>.growGridIncluding(
      pathCoords: Set<Coord>
    ) = Grid.create(rowCount * 2 + 1, columnCount * 2 + 1) { x, y ->
      if (x % 2 != 1 || y % 2 != 1) {
        Tile.Ground
      } else {
        val originalCoord = Coord(x / 2, y / 2)
        if (originalCoord in pathCoords) {
          get(originalCoord)
        } else {
          Tile.Ground
        }
      }
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

    val graph = tiles.generateGraph()

    return PipeLayout(tiles, graph)
  }

  data class PipeLayout(
    val tiles: Grid<Tile>,
    val graph: Graph<Coord>,
  )

  enum class Tile(val char: Char, private val visualChar: Char) {
    Vertical('|', '|') {
      override fun connectedNeighbours(coord: Coord): Sequence<Coord> {
        return sequenceOf(coord.up(), coord.down())
      }
    },

    Horizontal('-', '─') {
      override fun connectedNeighbours(coord: Coord): Sequence<Coord> {
        return sequenceOf(coord.left(), coord.right())
      }
    },

    NENinety('L', '└') {
      override fun connectedNeighbours(coord: Coord): Sequence<Coord> {
        return sequenceOf(coord.up(), coord.right())
      }
    },

    NWNinety('J', '┘') {
      override fun connectedNeighbours(coord: Coord): Sequence<Coord> {
        return sequenceOf(coord.up(), coord.left())
      }
    },

    SWNinety('7', '┐') {
      override fun connectedNeighbours(coord: Coord): Sequence<Coord> {
        return sequenceOf(coord.down(), coord.left())
      }
    },

    SENinety('F', '┌') {
      override fun connectedNeighbours(coord: Coord): Sequence<Coord> {
        return sequenceOf(coord.down(), coord.right())
      }
    },

    Ground('.', '.') {
      override fun connectedNeighbours(coord: Coord): Sequence<Coord> = sequenceOf()
    },

    Start('S', 'S') {
      override fun connectedNeighbours(coord: Coord): Sequence<Coord> {
        return sequenceOf(coord.up(), coord.down(), coord.left(), coord.right())
      }
    },
    ;

    abstract fun connectedNeighbours(coord: Coord): Sequence<Coord>

    fun disconnectedNeighbours(coord: Coord): List<Coord> {
      val connectedNeighbours = connectedNeighbours(coord)
      return listOf(coord.up(), coord.left(), coord.right(), coord.down())
        .filterNot { it in connectedNeighbours }
    }

    override fun toString(): String = visualChar.toString()

    companion object {
      private val charToTile = entries.associateBy { it.char }
      fun fromChar(char: Char): Tile {
        return charToTile.getValue(char)
      }
    }
  }
}

private fun Grid<Day10Solution.Tile>.generateGraph(): Graph<Coord> {
  return Graph.create(coords().asIterable()) { coord ->
    get(coord).connectedNeighbours(coord)
      .filter { neighbour ->
        val tile = getOrNull(neighbour) ?: return@filter false
        coord in tile.connectedNeighbours(neighbour)
      }
      .toList()
  }
}
