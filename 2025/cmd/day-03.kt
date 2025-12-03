data class Battery(val rating: Int)

class BatteryBank(val batteries: List<Battery>) {

    // maxJoltage computes the maximum joltage that can be produced by this battery bank.
    fun maxJoltage(): Int {
        // Find the maximum rating among the batteries in the bank (except the last one).
        val lastBattery = batteries.last()

        // Get the index of the largest battery excluding the last one.
        val maxIndex = batteries.subList(0, batteries.size - 1)
            .withIndex()
            .maxByOrNull { it.value.rating }?.index ?: -1
        val maxBattery = batteries[maxIndex]

        // Check for the next largest battery after the max battery, excluding the last one.
        val nextMaxBattery = batteries.subList(maxIndex, batteries.size - 1)
            .withIndex()
            .filter { it.index != 0 } // Hack to exclude the max battery itself.
            .maxByOrNull { it.value.rating }?.value ?: Battery(0) // Default to 0 if not found.

        // Compare the ratings of the last battery with the second largest battery.
        return if (lastBattery.rating > nextMaxBattery.rating) {
            (10 * maxBattery.rating) + lastBattery.rating
        } else {
            (10 * maxBattery.rating) + nextMaxBattery.rating
        }
    }
}

// parseBatteryBanks parses the input string into a list of BatteryBanks.
// Ex: "987654321111111" -> [BatteryBank([Battery(9), Battery(8), ..., Battery(1)])]
fun parseBatteryBanks(input: String): List<BatteryBank> {
    val banks = mutableListOf<BatteryBank>()
    val lines = input.lines().filter { it.isNotBlank() }
    for (line in lines) {
        val batteries = line.map { char -> Battery(char.digitToInt()) }
        banks.add(BatteryBank(batteries))
    }
    return banks
}

fun main(args: Array<String>) {
    // Accept the path to the input file as a command-line argument (default to data/input/day-03.txt).
    val inputPath = args.firstOrNull() ?: "data/input/day-03.txt"

    // Read the input file content.
    val input = java.io.File(inputPath).readText()

    // Parse the input into a list of BatteryBanks.
    val banks = parseBatteryBanks(input)

    // Map each bank to the largest joltage it can produce.
    // Aggregate the results into their sum.
    val joltages = mutableListOf<Int>()
    for (bank in banks) {
        val m = bank.maxJoltage()
        println("Bank max joltage: $m")
        joltages.add(m)
    }

    println("Part 1: ${joltages.sum()}")
}
