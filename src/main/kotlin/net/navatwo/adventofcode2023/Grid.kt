package net.navatwo.adventofcode2023

@JvmInline
value class Grid<T>(private val grid: MutableList<MutableList<T>>) {
  val rows: List<List<T>> get() = grid

  val rowCount get() = grid.size

  operator fun get(coord: Coord): T = grid[coord]
  operator fun get(x: Int, y: Int): T = grid[y][x]

  fun getOrNull(coord: Coord): T? = grid.getOrNull(coord)
  fun getOrNull(x: Int, y: Int): T? = grid.getOrNull(y)?.getOrNull(x)

  operator fun set(coord: Coord, value: T) {
    grid[coord] = value
  }

  operator fun set(x: Int, y: Int, value: T) {
    grid[y][x] = value
  }

  inline fun forEachCoord(block: (x: Int, y: Int) -> Unit) {
    val rows = rows
    for (y in rows.indices) {
      for (x in rows[y].indices) {
        block(x, y)
      }
    }
  }
}

@JvmInline
value class BooleanGrid(private val grid: Array<BooleanArray>) {
  val rows: Array<BooleanArray> get() = grid

  val rowCount get() = grid.size

  operator fun get(coord: Coord): Boolean = grid[coord]
  operator fun get(x: Int, y: Int): Boolean = grid[y][x]

  fun getOrNull(coord: Coord): Boolean? = grid.getOrNull(coord)
  fun getOrNull(x: Int, y: Int): Boolean? = grid.getOrNull(y)?.getOrNull(x)

  operator fun set(coord: Coord, value: Boolean) {
    grid[coord] = value
  }

  operator fun set(x: Int, y: Int, value: Boolean) {
    grid[y][x] = value
  }

  inline fun forEachCoord(block: (x: Int, y: Int) -> Unit) {
    val rows = rows
    for (y in rows.indices) {
      for (x in rows[y].indices) {
        block(x, y)
      }
    }
  }
}
