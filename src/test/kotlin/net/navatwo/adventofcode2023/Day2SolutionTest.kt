package net.navatwo.adventofcode2023

import net.navatwo.adventofcode2023.benchmarks.Benchmark
import net.navatwo.adventofcode2023.day2.Day2Solution
import net.navatwo.adventofcode2023.day2.Day2Solution.Game
import net.navatwo.adventofcode2023.day2.Day2Solution.Game.Colour
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day2SolutionTest {
  @Test
  fun `p1 parse`() {
    val resourceName = "day2/p1_sample.txt"
    val solution = Day2Solution.Part1
    val input = solution.parseResource(resourceName)
    assertThat(input).hasSize(5)
    assertThat(input[0]).isEqualTo(
      Game(
        id = 1,
        pulls = listOf(
          Game.Pull(mapOf(Colour.Blue to 3, Colour.Red to 4)),
          Game.Pull(mapOf(Colour.Red to 1, Colour.Green to 2, Colour.Blue to 6)),
          Game.Pull(mapOf(Colour.Green to 2)),
        )
      )
    )
    assertThat(input[3]).isEqualTo(
      Game(
        id = 4,
        pulls = listOf(
          Game.Pull(mapOf(Colour.Green to 1, Colour.Red to 3, Colour.Blue to 6)),
          Game.Pull(mapOf(Colour.Green to 3, Colour.Red to 6)),
          Game.Pull(mapOf(Colour.Green to 3, Colour.Blue to 15, Colour.Red to 14)),
        )
      )
    )
  }

  @Test
  fun `p1 sample`() {
    val resourceName = "day2/p1_sample.txt"
    val solution = Day2Solution.Part1
    val input = solution.parseResource(resourceName)
    assertThat(Day2Solution.Part1.solve(input)).isComputed(8)
  }

  @Test
  fun `p1`() {
    val resourceName = "day2/p1_input.txt"
    val solution = Day2Solution.Part1
    val input = solution.parseResource(resourceName)
    assertThat(Day2Solution.Part1.solve(input)).isComputed(2447)

    Benchmark.run(
      inputContent = loadText(resourceName),
      solution = solution,
    )
  }

//    @Test
//    fun `p2 sample`() {
//        val resourceName = "day1/p2_sample.txt"
//        val solution = Day2Solution.Part2
//        val input = solution.parseResource(resourceName)
//        assertThat(Day2Solution.Part2.solve(input)).isComputed(281)
//    }
//
//    @Test
//    fun `p2`() {
//        val resourceName = "day1/p1_input.txt"
//        val solution = Day2Solution.Part2
//        val input = solution.parseResource(resourceName)
//        assertThat(Day2Solution.Part2.solve(input)).isComputed(55093L)
//
//        Benchmark.run(
//            inputContent = loadText(resourceName),
//            solution = solution,
//        )
//    }
}
