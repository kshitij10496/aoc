class Machine(val indicatorLights: List<Boolean>, val buttonWirings: List<List<Int>>, val joltage: List<Int>) {
    fun pressButton(lights: List<Boolean>, i: Int): List<Boolean> {
        // Get the button wiring.
        val wiring = buttonWirings[i]

        // Return the new state of the indicator lights for this machine.
        val newLights = lights.toMutableList()
        for (lightIndex in wiring) {
            newLights[lightIndex] = !lights[lightIndex]
        }
        return newLights
    }

    // Returns whether the machine is on or not.
    // The input lights should match the expected indicator lights for the machine.
    fun on(lights: List<Boolean>): Boolean = (lights == indicatorLights)

    fun pressButtonJoltage(joltages: List<Int>, i: Int): List<Int> {
        // Get the button wiring.
        val wiring = buttonWirings[i]

        // Return the new state of the joltage for this machine.
        val newJoltages = joltages.toMutableList()
        for (joltageIndex in wiring) {
            newJoltages[joltageIndex] = (joltages[joltageIndex] + 1)
        }
        return newJoltages
    }

    // Returns whether the machine is on or not.
    // The input joltages should match the expected joltage for the machine.
    fun onJoltage(joltages: List<Int>): Boolean = (joltages == joltage)
}

// Returns the fewest number of button presses required to turn on the machine from an initally off state.
fun fastestFingersFirst(m: Machine): Int {
    // Initally all the indicator lights are off.
    var lights = List(m.indicatorLights.size) { false }

    var res = 0

    // Use a BFS approach to find the fewest button presses.
    val queue: ArrayDeque<Pair<List<Boolean>, Int>> = ArrayDeque()

    // This prevents infinite loops - as we always get the fewest presses needed to reach a specific state.
    val visited: MutableSet<List<Boolean>> = mutableSetOf()
    queue.add(Pair(lights, 0))
    visited.add(lights)
    while (queue.isNotEmpty()) {
        val (currentLights, presses) = queue.removeFirst()

        // Base case: Check if the machine is on.
        if (m.on(currentLights)) {
            res = presses
            break
        }

        // Try pressing each button.
        for (i in m.buttonWirings.indices) {
            val newLights = m.pressButton(currentLights, i)
            // Only add this node to the graph if it creates a new indicator light configuration.
            if (newLights !in visited) {
                visited.add(newLights)
                queue.add(Pair(newLights, presses + 1))
            }
        }
    }

    return res
}


// Returns the fewest number of button presses required to set the desired joltage for the machine.
// Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
// at java.base/java.util.ArrayList.iterator(ArrayList.java:1029)
// at Day_10Kt.fastestFingersFirstJoltage(day-10.kt:209)
// at Day_10Kt.main(day-10.kt:201)
fun fastestFingersFirstJoltage(m: Machine): Int {
    // Initially all joltage counters are at 0.
    val initial = IntArray(m.joltage.size) { 0 }
    val target = m.joltage.toIntArray()

    // Early return if already at target
    if (initial.contentEquals(target)) return 0

    // Use BFS to find the fewest button presses.
    // Use IntArray and contentHashCode for better memory efficiency
    val queue: ArrayDeque<Pair<IntArray, Int>> = ArrayDeque()
    val visited: MutableSet<Int> = mutableSetOf()  // Store hash codes instead of full arrays

    queue.add(Pair(initial, 0))
    visited.add(initial.contentHashCode())

    while (queue.isNotEmpty()) {
        val (currentJoltage, presses) = queue.removeFirst()

        // Try pressing each button.
        for (buttonIdx in m.buttonWirings.indices) {
            val wiring = m.buttonWirings[buttonIdx]
            val nextJoltage = currentJoltage.clone()

            // Increment the affected counters
            var overshot = false
            for (idx in wiring) {
                nextJoltage[idx]++
                if (nextJoltage[idx] > target[idx]) {
                    overshot = true
                    break
                }
            }

            // Skip if we overshot any counter
            if (overshot) continue

            // Check if we've reached the target
            if (nextJoltage.contentEquals(target)) {
                return presses + 1
            }

            // Only visit each state once (BFS guarantees shortest path)
            val hash = nextJoltage.contentHashCode()
            if (hash !in visited) {
                visited.add(hash)
                queue.add(Pair(nextJoltage, presses + 1))
            }
        }
    }

    // No solution found (shouldn't happen for valid inputs)
    return -1
}

