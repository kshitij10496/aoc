data class JunctionBox(val x: Int, val y: Int, val z: Int)

data class Circuit(val boxes: Set<JunctionBox>)

fun computeDistance(a: JunctionBox, b: JunctionBox): Double {
    return Math.sqrt(
        Math.pow((a.x - b.x).toDouble(), 2.0) +
                Math.pow((a.y - b.y).toDouble(), 2.0) +
                Math.pow((a.z - b.z).toDouble(), 2.0)
    )
}

fun parseJunctionBox(input: String): List<JunctionBox> {
    val lines = input.lines().filter { it.isNotBlank() }
    return lines.map { line ->
        val (x, y, z) = line.split(",").map { it.toInt() }
        JunctionBox(x, y, z)
    }
}

fun main(args: Array<String>) {
    // Accept the path to the input file as a command-line argument (default to data/input/day-08.txt)
    val inputPath = args.firstOrNull() ?: "data/input/day-08.txt"

    // Read the input file content.
    val input = java.io.File(inputPath).readText()

    val junctionBoxes = parseJunctionBox(input)
    println("Parsed ${junctionBoxes.size} junction boxes.")

    // Compute distances between pairs of junction boxes.
    // Store the distance in a map of Pair(box1, box2) -> distance.
    val distances: MutableMap<Pair<JunctionBox, JunctionBox>, Double> = mutableMapOf()
    for (i in junctionBoxes.indices) {
        for (j in i + 1..<junctionBoxes.size) {
            val distance = computeDistance(junctionBoxes[i], junctionBoxes[j])
            distances.put(Pair(junctionBoxes[i], junctionBoxes[j]), distance)
            distances.put(Pair(junctionBoxes[j], junctionBoxes[i]), distance)
        }
    }


    // Find the closest 10 pairs of junction boxes.
    val closestPairs = smallestDistancesN(distances, 1000)


    // Process the pairs into circuits
    var circuits: List<Circuit> = listOf()

    for ((box1, box2) in closestPairs) {
        // If the boxes are already part of the same circuit, do nothing.
        val circuit1 = circuits.find { it.boxes.contains(box1) }
        val circuit2 = circuits.find { it.boxes.contains(box2) }

        if (circuit1 != null && circuit2 != null) {
            if (circuit1 == circuit2) {
                continue
            } else {
                // Connect the two circuits.
                val mergedBoxes = circuit1.boxes + circuit2.boxes
                circuits = circuits.filter { it != circuit1 && it != circuit2 } + Circuit(mergedBoxes)
            }
        } else if (circuit1 != null) {
            // Add box2 to circuit1
            val updatedBoxes = circuit1.boxes + box2
            circuits = circuits.filter { it != circuit1 } + Circuit(updatedBoxes)
        } else if (circuit2 != null) {
            // Add box1 to circuit2
            val updatedBoxes = circuit2.boxes + box1
            circuits = circuits.filter { it != circuit2 } + Circuit(updatedBoxes)
        } else {
            // Create a new circuit with both boxes.
            circuits = circuits + Circuit(setOf(box1, box2))
        }
    }

    // Print the size of the 3 largest circuits.
    val largestCircuits = circuits.sortedByDescending { it.boxes.size }.take(3)
    for ((index, circuit) in largestCircuits.withIndex()) {
        println("Circuit ${index + 1} size: ${circuit.boxes.size}")
    }
}

// Returns N pairs of junction boxes with the smallest distance between them.
fun smallestDistancesN(
    distances: MutableMap<Pair<JunctionBox, JunctionBox>, Double>, n: Int
): List<Pair<JunctionBox, JunctionBox>> {
    val result: MutableList<Pair<JunctionBox, JunctionBox>> = mutableListOf()
    val distancesCopy = distances.toMutableMap()

    for (i in 0..<n) {
        var minDistance = Double.MAX_VALUE
        var closestPair: Pair<JunctionBox, JunctionBox>? = null

        for ((pair, distance) in distancesCopy) {
            if (distance < minDistance) {
                minDistance = distance
                closestPair = pair
            }
        }

        if (closestPair != null) {
            result.add(closestPair)
            // Remove both directions from the map.
            distancesCopy.remove(closestPair)
            distancesCopy.remove(Pair(closestPair.second, closestPair.first))
        } else {
            break
        }
    }

    return result
}
