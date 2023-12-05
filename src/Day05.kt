import kotlin.time.measureTime

fun main() {

    /*
    seeds: 79 14 55 13

    seed-to-soil map:
    50 98 2
    52 50 48

    soil-to-fertilizer map:
    0 15 37
    37 52 2
    39 0 15

    fertilizer-to-water map:
    49 53 8
    0 11 42
    42 0 7
    57 7 4

    water-to-light map:
    88 18 7
    18 25 70

    light-to-temperature map:
    45 77 23
    81 45 19
    68 64 13

    temperature-to-humidity map:
    0 69 1
    1 0 69

    humidity-to-location map:
    60 56 37
    56 93 4
     */
    data class MappingInfo(val destinationRangeStart: Long, val sourceRangeStart: Long, val range: Long)

    data class Almanac(
        val seedToSoilMap: List<MappingInfo> = emptyList(),
        val soilToFertilizerMap: List<MappingInfo> = emptyList(),
        val fertilizerToWaterMap: List<MappingInfo> = emptyList(),
        val waterToLightMap: List<MappingInfo> = emptyList(),
        val lightToTemperatureMap: List<MappingInfo> = emptyList(),
        val temperatureToHumidityMap: List<MappingInfo> = emptyList(),
        val humidityToLocationMap: List<MappingInfo> = emptyList()
    )

    fun MappingInfo.getMatch(source: Long): Long? {
        if (source < sourceRangeStart || source > sourceRangeStart + range) return null
        return destinationRangeStart + (source - sourceRangeStart)
    }

    fun MappingInfo.getReverseMatch(destination: Long): Long? {
        if (destination < destinationRangeStart || destination > destinationRangeStart + range) return null
        return sourceRangeStart + (destination - destinationRangeStart)
    }

    fun Almanac.getSeedLocation(seed: Long): Long {
        val soil = seedToSoilMap.firstNotNullOfOrNull { it.getMatch(seed) } ?: seed
        val fertilizer = soilToFertilizerMap.firstNotNullOfOrNull { it.getMatch(soil) } ?: soil
        val water = fertilizerToWaterMap.firstNotNullOfOrNull { it.getMatch(fertilizer) } ?: fertilizer
        val light = waterToLightMap.firstNotNullOfOrNull { it.getMatch(water) } ?: water
        val temperature = lightToTemperatureMap.firstNotNullOfOrNull { it.getMatch(light) } ?: light
        val humidity = temperatureToHumidityMap.firstNotNullOfOrNull { it.getMatch(temperature) } ?: temperature
        return humidityToLocationMap.firstNotNullOfOrNull { it.getMatch(humidity) } ?: humidity
    }

    fun Almanac.getLowestLocationForSeeds(seedRanges: List<LongRange>): Long {
        for (location in 0..Long.MAX_VALUE) {
            val humidity = humidityToLocationMap.firstNotNullOfOrNull { it.getReverseMatch(location) } ?: location
            val temperature =
                temperatureToHumidityMap.firstNotNullOfOrNull { it.getReverseMatch(humidity) } ?: humidity
            val light = lightToTemperatureMap.firstNotNullOfOrNull { it.getReverseMatch(temperature) } ?: temperature
            val water = waterToLightMap.firstNotNullOfOrNull { it.getReverseMatch(light) } ?: light
            val fertilizer = fertilizerToWaterMap.firstNotNullOfOrNull { it.getReverseMatch(water) } ?: water
            val soil = soilToFertilizerMap.firstNotNullOfOrNull { it.getReverseMatch(fertilizer) } ?: fertilizer
            val seed = seedToSoilMap.firstNotNullOfOrNull { it.getReverseMatch(soil) } ?: soil
            val seedInRanges = seedRanges.any { it.contains(seed) }
            if (seedInRanges) return location-1
        }

        return -1
    }

    fun getAlmanac(mappingInformation: List<String>) =
        mappingInformation.foldIndexed(Almanac()) { index, almanac, line ->
            val mapType = line.substringBefore(":")
            if (mapType != line) {
                val differentMappings = mutableListOf<MappingInfo>()
                var runningIndex = index + 1
                while (runningIndex in mappingInformation.indices && mappingInformation[runningIndex].isNotBlank()) {
                    val (destinationRangeStart, sourceRangeStart, range) = mappingInformation[runningIndex].split(" ")
                        .map { it.toLong() }
                    differentMappings.add(MappingInfo(destinationRangeStart, sourceRangeStart, range))
                    runningIndex++
                }
                when (mapType) {
                    "seed-to-soil map" -> almanac.copy(seedToSoilMap = differentMappings)
                    "soil-to-fertilizer map" -> almanac.copy(soilToFertilizerMap = differentMappings)
                    "fertilizer-to-water map" -> almanac.copy(fertilizerToWaterMap = differentMappings)
                    "water-to-light map" -> almanac.copy(waterToLightMap = differentMappings)
                    "light-to-temperature map" -> almanac.copy(lightToTemperatureMap = differentMappings)
                    "temperature-to-humidity map" -> almanac.copy(temperatureToHumidityMap = differentMappings)
                    "humidity-to-location map" -> almanac.copy(humidityToLocationMap = differentMappings)
                    else -> almanac
                }
            } else almanac
        }

    fun part1(input: List<String>): Long {
        val seeds = input[0].substringAfter("seeds: ").split(" ").map { it.toLong() }
        val mappingInformation = input.drop(2)
        val almanac: Almanac = getAlmanac(mappingInformation)
        return seeds.minOfOrNull { almanac.getSeedLocation(it) } ?: 0
    }

    fun part2(input: List<String>): Long {
        val seeds =
            input[0].substringAfter("seeds: ").split(" ").map { it.toLong() }.chunked(2).map { it[0]..it[0] + it[1] }
        val mappingInformation = input.drop(2)
        val almanac: Almanac = getAlmanac(mappingInformation)
        val sortedSeeds = seeds.sortedBy { it.first }
        return almanac.getLowestLocationForSeeds(sortedSeeds)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    val testResult1 = part1(testInput)
    println("Test Result part 1: $testResult1")
//    check(testResult1 == 13)

    val testResult2 = part2(testInput)
    println("Test Result part 2: $testResult2")
//    check(testResult2 == 30)

    val input = readInput("Day05")
    part1(input).println()
    measureTime {
        part2(input).println()
    }.also { println("Time: $it") }
}
