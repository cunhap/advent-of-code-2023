fun main() {
    fun showOfCubes(line: String): List<String> {
        return line.substringAfter(": ").split(";").map { it.trim() }
    }

    fun cubesInRound(round: String): List<String> {
        return round.split(",").map { it.trim() }
    }

    fun colorToAmount(cubes: List<String>): Map<String, Int> {
        return cubes.map { it.split(" ") }.associate { it[1] to it[0].toInt() }
    }

    fun part1(input: List<String>): Int {
        val possibleCubes = mapOf("red" to 12, "green" to 13, "blue" to 14)
        return input.sumOf { line ->
            val idOfGame = line.substringBefore(":").substringAfter("Game ").toInt()
            val showOfCubes = showOfCubes(line)
            val gamePossible = showOfCubes.all { round ->
                val cubes = cubesInRound(round)
                val colorToAmountMap = colorToAmount(cubes)
                colorToAmountMap.all { (color, amount) ->
                    possibleCubes[color]!! >= amount
                }
            }
            if(gamePossible) idOfGame else 0
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val minimalAmountOfCubes = mutableMapOf<String, Int>()
            val showOfCubes = showOfCubes(line)
            showOfCubes.map { round ->
                val cubes = cubesInRound(round)
                val colorToAmountMap = colorToAmount(cubes)
                colorToAmountMap.forEach { (color, amount) ->
                    minimalAmountOfCubes.putIfAbsent(color, amount)
                    if(amount > minimalAmountOfCubes[color]!!) {
                        minimalAmountOfCubes[color] = amount
                    }
                }
            }
            val power = minimalAmountOfCubes.values.fold(1) { acc, i -> acc * i }
            power
        }
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day02_test")
//    check(part1(testInput) == 1)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
