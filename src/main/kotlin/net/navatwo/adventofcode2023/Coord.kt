@file:Suppress("TooManyFunctions")

package net.navatwo.adventofcode2023

data class Coord(val x: Int, val y: Int) {
  inline fun forEachNeighbour(distance: Int = 1, consumer: (x: Int, y: Int) -> Unit) {
    for (y in y - distance..y + distance) {
      for (x in x - distance..x + distance) {
        if (x == this.x && y == this.y) continue
        consumer(x, y)
      }
    }
  }

  fun up(distance: Int = 1): Coord = copy(y = y - distance)
  fun down(distance: Int = 1): Coord = copy(y = y + distance)
  fun left(distance: Int = 1): Coord = copy(x = x - distance)
  fun right(distance: Int = 1): Coord = copy(x = x + distance)

  fun upLeft(distance: Int = 1): Coord = copy(x = x - distance, y = y - distance)
  fun upRight(distance: Int = 1): Coord = copy(x = x + distance, y = y - distance)
  fun downLeft(distance: Int = 1): Coord = copy(x = x - distance, y = y + distance)
  fun downRight(distance: Int = 1): Coord = copy(x = x + distance, y = y + distance)
}

fun <T> List<List<T>>.getCoord(x: Int, y: Int): T? {
  return getOrNull(Coord(x, y))
}

operator fun <T> List<List<T>>.get(coord: Coord): T {
  val (x, y) = coord
  return this[y][x]
}

fun <T> List<List<T>>.getOrNull(coord: Coord): T? {
  val (x, y) = coord
  val row = getOrNull(y)
  return row?.getOrNull(x)
}

operator fun <T> MutableList<MutableList<T>>.set(coord: Coord, value: T) {
  this[coord.y][coord.x] = value
}

operator fun Array<BooleanArray>.get(coord: Coord): Boolean {
  return this[coord.y][coord.x]
}

operator fun Array<IntArray>.get(coord: Coord): Int {
  return this[coord.y][coord.x]
}

operator fun Array<LongArray>.get(coord: Coord): Long {
  return this[coord.y][coord.x]
}

fun Array<BooleanArray>.getOrNull(coord: Coord): Boolean? {
  val row = getOrNull(coord.y)
  return row?.getOrNull(coord.x)
}

fun Array<LongArray>.getOrNull(coord: Coord): Long? {
  val row = getOrNull(coord.y)
  return row?.getOrNull(coord.x)
}

operator fun Array<BooleanArray>.set(coord: Coord, value: Boolean) {
  this[coord.y][coord.x] = value
}

operator fun Array<LongArray>.set(coord: Coord, value: Long) {
  this[coord.y][coord.x] = value
}

operator fun Array<IntArray>.set(coord: Coord, value: Int) {
  this[coord.y][coord.x] = value
}
