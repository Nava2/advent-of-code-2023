package net.navatwo.adventofcode2023.day7

import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution

sealed class Day7Solution : Solution<Day7Solution.Game> {

  data object Part1 : Day7Solution() {
    override fun solve(input: Game): ComputedResult {
      val computeHandTypes = input.handPairs.asSequence()
        .map { (hand, bid) ->
          ComputedTriple.create(hand, bid)
        }

      val rankedHands = computeHandTypes.toSortedSet(tripleComparator.reversed())

      val rankedBids = rankedHands.asSequence().map { it.bid }

      val result = rankedBids.withIndex()
        .fold(0L) { acc, (index, bid) ->
          acc + (index + 1) * bid.value
        }
      return ComputedResult.Simple(result)
    }

    private val cardComparator: Comparator<PlayingCard> = Comparator.comparing { it.ordinal }

    private val tripleComparator: Comparator<ComputedTriple> = Comparator
      .comparing<ComputedTriple, HandType> { it.handType }
      .thenBy(Hand.comparator(cardComparator)) { it.hand }

    private fun ComputedTriple.Companion.create(hand: Hand, bid: Bid): ComputedTriple {
      return ComputedTriple(hand, HandType.computePart1(hand), bid)
    }

    internal fun HandType.Companion.computePart1(hand: Hand): HandType {
      val cardCounts = hand.cards.fold(mutableMapOf<PlayingCard, Int>()) { acc, card ->
        acc.compute(card) { _, count ->
          (count ?: 0) + 1
        }
        acc
      }

      val (highestCountCard, highestCount) = cardCounts.entries.maxBy { (_, count) -> count }

      return when {
        cardCounts.size == 1 -> HandType.FiveOfAKind
        cardCounts.size == 2 -> {
          if (highestCount in 2..3) {
            HandType.FullHouse
          } else {
            HandType.FourOfAKind
          }
        }

        cardCounts.size == 3 && highestCount == 3 -> HandType.ThreeOfAKind
        cardCounts.size == 3 && highestCount == 2 -> HandType.TwoPair
        cardCounts.size == 4 -> HandType.OnePair
        else -> HandType.HighCard
      }
    }
  }

  override fun parse(lines: Sequence<String>): Game {
    val handPairs = lines.map { line ->
      val (hand, bid) = line.split(" ", limit = 2)
      Hand.parse(hand) to Bid(bid.toLong())
    }

    return Game(handPairs.toList())
  }

  data class ComputedTriple(
    val hand: Hand,
    val handType: HandType,
    val bid: Bid,
  ) {
    companion object
  }

  @JvmInline
  value class Hand(val cards: List<PlayingCard>) {
    override fun toString(): String = cards.joinToString(separator = "") { it.shortName.toString() }

    companion object {
      fun comparator(cardComparator: Comparator<PlayingCard>): Comparator<Hand> = Comparator { h1, h2 ->
        for ((c1, c2) in h1.cards.asSequence().zip(h2.cards.asSequence())) {
          val cardComparison = cardComparator.compare(c1, c2)
          if (cardComparison != 0) return@Comparator cardComparison
        }

        0
      }

      fun parse(line: CharSequence): Hand {
        val cards = line.map { PlayingCard.byShortName(it) }
        return Hand(cards)
      }
    }
  }

  @JvmInline
  value class Bid(val value: Long)

  @JvmInline
  value class Game(val handPairs: List<Pair<Hand, Bid>>)

  enum class PlayingCard(val shortName: Char) : Comparable<PlayingCard> {
    Ace('A'),
    King('K'),
    Queen('Q'),
    Jack('J'),
    Ten('T'),
    Nine('9'),
    Eight('8'),
    Seven('7'),
    Six('6'),
    Five('5'),
    Four('4'),
    Three('3'),
    Two('2'),
    ;

    companion object {
      private val byShortName = entries.associateBy { it.shortName }

      fun byShortName(shortName: Char): PlayingCard {
        return byShortName.getValue(shortName)
      }
    }
  }

  enum class HandType {
    FiveOfAKind,
    FourOfAKind,
    FullHouse,
    ThreeOfAKind,
    TwoPair,
    OnePair,
    HighCard,
    ;

    companion object
  }
}
