package net.navatwo.adventofcode2023.day10

import net.navatwo.adventofcode2023.benchmarks.Benchmark
import net.navatwo.adventofcode2023.day10.Day10Solution.Part1
import net.navatwo.adventofcode2023.day10.Day10Solution.Tile.Ground
import net.navatwo.adventofcode2023.day10.Day10Solution.Tile.Horizontal
import net.navatwo.adventofcode2023.day10.Day10Solution.Tile.NENinety
import net.navatwo.adventofcode2023.day10.Day10Solution.Tile.NWNinety
import net.navatwo.adventofcode2023.day10.Day10Solution.Tile.SENinety
import net.navatwo.adventofcode2023.day10.Day10Solution.Tile.SWNinety
import net.navatwo.adventofcode2023.day10.Day10Solution.Tile.Start
import net.navatwo.adventofcode2023.day10.Day10Solution.Tile.Vertical
import net.navatwo.adventofcode2023.isComputed
import net.navatwo.adventofcode2023.loadText
import net.navatwo.adventofcode2023.parseResource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class Day10SolutionTest {
  @Test
  fun `p1 parse`() {
    val filteredResourceName = "day10/p1_sample_2_filtered.txt"
    val filteredGrid = Part1.parseResource(filteredResourceName)
    assertThat(filteredGrid.rows).containsExactly(
      listOf(Ground, Ground, Ground, Ground, Ground),
      listOf(Ground, Start, Horizontal, SWNinety, Ground),
      listOf(Ground, Vertical, Ground, Vertical, Ground),
      listOf(Ground, NENinety, Horizontal, NWNinety, Ground),
      listOf(Ground, Ground, Ground, Ground, Ground),
    )

    val resourceName = "day10/p1_sample_2.txt"
    val grid = Part1.parseResource(resourceName)
    assertThat(grid.rows).containsExactly(
      listOf(Horizontal, NENinety, Vertical, SENinety, SWNinety),
      listOf(SWNinety, Start, Horizontal, SWNinety, Vertical),
      listOf(NENinety, Vertical, SWNinety, Vertical, Vertical),
      listOf(Horizontal, NENinety, Horizontal, NWNinety, Vertical),
      listOf(NENinety, Vertical, Horizontal, NWNinety, SENinety),
    )
  }

  @Test
  @Disabled("TODO")
  fun `p1 sample`() {
    val resourceName = "day10/p1_sample.txt"
    val solution = Part1
    val input = solution.parseResource(resourceName)
    assertThat(Part1.solve(input)).isComputed(32L)
  }

  @Test
  @Disabled("TODO")
  fun `p1`() {
    val resourceName = "day10/p1_input.txt"
    val solution = Part1
    val input = solution.parseResource(resourceName)
    assertThat(Part1.solve(input)).isComputed(32L)

    Benchmark.run(
      inputContent = loadText(resourceName),
      solution = solution,
    )
  }

//  @Test
//  fun `p2 sample`() {
//    val resourceName = "day10/p1_sample.txt"
//    val solution = Day10Solution.Part2
//    val input = solution.parseResource(resourceName)
//    assertThat(Day10Solution.Part2.solve(input)).isComputed(46)
//  }
//
//  @Test
//  fun `p2`() {
//    val resourceName = "day10/p1_input.txt"
//    val solution = Day10Solution.Part2
//    val input = solution.parseResource(resourceName)
//    assertThat(Day10Solution.Part2.solve(input)).isComputed(5132675L)
//
//    Benchmark.run(
//      inputContent = loadText(resourceName),
//      solution = solution,
//    )
//  }
}
