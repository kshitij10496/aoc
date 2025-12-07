fun processManifold(input: String): Int {
    // Processes each line of the input as Tachyon manifold.
    // Returns the number of splits.
    val lines = input.lines().filter { it.isNotBlank() }
    var splitCount = 0

    // Use a List collection to track the indices where the tachyon beam exists.
    var beamIndices = mutableSetOf<Int>()

    // Parse the starting index on the first line of the input.
    // "S" indicates the entry point.
    val firstLine = lines.first()
    for (c in firstLine) {
        if (c == 'S') {
            beamIndices.add(firstLine.indexOf(c))
        }
    }
    // Skip first line.
    for (line in lines.drop(1)) {
        var newBeamIndices = mutableSetOf<Int>()
        for (index in beamIndices) {
            if (index < 0 || index >= line.length) {
                continue
            }
            when (line[index]) {
                '.' -> {
                    // Beam continues downwards.
                    newBeamIndices.add(index)
                }

                '^' -> {
                    // Beam splits: one goes left-up, one goes right-down.
                    newBeamIndices.add(index - 1)
                    newBeamIndices.add(index + 1)
                    splitCount++
                }

                else -> {
                    // Beam is blocked or out of bounds; do nothing.
                    println("Beam at index $index blocked by '${line[index]}'")
                }
            }
        }
        beamIndices.clear()
        beamIndices.addAll(newBeamIndices)
    }

    return splitCount
}

fun quantumManifold(input: String): Long {
    // Processes each line of the input as Tachyon manifold.
    // Returns the number of timelines.
    val lines = input.lines().filter { it.isNotBlank() }
    var counter: Long = 0

    // Use a List collection to track the indices where the tachyon beam exists.
    // Each entry in the list is a Pair of (index, timelineCount).
    // This allows us to track multiple timelines at the same index.
    // For example, if there are 3 timelines at index 5, we store (5, 3).
    // This avoids exponential growth of the list.
    // We use a Map to track the indices and their timeline counts.
    var beamMap = mutableMapOf<Int, Long>()
    // Parse the starting index on the first line of the input.
    // "S" indicates the entry point.
    // The initial timeline count is 1.
    val firstLine = lines.first()
    for (c in firstLine) {
        if (c == 'S') {
            beamMap[firstLine.indexOf(c)] = 1
        }
    }

    // Skip first line.
    for (line in lines.drop(1)) {
        var newBeamMap = mutableMapOf<Int, Long>()
        for ((index, timelineCount) in beamMap) {
            if (index < 0 || index >= line.length) {
                continue
            }
            when (line[index]) {
                '.' -> {
                    // Beam continues downwards.
                    newBeamMap[index] = newBeamMap.getOrDefault(index, 0) + timelineCount
                }

                '^' -> {
                    // Beam splits: one goes left-up, one goes right-down.
                    newBeamMap[index - 1] = newBeamMap.getOrDefault(index - 1, 0) + timelineCount
                    newBeamMap[index + 1] = newBeamMap.getOrDefault(index + 1, 0) + timelineCount
                }

                else -> {
                    // Beam is blocked or out of bounds; do nothing.
                    println("Beam at index $index blocked by '${line[index]}'")
                }
            }
        }
        beamMap.clear()
        beamMap.putAll(newBeamMap)
    }

    // Sum all timeline counts in the final beamMap.
    for ((_, timelineCount) in beamMap) {
        counter += timelineCount
    }

    return counter

}

fun main(args: Array<String>) {
    // Accept the path to the input file as a command-line argument (default to data/input/day-07.txt)
    val inputPath = args.firstOrNull() ?: "data/input/day-07.txt"

    // Read the input file content.
    val input = java.io.File(inputPath).readText()

    // println("Part 1: ${processManifold(input)}")

    // Part 2
    println("Part 1: ${quantumManifold(input)}")
}
