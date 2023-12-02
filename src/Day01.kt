fun main() {

    fun part1(input: List<String>): Int {
        return input.sumOf {
            val firstDigit = it.first { c -> c.isDigit() }
            val lastDigit = it.last { c -> c.isDigit() }
            "$firstDigit$lastDigit".toInt()
        }
    }

    val digits = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9
    )

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val firstDigitIndex = line.indexOfFirst { c -> c.isDigit() }
            val lastDigitIndex = line.indexOfLast { c -> c.isDigit() }
            val firstDigit = line.findAnyOf(digits.keys, ignoreCase = true, startIndex = 0)?.let {
                val (index, value) = it
                if (index < firstDigitIndex) digits[value] else line[firstDigitIndex].digitToInt()
            } ?: line[firstDigitIndex].digitToInt()
            val lastDigit = line.findLastAnyOf(digits.keys, ignoreCase = true)?.let {
                val (index, value) = it
                if (index > lastDigitIndex) digits[value] else line[lastDigitIndex].digitToInt()
            } ?: line[lastDigitIndex].digitToInt()
            "$firstDigit$lastDigit".toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    //val testInput = readInput("Day01_test")
    //check(part1(testInput) == 1)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
