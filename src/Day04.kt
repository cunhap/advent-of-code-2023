
fun main() {

    /*
    Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
    Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
    Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
    Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
    Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
    Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
     */
    data class WinningCard(val cardId: Int, val winningNumbers: Set<Int>)

    fun getWinningNumbers(card: String): WinningCard {
        val (cardId, cardNumbers) = card.split(":")
        val numbers = cardNumbers.trim().split("|").map { it.trim() }
        val winningNumbers = numbers[0].split(" ").filter { it.isNotBlank() }.map { it.toInt() }.toSet()
        val myNumbers = numbers[1].split(" ").filter { it.isNotBlank() }.map { it.toInt() }.toSet()
        val cardNumber = cardId.replace(" ", "").substringAfter("Card").toInt()
        return WinningCard(cardNumber, winningNumbers.intersect(myNumbers))
    }

    fun part1(input: List<String>): Int {
        val result = input.sumOf { card ->
            val myWinningNumbers = getWinningNumbers(card).winningNumbers
            if (myWinningNumbers.isNotEmpty()) {
                val points = myWinningNumbers.map { 1 }.reduce { acc, _ -> acc * 2 }
                points
            } else 0
        }
        return result
    }

    fun part2(input: List<String>): Int {
        val scratchCardsQuantity = mutableMapOf<Int, Int>()
        input.forEach { card ->
            val (cardId, winningNumbers) = getWinningNumbers(card)
            val numberOfCopies = scratchCardsQuantity[cardId] ?: 0
            scratchCardsQuantity.computeIfPresent(cardId) { _, v -> v + 1 }
            scratchCardsQuantity.putIfAbsent(cardId, 1)
            val copiedCardsWon = List(winningNumbers.size) { index -> cardId + index + 1 }
            copiedCardsWon.forEach { cardWon ->
                scratchCardsQuantity.computeIfPresent(cardWon) { _, v -> v + 1 + numberOfCopies }
                scratchCardsQuantity.putIfAbsent(cardWon, 1 + numberOfCopies)
            }
        }
        return scratchCardsQuantity.values.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    val testResult1 = part1(testInput)
    println("Test Result part 1: $testResult1")
    check(testResult1 == 13)

    val testResult2 = part2(testInput)
    println("Test Result part 2: $testResult2")
    check(testResult2 == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
