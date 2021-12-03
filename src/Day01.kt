fun main() {
	fun part1(input: List<String>): Int {
		var increases = 0

		for (i in 1 until input.size) {
			val prev = input[i - 1].toInt()
			val cur = input[i].toInt()

			if (cur > prev) {
				increases += 1
			}
		}

		return increases
	}

	fun part2(input: List<String>): Int {
		val sums = mutableListOf<Int>()

		for (i in input.indices) {
			if (i + 2 >= input.size) {
				break
			}

			val sum = input[i].toInt() + input[i + 1].toInt() + input[i + 2].toInt()
			sums.add(sum)
		}

		var increases = 0

		for (i in 1 until sums.size) {
			if (sums[i] > sums[i - 1]) {
				increases += 1
			}
		}

		return increases
	}

	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day01_test")
	println(testInput)
	check(part1(testInput) == 7)
	check(part2(testInput) == 5)

	val input = readInput("Day01")
	println(part1(input))
	println(part2(input))
}
