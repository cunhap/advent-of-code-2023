import kotlin.math.abs
import kotlin.math.exp
import kotlin.time.measureTime


data class Galaxy(val id: String, override val coordinate: Coordinate) : MapElement
data class Empty(override val coordinate: Coordinate) : MapElement

fun main() {

    fun buildMap(input: List<String>, expansionRate: Int = 2): List<Galaxy> {
        val emptyRows = List(input.size) { rowIndex ->
            if (input[rowIndex].all { it == '.' }) {
                rowIndex
            } else null
        }.filterNotNull().toSet()

        val numberOfColumns = input.first().length
        val columns = List(numberOfColumns) { columnIndex ->
            input.map { row -> row[columnIndex] }
        }
        val emptyColumns = columns.mapIndexed { colIdx, column ->
            if (column.all { it == '.' }) {
                colIdx
            } else null
        }.filterNotNull().toSet()

        val galaxies = input.map { it.toCharArray().toList() }.mapIndexed { rowIndex, row ->
            List(row.size) { colIndex ->
                val char = row[colIndex]
                if (char == '#') {
                    Galaxy("$char$rowIndex$colIndex", Coordinate(rowIndex, colIndex))
                } else null
            }.filterNotNull()
        }.flatten()

        val expandedGalaxies = galaxies.map { galaxy ->
            val row = galaxy.coordinate.row
            val column = galaxy.coordinate.column
            val emptyRowsBefore = emptyRows.filter { it < row }.size
            val emptyColumnsBefore = emptyColumns.filter { it < column }.size
            galaxy.copy(
                coordinate = Coordinate(
                    row = row + (emptyRowsBefore * (expansionRate-1)),
                    column = column + (emptyColumnsBefore * (expansionRate-1))
                )
            )
        }

        return expandedGalaxies
    }

    fun buildPairsGalaxies(galaxies: List<Galaxy>): List<Pair<Galaxy, Galaxy>> {
        return galaxies.mapIndexed { index1, galaxy1 ->
            val pairs = mutableListOf<Pair<Galaxy, Galaxy>>()
            for (index2 in index1 + 1 until galaxies.size) {
                pairs.add(galaxy1 to galaxies[index2])
            }
            pairs
        }.flatten()
    }

    fun part1(input: List<String>): Int {
        val galaxies = buildMap(input)
        val allPairs = buildPairsGalaxies(galaxies)
        val shortestSteps = allPairs.map { (galaxy1, galaxy2) ->
            abs(galaxy1.coordinate.row - galaxy2.coordinate.row) +
                    abs(galaxy1.coordinate.column - galaxy2.coordinate.column)
        }
        return shortestSteps.sum()
    }


    fun part2(input: List<String>): Long {
        val galaxies = buildMap(input, expansionRate = 1000000)
        val allPairs = buildPairsGalaxies(galaxies)
        val shortestSteps = allPairs.map { (galaxy1, galaxy2) ->
            val steps = abs(galaxy1.coordinate.row - galaxy2.coordinate.row) +
                    abs(galaxy1.coordinate.column - galaxy2.coordinate.column)
            steps.toLong()
        }
        return shortestSteps.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    part1(testInput).also { println("Part 1 Test = $it") }
    part2(testInput).also { println("Part 2 Test = $it") }

    val input = readInput("Day11")
    measureTime {
        println("Result Part 1: ${part1(input)}")
    }.also { println("Time Part 1: $it") }
    measureTime {
        println("Result Part 2: ${part2(input)}")
    }.also { println("Time Part 2: $it") }
}
