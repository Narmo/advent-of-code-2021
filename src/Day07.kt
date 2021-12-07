import kotlin.math.abs

fun main() {
	fun getPositions(input: String): IntArray = input.split(",").map { it.toInt() }.toIntArray()

	fun IntArray.median(): Int {
		val sorted = this.sortedArray()

		return if (sorted.size % 2 == 0) {
			((sorted[(sorted.size / 2)] + sorted[(sorted.size / 2) - 1])) / 2
		}
		else {
			(sorted[(sorted.size - 1) / 2])
		}
	}

	fun fuelConsumption(steps: Int): Int = steps * (steps + 1) / 2

	fun part1(input: List<String>): Int {
		val positions = getPositions(input[0])
		val median = positions.median()

		return positions.fold(0) { acc, i ->
			acc + abs(i - median)
		}
	}

	fun part2(input: List<String>): Int {
		val positions = getPositions(input[0])

		return (0..positions.maxOrNull()!!).minOf { pos ->
			positions.fold(0) { acc, i ->
				acc + fuelConsumption(abs(i - pos))
			}
		}
	}

	val testInput = readInput("Day07_test")
	println(testInput)
	check(part1(testInput) == 37)
	check(part2(testInput) == 168)

	val input = readInput("Day07")
	println(part1(input))
	println(part2(input))
}
