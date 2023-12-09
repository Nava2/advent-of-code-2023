package net.navatwo.adventofcode2023.day5

import com.github.nava2.interval_tree.Interval
import net.navatwo.adventofcode2023.benchmarks.Benchmark
import net.navatwo.adventofcode2023.day5.Day5Solution.Almanac
import net.navatwo.adventofcode2023.isComputed
import net.navatwo.adventofcode2023.loadText
import net.navatwo.adventofcode2023.parseResource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day5SolutionTest {
  @Test
  fun `p1 parse`() {
    val resourceName = "day5/p1_sample.txt"
    val solution = Day5Solution.Part1
    val almanac = solution.parseResource(resourceName)
    assertThat(almanac.seeds).containsExactly(
      Almanac.Seed(79),
      Almanac.Seed(14),
      Almanac.Seed(55),
      Almanac.Seed(13),
    )
    assertThat(almanac.mappingsBySourceType).hasSize(7)
      .containsOnlyKeys(
        Almanac.Type.Seed,
        Almanac.Type.Soil,
        Almanac.Type.Fertilizer,
        Almanac.Type.Water,
        Almanac.Type.Light,
        Almanac.Type.Temperature,
        Almanac.Type.Humidity
      )
      .satisfies { mappings ->
        assertThat(mappings)
          .containsEntry(
            Almanac.Type.Water,
            Almanac.Mapping(
              sourceType = Almanac.Type.Water,
              destType = Almanac.Type.Light,
              entries = listOf(
                Almanac.Entry(
                  sourceRange = 18L..25,
                  destRange = 88L..95,
                ),
                Almanac.Entry(
                  sourceRange = 25L..95,
                  destRange = 18L..88,
                ),
              )
            )
          )
      }
  }

  @Test
  fun `verify mapping behaviour`() {
    val mapping = Almanac.Mapping(
      sourceType = Almanac.Type.Water,
      destType = Almanac.Type.Light,
      entries = listOf(
        Almanac.Entry(
          sourceRange = 18L..25,
          destRange = 88L..95,
        ),
        Almanac.Entry(
          sourceRange = 25L..95,
          destRange = 18L..88,
        ),
      )
    )

    assertThat(mapping.map(0L)).isEqualTo(0L)
    assertThat(mapping.map(1L)).isEqualTo(1L)
    assertThat(mapping.map(18L)).isEqualTo(88L)
    assertThat(mapping.map(25L)).isEqualTo(95L)
    assertThat(mapping.map(26L)).isEqualTo(19L)
    assertThat(mapping.map(95L)).isEqualTo(88L)
    assertThat(mapping.map(96L)).isEqualTo(96L)
  }

  @Test
  fun `verify intersect behaviour`() {
    val smaller = Interval.Simple(79, 14)
    val largerLeft = Interval.Simple(60, 79 - 60 + 10)

    val bad1 = Interval.Simple(79, 14)
    val bad2 = Interval.Simple(50, 98-50)
    assertThat(bad1.intersect(bad2))
      .isEqualTo(Interval.Simple(79, 14))
      .isEqualTo(bad1.intersect(bad2))

    val bad3 = Interval.Simple(74, 14)
    val bad4 = Interval.Simple(64, 77 - 64)
    assertThat(bad3.intersect(bad4))
      .isEqualTo(Interval.Simple(74, 3))
      .isEqualTo(bad3.intersect(bad4))

    assertThat(smaller.intersect(largerLeft))
      .isEqualTo(Interval.Simple(79, 10))
      .isEqualTo(largerLeft.intersect(smaller))

    val largerRight = Interval.Simple(82, 40)
    assertThat(smaller.intersect(largerRight))
      .isEqualTo(Interval.Simple(82, 11))
      .isEqualTo(largerRight.intersect(smaller))

    val withinSmaller = Interval.Simple(80, 5)
    assertThat(smaller.intersect(withinSmaller))
      .isEqualTo(withinSmaller)
      .isEqualTo(withinSmaller.intersect(smaller))

    val touchingSmallerLeft = Interval.Simple(75, 4)
    assertThat(smaller.intersect(touchingSmallerLeft))
      .isEqualTo(touchingSmallerLeft.intersect(smaller))
      .isNull()
  }

  @Test
  fun `p1 sample`() {
    val resourceName = "day5/p1_sample.txt"
    val solution = Day5Solution.Part1
    val input = solution.parseResource(resourceName)
    assertThat(Day5Solution.Part1.solve(input)).isComputed(35L)
  }

  @Test
  fun `p1`() {
    val resourceName = "day5/p1_input.txt"
    val solution = Day5Solution.Part1
    val input = solution.parseResource(resourceName)
    assertThat(Day5Solution.Part1.solve(input)).isComputed(165788812L)

    Benchmark.run(
      inputContent = loadText(resourceName),
      solution = solution,
    )
  }

  @Test
  fun `p2 sample`() {
    val resourceName = "day5/p1_sample.txt"
    val solution = Day5Solution.Part2
    val input = solution.parseResource(resourceName)
    assertThat(Day5Solution.Part2.solve(input)).isComputed(46)
  }

  @Test
  fun `p2`() {
    val resourceName = "day5/p1_input.txt"
    val solution = Day5Solution.Part2
    val input = solution.parseResource(resourceName)
    assertThat(Day5Solution.Part2.solve(input)).isComputed(5132675L)

    Benchmark.run(
      inputContent = loadText(resourceName),
      solution = solution,
    )
  }
}
