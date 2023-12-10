package net.navatwo.adventofcode2023

interface GridOperations<T> {
  val rowCount: Int

  val columnCount: Int

  operator fun get(coord: Coord): T

  operator fun get(x: Int, y: Int): T
  fun getOrNull(coord: Coord): T?
  fun getOrNull(x: Int, y: Int): T?

  operator fun set(coord: Coord, value: T)

  operator fun set(x: Int, y: Int, value: T)

  operator fun contains(coord: Coord): Boolean = getOrNull(coord) != null

  override fun toString(): String
}

inline fun GridOperations<*>.forEachCoord(block: (x: Int, y: Int) -> Unit) {
  for (y in 0 until rowCount) {
    for (x in 0 until columnCount) {
      block(x, y)
    }
  }
}

fun GridOperations<*>.coords(): Sequence<Coord> {
  return sequence {
    forEachCoord { x, y ->
      yield(Coord(x, y))
    }
  }
}

val GridOperations<*>.columnIndices: IntRange
  get() = 0 until columnCount

val GridOperations<*>.rowIndices: IntRange
  get() = 0 until rowCount

data class Grid<T>(private val grid: MutableList<MutableList<T>>) : GridOperations<T> {
  val rows: List<List<T>> get() = grid

  override val rowCount = grid.size
  override val columnCount: Int = grid.firstOrNull()?.size ?: 0

  override operator fun get(coord: Coord): T = grid[coord]
  override operator fun get(x: Int, y: Int): T = grid[y][x]

  override fun getOrNull(coord: Coord): T? = grid.getOrNull(coord)
  override fun getOrNull(x: Int, y: Int): T? = grid.getOrNull(y)?.getOrNull(x)

  override operator fun set(coord: Coord, value: T) {
    grid[coord] = value
  }

  override operator fun set(x: Int, y: Int, value: T) {
    grid[y][x] = value
  }

  override fun toString(): String {
    return buildString {
      for (row in rows) {
        for (cell in row) {
          append(cell)
        }
        append('\n')
      }
    }
  }

  companion object {

    inline fun <T> create(rows: Int, columns: Int, init: (x: Int, y: Int) -> T): Grid<T> {
      return Grid(
        MutableList(rows) { y ->
          MutableList(columns) { x ->
            init(x, y)
          }
        },
      )
    }
  }
}

@JvmInline
value class BooleanGrid(private val grid: Array<BooleanArray>) : GridOperations<Boolean> {
  val rows: Array<BooleanArray> get() = grid

  override val rowCount get() = grid.size
  override val columnCount: Int get() = grid.firstOrNull()?.size ?: 0

  override operator fun get(coord: Coord): Boolean = grid[coord]
  override operator fun get(x: Int, y: Int): Boolean = grid[y][x]

  override fun getOrNull(coord: Coord): Boolean? = grid.getOrNull(coord)
  override fun getOrNull(x: Int, y: Int): Boolean? = grid.getOrNull(y)?.getOrNull(x)

  override operator fun set(coord: Coord, value: Boolean) {
    grid[coord] = value
  }

  override operator fun set(x: Int, y: Int, value: Boolean) {
    grid[y][x] = value
  }

  override fun toString(): String {
    return buildString {
      for (row in rows) {
        for (cell in row) {
          append(if (cell) '+' else '.')
        }
        append('\n')
      }
    }
  }

  companion object {
    inline fun create(rows: Int, columns: Int, init: (x: Int, y: Int) -> Boolean): BooleanGrid {
      return BooleanGrid(
        Array(rows) { y ->
          BooleanArray(columns) { x ->
            init(x, y)
          }
        },
      )
    }

    fun create(rows: Int, columns: Int, defaultValue: Boolean): BooleanGrid {
      return create(rows, columns) { _, _ -> defaultValue }
    }

    fun maskFor(grid: GridOperations<*>, defaultValue: Boolean): BooleanGrid {
      return create(grid.rowCount, grid.columnCount) { _, _ -> defaultValue }
    }

    inline fun maskFor(grid: GridOperations<*>, init: (x: Int, y: Int) -> Boolean): BooleanGrid {
      return create(grid.rowCount, grid.columnCount, init)
    }

    fun parse(input: String, trueValue: Char = '+', falseValue: Char = '.'): BooleanGrid {
      require(input.isNotBlank())

      val lines = input.lineSequence().filter { it.isNotBlank() }.toList()

      val grid = create(lines.size, lines.first().length) { x, y ->
        when (val char = lines[y][x]) {
          trueValue -> true
          falseValue -> false
          else -> error("Unexpected char '$char' at ($x, $y)")
        }
      }

      return grid
    }
  }
}

@JvmInline
value class LongGrid(private val grid: Array<LongArray>) : GridOperations<Long> {
  val rows: Array<LongArray> get() = grid

  override val rowCount get() = grid.size
  override val columnCount: Int get() = grid.firstOrNull()?.size ?: 0

  override operator fun get(coord: Coord): Long = grid[coord]
  override operator fun get(x: Int, y: Int): Long = grid[y][x]

  override fun getOrNull(coord: Coord): Long? = grid.getOrNull(coord)
  override fun getOrNull(x: Int, y: Int): Long? = grid.getOrNull(y)?.getOrNull(x)

  override operator fun set(coord: Coord, value: Long) {
    grid[coord] = value
  }

  override operator fun set(x: Int, y: Int, value: Long) {
    grid[y][x] = value
  }

  companion object {
    inline fun create(rows: Int, columns: Int, init: (x: Int, y: Int) -> Long): LongGrid {
      return LongGrid(
        Array(rows) { y ->
          LongArray(columns) { x ->
            init(x, y)
          }
        },
      )
    }

    fun create(rows: Int, columns: Int, defaultValue: Long): LongGrid = create(rows, columns) { _, _ -> defaultValue }

    fun maskFor(grid: GridOperations<*>, defaultValue: Long): LongGrid {
      return create(grid.rowCount, grid.columnCount) { _, _ -> defaultValue }
    }
  }
}
