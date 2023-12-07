import kotlin.time.measureTime

sealed interface HandType {
    data object HighCard : HandType
    data object Pair : HandType
    data object TwoPair : HandType
    data object ThreeOfAKind : HandType
    data object FullHouse : HandType
    data object FourOfAKind : HandType
    data object FiveOfAKind : HandType
}

fun main() {

    /*
    32T3K 765
    T55J5 684
    KK677 28
    KTJJT 220
    QQQJA 483
      */


    val handTypeByRank = listOf(
        HandType.HighCard,
        HandType.Pair,
        HandType.TwoPair,
        HandType.ThreeOfAKind,
        HandType.FullHouse,
        HandType.FourOfAKind,
        HandType.FiveOfAKind,
    )

    data class HandWithBid(val hand: String, val bid: Int, val handType: HandType)


    fun getHandType(hand: String): HandType {
        if (hand.toSet().size == hand.length - 1) return HandType.Pair
        if (hand.toSet().size == hand.length - 2) {
            hand.groupBy { it }.values.firstOrNull { it.size == 3 }?.let { return HandType.ThreeOfAKind }
                ?: return HandType.TwoPair
        }
        if (hand.toSet().size == hand.length - 3) {
            hand.groupBy { it }.values.firstOrNull { it.size == 4 }?.let { return HandType.FourOfAKind }
                ?: return HandType.FullHouse
        }
        if (hand.toSet().size == 1) return HandType.FiveOfAKind
        return HandType.HighCard
    }

    fun applyWildCard(hand: String, originalHandType: HandType, cardListByRank: List<Char>): HandType {
        val allJokers = hand.all { it == 'J' }
        if (allJokers) return HandType.FiveOfAKind
        val cardsNotJ = hand.filter { it != 'J' }

        fun greatestCard(hand: String): Char = hand.maxBy { cardListByRank.indexOf(it) }

        fun cardToReplace(hand: String, cardsCount: Int): Char {
            return try {
                hand.groupBy { it }.values.first { it.size == cardsCount }.first()
            } catch (ex: NoSuchElementException) {
                greatestCard(hand)
            }
        }

        val newHand = when (originalHandType) {
            HandType.HighCard -> hand.replace('J', greatestCard(cardsNotJ))
            HandType.Pair -> hand.replace('J', cardToReplace(cardsNotJ, 2))
            HandType.TwoPair -> hand.replace('J', cardToReplace(cardsNotJ, 2))
            HandType.ThreeOfAKind -> hand.replace('J', cardToReplace(cardsNotJ, 3))
            else -> hand.replace('J', cardsNotJ.first())
        }
        return getHandType(newHand)
    }

    fun processHands(
        input: List<String>,
        cardListByRank: List<Char>,
        handTypeStrategy: (String, HandType, List<Char>) -> HandType
    ): Long {
        val handComparator = Comparator<HandWithBid> { o1, o2 ->
            for (cardIndex in o1.hand.toCharArray().indices) {
                val card1Rank = cardListByRank.indexOf(o1.hand[cardIndex])
                val card2Rank = cardListByRank.indexOf(o2.hand[cardIndex])
                if (card1Rank < card2Rank) return@Comparator -1
                if (card1Rank > card2Rank) return@Comparator 1
            }
            return@Comparator 0
        }
        val handsWithBid = input.map { line ->
            val (hand, bid) = line.split(" ").filter { it.isNotBlank() }
            val originalHandType = getHandType(hand)
            HandWithBid(hand, bid.toInt(), handTypeStrategy(hand, originalHandType, cardListByRank))
        }
        val sortedHandsByRank = handsWithBid.groupBy { it.handType }
            .mapValues { (_, handValue) -> handValue.sortedWith(handComparator) }
        val allHandsSorted = handTypeByRank.flatMap { sortedHandsByRank[it] ?: emptyList() }
        return allHandsSorted.foldIndexed(0L) { index, acc, handWithBid ->
            acc + handWithBid.bid * (index + 1)
        }
    }

    fun part1(input: List<String>) =
        processHands(
            input = input,
            cardListByRank = ('2'..'9') + 'T' + 'J' + 'Q' + 'K' + 'A'
        ) { _, originalHandType, _ -> originalHandType }

    fun part2(input: List<String>) = processHands(
        input = input,
        cardListByRank = listOf('J') + ('2'..'9') + 'T' + 'Q' + 'K' + 'A'
    ) { hand, originalHandType, cardListByRank ->
        if (hand.contains('J')) applyWildCard(hand, originalHandType, cardListByRank) else originalHandType
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    val testResult1 = part1(testInput)
    println("Test Result part 1: $testResult1")

    val testResult2 = part2(testInput)
    println("Test Result part 2: $testResult2")

    val input = readInput("Day07")
    measureTime {
        part1(input).println()
    }.also { println("Time Part 1: $it") }
    measureTime {
        part2(input).println()
    }.also { println("Time Part 2: $it") }
}
