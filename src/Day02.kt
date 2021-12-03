fun main() {
	fun part1(input: List<String>): Int {
		var position = 0
		var depth = 0

		input.forEach {
			val seq = it.split(" ")
			val command = seq[0]
			val value = seq[1].toInt()

			when (command) {
				"forward" -> position += value
				"down" -> depth += value
				"up" -> depth -= value
			}
		}

		return position * depth
	}

	fun part2(input: List<String>): Int {
		var position = 0
		var depth = 0
		var aim = 0

		input.forEach {
			val seq = it.split(" ")
			val command = seq[0]
			val value = seq[1].toInt()

			when (command) {
				"down" -> {
					aim += value
				}
				"up" -> {
					aim -= value
				}
				"forward" -> {
					position += value
					depth += aim * value
				}
			}
		}

		return position * depth
	}

	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day02_test")
	println(testInput)
	check(part1(testInput) == 150)
	check(part2(testInput) == 900)

	val input = readInput("Day02")
	println(part1(input))
	println(part2(input))
}
