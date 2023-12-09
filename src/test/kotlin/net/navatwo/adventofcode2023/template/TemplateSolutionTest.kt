package net.navatwo.adventofcode2023.template

import net.navatwo.adventofcode2023.benchmarks.Benchmark
import net.navatwo.adventofcode2023.isComputed
import net.navatwo.adventofcode2023.loadText
import net.navatwo.adventofcode2023.parseResource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class TemplateSolutionTest {
  @Test
  @Disabled("TODO")
  fun `p1 parse`() {
    val resourceName = "day{{day_number}}/p1_sample.txt"
    val solution = TemplateSolution.Part1
    val inputs = solution.parseResource(resourceName)
    assertThat(inputs).isNotEmpty()
  }

  @Test
  @Disabled("TODO")
  fun `p1 sample`() {
    val resourceName = "day{{day_number}}/p1_sample.txt"
    val solution = TemplateSolution.Part1
    val input = solution.parseResource(resourceName)
    assertThat(TemplateSolution.Part1.solve(input)).isComputed(32L)
  }

  @Test
  @Disabled("TODO")
  fun `p1`() {
    val resourceName = "day{{day_number}}/p1_input.txt"
    val solution = TemplateSolution.Part1
    val input = solution.parseResource(resourceName)
    assertThat(TemplateSolution.Part1.solve(input)).isComputed(32L)

    Benchmark.run(
      inputContent = loadText(resourceName),
      solution = solution,
    )
  }

//  @Test
//  fun `p2 sample`() {
//    val resourceName = "day{{day_number}}/p1_sample.txt"
//    val solution = TemplateSolution.Part2
//    val input = solution.parseResource(resourceName)
//    assertThat(TemplateSolution.Part2.solve(input)).isComputed(46)
//  }
//
//  @Test
//  fun `p2`() {
//    val resourceName = "day{{day_number}}/p1_input.txt"
//    val solution = TemplateSolution.Part2
//    val input = solution.parseResource(resourceName)
//    assertThat(TemplateSolution.Part2.solve(input)).isComputed(5132675L)
//
//    Benchmark.run(
//      inputContent = loadText(resourceName),
//      solution = solution,
//    )
//  }
}
