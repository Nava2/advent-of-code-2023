package net.navatwo.adventofcode2023.day8

import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution

sealed class Day8Solution : Solution<Day8Solution.Input> {
  data object Part1 : Day8Solution() {
    override fun solve(input: Input): ComputedResult {
      val graph = Graph.create(input.nodes)

      val initialNodeName = NodeName("AAA")

      var currentNode = graph.nodes.getValue(initialNodeName)

      val directionsIterator = iterator {
        while (true) {
          yieldAll(input.directions.directions)
        }
      }

      var result = 0L
      while (currentNode.name != NodeName("ZZZ")) {
        val direction = directionsIterator.next()

        currentNode = when (direction) {
          Direction.Left -> currentNode.left
          Direction.Right -> currentNode.right
        }

        result += 1
      }

      return ComputedResult.Simple(result)
    }
  }

  data class Graph private constructor(
    val nodes: Map<NodeName, Node>,
  ) {
    companion object {
      fun create(nodes: Map<NodeName, NodeData>): Graph {
        val graph = mutableMapOf<NodeName, Node>()

        fun getOrCreate(graph: MutableMap<NodeName, Node>, data: NodeData): Node {
          val existing = graph[data.name]
          if (existing != null) return existing

          val newNode = Node(data.name)

          graph[data.name] = newNode

          newNode.left = getOrCreate(graph, nodes.getValue(data.left))
          newNode.right = getOrCreate(graph, nodes.getValue(data.right))

          return newNode
        }

        for (node in nodes.values) {
          getOrCreate(graph, node)
        }

        return Graph(graph)
      }
    }
  }

  class Node(
    val name: NodeName,
  ) {
    lateinit var left: Node

    lateinit var right: Node

    override fun toString(): String {
      return "${name.name} = (${left.name.name}, ${right.name.name})"
    }

    override fun equals(other: Any?): Boolean {
      return this === other || (other is Node && name == other.name)
    }

    override fun hashCode(): Int {
      return name.hashCode()
    }

    companion object {
      private val nil = Node(NodeName("NIL")).apply {
        left = this
        right = this
      }

      fun leaf(name: NodeName) = Node(name).apply {
        left = nil
        right = nil
      }
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

  data class NodeData(val name: NodeName, val left: NodeName, val right: NodeName) {
    fun isLeaf() = name == left && name == right
  }

  enum class Direction {
    Left,
    Right,
  }
}
