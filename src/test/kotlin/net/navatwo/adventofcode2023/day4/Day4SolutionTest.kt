package net.navatwo.adventofcode2023.day4

import net.navatwo.adventofcode2023.day4.Day4Solution.Card
import net.navatwo.adventofcode2023.parseResource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day4SolutionTest {
  @Test
  fun `p1 parse`() {
    val resourceName = "day4/p1_sample.txt"
    val solution = Day4Solution.Part1
    val scratchCards = solution.parseResource(resourceName)
    assertThat(scratchCards).hasSize(6)

    assertThat(scratchCards[0]).isEqualTo(
      Card(
        id = 1,
        winningNumbers = listOf(41, 48, 83, 86, 17),
        playableNumbers = listOf(83, 86, 6, 31, 17, 9, 48, 53),
      )
    )

    assertThat(scratchCards[5]).isEqualTo(
      Card(
        id = 6,
        winningNumbers = listOf(31, 18, 13, 56, 72),
        playableNumbers = listOf(74, 77, 10, 23, 35, 67, 36, 11),
      )
    )
  }

//  @Test
//  fun `p1 sample`() {
//    val resourceName = "day3/p1_sample.txt"
//    val solution = Day4Solution.Part1
//    val input = solution.parseResource(resourceName)
//    assertThat(Day4Solution.Part1.solve(input)).isComputed(4361L)
//  }
//
//  @Test
//  fun `p1`() {
//    val resourceName = "day3/p1_input.txt"
//    val solution = Day4Solution.Part1
//    val input = solution.parseResource(resourceName)
//    assertThat(Day4Solution.Part1.solve(input)).isComputed(526404L)
//
//    Benchmark.run(
//      inputContent = loadText(resourceName),
//      solution = solution,
//    )
//  }
//
//  @Test
//  fun `p2 sample`() {
//    val resourceName = "day3/p1_sample.txt"
//    val solution = Day4Solution.Part2
//    val input = solution.parseResource(resourceName)
//    assertThat(Day4Solution.Part2.solve(input)).isComputed(467835)
//  }
//
//  @Test
//  fun `p2`() {
//    val resourceName = "day3/p1_input.txt"
//    val solution = Day4Solution.Part2
//    val input = solution.parseResource(resourceName)
//    assertThat(Day4Solution.Part2.solve(input)).isComputed(84399773L)
//
//    Benchmark.run(
//      inputContent = loadText(resourceName),
//      solution = solution,
//    )
//  }
}
