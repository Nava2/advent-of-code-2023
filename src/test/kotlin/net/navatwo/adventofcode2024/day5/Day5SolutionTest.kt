package net.navatwo.adventofcode2024.day5

import net.navatwo.adventofcode2023.benchmarks.Benchmark
import net.navatwo.adventofcode2023.isComputed
import net.navatwo.adventofcode2023.loadText
import net.navatwo.adventofcode2023.parseResource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day5SolutionTest {
  @Test
  fun `p1 sample`() {
    val resourceName = "2024/day5/p1_sample.txt"
    val solution = Day5Solution.Part1
    val input = solution.parseResource(resourceName)
    assertThat(Day5Solution.Part1.solve(input)).isComputed(143L)
  }

  @Test
  fun `p1`() {
    val resourceName = "2024/day5/p1_input.txt"
    val solution = Day5Solution.Part1
    val input = solution.parseResource(resourceName)
    assertThat(Day5Solution.Part1.solve(input)).isComputed(4462L)

    Benchmark.run(
      inputContent = loadText(resourceName),
      solution = solution,
    )
  }

//  @Test
//  fun `p2 sample`() {
//    val resourceName = "2024/day5/p2_sample.txt"
//    val solution = day5Solution.Part2
//    val input = solution.parseResource(resourceName)
//    assertThat(day5Solution.Part2.solve(input)).isComputed(281)
//  }
//
//  @Test
//  fun `p2`() {
//    val resourceName = "2024/day5/p1_input.txt"
//    val solution = day5Solution.Part2
//    val input = solution.parseResource(resourceName)
//    assertThat(day5Solution.Part2.solve(input)).isComputed(55093L)
//
//    Benchmark.run(
//      inputContent = loadText(resourceName),
//      solution = solution,
//    )
//  }
}
