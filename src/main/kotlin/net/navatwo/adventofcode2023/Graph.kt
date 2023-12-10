package net.navatwo.adventofcode2023

class Graph<ID : Any> private constructor(
  private val nodes: Map<ID, List<ID>>,
) {
  operator fun get(identifier: ID): List<ID> {
    return getOrNull(identifier) ?: error("No node with identifier $identifier")
  }

  fun getOrNull(identifier: ID): List<ID>? {
    return nodes[identifier]
  }

  override fun toString(): String {
    return buildString {
      append("Graph(\n")
      for ((node, connectedNodes) in nodes) {
        append("  $node -> $connectedNodes\n")
      }
      append(")")
    }
  }

  override fun hashCode(): Int {
    return nodes.hashCode()
  }

  override fun equals(other: Any?): Boolean {
    return this === other || (other is Graph<*> && nodes == other.nodes)
  }

  companion object {
    fun <ID : Any> create(
      nodes: Iterable<ID>,
      getConnectedNodes: (identifier: ID) -> Collection<ID>,
    ): Graph<ID> {
      val graph = mutableMapOf<ID, List<ID>>()

      val queue = ArrayDeque<ID>()
      queue.addAll(nodes)

      while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        if (current in graph) continue

        val connectedNodes = getConnectedNodes(current)

        graph[current] = connectedNodes.toList()

        queue.addAll(connectedNodes)
      }

      return Graph(graph)
    }
  }
}
