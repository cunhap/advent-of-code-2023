import kotlin.time.measureTime

fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b

fun createMap(input: List<String>): Map<String, Pair<String, String>> {
    return input.drop(2).associate {
        val (nodeId, destinations) = it.split(" = ")
        val (left, right) = destinations.removePrefix("(").removeSuffix(")").split(", ")
        nodeId to Pair(left, right)
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val moves = input[0]
        val map = createMap(input)

        return sequence {
            var currentValue = "AAA"
            while (true) {
                for (move in moves) {
                    currentValue = if (move == 'L') map[currentValue]!!.first else map[currentValue]!!.second
                    yield(currentValue)
                }
            }
        }.takeWhile { it != "ZZZ" }.count()
    }


    fun part2(input: List<String>): Long {
        val map = createMap(input)
        val counts = map.keys.filter { it.endsWith("A") }.map { startingPoint ->
            sequence {
                var current = startingPoint
                while (!current.endsWith("Z")) {
                    for (move in input[0]) {
                        current = if (move == 'R') map[current]!!.second else map[current]!!.first
                        yield(current)
                    }
                }
            }.count().toLong()
        }
        return counts.reduce { acc, i -> lcm(acc, i) }
    }

    val testInput = readInput("Day08_test")
    val testResult1 = part1(testInput)
    println("Test Result part 1: $testResult1")

    val testResult2 = part2(testInput)
    println("Test Result part 2: $testResult2")

    val input = readInput("Day08")
    measureTime {
        println("Result Part 1: ${part1(input)}")
    }.also { println("Time Part 1: $it") }
    measureTime {
        println("Result Part 2: ${part2(input)}")
    }.also { println("Time Part 2: $it") }
}