import kotlin.math.absoluteValue
import kotlin.time.measureTime

fun main() {

    fun findHorizontalMirror(pattern: List<String>, goalTotal: Int): Int? =
        (0 until pattern.lastIndex).firstNotNullOfOrNull { start ->
            if (createMirrorRanges(start, pattern.lastIndex)
                    .sumOf { (up, down) ->
                        pattern[up] diff pattern[down]
                    } == goalTotal
            ) (start + 1) * 100
            else null
        }

    fun findVerticalMirror(pattern: List<String>, goalTotal: Int): Int? =
        (0 until pattern.first().lastIndex).firstNotNullOfOrNull { start ->
            if (createMirrorRanges(start, pattern.first().lastIndex)
                    .sumOf { (left, right) ->
                        pattern.columnToString(left) diff pattern.columnToString(right)
                    } == goalTotal
            ) start + 1
            else null
        }


    fun findMirror(pattern: List<String>, goalTotal: Int): Int =
        findHorizontalMirror(pattern, goalTotal) ?:
        findVerticalMirror(pattern, goalTotal) ?:
        throw IllegalStateException("Pattern does not mirror")

    fun part1(input: List<String>): Long {
        return parseInput(input).sumOf { findMirror(it, 0) }.toLong()
    }


    fun part2(input: List<String>): Long {
        return parseInput(input).sumOf { findMirror(it, 1) }.toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    part1(testInput).also { println("Part 1 Test = $it") }
    part2(testInput).also { println("Part 2 Test = $it") }

    val input = readInput("Day13")
    measureTime {
        println("Result Part 1: ${part1(input)}")
    }.also { println("Time Part 1: $it") }
    measureTime {
        println("Result Part 2: ${part2(input)}")
    }.also { println("Time Part 2: $it") }
}

fun createMirrorRanges(start: Int, max: Int): List<Pair<Int, Int>> =
    (start downTo 0).zip(start + 1..max)

fun List<String>.columnToString(column: Int): String =
    this.map { it[column] }.joinToString("")

fun parseInput(input: List<String>): List<List<String>> =
    input.joinToString("\n").split("\n\n").map { it.lines() }