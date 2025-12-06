class Problem(var nums: MutableList<Long>, var op: String?) {
    fun solve(): Long {
        // println("Solving problem with nums=$nums and op=$op")
        // TODO: Perhaps I can use either an enum or a sealed class to mode lthe operators.
        when (op) {
            "+" -> return nums.sum()
            "*" -> return nums.reduce { acc, i -> acc * i }
            else -> throw IllegalArgumentException("Unknown operation: $op")
        }
    }
}

fun parseProblems2D(input: String): List<Problem> {
    val lines = input.lines().filter { it.isNotBlank() }
    if (lines.isEmpty()) return emptyList()

    var problems = mutableListOf<Problem>()

    // Parse the last line as a list of operators.
    // Set these as the operator of the problems.
    val operatorLine = lines.last()
    val operators = operatorLine.toCharArray().filter { it != ' ' }.map { it.toString() }
    for (op in operators) {
        problems.add(Problem(mutableListOf(), op))
    }

    // Parse the input into 2D list of characters.
    // Each line becomes a row, each character a column.
    // For example:
    // 123
    // 456
    // becomes:
    // [['1', '2', '3'],
    //  ['4', '5', '6']]
    val grid = lines.map { it.toCharArray().toList() }

    // Debug print the grid.
    // for (row in grid) {
    //     println(row.joinToString(" "))
    // }

    // Transpose the grid - note that the grid is not symmetric.
    // For example:
    // [['1', '2', '3'],
    // ['4', '5', '6']]
    // becomes:
    // [['1', '4'],
    // ['2', '5'],
    // ['3', '6']]

    // Find the longest row in the grid.
    // This is needed because the rows may have different lengths.
    val maxRowLength = grid.maxOf { it.size }

    // Ignore the last row of the grid as that contains the operators.
    val gridWithoutOperators = grid.dropLast(1)
    val transposed = List(maxRowLength) { col ->
        List(gridWithoutOperators.size) { row ->
            // println("Accessing grid[$row][$col]")
            // Check if the column index is within bounds for this row.
            // Else use a space character as a placeholder.
            if (col >= gridWithoutOperators[row].size) ' ' else
                gridWithoutOperators[row][col]
        }
    }

    // Debug print the grid.
    // for (row in transposed) {
    //     println(row.joinToString(" "))
    // }

    // Process each row in the grid as a number.
    // IDEA: Join all the items of the row as a string.
    // Parse the strings as a Long.
    var problemIdx = 0
    for (row in transposed) {
        val numberStr = row.joinToString("").trim()
        if (numberStr.isNotEmpty()) {
            val number = numberStr.toLong()
            // println("Parsed number: $number")
            // Add the number to the problem at the specified index.
            if (problemIdx >= problems.size) {
                problems.add(Problem(mutableListOf(), null))
            }
            problems[problemIdx].nums.add(number)
        } else {
            problemIdx++
        }
    }

    return problems
}

fun parseProblems(input: String): List<Problem> {
// Each column defines a single problem.
// The last line contains the operator.
// For example:
// 1 2 3
// 42 5 69
// + * +
// Would define three problems:
// Problem 1: nums = [1, 42], op = "+"
// Problem 2: nums = [2, 5], op = "*"
// Problem 3: nums = [3, 69], op = "+"
    val lines = input.lines().filter { it.isNotBlank() }
    if (lines.isEmpty()) return emptyList()
    // Sanitize the parsed line by filtering for empty strings.
    val numProblems = lines[0].split(" ").filter { it.isNotBlank() }.size
    val problems = MutableList(numProblems) { Problem(mutableListOf(), null) }
    for (lineIndex in lines.indices) {
        val line = lines[lineIndex]
        val tokens = line.split(" ").filter { it.isNotBlank() }
        for (i in 0..<numProblems) {
            val token = tokens[i]
            if (lineIndex == lines.size - 1) {
                // Last line: operator
                problems[i].op = token
            } else {
                // Number line
                // Append the number to the problem's nums list
                // Note: We need to create a new list since nums is immutable.
                // println("Adding ${token} to problem $i")
                problems[i].nums.add(token.toLong())
            }
        }
    }
    return problems
}


fun main(args: Array<String>) {
    // Accept the path to the input file as a command-line argument (default to data/input/day-06.txt)
    val inputPath = args.firstOrNull() ?: "data/input/day-06.txt"

    // Read the input file content.
    val input = java.io.File(inputPath).readText()

    // Parse the input into a collection of problems.
    val problems = parseProblems(input)

    // Part 1
    val solutions = problems.map { it.solve() }
    println("Part 1: ${solutions.sum()}")

    // Part 2
    val problemsColumnar = parseProblems2D(input)

    val solutionsColumnar = problemsColumnar.map { it.solve() }
    println("Part 2: ${solutionsColumnar.sum()}")
}
