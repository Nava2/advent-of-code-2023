package net.navatwo.adventofcode2023.day9

import net.navatwo.adventofcode2023.benchmarks.Benchmark
import net.navatwo.adventofcode2023.day9.Day9Solution.History
import net.navatwo.adventofcode2023.isComputed
import net.navatwo.adventofcode2023.loadText
import net.navatwo.adventofcode2023.parseResource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class Day9SolutionTest {
  @Test
  fun `p1 parse`() {
    val resourceName = "day9/p1_sample.txt"
    val solution = Day9Solution.Part1
    val histories = solution.parseResource(resourceName)
    assertThat(histories)
      .hasSize(3)
      .satisfies {
        assertThat(histories[0].values)
          .containsExactly(
            History.Value(0),
            History.Value(3),
            History.Value(6),
            History.Value(9),
            History.Value(12),
            History.Value(15),
          )
      }
  }

  @Test
  @Disabled("TODO")
  fun `p1 sample`() {
    val resourceName = "day9/p1_sample.txt"
    val solution = Day9Solution.Part1
    val input = solution.parseResource(resourceName)
    assertThat(Day9Solution.Part1.solve(input)).isComputed(32L)
  }

  @Test
  @Disabled("TODO")
  fun `p1`() {
    val resourceName = "day9/p1_input.txt"
    val solution = Day9Solution.Part1
    val input = solution.parseResource(resourceName)
    assertThat(Day9Solution.Part1.solve(input)).isComputed(32L)

    Benchmark.run(
      inputContent = loadText(resourceName),
      solution = solution,
    )
  }

//  @Test
//  fun `p2 sample`() {
//    val resourceName = "day9/p1_sample.txt"
//    val solution = Day9Solution.Part2
//    val input = solution.parseResource(resourceName)
//    assertThat(Day9Solution.Part2.solve(input)).isComputed(46)
//  }
//
//  @Test
//  fun `p2`() {
//    val resourceName = "day9/p1_input.txt"
//    val solution = Day9Solution.Part2
//    val input = solution.parseResource(resourceName)
//    assertThat(Day9Solution.Part2.solve(input)).isComputed(5132675L)
//
//    Benchmark.run(
//      inputContent = loadText(resourceName),
//      solution = solution,
//    )
//  }
}
