data class IngredientID(val i: Long)

data class IngredientIDRange(val start: Long, val end: Long)

fun IngredientIDRange.size(): Long = end - start + 1

fun IngredientIDRange.contains(i: IngredientID): Boolean =
	i.i in start..end

fun parseFreshIngredientRanges(input: String): List<IngredientIDRange> {
	// Parse the input line by line for the regex pattern <start>-<end>.
	// Adopted from Kotlin Koans - String Templates.
	val regex = """(\d+)-(\d+)""".toRegex()
	return input.lines().mapNotNull { line ->
		val matchResult = regex.find(line)
		if (matchResult != null) {
			val (start, end) = matchResult.destructured
			IngredientIDRange(start.toLong(), end.toLong())
		} else {
			null
		}
	}
}

fun parseAvailableIngredients(input: String): List<IngredientID> {
// Parse the input line by line.
	return input.lines().mapNotNull { line ->
		val id = line.toLongOrNull()
		// TODO: Scope function or the null safety operator.
		if (id != null) {
			IngredientID(id)
		} else {
			null
		}
	}
}

fun availableAndFreshIngredients(
	freshIngredientRanges: List<IngredientIDRange>,
	availableIngredients: List<IngredientID>
): List<IngredientID> {
	return availableIngredients.filter { ingredient ->
		freshIngredientRanges.any { range ->
			range.contains(ingredient)
		}
	}
}

fun allFreshIngredients(ranges: List<IngredientIDRange>): Long {
	// Detect any overlaps between the ranges and merge them.
	val mergedRanges = mutableListOf<IngredientIDRange>()
	val sortedRanges = ranges.sortedBy { it.start }
	for (range in sortedRanges) {
		if (mergedRanges.isEmpty() || mergedRanges.last().end < range.start - 1) {
			// No overlap.
			//
			// 5-7 and 8-10 should merge to 5-10.
			// But 5-7 and 9-11 should not merge.
			mergedRanges.add(range)
		} else {
			// Update the existing range potentially extending it.
			val lastRange = mergedRanges.last()
			mergedRanges[mergedRanges.size - 1] = IngredientIDRange(
				start = lastRange.start, // This should work since the ranges are already sorted by start.
				end = maxOf(lastRange.end, range.end)
			)
		}
	}
	// Sum up the size of every merged range.
	return mergedRanges.sumOf { it.size() }
}

fun main(args: Array<String>) {
	// Accept the path to the input file as a command-line argument (default to data/input/day-05.txt).
	val inputPath = args.firstOrNull() ?: "data/input/day-05.txt"

	// Read the input file content.
	val input = java.io.File(inputPath).readText()

	// Parse the input into a collection of paper rolls.
	val freshIngredientRanges = parseFreshIngredientRanges(input)
	val availableIngredients = parseAvailableIngredients(input)

	// Part 1
	println("Part 1: ${availableAndFreshIngredients(freshIngredientRanges, availableIngredients).size}")

	// Part 2
	println("Part 2: ${allFreshIngredients(freshIngredientRanges)}")
}
