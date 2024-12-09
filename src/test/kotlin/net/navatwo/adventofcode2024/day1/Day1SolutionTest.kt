package net.navatwo.adventofcode2024.day1

import net.navatwo.adventofcode2023.benchmarks.Benchmark
import net.navatwo.adventofcode2023.isComputed
import net.navatwo.adventofcode2023.loadText
import net.navatwo.adventofcode2023.parseResource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day1SolutionTest {
  @Test
  fun `p1 sample`() {
    val resourceName = "2024/day1/p1_sample.txt"
    val solution = Day1Solution.Part1
    val input = solution.parseResource(resourceName)
    assertThat(Day1Solution.Part1.solve(input)).isComputed(11L)
  }

  @Test
  fun `p1`() {
    val resourceName = "2024/day1/p1_input.txt"
    val solution = Day1Solution.Part1
    val input = solution.parseResource(resourceName)
    assertThat(Day1Solution.Part1.solve(input)).isComputed(2367773L)

    Benchmark.run(
      inputContent = loadText(resourceName),
      solution = solution,
    )
  }

  @Test
  fun `p2 sample`() {
    val resourceName = "2024/day1/p1_sample.txt"
    val solution = Day1Solution.Part2
    val input = solution.parseResource(resourceName)
    assertThat(Day1Solution.Part2.solve(input)).isComputed(31)
  }

  @Test
  fun `p2`() {
    val resourceName = "2024/day1/p1_input.txt"
    val solution = Day1Solution.Part2
    val input = solution.parseResource(resourceName)
    assertThat(Day1Solution.Part2.solve(input)).isComputed(21271939L)

    Benchmark.run(
      inputContent = loadText(resourceName),
      solution = solution,
    )
  }
}
