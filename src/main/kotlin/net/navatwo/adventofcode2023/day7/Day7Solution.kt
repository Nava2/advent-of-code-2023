package net.navatwo.adventofcode2023.day7

import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution

sealed class Day7Solution : Solution<Day7Solution.Game> {
  data object Part1 : Day7Solution() {
    override fun solve(input: Game): ComputedResult {
      // TODO Implement
      return ComputedResult.Simple(32L)
    }
  }

  override fun parse(lines: Sequence<String>): Game {
    val handPairs = lines.map { line ->
      val (hand, bid) = line.split(" ", limit = 2)
      Hand.parse(hand) to Bid(bid.toLong())
    }

    return Game(handPairs.toList())
  }

  @JvmInline
  value class Hand(val cards: List<PlayingCard>) {
    companion object {
      fun parse(line: CharSequence): Hand {
        val cards = line.map { PlayingCard.byShortName(it) }
        return Hand(cards)
      }
    }
  }

  @JvmInline
  value class Bid(val long: Long)

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
      fun compute(hand: Hand): HandType {
        val cardCounts = hand.cards.fold(mutableMapOf<PlayingCard, Int>()) { acc, card ->
          acc.compute(card) { _, count ->
            (count ?: 0) + 1
          }
          acc
        }

        val (highestCountCard, highestCount) = cardCounts.entries.maxBy { (_, count) -> count }

        return when {
          cardCounts.size == 1 -> FiveOfAKind
          cardCounts.size == 2 -> {
            if (highestCount in 2..3) {
              FullHouse
            } else {
              FourOfAKind
            }
          }
          cardCounts.size == 3 && highestCount == 3 -> ThreeOfAKind
          cardCounts.size == 3 && highestCount == 2 -> TwoPair
          cardCounts.size == 4 -> OnePair
          else -> HighCard
        }
      }
    }
  }
}
