import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.time.measureTime

private val day = object {}::class.java.name.substringBefore("Kt").substringAfter("Day").toInt()

fun main() {

    fun calculateMirror(map: List<String>, lowerIndex: Int, higherIndex: Int, maxAllowed: Int = 0): Int? {
        val numberOfMatches = minOf(abs(0 - lowerIndex), abs(map.size - 1 - higherIndex))

        val underElements = map.subList(lowerIndex - numberOfMatches, lowerIndex+1).reversed()
        val overElements = map.subList(higherIndex, higherIndex + numberOfMatches + 1)

        val differences = underElements.zip(overElements).sumOf { (under, over) -> under diff over }

        return if (differences == maxAllowed)
            abs(0 - lowerIndex) + 1 else null
    }

    fun findAllTwoContiguousMatching(map: List<String>, maxAllowed: Int = 0): Int? {
        for (i in map.indices) {
            val j = (i + 1).coerceAtMost(map.size - 1)
            if (i != j && (map[i] diff map[j] == maxAllowed || map[i] == map[j])) {
                val numberOfMatches = calculateMirror(map, i, j, maxAllowed)
                if (numberOfMatches != null)
                    return numberOfMatches
            }

        }
        return null
    }

    fun rotateMap(inputMap: List<String>): List<String> {
        val numberOfColumns = inputMap[0].length
        val columns = (0 until numberOfColumns).map { i ->
            inputMap.map { it[i] }.joinToString("")
        }
        return columns
    }


    fun part1(input: List<String>): Long {
        val mapSeparations = input.mapIndexed { index, s -> if (s.isEmpty()) index else null }.filterNotNull()
        val maps = mapSeparations.mapIndexed { index, mapSeparatorIndex ->
            if (index == 0) input.subList(0, mapSeparatorIndex)
            else input.subList(mapSeparations[index - 1] + 1, mapSeparatorIndex)
        } + listOf(input.subList(mapSeparations.last() + 1, input.size))
        val allRowsAndColumns = maps.map {
            val numberOfRowsMirror = findAllTwoContiguousMatching(it) ?: 0
            val numberOfColumnsMirror = findAllTwoContiguousMatching(rotateMap(it)) ?: 0
            Pair(numberOfRowsMirror, numberOfColumnsMirror)
        }.reduce { acc, pair ->
            acc.first + pair.first to acc.second + pair.second
        }
        return allRowsAndColumns.second.toLong() + 100L * allRowsAndColumns.first.toLong()
    }


    fun part2(input: List<String>): Long {
        val mapSeparations = input.mapIndexed { index, s -> if (s.isEmpty()) index else null }.filterNotNull()
        val maps = mapSeparations.mapIndexed { index, mapSeparatorIndex ->
            if (index == 0) input.subList(0, mapSeparatorIndex)
            else input.subList(mapSeparations[index - 1] + 1, mapSeparatorIndex)
        } + listOf(input.subList(mapSeparations.last() + 1, input.size))
        val allRowsAndColumns = maps.map {
            val numberOfRowsMirror = findAllTwoContiguousMatching(it, 1) ?: 0
            val numberOfColumnsMirror = findAllTwoContiguousMatching(rotateMap(it), 1) ?: 0
            Pair(numberOfRowsMirror, numberOfColumnsMirror)
        }.reduce { acc, pair ->
            acc.first + pair.first to acc.second + pair.second
        }
        return allRowsAndColumns.second.toLong() + 100L * allRowsAndColumns.first.toLong()
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
