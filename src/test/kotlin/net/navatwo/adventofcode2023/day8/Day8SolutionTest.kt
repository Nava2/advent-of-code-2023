package net.navatwo.adventofcode2023.day8

import net.navatwo.adventofcode2023.benchmarks.Benchmark
import net.navatwo.adventofcode2023.day8.Day8Solution.Direction
import net.navatwo.adventofcode2023.day8.Day8Solution.Directions
import net.navatwo.adventofcode2023.day8.Day8Solution.Input
import net.navatwo.adventofcode2023.day8.Day8Solution.NodeData
import net.navatwo.adventofcode2023.day8.Day8Solution.NodeName
import net.navatwo.adventofcode2023.isComputed
import net.navatwo.adventofcode2023.loadText
import net.navatwo.adventofcode2023.parseResource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day8SolutionTest {
  @Test
  fun `p1 parse sample 1`() {
    val resourceName = "day8/p1_sample1.txt"
    val solution = Day8Solution.Part1
    val input = solution.parseResource(resourceName)
    assertThat(input).isEqualTo(
      Input(
        directions = Directions(listOf(Direction.Right, Direction.Left)),
        nodes = mapOf(
          NodeName("AAA") to NodeData(
            name = NodeName("AAA"),
            left = NodeName("BBB"),
            right = NodeName("CCC"),
          ),
          NodeName("BBB") to NodeData(
            name = NodeName("BBB"),
            left = NodeName("DDD"),
            right = NodeName("EEE"),
          ),
          NodeName("CCC") to NodeData(
            name = NodeName("CCC"),
            left = NodeName("ZZZ"),
            right = NodeName("GGG"),
          ),
          NodeName("DDD") to NodeData(
            name = NodeName("DDD"),
            left = NodeName("DDD"),
            right = NodeName("DDD"),
          ),
          NodeName("EEE") to NodeData(
            name = NodeName("EEE"),
            left = NodeName("EEE"),
            right = NodeName("EEE"),
          ),
          NodeName("GGG") to NodeData(
            name = NodeName("GGG"),
            left = NodeName("GGG"),
            right = NodeName("GGG"),
          ),
          NodeName("ZZZ") to NodeData(
            name = NodeName("ZZZ"),
            left = NodeName("ZZZ"),
            right = NodeName("ZZZ"),
          ),
        ),
      )
    )
  }

  @Test
  fun `p1 parse sample 2`() {
    val resourceName = "day8/p1_sample2.txt"
    val solution = Day8Solution.Part1
    val input = solution.parseResource(resourceName)
    assertThat(input).isEqualTo(
      Input(
        directions = Directions(listOf(Direction.Left, Direction.Left, Direction.Right)),
        nodes = mapOf(
          NodeName("AAA") to NodeData(
            name = NodeName("AAA"),
            left = NodeName("BBB"),
            right = NodeName("BBB"),
          ),
          NodeName("BBB") to NodeData(
            name = NodeName("BBB"),
            left = NodeName("AAA"),
            right = NodeName("ZZZ"),
          ),
          NodeName("ZZZ") to NodeData(
            name = NodeName("ZZZ"),
            left = NodeName("ZZZ"),
            right = NodeName("ZZZ"),
          ),
        ),
      )
    )
  }

  @Test
  fun `p1 sample 1`() {
    val resourceName = "day8/p1_sample1.txt"
    val solution = Day8Solution.Part1
    val input = solution.parseResource(resourceName)
    assertThat(Day8Solution.Part1.solve(input)).isComputed(2)
  }

  @Test
  fun `p1 sample 2`() {
    val resourceName = "day8/p1_sample2.txt"
    val solution = Day8Solution.Part1
    val input = solution.parseResource(resourceName)
    assertThat(Day8Solution.Part1.solve(input)).isComputed(6)
  }

  @Test
  fun `p1`() {
    val resourceName = "day8/p1_input.txt"
    val solution = Day8Solution.Part1
    val input = solution.parseResource(resourceName)
    assertThat(Day8Solution.Part1.solve(input)).isComputed(19637L)

    Benchmark.run(
      inputContent = loadText(resourceName),
      solution = solution,
    )
  }

  @Test
  fun `p2 sample`() {
    val resourceName = "day8/p2_sample1.txt"
    val solution = Day8Solution.Part2
    val input = solution.parseResource(resourceName)
    assertThat(Day8Solution.Part2.solve(input)).isComputed(6)
  }

  @Test
  fun `p2`() {
    val resourceName = "day8/p1_input.txt"
    val solution = Day8Solution.Part2
    val input = solution.parseResource(resourceName)
    assertThat(Day8Solution.Part2.solve(input)).isComputed(8811050362409L)

    Benchmark.run(
      inputContent = loadText(resourceName),
      solution = solution,
    )
  }
}
