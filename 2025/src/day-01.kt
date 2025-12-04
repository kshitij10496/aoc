// Instruction represents a single movement command with a direction and a value.
// Example: L68 -> Instruction("L", 68)
data class Instruction(
	val direction: String,
	val value: Int
)

// Result represents the outcome of processing instructions, including final position and count of zeros visited.
data class Result(
	val position: Int,
	val zeroCount: Int
)

fun parseInstruction(s: String): Instruction {
	// Trim any whitespace from the input string.
	val s = s.trim();

	// Assume the first character is the direction and the rest is the value.
	val direction = s.take(1).uppercase()
	val value = s.substring(1).toInt()
	return Instruction(direction, value)
}

// Function to parse the example input for Day 01 of Advent of Code 2025.
fun parseInput(s: String): List<Instruction> {
	// Split the input string by newline character.
	// Parse each line into an Instruction object.
	return s.trim().lines().map { parseInstruction(it) }
}

// applyInstruction returns the new position after applying the instruction.
// "L" decreases the position, "R" increases it.
// Consider the circular nature of the track of length 100, starting at position 0 and wrapping around.
// Example: applyInstruction(10, Instruction("L", 3)) -> 7
fun applyInstruction(pos: Int, i: Instruction): Result {
	if (i.direction == "L") {
		return Result((pos - i.value).mod(100), 0)
	} else if (i.direction == "R") {
		return Result((pos + i.value).rem(100), 0)
	}
	throw IllegalArgumentException("Unknown direction: ${i.direction}")
}

// getPassword computes the number of times the position 0 is visited after each instruction is applied.
fun getPassword(startPos: Int, instructions: List<Instruction>): Int {
	var pos = startPos
	var zeroCount = 0

	for (instruction in instructions) {
		val result = applyInstruction(pos, instruction)
		pos = result.position
		if (pos == 0) {
			// Increment zero count if position is 0
			zeroCount += 1
		}
	}

	return zeroCount
}


// getPasswordMethodCLICK computes the number of times the position 0 is visited after or
// during the application of each instruction.
fun getPasswordMethodCLICK(startPos: Int, instructions: List<Instruction>): Int {
	var pos = startPos
	var zeroCount = 0

	for (instruction in instructions) {
		// We go through each dial movement one step at a time.
		val delta = if (instruction.direction == "L") -1 else 1

		for (step in 1..instruction.value) {
			pos = (pos + delta).mod(100)
			if (pos == 0) {
				zeroCount += 1
			}
		}
	}

	return zeroCount
}

fun main(args: Array<String>) {
// Accept the path to the input file as a command-line argument (default to data/input/day-01.txt).
	val inputPath = args.firstOrNull() ?: "data/input/day-01.txt"

// Read the input file content.
	val input = java.io.File(inputPath).readText()

// Parse the input into a list of Instructions.
	val instructions = parseInput(input)

// Compute the password starting from position 50.
	println("Part 1: ${getPassword(50, instructions)}")
	println("Part 2: ${getPasswordMethodCLICK(50, instructions)}")
}
