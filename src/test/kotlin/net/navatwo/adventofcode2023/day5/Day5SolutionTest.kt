package net.navatwo.adventofcode2023.day5

import net.navatwo.adventofcode2023.day5.Day5Solution.Almanac
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

//  @Test
//  fun `p1 sample`() {
//    val resourceName = "day5/p1_sample.txt"
//    val solution = Day5Solution.Part1
//    val input = solution.parseResource(resourceName)
//    assertThat(Day5Solution.Part1.solve(input)).isComputed(13)
//  }
//
//  @Test
//  fun `p1`() {
//    val resourceName = "day5/p1_input.txt"
//    val solution = Day5Solution.Part1
//    val input = solution.parseResource(resourceName)
//    assertThat(Day5Solution.Part1.solve(input)).isComputed(21959L)
//
//    Benchmark.run(
//      inputContent = loadText(resourceName),
//      solution = solution,
//    )
//  }
//
//  @Test
//  fun `p2 sample`() {
//    val resourceName = "day5/p1_sample.txt"
//    val solution = Day5Solution.Part2
//    val input = solution.parseResource(resourceName)
//    assertThat(Day5Solution.Part2.solve(input)).isComputed(30)
//  }
//
//  @Test
//  fun `p2`() {
//    val resourceName = "day5/p1_input.txt"
//    val solution = Day5Solution.Part2
//    val input = solution.parseResource(resourceName)
//    assertThat(Day5Solution.Part2.solve(input)).isComputed(5132675L)
//
//    Benchmark.run(
//      inputContent = loadText(resourceName),
//      solution = solution,
//    )
//  }
}
