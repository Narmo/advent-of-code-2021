import java.util.*

fun main() {
	val resetValue = 10

	data class Point(val x: Int, val y: Int, val risk: Int, var distance: Int = Int.MAX_VALUE - resetValue, var processed: Boolean = false)

	fun getPoints(input: List<String>): List<List<Point>> = input.mapIndexed { y, line ->
		line.mapIndexed { x, char ->
			Point(x, y, char.digitToInt())
		}
	}

	fun getAdjacentPoints(points: List<List<Point>>, row: Int, col: Int): Set<Point> {
		/*
		 -------------------
		 |     |     |     |
		 |     |-1,0 |     |
		 |     |     |     |
		 -------------------
		 |     |     |     |
		 | 0,-1| 0,0 | 0,1 |
		 |     |     |     |
		 -------------------
		 |     |     |     |
		 |     | 1,0 |     |
		 |     |     |     |
		 -------------------
		 */

		return listOf(Pair(1, 0), Pair(0, 1), Pair(0, -1), Pair(-1, 0)).mapNotNull {
			if ((row + it.first) in points.indices && (col + it.second) in points[row].indices) {
				points[row + it.first][col + it.second]
			}
			else {
				null
			}
		}.toSet()
	}

	fun expandPoints(input: List<String>): List<String> {
		val side = input.first().length
		val matrix = Array(side * 5) { IntArray(side * 5) { 0 } }

		val expandedLines = input.mapIndexed { row, line ->
			val ints = line.map { it.digitToInt() }.toMutableList()

			repeat(4) { step ->
				ints.subList(0, input.first().length).toList().forEach {
					var newValue = it + step + 1

					if (newValue >= resetValue) {
						newValue -= (resetValue - 1)
					}

					ints.add(newValue)
				}
			}

			ints.forEachIndexed { col, i ->
				matrix[row][col] = i
			}

			ints.toList()
		}

		repeat(4) { step ->
			for (col in expandedLines.first().indices) {
				for (row in expandedLines.indices) {
					val offset = step * side + side
					val realRow = row + offset
					var newValue = expandedLines[row][col] + step + 1

					if (newValue >= resetValue) {
						newValue -= (resetValue - 1)
					}

					matrix[realRow][col] = newValue
				}
			}
		}

		return matrix.map {
			it.joinToString("")
		}
	}

	// Following solution is bBased on
	// https://github.com/PhenixFine/advent-of-code-kotlin-2021/blob/main/src/Day15.kt

	fun Point.updateMinDistance(adjacent: Set<Point>) {
		distance = distance.coerceAtMost(adjacent.minOf { it.distance } + risk)
	}

	fun Set<Point>.updateMinDistance(point: Point) {
		forEach {
			it.distance = it.distance.coerceAtMost(point.distance + it.risk)
		}
	}

	fun traverseAll(points: List<List<Point>>): Int {
		val start = points.first().first().also { it.distance = 0 }
		val end = points.last().last()
		val queue = PriorityQueue<Point>(compareBy { it.distance }).also { it.add(start) }

		while (queue.isNotEmpty() && !end.processed) {
			val point = queue.poll()

			if (point.processed) {
				continue
			}

			getAdjacentPoints(points, point.y, point.x).run {
				point.updateMinDistance(this)

				filterNot { it.processed }.toSet().run {
					this.updateMinDistance(point)
					queue.addAll(this)
				}
			}

			point.processed = true
		}

		return end.distance
	}

	fun part1(input: List<String>): Int {
		val points = getPoints(input)
		return traverseAll(points)
	}

	fun part2(input: List<String>): Int {
		val expandedInput = expandPoints(input)
		val points = getPoints(expandedInput)
		return traverseAll(points)
	}

	val testInput = readInput("Day15_test")
	println(testInput)
	check(part1(testInput) == 40)
	check(part2(testInput) == 315)

	val testInput2 = readInput("Day15_test2")
	println(testInput2)
	check(part1(testInput2) == 8)

	val input = readInput("Day15")
	println(part1(input))
	println(part2(input))
}
