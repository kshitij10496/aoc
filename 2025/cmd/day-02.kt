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

// Function to check if an ID is invalid.
// An ID is invalid if it is made only of some sequence of digits repeated atleast twice.
// Ex: 55 (5 twice), 6464 (64 twice), 123123 (123 twice) are invalid.
// Ex: 12341234 (1234 two times), 123123123 (123 three times), 1212121212 (12 five times), and 1111111 (1 seven times) are all invalid IDs.
fun isInvalidIDGeneric(n: Long): Boolean {
    val idStr = n.toString()
    val len = idStr.length

    // Check for all possible lengths of the repeating unit with length from 1 to len/2.
    // Each repeating unit must be at most half the length of the ID.
    for (unitLen in 1..(len / 2)) {
        // Complete repeating units can only be formed when the length of the ID is divisible by the unit length.
        // Alternate: We can consider factorization of len to get possible unit lengths.
        if (len % unitLen == 0) {
            val unit = idStr.substring(0, unitLen)
            val repeated = unit.repeat(len / unitLen)
            if (repeated == idStr) {
                return true
            }
        }
    }
    return false
}

// Function to get all invalid IDs in a given range using the specified invalidation function.
fun invalidIDs(range: Range, invalidator: (Long) -> Boolean): List<Long> {
    val invalids = mutableListOf<Long>()
    for (id in range.start..range.end) {
        if (invalidator(id)) {
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
        invalidIDs.addAll(invalidIDs(range, ::isInvalidID))
    }

    println("Part 1: ${invalidIDs.sum()}")

    // Build a list of all invalid IDs across all ranges.
    val invalidIDsPart2 = mutableListOf<Long>()
    for (range in ranges) {
        invalidIDsPart2.addAll(invalidIDs(range, ::isInvalidIDGeneric))
    }
    println("Part 2: ${invalidIDsPart2.sum()}")
}
