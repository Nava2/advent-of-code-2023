package net.navatwo.adventofcode2023.day10

import net.navatwo.adventofcode2023.BooleanGrid
import net.navatwo.adventofcode2023.Coord
import net.navatwo.adventofcode2023.benchmarks.Benchmark
import net.navatwo.adventofcode2023.day10.Day10Solution.Part1
import net.navatwo.adventofcode2023.day10.Day10Solution.Part2
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
import org.assertj.core.api.ObjectAssert
import org.junit.jupiter.api.Test

class Day10SolutionTest {
  @Test
  fun `p1 parse`() {
    val filteredResourceName = "2023/day10/p1_sample_2_filtered.txt"
    val filteredLayout = Part1.parseResource(filteredResourceName)
    assertThat(filteredLayout.tiles.rows).containsExactly(
      listOf(Ground, Ground, Ground, Ground, Ground),
      listOf(Ground, Start, Horizontal, SWNinety, Ground),
      listOf(Ground, Vertical, Ground, Vertical, Ground),
      listOf(Ground, NENinety, Horizontal, NWNinety, Ground),
      listOf(Ground, Ground, Ground, Ground, Ground),
    )

    val resourceName = "2023/day10/p1_sample_2.txt"
    val layout = Part1.parseResource(resourceName)
    assertThat(layout.tiles.rows).containsExactly(
      listOf(Horizontal, NENinety, Vertical, SENinety, SWNinety),
      listOf(SWNinety, Start, Horizontal, SWNinety, Vertical),
      listOf(NENinety, Vertical, SWNinety, Vertical, Vertical),
      listOf(Horizontal, NENinety, Horizontal, NWNinety, Vertical),
      listOf(NENinety, Vertical, Horizontal, NWNinety, SENinety),
    )
  }

  @Test
  fun `p1 sample 2 filtered`() {
    val resourceName = "2023/day10/p1_sample_2_filtered.txt"
    val solution = Part1
    val input = solution.parseResource(resourceName)
    assertThat(Part1.solve(input)).isComputed(4L)
  }

  @Test
  fun `p1 sample 2`() {
    val resourceName = "2023/day10/p1_sample_2.txt"
    val solution = Part1
    val input = solution.parseResource(resourceName)
    assertThat(Part1.solve(input)).isComputed(4L)
  }

  @Test
  fun `p1 sample 3`() {
    val resourceName = "2023/day10/p1_sample_3.txt"
    val solution = Part1
    val input = solution.parseResource(resourceName)
    assertThat(Part1.solve(input)).isComputed(8L)
  }

  @Test
  fun `p1`() {
    val resourceName = "2023/day10/p1_input.txt"
    val solution = Part1
    val input = solution.parseResource(resourceName)
    assertThat(Part1.solve(input)).isComputed(6806L)

    Benchmark.run(
      inputContent = loadText(resourceName),
      solution = solution,
      inputConfig = Benchmark.Stage.INPUT.config(warmupIterations = 1000U, iterations = 2_000U),
    )
  }

  @Test
  fun `p2 growGrid`() {
    val layout = Part2.parse(
      """
        S----7
        |F--7|
        ||..||
        |L7FJ|
        L-JL-J
        ......
      """.trimIndent().lineSequence()
    )

    val expectedLayout = Part2.parse(
      """
        .............
        .S---------7.
        .|.........|.
        .|.F-----7.|.
        .|.|.....|.|.
        .|.|.....|.|.
        .|.|.....|.|.
        .|.L-7.F-J.|.
        .|...|.|...|.
        .L---J.L---J.
        .............
        .............
        .............
      """.trimIndent().lineSequence()
    )

    val grownLayout = Part2.growGrid(layout, Coord(0, 0))
    assertThat(grownLayout.tiles.rows).containsExactlyElementsOf(expectedLayout.tiles.rows)
  }

  @Test
  fun `p2 growGrid small`() {
    val layout = Part2.parse(
      """
        S-7
        |.|
        L-J
      """.trimIndent().lineSequence()
    )

    val expectedLayout = Part2.parse(
      """
        .......
        .S---7.
        .|...|.
        .|...|.
        .|...|.
        .L---J.
        .......
      """.trimIndent().lineSequence()
    )

    val grownLayout = Part2.growGrid(layout, Coord(0, 0))
    assertThat(grownLayout.tiles.rows).containsExactlyElementsOf(expectedLayout.tiles.rows)
  }

  @Test
  fun `p2 solve small`() {
    val layout = Part2.parse(
      """
        S-7
        |.|
        L-J
      """.trimIndent().lineSequence()
    )
    val interiorTileCount = Part2.solve(layout)
    assertThat(interiorTileCount).isComputed(1)
  }

  @Test
  fun `p2 solve larger`() {
    val layout = Part2.parse(
      """
        S----7
        |F--7|
        ||..||
        |L7FJ|
        L-JL-J
        ......
      """.trimIndent().lineSequence()
    )
    val interiorTileCount = Part2.solve(layout)
    assertThat(interiorTileCount).isComputed(0)
  }

  @Test
  fun `p2 sample`() {
    val resourceName = "2023/day10/p2_sample_1.txt"
    val solution = Part2
    val input = solution.parseResource(resourceName)
    assertThat(Part2.solve(input)).isComputed(4)
  }

  @Test
  fun `p2 sample 2`() {
    val resourceName = "2023/day10/p2_sample_2.txt"
    val solution = Part2
    val input = solution.parseResource(resourceName)
    assertThat(Part2.solve(input)).isComputed(8)
  }

  @Test
  fun `p2 sample 3`() {
    val resourceName = "2023/day10/p2_sample_3.txt"
    val solution = Part2
    val input = solution.parseResource(resourceName)
    assertThat(Part2.solve(input)).isComputed(10)
  }

  @Test
  fun `p2`() {
    val resourceName = "2023/day10/p1_input.txt"
    val solution = Part2
    val input = solution.parseResource(resourceName)
    assertThat(Part2.solve(input)).isComputed(449L)

    Benchmark.run(
      inputContent = loadText(resourceName),
      solution = solution,
      inputConfig = Benchmark.Stage.INPUT.config(warmupIterations = 1000U, iterations = 2_000U),
    )
  }
}

fun ObjectAssert<BooleanGrid>.isDeepEqualTo(other: BooleanGrid): ObjectAssert<BooleanGrid> {
  return satisfies { that ->
    assertThat(that.rows).isDeepEqualTo(other.rows)
  }
}
