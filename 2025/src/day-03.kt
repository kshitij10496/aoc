data class Battery(val rating: Int)

class BatteryBank(val batteries: List<Battery>) {
	// maxJoltage returns the maximum joltage from "n" batteries in the bank.
	// Ex: maxJoltage(12)
	// "987654321111111" -> 987654321111
	// "811111111111119" -> 811111111119
	// "234234234234278" -> 434234234278
	// "818181911112111" -> 888911112111
	fun maxJoltage(n: Int): Long {
		var res = mutableListOf<Battery>()
		var startIndex = 0
		var endIndex = batteries.size - (n - 1)

		// Greedy algorithm that optimizes (max) the battery at current index.
		while (res.size < n) {
			// Choose the max rating battery from the sublist of batteries between startIndex and endIndex.
			val workingList = batteries.subList(startIndex, endIndex)
			val maxBattery = workingList
				.withIndex()
				.maxByOrNull { it.value.rating }?.value ?: Battery(0)

			startIndex += workingList.indexOf(maxBattery) + 1
			endIndex += 1
			res.add(maxBattery)
		}

		// Reduce the list of batteries to a single integer joltage.
		var sum: Long = 0
		for (battery in res) {
			sum = (sum * 10) + battery.rating
		}
		return sum
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
	// Reduce the results into their sum.
	// Part 1: Compute the sum of maxJoltage for each bank with 2 batteries.
	val result: Long = banks
		.map { it.maxJoltage(2) }
		.sum()
	println("Part 1: ${result}")

	// Part 2: Compute the sum of maxJoltage for each bank with 12 batteries.
	val result2: Long = banks
		.map { it.maxJoltage(12) }
		.sum()
	println("Part 2: ${result2}")
}
