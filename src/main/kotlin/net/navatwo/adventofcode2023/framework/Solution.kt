package net.navatwo.adventofcode2023.framework

interface Solution<I> {
    fun parse(lines: Sequence<String>): I

    fun solve(input: I): ComputedResult
}
