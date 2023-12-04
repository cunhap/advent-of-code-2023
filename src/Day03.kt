fun main() {

    val directions = listOf(
        Pair(1, 0), // down
        Pair(0, 1), // right
        Pair(-1, 0), // up
        Pair(0, -1), // left
        Pair(1, 1), // down right
        Pair(-1, -1), // up left
        Pair(1, -1), // down left
        Pair(-1, 1) // up right
    )

    fun isWithinBounds(
        newLine: Int,
        engineMap: List<List<Char>>,
        newColumn: Int,
        line: Int
    ) = newLine >= 0 && newLine < engineMap.size && newColumn >= 0 && newColumn < engineMap[line].size

    fun findStartOfNumber(newColumn: Int, lookingAtNew: String): Int {
        var startIndex = newColumn
        while (startIndex >= 0 && lookingAtNew[startIndex].isDigit()) {
            startIndex--
        }
        startIndex++
        return startIndex
    }

    fun getCoordinatesToNumbers(
        row: Int,
        column: Int,
        engineMap: List<List<Char>>,
    ): MutableMap<Pair<Int, Int>, Int> {
        val coordinatesToNumbers = mutableMapOf<Pair<Int, Int>, Int>()
        directions
            .forEach { direction ->
                val nextRow = row + direction.first
                val nextColumn = column + direction.second
                if (isWithinBounds(nextRow, engineMap, nextColumn, row)) {
                    val lookingAtNew = engineMap[nextRow].joinToString("")
                    if (lookingAtNew[nextColumn].isDigit()) {
                        val startIndex = findStartOfNumber(nextColumn, lookingAtNew)
                        val coordinatesOfFirstDigit = Pair(nextRow, startIndex)
                        if (!coordinatesToNumbers.containsKey(coordinatesOfFirstDigit)) {
                            val stringStartingAtFirstDigit = lookingAtNew.substring(startIndex)
                            val numberEndIndex = stringStartingAtFirstDigit.indexOfFirst { !it.isDigit() }
                            val number = if (numberEndIndex != -1) {
                                stringStartingAtFirstDigit.substring(0, numberEndIndex).toInt()
                            }
                            else {
                                stringStartingAtFirstDigit.toInt()
                            }
                            coordinatesToNumbers[coordinatesOfFirstDigit] = number
                        }
                    }
                }
            }
        return coordinatesToNumbers
    }

    fun part1(input: List<String>): Int {
        val engineMap = input.map { line -> line.toCharArray().toList() }
        return engineMap.indices.flatMap { line ->
            val lookingAtLine = engineMap[line].joinToString("")
            lookingAtLine.indices.flatMap { column ->
                val lookingAt = engineMap[line][column]
                if (!lookingAt.isDigit() && lookingAt != '.') {
                    val coordinatesToNumbers = getCoordinatesToNumbers(line, column, engineMap)
                    coordinatesToNumbers.values
                } else emptyList()
            }
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val engineMap = input.map { line -> line.toCharArray().toList() }
        val numbers = mutableListOf<Int>()

        engineMap.indices.forEach { line ->
            val lookingAtLine = engineMap[line].joinToString("")
            lookingAtLine.indices.forEach { column ->
                val lookingAt = engineMap[line][column]
                if (!lookingAt.isDigit() && lookingAt != '.' && lookingAt == '*') {
                    val coordinatesToNumbers = getCoordinatesToNumbers(line, column, engineMap)
                    if (coordinatesToNumbers.size == 2) {
                        val firstNumber = coordinatesToNumbers.values.first()
                        val secondNumber = coordinatesToNumbers.values.last()
                        numbers.add(firstNumber * secondNumber)
                    }
                }
            }
        }
        return numbers.sum()
    }

    // test if implementation meets criteria from the description, like:
    //val testInput = readInput("Day01_test")
    //check(part1(testInput) == 1)

    val input = readInput("Day03")
    part1(input).println() // 522726
    part2(input).println() // 81721933
}


