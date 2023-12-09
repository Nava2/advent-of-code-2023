package net.navatwo.adventofcode2023.day8

import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution

sealed class Day8Solution : Solution<Day8Solution.Input> {
  data object Part1 : Day8Solution() {
    override fun solve(input: Input): ComputedResult {
      // TODO Implement
      return ComputedResult.Simple(32L)
    }
  }

  override fun parse(lines: Sequence<String>): Input {
    var directions: Directions? = null

    val nodes = mutableListOf<NodeData>()
    for (line in lines.filter { it.isNotBlank() }) {
      if (directions == null) {
        val directionList = line.map { char ->
          when (char) {
            'L' -> Direction.Left
            'R' -> Direction.Right
            else -> throw IllegalArgumentException("Invalid direction: $char")
          }
        }
        directions = Directions(directionList)
      } else {
        val name = NodeName(line.substring(0..2))

        val leftFirstIndex = "AAA = (".length
        val left = NodeName(line.substring(leftFirstIndex, leftFirstIndex + 3))

        val rightFirstIndex = line.length - "CCC)".length
        val right = NodeName(line.substring(rightFirstIndex, rightFirstIndex + 3))

        nodes += NodeData(name, left, right)
      }
    }

    return Input(directions!!, nodes.associateBy { it.name })
  }

  data class Input(
    val directions: Directions,
    val nodes: Map<NodeName, NodeData>,
  )

  @JvmInline
  value class Directions(val directions: List<Direction>)

  @JvmInline
  value class NodeName(val name: String) {
    init {
      require(name.length == 3)
    }
  }

  data class NodeData(val name: NodeName, val left: NodeName, val right: NodeName)

  enum class Direction {
    Left,
    Right,
  }
}
