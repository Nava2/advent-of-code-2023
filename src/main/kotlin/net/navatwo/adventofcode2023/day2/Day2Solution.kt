package net.navatwo.adventofcode2023.day2

import net.navatwo.adventofcode2023.framework.ComputedResult
import net.navatwo.adventofcode2023.framework.Solution

sealed class Day2Solution : Solution<List<Day2Solution.Game>> {
  data object Part1 : Day2Solution() {
    override fun solve(input: List<Game>): ComputedResult {
      return ComputedResult.Simple(4)
    }
  }


  override fun parse(lines: Sequence<String>): List<Game> {
    return lines
      .map { line ->
        val (gamePreamble, pullValues) = line.split(':', limit = 2)
        val gameId = gamePreamble.slice("Game ".length..< gamePreamble.length).toInt()
        val pulls = pullValues.splitToSequence(';')
          .map(Game.Pull::parse)
          .toList()

        Game(gameId, pulls)
      }
      .toList()
  }

  data class Game(
    val id: Int,
    val pulls: List<Pull>,
  ) {
    @JvmInline
    value class Pull(
      val pulls: Map<Colour, Int>,
    ) {
      companion object {
        fun parse(input: String): Pull {
          val pullMap = input.splitToSequence(',')
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .map { hand ->
              val (count, colour) = hand.split(' ', limit = 2)
              Colour.valueOfLowered(colour) to count.toInt()
            }
            .toMap()

          return Pull(pullMap)
        }
      }
    }

    enum class Colour {
      Red,
      Green,
      Blue,
      ;

      val lowered: String = name.lowercase()

      companion object {
        private val loweredValues = entries.associateBy { it.lowered }

        fun valueOfLowered(name: String): Colour {
          return loweredValues.getValue(name)
        }
      }
    }
  }
}
