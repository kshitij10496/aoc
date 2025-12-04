data class Roll(val x: Int, val y: Int, val hasRoll: Boolean = false)

class Grid(val rolls: List<Roll>) {

    // Removes the roll at location (x, y) from the grid.
    fun removeRoll(x: Int, y: Int) {
        val index = rolls.indexOfFirst { it.x == x && it.y == y }
        if (index != -1) {
            (rolls as MutableList)[index] = Roll(x, y, false)
        }
    }

    // Returns the list of rolls counts that are accessible in the grid.
    fun accessibleRolls(): List<Roll> {
        return rolls.filter { it.hasRoll && accessible(it.x, it.y) }
    }

    // Returns whether the paper roll at location (x, y) on the grid is accesssible or not.
    // A paper rolls is accessible only if there are fewer than four rolls of paper in the
    // eight adjacent positions.
    fun accessible(x: Int, y: Int): Boolean {
        // Find all the neighbours of the current paper roll.
        val neighbours = mutableListOf<Roll?>()

        // Check for neighbours in the row above.
        for (dx in -1..1) {
            neighbours.add(rolls.find { it.x == x + dx && it.y == y - 1 })
        }

        // Check for neighbours in the same row.
        neighbours.add(rolls.find { it.x == x - 1 && it.y == y })
        neighbours.add(rolls.find { it.x == x + 1 && it.y == y })

        // Check for neighbours in the row below.
        for (dx in -1..1) {
            neighbours.add(rolls.find { it.x == x + dx && it.y == y + 1 })
        }

        return neighbours.count { it != null && it.hasRoll } < 4
    }
}

fun parsePaperRollsGrid(input: String): Grid {
    val result = mutableListOf<Roll>()

    // Parse each character from each line sequentially.
    val lines = input.lines().filter { it.isNotBlank() }

    // Iterate over the lines with an index.
    for ((y, line) in lines.withIndex()) {
        for ((x, char) in line.withIndex()) {
            val hasRoll = when (char) {
                '@' -> true
                '.' -> false
                else -> false
            }
            result.add(Roll(x, y, hasRoll))
        }
    }

    // Make the collection a Grid.
    return Grid(result)
}

fun main(args: Array<String>) {
    // Accept the path to the input file as a command-line argument (default to data/input/day-04.txt).
    val inputPath = args.firstOrNull() ?: "data/input/day-04.txt"

    // Read the input file content.
    val input = java.io.File(inputPath).readText()

    // Parse the input into a collection of paper rolls.
    var grid = parsePaperRollsGrid(input)

    // Part 1
    var result = grid.accessibleRolls()
    println("Part 1: ${result.size}")

    // Part 2
    var countRemovedPaperRolls = result.size
    while (result.isNotEmpty()) {
        // Create a new grid.
        var rolls = grid.rolls
        // Remove the accessible paper rolls from the new grid.
        for (r in result) {
            rolls = rolls.map {
                if (it.x == r.x && it.y == r.y) {
                    // Remove the accessible roll.
                    Roll(it.x, it.y, false)
                } else {
                    it
                }
            }
        }
        grid = Grid(rolls)

        // Find more accessible rolls.
        result = grid.accessibleRolls()
        countRemovedPaperRolls += result.size
    }
    println("Part 2: ${countRemovedPaperRolls}")
}
