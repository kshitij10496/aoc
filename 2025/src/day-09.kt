fun parseVertices(input: String): List<Point2D> {
    val lines = input.lines().filter { it.isNotBlank() }
    return lines.map { line ->
        val parts = line.split(",")
        Point2D(parts[0].trim().toLong(), parts[1].trim().toLong())
    }
}

fun main(args: Array<String>) {
    // Accept the path to the input file as a command-line argument (default to data/input/day-09.txt)
    val inputPath = args.firstOrNull() ?: "data/input/day-09.txt"

    // Read the input file content.
    val input = java.io.File(inputPath).readText()

    // Parse vertices from the input.
    val vertices = parseVertices(input)

    // Create polygon from vertices
    val polygon = Polygon(vertices)

    // Part 1: Find the area of the largest rectangle between any two vertices.
    println("Part 1: ${polygon.largestRectangleBetweenVertices()}")

    // Part 2: Find the largest rectangle that fits entirely within the polygon.
    // Only show visualization and stats for small polygons (< 50 vertices)
    if (vertices.size < 50) {
        println("\nPolygon visualization:")
        println(polygon.render())
        println("\nPolygon stats:")
        println("  Area: ${polygon.area()}")
        println("  Perimeter: ${polygon.perimeter()}")
        println("  Total points: ${polygon.totalPoints()}")
    }

    println("\nPart 2: ${polygon.largestRectangleWithVertexCorners()}")
}
