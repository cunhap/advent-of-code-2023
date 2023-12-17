import kotlin.time.measureTime

private val day = object {}::class.java.name.substringBefore("Kt").substringAfter("Day").toInt()

fun main() {

    val cacheOfCycles = mutableMapOf<List<String>, List<String>>()

    fun moveRocks(map: List<String>, direction: Direction): List<String> {
        val rotatedMap = when (direction) {
            Direction.UP, Direction.DOWN -> rotateMap(map)
            else -> map
        }
        val movedRocksMap = rotatedMap.map { line ->
            val movedRocks = line.toCharArray()
            when {
                (direction == Direction.UP || direction == Direction.LEFT) -> {
                    for (space in line.indices) {
                        val element = movedRocks[space]
                        if (element == 'O') {
                            var newSpace = space
                            while (newSpace > 0 && movedRocks[newSpace - 1] == '.') {
                                movedRocks[newSpace] = '.'
                                movedRocks[newSpace - 1] = 'O'
                                newSpace--
                            }
                        }
                    }
                }

                (direction == Direction.DOWN || direction == Direction.RIGHT) -> {
                    for (space in line.indices.reversed()) {
                        val element = movedRocks[space]
                        if (element == 'O') {
                            var newSpace = space
                            while (newSpace < line.length - 1 && newSpace >= 0 && movedRocks[newSpace + 1] == '.') {
                                movedRocks[newSpace] = '.'
                                movedRocks[newSpace + 1] = 'O'
                                newSpace++
                            }
                        }
                    }
                }
            }
            movedRocks.joinToString("")
        }
        return when (direction) {
            Direction.UP, Direction.DOWN -> rotateMap(movedRocksMap)
            else -> movedRocksMap
        }
    }

    fun calculateLoad(map: List<String>): Long {
        val sizeOfStructure = map[0].length.toLong()
        val totalLoad =
            map.mapIndexed { index, row -> row.count { it == 'O' } * (sizeOfStructure - index) }.sum()
        return totalLoad
    }

    fun part1(input: List<String>): Long {
        val rocksMoved = moveRocks(input, Direction.UP)
        val totalLoad = calculateLoad(rocksMoved)
        return totalLoad
    }


    fun doCycle(map: List<String>): List<String> {
        val movedUp = moveRocks(map, Direction.UP)
        val movedLeft = moveRocks(movedUp, Direction.LEFT)
        val movedDown = moveRocks(movedLeft, Direction.DOWN)
        val movedRight = moveRocks(movedDown, Direction.RIGHT)
        return movedRight
    }

    fun part2(input: List<String>): Long {
        var step = 0
        var skip = true
        val seen = mutableMapOf<String, Int>()
        var map = input
        while (step < 1_000_000_000) {
            map = doCycle(map)
            if (skip) {
                when (val key = map.joinToString("\n")) {
                    in seen -> {
                        val cycle = step - seen.getValue(key)
                        val cyclesLeft = (1_000_000_000 - step) / cycle
                        step += cycle * cyclesLeft
                        skip = false
                    }
                    else -> seen[key] = step
                }
            }

            step++
        }

        return calculateLoad(map)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    part1(testInput).also { println("Part 1 Test = $it") }
//    part2(testInput).also { println("Part 2 Test = $it") }

    val input = readInput("Day${day}")
    measureTime {
        println("Result Part 1: ${part1(input)}")
    }.also { println("Time Part 1: $it") }
    measureTime {
        println("Result Part 2: ${part2(input)}") // 83516
    }.also { println("Time Part 2: $it") }
}
