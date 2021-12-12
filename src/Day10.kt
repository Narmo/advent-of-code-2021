fun main() {
	val valid = listOf(Pair('(', ')'), Pair('[', ']'), Pair('{', '}'), Pair('<', '>'))
	val openings = valid.map { it.first }
	val closings = valid.map { it.second }
	val illegalPoints = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
	val incompletePoints = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)

	fun part1(input: List<String>): Int {
		val invalid = mutableListOf<Char>()

		input.forEach lines@{ line ->
			val open = mutableListOf<Char>()

			line.forEach chars@{ char ->
				if (openings.contains(char)) {
					open.add(char)
				}

				if (closings.contains(char)) {
					val matchingOpening = openings[closings.indexOf(char)]

					if (open.last() != matchingOpening) {
						invalid.add(char)
						return@lines
					}
					else {
						open.removeLast()
					}
				}
			}
		}

		return invalid.sumOf { illegalPoints[it] ?: 0 }
	}

	fun part2(input: List<String>): Long {
		val requiredClosings = mutableListOf<List<Char>>()

		input.forEach lines@{ line ->
			val open = mutableListOf<Char>()

			line.forEach chars@{ char ->
				if (openings.contains(char)) {
					open.add(char)
				}

				if (closings.contains(char)) {
					val matchingOpening = openings[closings.indexOf(char)]

					if (open.last() != matchingOpening) {
						return@lines
					}
					else {
						open.removeLast()
					}
				}
			}

			if (open.isNotEmpty()) {
				requiredClosings.add(open.reversed().map { closings[openings.indexOf(it)] })
			}
		}

		val scores = requiredClosings.map {
			it.fold(0L) { acc, c ->
				acc * 5L + (incompletePoints[c] ?: 0).toLong()
			}
		}.sortedDescending()

		return scores[scores.size / 2]
	}

	val testInput = readInput("Day10_test")
	println(testInput)
	check(part1(testInput) == 26397)
	check(part2(testInput) == 288957L)

	val input = readInput("Day10")
	println(part1(input))
	println(part2(input))
}
