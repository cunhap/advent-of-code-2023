import kotlin.time.measureTime

fun main() {

    fun recursiveDifference(sequence: List<Int>, accumulator: List<List<Int>>): List<List<Int>>{
        if (sequence.all { it == 0 }) return accumulator

        val differences = sequence.windowed(2) {
            it[1] - it[0]
        }

        return accumulator + recursiveDifference(differences, listOf(differences))
    }

    fun calculateNextElement(sequence: List<Int>): Int {
        val differences = recursiveDifference(sequence, listOf())
        val newElemnent = differences.fold(0) { acc, list ->
            acc + list.last()
        }
        return sequence.last() + newElemnent
    }

    fun part1(input: List<String>): Int {
        val nextNumbers = input.sumOf { textSequence ->
            val sequence = textSequence.split(" ").map { it.toInt() }
            calculateNextElement(sequence)
        }
        return nextNumbers
    }


    fun part2(input: List<String>): Int {
        val nextNumbers = input.sumOf { textSequence ->
            val sequence = textSequence.split(" ").map { it.toInt() }.reversed()
            calculateNextElement(sequence)
        }
        return nextNumbers
    }

    val testInput = readInput("Day09_test")
    val testResult1 = part1(testInput)
    println("Test Result part 1: $testResult1")

    val testResult2 = part2(testInput)
    println("Test Result part 2: $testResult2")

    val input = readInput("Day09")
    measureTime {
        println("Result Part 1: ${part1(input)}")
    }.also { println("Time Part 1: $it") }
    measureTime {
        println("Result Part 2: ${part2(input)}")
    }.also { println("Time Part 2: $it") }
}