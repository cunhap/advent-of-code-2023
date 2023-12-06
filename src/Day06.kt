import kotlin.time.measureTime

fun main() {

    /*
    Time:      7  15   30
     Distance:  9  40  200
      */

    fun runningDistance(timeHoldingButton: Long, timeLeft: Long) = timeHoldingButton * timeLeft

    fun findFirstNonViable(timeLeft: Long, distance: Long, maxTime: Long): Pair<Long, Long> {
        val timeHoldingButton = timeLeft / 2
        val timeLeftAfterHoldingButton = maxTime - timeHoldingButton
        val distanceThatCanRun = runningDistance(timeHoldingButton, timeLeftAfterHoldingButton)
        return if (distanceThatCanRun > distance) {
            findFirstNonViable(timeHoldingButton, distance, maxTime)
        } else {
            timeHoldingButton to timeLeft
        }
    }

    fun findMinimumViableTime(maxTime: Long, distance: Long): Long {
        val (minNonViable, definitelyViable) = findFirstNonViable(maxTime, distance, maxTime)
        val searchRange = minNonViable..definitelyViable
        return searchRange.first { timeHoldingButton ->
            val runningDistance = runningDistance(timeHoldingButton, maxTime - timeHoldingButton)
            runningDistance > distance
        }
    }

    fun findMaximumNonViableTime(maxHoldingTime: Long, distance: Long, maxTime: Long): Pair<Long, Long> {
        val timeHoldingButton = (maxTime + maxHoldingTime) / 2
        val timeLeftAfterHoldingButton = maxTime - timeHoldingButton
        val distanceThatCanRun = runningDistance(timeHoldingButton, timeLeftAfterHoldingButton)
        return if (distanceThatCanRun > distance && timeLeftAfterHoldingButton > 0) {
            findMaximumNonViableTime(timeHoldingButton, distance, maxTime)
        } else {
            timeHoldingButton to maxHoldingTime
        }
    }

    fun findMaximumViableTime(maxTime: Long, distance: Long): Long {
        val (maxNonViable, lastKnownViable) = findMaximumNonViableTime(0, distance, maxTime)
        val searchRange = lastKnownViable..maxNonViable
        return searchRange.last { timeHoldingButton ->
            val runningDistance = runningDistance(timeHoldingButton, maxTime - timeHoldingButton)
            runningDistance > distance
        }
    }

    fun numberOfWinningPossibilities(time: Long, distance: Long): Long {
        val minimumViableTime = findMinimumViableTime(time, distance)
        val maxViableTime = findMaximumViableTime(time, distance)
        return (minimumViableTime..maxViableTime).count().toLong()
    }

    fun part1(input: List<String>): Long {
        val times = input[0].substringAfter("Time:").split(" ").filter { it.isNotBlank() }.map { it.trim().toLong() }
        val distances =
            input[1].substringAfter("Distance:").split(" ").filter { it.isNotBlank() }.map { it.trim().toLong() }
        val races = times.zip(distances)
        return races.map { (time, distance) ->
            numberOfWinningPossibilities(time, distance)
        }.reduce { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Long {
        val time = input[0].substringAfter("Time:").trim().replace(" ", "").toLong()
        val distance =
            input[1].substringAfter("Distance:").trim().replace(" ", "").toLong()
        return numberOfWinningPossibilities(time, distance)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    val testResult1 = part1(testInput)
    println("Test Result part 1: $testResult1")
//    check(testResult1 == 13)

    val testResult2 = part2(testInput)
    println("Test Result part 2: $testResult2")
//    check(testResult2 == 30)

    val input = readInput("Day06")
    measureTime {
        part1(input).println()
    }.also { println("Time Part 1: $it") }
    measureTime {
        part2(input).println()
    }.also { println("Time Part 2: $it") }
}
