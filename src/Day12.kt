import java.util.Collections
import kotlin.math.pow
import kotlin.time.measureTime

private val day = object {}::class.java.name.substringBefore("Kt").substringAfter("Day").toInt()

fun main() {

    val cache = mutableMapOf<Pair<String, List<Int>>, Long>()
    fun count(config: String, groups: List<Int>): Long {
        if (groups.isEmpty()) return if ("#" in config) 0 else 1
        if (config.isEmpty()) return 0

        return cache.getOrPut(config to groups) {
            var result = 0L
            if (config.first() in ".?")
                result += count(config.drop(1), groups)
            if (config.first() in "#?" && groups.first() <= config.length && "." !in config.take(groups.first()) && (groups.first() == config.length || config[groups.first()] != '#'))
                result += count(config.drop(groups.first() + 1), groups.drop(1))
            result
        }
    }

    fun part1(input: List<String>): Long {
        return input.sumOf { line ->
            val (springs, damagedGroupsInformation) = line.split(" ")
            count(springs, damagedGroupsInformation.split(",").map { it.toInt() })
        }
    }


    fun part2(input: List<String>): Long {
        return input.sumOf { line ->
            val (springs, damagedGroupsInformation) = line.split(" ")
            count(
                "${springs.first()}?".repeat(5).dropLast(1),
                "${damagedGroupsInformation},".repeat(5).split(",").dropBlanks().map(String::toInt)
            )
        }

    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    part1(testInput).also { println("Part 1 Test = $it") }
    part2(testInput).also { println("Part 2 Test = $it") }

    val input = readInput("Day${day}")
    measureTime {
        println("Result Part 1: ${part1(input)}")
    }.also { println("Time Part 1: $it") }
    measureTime {
        println("Result Part 2: ${part2(input)}")
    }.also { println("Time Part 2: $it") }
}
