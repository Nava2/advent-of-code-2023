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

    internal val cardComparator: Comparator<PlayingCard> = Comparator.comparing { it.ordinal }
    internal val handComparator = Hand.comparator(cardComparator)

    private val tripleComparator: Comparator<ComputedTriple> = Comparator
      .comparing<ComputedTriple, HandType> { it.handType }
      .thenBy(handComparator) { it.hand }

    private fun ComputedTriple.Companion.create(hand: Hand, bid: Bid): ComputedTriple {
      return ComputedTriple(hand, HandType.computePart1(hand), bid)
    }

    internal fun HandType.Companion.computePart1(hand: Hand): HandType {
      val cardFrequencies = hand.countCards()
      return computeFromCardFrequencies(cardFrequencies)
    }
  }

  data object Part2 : Day7Solution() {
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

    internal val cardComparator: Comparator<PlayingCard> = Comparator.comparing { card ->
      if (card == PlayingCard.Jack) {
        PlayingCard.Two.ordinal + 50 // really after 2
      } else {
        card.ordinal
      }
    }
    internal val handComparator = Hand.comparator(cardComparator)

    private val tripleComparator: Comparator<ComputedTriple> = Comparator
      .comparing<ComputedTriple, HandType> { it.handType }
      .thenBy(handComparator) { it.hand }

    private fun ComputedTriple.Companion.create(hand: Hand, bid: Bid): ComputedTriple {
      return ComputedTriple(hand, HandType.computePart2(hand), bid)
    }

    internal fun HandType.Companion.computePart2(hand: Hand): HandType {
      val cardFrequencies = hand.countCards()

      val jackCount = cardFrequencies[PlayingCard.Jack]
        // No jacks => no need to upgrade them
        ?: return computeFromCardFrequencies(cardFrequencies)

      // update most frequent, non-Jack card to add count of jacks or add Ace with jack count otherwise
      val mostFrequentCard = cardFrequencies.entries.asSequence()
        .filter { (card, _) -> card != PlayingCard.Jack }
        .sortedWith { p1, p2 ->
          val (card1, count1) = p1
          val (card2, count2) = p2

          val countComparison = count2.compareTo(count1)
          if (countComparison != 0) return@sortedWith countComparison

          cardComparator.compare(card1, card2)
        }
        .maxByOrNull { (_, count) -> count }?.key
        ?: PlayingCard.Ace

      val newFrequencies = cardFrequencies.toMutableMap().apply {
        remove(PlayingCard.Jack)
        compute(mostFrequentCard) { _, count ->
          (count ?: 0) + jackCount
        }
      }

      return computeFromCardFrequencies(newFrequencies)
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
    fun countCards(): MutableMap<PlayingCard, Int> = cards.fold(mutableMapOf()) { acc, card ->
      acc.compute(card) { _, count ->
        (count ?: 0) + 1
      }
      acc
    }

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

    companion object {
      fun computeFromCardFrequencies(cardFrequencies: Map<PlayingCard, Int>): HandType {
        val (highestCountCard, highestCount) = cardFrequencies.entries.maxBy { (_, count) -> count }

        return when {
          cardFrequencies.size == 1 -> FiveOfAKind
          cardFrequencies.size == 2 -> {
            if (highestCount in 2..3) {
              FullHouse
            } else {
              FourOfAKind
            }
          }

          cardFrequencies.size == 3 && highestCount == 3 -> ThreeOfAKind
          cardFrequencies.size == 3 && highestCount == 2 -> TwoPair
          cardFrequencies.size == 4 -> OnePair
          else -> HighCard
        }
      }
    }
  }
}
