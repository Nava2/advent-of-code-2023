package net.navatwo.adventofcode2023.day3

import net.navatwo.adventofcode2023.Coord
import net.navatwo.adventofcode2023.benchmarks.Benchmark
import net.navatwo.adventofcode2023.day3.Day3Solution.EngineSchematic
import net.navatwo.adventofcode2023.get
import net.navatwo.adventofcode2023.isComputed
import net.navatwo.adventofcode2023.loadText
import net.navatwo.adventofcode2023.parseResource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day3SolutionTest {
  @Test
  fun `p1 parse`() {
    val resourceName = "day3/p1_sample.txt"
    val solution = Day3Solution.Part1
    val schematic = solution.parseResource(resourceName)
    assertThat(schematic.grid).hasSize(10)
      .allSatisfy { assertThat(it).hasSize(10) }

    assertThat(schematic.grid[Coord(0, 0)])
      .isEqualTo(EngineSchematic.Value('4'))
    assertThat(schematic.grid[Coord(1, 0)])
      .isEqualTo(EngineSchematic.Value('6'))
    assertThat(schematic.grid[Coord(2, 0)])
      .isEqualTo(EngineSchematic.Value('7'))
    assertThat(schematic.grid[Coord(3, 0)])
      .isEqualTo(EngineSchematic.Value('.'))

    assertThat(schematic.grid[Coord(3, 1)])
      .isEqualTo(EngineSchematic.Value('*'))
  }

  @Test
  fun `p1 sample`() {
    val resourceName = "day3/p1_sample.txt"
    val solution = Day3Solution.Part1
    val input = solution.parseResource(resourceName)
    assertThat(Day3Solution.Part1.solve(input)).isComputed(4361L)
  }

  @Test
  fun `p1`() {
    val resourceName = "day3/p1_input.txt"
    val solution = Day3Solution.Part1
    val input = solution.parseResource(resourceName)
    assertThat(Day3Solution.Part1.solve(input)).isComputed(526404L)

    Benchmark.run(
      inputContent = loadText(resourceName),
      solution = solution,
    )
  }

  @Test
  fun `p2 sample`() {
    val resourceName = "day3/p1_sample.txt"
    val solution = Day3Solution.Part2
    val input = solution.parseResource(resourceName)
    assertThat(Day3Solution.Part2.solve(input)).isComputed(467835)
  }

  @Test
  fun `p2`() {
    val resourceName = "day3/p1_input.txt"
    val solution = Day3Solution.Part2
    val input = solution.parseResource(resourceName)
    assertThat(Day3Solution.Part2.solve(input)).isComputed(84399773L)

    Benchmark.run(
      inputContent = loadText(resourceName),
      solution = solution,
    )
  }
}
