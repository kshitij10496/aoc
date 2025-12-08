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
    println("Computed distances between junction boxes.")

    // Order all the possible pairs by distance.
    val n = junctionBoxes.size * 10
    val closestPairs = smallestDistancesN(distances, n)
    println("Found $n closest pairs of junction boxes.")

    val circuits = mutableListOf<Circuit>()

    // Connect the closest pair of junction boxes in to the same circuit.
    // If neither box is in a circuit, create a new circuit.
    for ((box1, box2) in closestPairs) {
        val circuit1 = circuits.find { it.boxes.contains(box1) }
        val circuit2 = circuits.find { it.boxes.contains(box2) }

        if (circuit1 == null && circuit2 == null) {
            // Neither box is in a circuit, create a new circuit.
            val newCircuit = Circuit(setOf(box1, box2))
            circuits.add(newCircuit)
            println("created new circuit: $box1, $box2")
        } else if (circuit1 != null && circuit2 == null) {
            // box1 is in a circuit, add box2 to it.
            val updatedBoxes = circuit1.boxes + box2
            circuits.remove(circuit1)
            circuits.add(Circuit(updatedBoxes))
            println("added $box2 to existing circuit with $box1")
        } else if (circuit1 == null && circuit2 != null) {
            // box2 is in a circuit, add box1 to it.
            val updatedBoxes = circuit2.boxes + box1
            circuits.remove(circuit2)
            circuits.add(Circuit(updatedBoxes))
            println("added $box1 to existing circuit with $box2")
        } else if (circuit1 != null && circuit2 != null && circuit1 != circuit2) {
            // Both boxes are in different circuits, merge them.
            val mergedBoxes = circuit1.boxes + circuit2.boxes
            circuits.remove(circuit1)
            circuits.remove(circuit2)
            circuits.add(Circuit(mergedBoxes))
            println("merged circuits with $box1 and $box2")
        }

        // Exit if all the boxes are connected in to a single circuit.
        if (circuits.size == 1 && circuits[0].boxes.size == junctionBoxes.size) {
            println("connection completed: $box1, $box2")
            break
        }
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
