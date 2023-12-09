package net.navatwo.adventofcode2023.day7

import net.navatwo.adventofcode2023.benchmarks.Benchmark
import net.navatwo.adventofcode2023.day7.Day7Solution.Bid
import net.navatwo.adventofcode2023.day7.Day7Solution.Game
import net.navatwo.adventofcode2023.day7.Day7Solution.Hand
import net.navatwo.adventofcode2023.day7.Day7Solution.HandType
import net.navatwo.adventofcode2023.day7.Day7Solution.Part1
import net.navatwo.adventofcode2023.day7.Day7Solution.Part1.computePart1
import net.navatwo.adventofcode2023.day7.Day7Solution.Part2
import net.navatwo.adventofcode2023.day7.Day7Solution.Part2.cardComparator
import net.navatwo.adventofcode2023.day7.Day7Solution.Part2.computePart2
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
    val solution = Part1
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
    val solution = Part1
    val input = solution.parseResource(resourceName)
    assertThat(Part1.solve(input)).isComputed(6440L)
  }

  @Test
  fun `p1`() {
    val resourceName = "day7/p1_input.txt"
    val solution = Part1
    val input = solution.parseResource(resourceName)
    assertThat(Part1.solve(input)).isComputed(250232501L)

    Benchmark.run(
      inputContent = loadText(resourceName),
      solution = solution,
    )
  }

  @Test
  fun `p2 sample`() {
    val resourceName = "day7/p1_sample.txt"
    val solution = Part2
    val input = solution.parseResource(resourceName)
    assertThat(Part2.solve(input)).isComputed(5905)
  }

  @Test
  fun `p2`() {
    val resourceName = "day7/p1_input.txt"
    val solution = Part2
    val input = solution.parseResource(resourceName)
    assertThat(Part2.solve(input)).isComputed(5132675L)

    Benchmark.run(
      inputContent = loadText(resourceName),
      solution = solution,
    )
  }

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
  @MethodSource("part1HandTypeTestProvider")
  fun `p1 compute handType`(handString: String, expected: HandType) {
    val hand = Hand.parse(handString)

    assertThat(HandType.computePart1(hand)).isEqualTo(expected)
  }

  @Test
  fun `p1 card sorting`() {
    val playingCardsUnsorted = listOf(
      PlayingCard.Ace,
      PlayingCard.Jack,
      PlayingCard.Ten,
      PlayingCard.Two,
    )

    assertThat(playingCardsUnsorted.sortedWith(Part1.cardComparator)).containsExactly(
      PlayingCard.Ace,
      PlayingCard.Jack,
      PlayingCard.Ten,
      PlayingCard.Two,
    )
  }

  @ParameterizedTest(name = "hand = {0}, expected = {1}")
  @MethodSource("part2HandTypeTestProvider")
  fun `p2 compute handType`(handString: String, expected: HandType) {
    val hand = Hand.parse(handString)

    assertThat(HandType.computePart2(hand)).isEqualTo(expected)
  }

  @Test
  fun `p2 card sorting`() {
    val playingCardsUnsorted = listOf(
      PlayingCard.Ace,
      PlayingCard.Jack,
      PlayingCard.Ten,
      PlayingCard.Two,
    )

    assertThat(playingCardsUnsorted.sortedWith(cardComparator)).containsExactly(
      PlayingCard.Ace,
      PlayingCard.Ten,
      PlayingCard.Two,
      PlayingCard.Jack,
    )
  }

  @Test
  fun `p2 hand sorting`() {
    val aces = Hand.parse("AAAAA")
    val jokers = Hand.parse("JJJJJ")
    val twos = Hand.parse("22222")

    assertThat(listOf(aces, jokers, twos).sortedWith(Part2.handComparator))
      .containsExactly(
        aces,
        twos,
        jokers,
      )
  }

  companion object {
    @JvmStatic
    fun part1HandTypeTestProvider(): List<Array<Any>> = listOf(
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

    @JvmStatic
    fun part2HandTypeTestProvider(): List<Array<Any>> = listOf(
      arrayOf("JJJJJ", HandType.FiveOfAKind),

      arrayOf("AAAAJ", HandType.FiveOfAKind),
      arrayOf("AAAJJ", HandType.FiveOfAKind),
      arrayOf("AAJJJ", HandType.FiveOfAKind),
      arrayOf("AJJJJ", HandType.FiveOfAKind),
      arrayOf("AAAAA", HandType.FiveOfAKind),

      arrayOf("AAAAQ", HandType.FourOfAKind),
      arrayOf("AAAJQ", HandType.FourOfAKind),
      arrayOf("QJAAA", HandType.FourOfAKind),
      arrayOf("QJJJA", HandType.FourOfAKind),

      arrayOf("AA222", HandType.FullHouse),
      arrayOf("AA2J2", HandType.FullHouse),
      arrayOf("A22A2", HandType.FullHouse),
      arrayOf("AJ2A2", HandType.FullHouse),
      arrayOf("22AA2", HandType.FullHouse),

      arrayOf("22QA2", HandType.ThreeOfAKind),
      arrayOf("2JQA2", HandType.ThreeOfAKind),
      arrayOf("J2245", HandType.ThreeOfAKind),

      arrayOf("2244A", HandType.TwoPair),
      arrayOf("24A2A", HandType.TwoPair),

      arrayOf("2457J", HandType.OnePair),
      arrayOf("24572", HandType.OnePair),
      arrayOf("AA425", HandType.OnePair),

      arrayOf("98746", HandType.HighCard),
    )
  }
}
