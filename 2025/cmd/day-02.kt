// Range data class to represent a range of long integers.
// Both the start and end are inclusive.
data class Range(val start: Long, val end: Long)

// Parse the input string into a list of Ranges.
// It assumes that the entire list of ranges is a comma-separated string of ranges in the format "start-end".
fun parseRanges(input: String): List<Range> {
    return input.trim().split(",").map { part ->
        val bounds = part.split("-").map { it.toLong() }
        Range(bounds[0], bounds[1])
    }
}

// Function to check if an ID is invalid.
// An ID is invalid if it is made only of some sequence of digits repeated twice.
// Ex: 55 (5 twice), 6464 (64 twice), 123123 (123 twice) are invalid.
fun isInvalidID(n: Long): Boolean {
    val idStr = n.toString()
    val len = idStr.length
    if (len % 2 != 0) return false // Must be even length to be repeated twice.

    // Decompose the string into two halves and compare.
    val half = len / 2
    val firstHalf = idStr.substring(0, half)
    val secondHalf = idStr.substring(half)
    return firstHalf == secondHalf
}

fun invalidIDs(range: Range): List<Long> {
    val invalids = mutableListOf<Long>()
    for (id in range.start..range.end) {
        if (isInvalidID(id)) {
            invalids.add(id)
        }
    }
    return invalids
}

fun main(args: Array<String>) {
    // Accept the path to the input file as a command-line argument (default to data/input/day-01.txt).
    val inputPath = args.firstOrNull() ?: "data/input/day-02.txt"

    // Read the input file content.
    val input = java.io.File(inputPath).readText()

    // Parse the input into a list of Ranges.
    val ranges = parseRanges(input)

    // Build a list of all invalid IDs across all ranges.
    val invalidIDs = mutableListOf<Long>()
    for (range in ranges) {
        invalidIDs.addAll(invalidIDs(range))
    }

    println("Part 1: ${invalidIDs.sum()}")
}
