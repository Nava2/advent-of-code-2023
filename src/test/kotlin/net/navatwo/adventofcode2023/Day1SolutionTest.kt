package net.navatwo.adventofcode2023

import net.navatwo.adventofcode2023.benchmarks.Benchmark
import net.navatwo.adventofcode2023.day1.Day1Solution
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day1SolutionTest {
    @Test
    fun `p1 sample`() {
        val resourceName = "day1/p1_sample.txt"
        val solution = Day1Solution.Part1
        val input = solution.parseResource(resourceName)
        assertThat(Day1Solution.Part1.solve(input)).isComputed(142L)
    }

    @Test
    fun `p1`() {
        val resourceName = "day1/p1_input.txt"
        val solution = Day1Solution.Part1
        val input = solution.parseResource(resourceName)
        assertThat(Day1Solution.Part1.solve(input)).isComputed(55002L)

        Benchmark.run(
            inputContent = loadText(resourceName),
            solution = solution,
        )
    }

    @Test
    fun `p2 sample`() {
        val resourceName = "day1/p2_sample.txt"
        val solution = Day1Solution.Part2
        val input = solution.parseResource(resourceName)
        assertThat(Day1Solution.Part2.solve(input)).isComputed(281)
    }

    @Test
    fun `p2`() {
        val resourceName = "day1/p1_input.txt"
        val solution = Day1Solution.Part2
        val input = solution.parseResource(resourceName)
        assertThat(Day1Solution.Part2.solve(input)).isComputed(55093L)

        Benchmark.run(
            inputContent = loadText(resourceName),
            solution = solution,
        )
    }
}
