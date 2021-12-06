import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
	fun gcd(n1: Int, n2: Int): Int {
		if (n2 == 0) {
			return n1
		}

		return gcd(n2, n1 % n2)
	}

	data class Point(val x: Int, val y: Int)

	class Matrix(val width: Int, height: Int) {
		val items = Array(width * height) { 0 }

		operator fun get(row: Int, column: Int): Int {
			val index = row * width + column
			return items[index]
		}

		operator fun set(row: Int, column: Int, element: Int): Int {
			val index = row * width + column
			val prevValue = items[index]
			items[index] = element
			return prevValue
		}

		override fun toString(): String {
			return items.toList().windowed(width).toString()
		}
	}

	data class Line(val start: Point, val end: Point) {
		val isDiagonal = abs(start.x - end.x) == abs(start.y - end.y)
		val points: List<Point>

		init {
			val points = mutableListOf<Point>()
			val n = gcd(abs(start.x - end.x), abs(start.y - end.y))
			val dx = (end.x - start.x) / n
			val dy = (end.y - start.y) / n

			for (i in 0..n) {
				points.add(Point(x = start.x + dx * i, y = start.y + dy * i))
			}

			this.points = points.toList()
		}
	}

	/**
	 * @return Triple: list of lines, top left corner, bottom right corner
	 */
	fun getLines(input: List<String>): Pair<List<Line>, Point> {
		var (minX, minY, maxX, maxY) = listOf(Int.MAX_VALUE, Int.MAX_VALUE, Int.MIN_VALUE, Int.MIN_VALUE)

		val lines = input.map {
			val coordinates = it.split(" -> ")
			val start = coordinates[0].split(",")
			val end = coordinates[1].split(",")
			val startPoint = Point(x = start[0].toInt(), y = start[1].toInt())
			val endPoint = Point(x = end[0].toInt(), y = end[1].toInt())

			minX = min(min(startPoint.x, endPoint.x), minX)
			maxX = max(max(startPoint.x, endPoint.x), maxX)
			minY = min(min(startPoint.y, endPoint.y), minY)
			maxY = max(max(startPoint.y, endPoint.y), maxY)

			Line(start = startPoint, end = endPoint)
		}

		return Pair(lines, Point(x = maxX, y = maxY))
	}

	fun countOverlaps(lines: List<Line>, maxPoint: Point): Int {
		val matrix = Matrix(maxPoint.x + 1, maxPoint.y + 1)

		lines.forEach { line ->
			line.points.forEach {
				matrix[it.y, it.x] += 1
			}
		}

		val overlaps = matrix.items.filter { it >= 2 }

		return overlaps.size
	}

	fun part1(input: List<String>): Int {
		val (lines, max) = getLines(input)

		return countOverlaps(lines.filter {
			it.start.x == it.end.x || it.start.y == it.end.y
		}, max)
	}

	fun part2(input: List<String>): Int {
		val (lines, max) = getLines(input)

		return countOverlaps(lines.filter {
			it.start.x == it.end.x || it.start.y == it.end.y || it.isDiagonal
		}, max)
	}

	val testInput = readInput("Day05_test")
	println(testInput)
	check(part1(testInput) == 5)
	check(part2(testInput) == 12)

	val input = readInput("Day05")
	println(part1(input))
	println(part2(input))
}