// Returns all unique combinations of choosing `k` items from the given list.
// For example: combinations(listOf('a', 'b', 'c', 'd'), 2) returns:
// [[a, b], [a, c], [a, d], [b, c], [b, d], [c, d]]
fun <T> combinations(list: List<T>, k: Int): List<List<T>> {
    val result = mutableListOf<List<T>>()

    fun backtrack(start: Int, path: MutableList<T>) {
        if (path.size == k) {
            result.add(path.toList())
            return
        }
        for (i in start until list.size) {
            path.add(list[i])
            backtrack(i + 1, path)
            path.removeAt(path.size - 1)
        }
    }

    backtrack(0, mutableListOf())
    return result
}

// Returns all combinations of choosing `combinationSize` items from a list of size `n`.
// For example: chooseButtons(5, 2) returns: [[0,1], [0,2], [0,3], [0,4], [1,2], [1,3], [1,4], [2,3], [2,4], [3,4]]
fun chooseButtons(n: Int, combinationSize: Int): List<List<Int>> {
    val result = mutableListOf<List<Int>>()

    fun backtrack(start: Int, path: MutableList<Int>) {
        if (path.size == combinationSize) {
            result.add(path.toList())
            return
        }
        for (j in start until n) {
            path.add(j)
            backtrack(j + 1, path)
            path.removeAt(path.size - 1)
        }
    }

    backtrack(0, mutableListOf())
    return result
}

// Returns all combinations of choosing `combinationSize` button wirings from the given wirings list.
// For example: chooseButtonWirings([[1,2], [3], [4,5]], 2) returns:
// [[[1,2], [3]], [[1,2], [4,5]], [[3], [4,5]]]
fun chooseButtonWirings(buttonWirings: List<List<Int>>, combinationSize: Int): List<List<List<Int>>> {
    val result = mutableListOf<List<List<Int>>>()
    val n = buttonWirings.size

    fun backtrack(start: Int, path: MutableList<List<Int>>) {
        if (path.size == combinationSize) {
            result.add(path.toList())
            return
        }
        for (j in start until n) {
            path.add(buttonWirings[j])
            backtrack(j + 1, path)
            path.removeAt(path.size - 1)
        }
    }

    backtrack(0, mutableListOf())
    return result
}

fun parseMachines(input: String): List<Machine> {
    return input.lines().filter { it.isNotBlank() }.map { line ->
        val parts = line.split(" ")

        // [.##.] -> [false, true, true, false]
        val indicatorLights =
            parts
                .first()
                .trim()
                .removePrefix("[")
                .removeSuffix("]")
                .split("")
                .filter { it.isNotEmpty() }
                .map { it.trim() == "#" }

        // {3,5,4,7} -> [3, 5, 4, 7]
        val joltage = parts
            .last()
            .trim()
            .removePrefix("{")
            .removeSuffix("}")
            .split(",")
            .filter { it.isNotEmpty() }
            .map { it.trim().toInt() }

        // (3) (1,3) (2) (2,3) (0,2) (0,1)
        val buttonWirings = parts
            .subList(1, parts.size - 1)
            .map { wiringPart ->
                wiringPart
                    .trim()
                    .removePrefix("(")
                    .removeSuffix(")")
                    .split(",")
                    .filter { it.isNotEmpty() }
                    .map { it.trim().toInt() }
            }

        Machine(indicatorLights, buttonWirings, joltage)
    }
}

fun main(args: Array<String>) {
    // Accept the path to the input file as a command-line argument (default to data/input/day-10.txt)
    val inputPath = args.firstOrNull() ?: "data/input/day-10.txt"

    // Read the input file content.
    val input = java.io.File(inputPath).readText()

// Parse each line into a machine.
    val machines = parseMachines(input)

    println("Parsed ${machines.size} machines.")
    for ((index, machine) in machines.withIndex()) {
        println("Machine ${index + 1}:")
        println("  Indicator Lights: ${machine.indicatorLights}")
        println("  Button Wirings: ${machine.buttonWirings}")
        println("  Joltage: ${machine.joltage}")
    }

    // // Test the logic.
    // val testMachine = machines.first()
    // // Initally all the indicator lights are off.
    // var lights = List(testMachine.indicatorLights.size) { false }
    // println("Initial Lights: $lights")

    // // Press buttons 0, 1, 2 once.
    // for (i in 0..2) {
    //     lights = testMachine.pressButton(lights, i)
    //     println("After pressing button $i: $lights, Machine On: ${testMachine.on(lights)}")
    // }

    // Part 1: Run Fastest Fingers First for every machine individually.
    var totalPresses = 0
    for (machine in machines) {
        val presses = fastestFingersFirst(machine)
        totalPresses += presses
    }
    println("Part 1: $totalPresses")

    // Part 2: Run Fastest Fingers First for joltage for every machine individually.
    // Reset total presses.
    totalPresses = 0
    for (machine in machines) {
        val presses = fastestFingersFirstJoltage(machine)
        totalPresses += presses
    }
    println("Part 2: $totalPresses")
}
