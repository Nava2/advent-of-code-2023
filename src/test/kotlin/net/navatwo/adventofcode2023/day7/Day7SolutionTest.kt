package net.navatwo.adventofcode2023.day7

import net.navatwo.adventofcode2023.benchmarks.Benchmark
import net.navatwo.adventofcode2023.day7.Day7Solution.Bid
import net.navatwo.adventofcode2023.day7.Day7Solution.Game
import net.navatwo.adventofcode2023.day7.Day7Solution.Hand
import net.navatwo.adventofcode2023.day7.Day7Solution.HandType
import net.navatwo.adventofcode2023.day7.Day7Solution.PlayingCard
import net.navatwo.adventofcode2023.isComputed
import net.navatwo.adventofcode2023.loadText
import net.navatwo.adventofcode2023.parseResource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class Day7SolutionTest {
  @Test
  fun `p1 parse`() {
    val resourceName = "day7/p1_sample.txt"
    val solution = Day7Solution.Part1
    val game = solution.parseResource(resourceName)
    assertThat(game).isEqualTo(
      Game(
        handPairs = listOf(
          Hand.parse("32T3K") to Bid(765),
          Hand.parse("T55J5") to Bid(684),
          Hand.parse("KK677") to Bid(28),
          Hand.parse("KTJJT") to Bid(220),
          Hand.parse("QQQJA") to Bid(483),
        )
      )
    )
  }

  @Test
  fun `p1 sample`() {
    val resourceName = "day7/p1_sample.txt"
    val solution = Day7Solution.Part1
    val input = solution.parseResource(resourceName)
    assertThat(Day7Solution.Part1.solve(input)).isComputed(6440L)
  }

  @Test
  fun `p1`() {
    val resourceName = "day7/p1_input.txt"
    val solution = Day7Solution.Part1
    val input = solution.parseResource(resourceName)
    assertThat(Day7Solution.Part1.solve(input)).isComputed(32L)

    Benchmark.run(
      inputContent = loadText(resourceName),
      solution = solution,
    )
  }

//  @Test
//  fun `p2 sample`() {
//    val resourceName = "day7/p1_sample.txt"
//    val solution = Day7Solution.Part2
//    val input = solution.parseResource(resourceName)
//    assertThat(Day7Solution.Part2.solve(input)).isComputed(46)
//  }
//
//  @Test
//  fun `p2`() {
//    val resourceName = "day7/p1_input.txt"
//    val solution = Day7Solution.Part2
//    val input = solution.parseResource(resourceName)
//    assertThat(Day7Solution.Part2.solve(input)).isComputed(5132675L)
//
//    Benchmark.run(
//      inputContent = loadText(resourceName),
//      solution = solution,
//    )
//  }

  @Test
  fun `hand parse`() {
    val hand = Hand.parse("AKQJT98765432")
    assertThat(hand.cards).containsExactly(
      PlayingCard.Ace,
      PlayingCard.King,
      PlayingCard.Queen,
      PlayingCard.Jack,
      PlayingCard.Ten,
      PlayingCard.Nine,
      PlayingCard.Eight,
      PlayingCard.Seven,
      PlayingCard.Six,
      PlayingCard.Five,
      PlayingCard.Four,
      PlayingCard.Three,
      PlayingCard.Two,
    )
  }

  @ParameterizedTest(name = "hand = {0}, expected = {1}")
  @MethodSource("handTypeTestProvider")
  fun `compute handType`(handString: String, expected: HandType) {
    val hand = Hand.parse(handString)

    assertThat(HandType.compute(hand)).isEqualTo(expected)
  }

  companion object {
    @JvmStatic
    fun handTypeTestProvider(): List<Array<Any>> = listOf(
      arrayOf("AAAAA", HandType.FiveOfAKind),

      arrayOf("AAAAJ", HandType.FourOfAKind),
      arrayOf("JAAAA", HandType.FourOfAKind),
      arrayOf("AJAAA", HandType.FourOfAKind),

      arrayOf("AA222", HandType.FullHouse),
      arrayOf("A22A2", HandType.FullHouse),
      arrayOf("22AA2", HandType.FullHouse),

      arrayOf("22QA2", HandType.ThreeOfAKind),
      arrayOf("22245", HandType.ThreeOfAKind),

      arrayOf("2244A", HandType.TwoPair),
      arrayOf("24A2A", HandType.TwoPair),

      arrayOf("24572", HandType.OnePair),
      arrayOf("AA425", HandType.OnePair),

      arrayOf("98746", HandType.HighCard),
    )
  }
}
