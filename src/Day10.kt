import kotlin.time.measureTime

/*
| is a vertical pipe connecting north and south.
- is a horizontal pipe connecting east and west.
L is a 90-degree bend connecting north and east.
J is a 90-degree bend connecting north and west.
7 is a 90-degree bend connecting south and west.
F is a 90-degree bend connecting south and east.
. is ground; there is no pipe in this tile.
S is the starting position of the animal; there is a pipe on this tile, but your sketch doesn't show what shape the pipe has.
*/

data class Pipe(
    val symbol: Char,
    val coordinates: Coordinate,
    val connectedCoordinate1: Coordinate,
    val connectedCoordinate2: Coordinate,
    val allowSlip: Boolean,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Pipe) return false

        if (symbol != other.symbol) return false
        if (coordinates != other.coordinates) return false

        return true
    }

    override fun hashCode(): Int {
        var result = symbol.hashCode()
        result = 31 * result + coordinates.hashCode()
        return result
    }
}

fun coordinatesOfConnectedPipes(
    pipeValue: Char,
    pipeCoordinates: Coordinate
): Pair<Coordinate, Coordinate> {
    val (row, column) = pipeCoordinates
    return when (pipeValue) {
        '|' -> Pair(Coordinate(row - 1, column), Coordinate(row + 1, column))
        '-' -> Pair(Coordinate(row, column - 1), Coordinate(row, column + 1))
        'L' -> Pair(Coordinate(row - 1, column), Coordinate(row, column + 1))
        'J' -> Pair(Coordinate(row, column - 1), Coordinate(row - 1, column))
        '7' -> Pair(Coordinate(row, column - 1), Coordinate(row + 1, column))
        'F' -> Pair(Coordinate(row + 1, column), Coordinate(row, column + 1))
        else -> Pair(Coordinate(0, 0), Coordinate(0, 0))
    }
}

val directions = listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))

fun Pipe(map: List<List<Char>>, coordinates: Coordinate): Pipe {
    val (row, column) = coordinates
    try {
        map[row][column]
    } catch (e: IndexOutOfBoundsException) {
        throw e
    }
    val pipeSymbol = map[row][column]
    val connectedCoordinates = coordinatesOfConnectedPipes(pipeSymbol, coordinates)
    val allowSlip = pipeSymbol != '|' && pipeSymbol != '-'
    return Pipe(pipeSymbol, coordinates, connectedCoordinates.first, connectedCoordinates.second, allowSlip)
}

fun main() {

    fun getLoop(map: List<List<Char>>): MutableList<Pipe> {
        var startingPipe: Pipe? = null

        for (row in map.indices) {
            for (column in map[row].indices) {
                if (map[row][column] == 'S') {
                    val startingCoordinate = Coordinate(row, column)
                    val pipesAroundViable = directions.map {
                        val pipeCoordinates = Coordinate(row + it.first, column + it.second)
                        if (pipeCoordinates.row !in map.indices || pipeCoordinates.column !in map[pipeCoordinates.row].indices) {
                            return@map null
                        }
                        Pipe(map, pipeCoordinates)
                    }.filterNotNull()
                        .filter { it.connectedCoordinate1 != startingCoordinate || it.connectedCoordinate2 != startingCoordinate }
                    val (connectedPipe1, connectedPipe2) = pipesAroundViable
                    startingPipe =
                        Pipe('S', Coordinate(row, column), connectedPipe1.coordinates, connectedPipe2.coordinates, false)
                    break
                }
            }
            if (startingPipe != null) break
        }

        val loop = mutableListOf(startingPipe!!)
        do {
            val previousNode = loop.last()
            val nextCoordinate =
                if (loop.size > 1 && previousNode.connectedCoordinate1 == loop[loop.size - 2].coordinates) {
                    previousNode.connectedCoordinate2
                } else {
                    previousNode.connectedCoordinate1
                }
            val nextPipe = Pipe(map, nextCoordinate)
            loop.add(nextPipe)
        } while (nextPipe != startingPipe)
        return loop
    }

    fun part1(input: List<String>): Int {
        val map = input.map { it.toCharArray().toList() }

        val loop = getLoop(map)

        return loop.size / 2
    }


    fun part2(input: List<String>): Int {
        val map = input.map { it.toCharArray().toList() }
        val loop = getLoop(map)
        val coordinatesToPipes = loop.associateBy { it.coordinates }
        return 0
    }

    val testInput = readInput("Day10_test")
    val testResult1 = part1(testInput)
    println("Test Result part 1: $testResult1")

    val testResult2 = part2(testInput)
    println("Test Result part 2: $testResult2")

    val input = readInput("Day10")
    measureTime {
        println("Result Part 1: ${part1(input)}")
    }.also { println("Time Part 1: $it") }
    measureTime {
        println("Result Part 2: ${part2(input)}")
    }.also { println("Time Part 2: $it") }
}