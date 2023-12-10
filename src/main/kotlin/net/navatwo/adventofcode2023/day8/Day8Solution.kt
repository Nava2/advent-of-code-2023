package net.navatwo.adventofcode2023.day8

import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution

sealed class Day8Solution : Solution<Day8Solution.Input> {
  data object Part1 : Day8Solution() {
    override fun solve(input: Input): ComputedResult {
      val tree = Tree.create(input.nodes)

      val initialNodeName = NodeName("AAA")

      val result = computeDistanceToEnd(tree, initialNodeName, input) { it == NodeName("ZZZ") }

      return ComputedResult.Simple(result)
    }
  }

  protected fun computeDistanceToEnd(
    tree: Tree,
    initialNodeName: NodeName,
    input: Input,
    isEndNode: (NodeName) -> Boolean,
  ): Long {
    var currentNode = tree[initialNodeName]

    val directionsIterator = input.directionsIterator()

    var result = 0L
    while (!isEndNode(currentNode.name)) {
      currentNode = when (directionsIterator.next()) {
        Direction.Left -> currentNode.left
        Direction.Right -> currentNode.right
      }

      result += 1
    }
    return result
  }

  data object Part2 : Day8Solution() {
    override fun solve(input: Input): ComputedResult {
      val tree = Tree.create(input.nodes)

      val currentNodes = tree.keys.asSequence()
        .filter { it.name.last() == 'A' }
        .map { tree[it] }
        .toList()

      val distanceToFirst = currentNodes.associateWith { node ->
        computeDistanceToEnd(tree, node.name, input) { it.name.last() == 'Z' }
      }

      /**
       * computes the greatest common divisor of a list of numbers
       */
      fun gcd(a: Long, b: Long): Long {
        var x = a
        var y = b
        while (y > 0) {
          val temp = y
          y = x % y
          x = temp
        }

        return x
      }

      /**
       * Compute the least common multiple of two numbers
       */
      fun lcm(a: Long, b: Long): Long {
        return a * b / gcd(a, b)
      }

      // Using the distance it takes to get to one, we know each one will always go to one end.
      // We can then figure out that cycles happen and push each one _eventually_ back to the end. This assumed
      // that the _first_ end node is the right one.
      //
      // This also assumes it cycles *from beginning* to the end node, such that the cycles traverse the entire space.
      val result = distanceToFirst.values.reduce(::lcm)
      return ComputedResult.Simple(result)
    }
  }

  data class Tree private constructor(
    private val nodes: Map<NodeName, Node>,
  ) {
    val keys = nodes.keys

    operator fun get(name: NodeName): Node = nodes.getValue(name)

    companion object {
      fun create(nodes: Map<NodeName, NodeData>): Tree {
        val tree = mutableMapOf<NodeName, Node>()

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
          getOrCreate(tree, node)
        }

        return Tree(tree)
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
  ) {
    fun directionsIterator(): Iterator<Direction> = iterator {
      while (true) {
        yieldAll(directions.directions)
      }
    }
  }

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
