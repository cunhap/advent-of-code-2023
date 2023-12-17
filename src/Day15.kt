import kotlin.time.measureTime

private val day = object {}::class.java.name.substringBefore("Kt").substringAfter("Day").toInt()
fun main() {

    fun String.calculateHash(): Long {
        val hash = this.toCharArray().fold(0L) { acc, char ->
            val ascii = char.code
            val hash = acc.run {
                ((this + ascii) * 17).rem(256)
            }
            hash
        }
        return hash
    }

    fun part1(input: List<String>): Long {
        val steps = input[0].split(",").filter { it.isNotEmpty() }.filter { it != "\n" }
        return steps.sumOf { it.calculateHash() }
    }


    data class Slot(val focusLength: Long, val iteration: Int)

    fun part2(input: List<String>): Long {
        val boxesToFilledSlots: MutableMap<Long, MutableMap<String, Slot>> = mutableMapOf()
        val steps = input[0].split(",").filter { it.isNotEmpty() }.filter { it != "\n" }
        steps.forEachIndexed { iteration, step ->
            val shouldRemove = step.endsWith("-")
            val label = if(shouldRemove) step.substringBefore("-") else step.substringBefore("=")
            val boxNumber = label.calculateHash()
            val box = boxesToFilledSlots[boxNumber]
            if(shouldRemove) {
                box?.remove(label)
                if(box?.isEmpty() == true) {
                    boxesToFilledSlots.remove(boxNumber)
                }
            } else {
                boxesToFilledSlots.computeIfAbsent(boxNumber) { mutableMapOf() }
                val focusLength = step.substringAfter("=").toLong()
                val previousSlot = box?.get(label)
                val slot = Slot(focusLength, iteration)
                previousSlot?.let {
                    box[label] = previousSlot.copy(focusLength = focusLength)
                } ?: box?.put(label, slot)
                boxesToFilledSlots[boxNumber] = box ?: mutableMapOf(label to slot)
            }
        }

        val focusPower = boxesToFilledSlots.map { box ->
            val boxNumber = box.key
            val sortedBox = box.value.toList().sortedBy { it.second.iteration }
            sortedBox.mapIndexed { index, slot ->
                (1 + boxNumber) * (index + 1) * slot.second.focusLength
            }.sum()
        }.sum()
        return focusPower
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
