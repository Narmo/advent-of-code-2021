fun main() {
	data class Fold(val axis: String, val value: Int)

	fun print(header: String, points: Set<Pair<Int, Int>>) {
		println("======== $header ========")

		val maxX = points.maxOf { it.first }
		val maxY = points.maxOf { it.second }

		for (y in 0..maxY) {
			val line = points.filter { it.second == y }

			for (x in 0..maxX) {
				val exists = line.firstOrNull { it.first == x } != null
				print(if (exists) "#" else " ")
			}

			print("\n")
		}
	}

	fun getTransparent(input: List<String>): Pair<Set<Pair<Int, Int>>, List<Fold>> {
		val points = mutableSetOf<Pair<Int, Int>>()
		val folds = mutableListOf<Fold>()

		input.forEach {
			when {
				it.startsWith("fold") -> {
					val params = it.removePrefix("fold along ").split("=")
					folds.add(Fold(params[0], params[1].toInt()))
				}
				it.isEmpty() -> {
					// skip
				}
				else -> {
					val params = it.split(",")
					points.add(Pair(params[0].toInt(), params[1].toInt()))
				}
			}
		}

		return Pair(points, folds)
	}

	fun <A, B> Pair<A, B>.swap(): Pair<B, A> = Pair(second, first)

	fun fold(points: Set<Pair<Int, Int>>, axis: String, value: Int): Set<Pair<Int, Int>> = points.map {
		val pair = if (axis == "y") it.swap() else it

		if (pair.first < value) {
			it
		}
		else {
			val offset = pair.first - value
			val newValue = value - offset

			if (axis == "y") Pair(it.first, newValue) else Pair(newValue, it.second)
		}
	}.toSet()

	fun part1(input: List<String>): Int {
		val (points, folds) = getTransparent(input)
		val result = fold(points, folds[0].axis, folds[0].value)

		print("Part 1", result)

		return result.size
	}

	fun part2(input: List<String>): Int {
		val (points, folds) = getTransparent(input)
		var result = points.toSet()

		folds.forEach {
			result = fold(result, it.axis, it.value)
		}

		print("Part 2", result)

		return result.size
	}

	val testInput = readInput("Day13_test")
	println(testInput)
	check(part1(testInput) == 17)

	val input = readInput("Day13")
	println(part1(input))
	println(part2(input))
}
