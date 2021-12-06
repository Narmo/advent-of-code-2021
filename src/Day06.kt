fun main() {
	class Shoal(fishes: List<Int>) {
		val counters = LongArray(9)

		init {
			fishes.forEach {
				counters[it] = counters[it] + 1
			}
		}

		val total: Long
			get() = counters.fold(0) { acc, l ->
				acc + l
			}

		fun elapseDay() {
			var newborns = 0L

			for (i in counters.indices) {
				val decrement = counters[i]
				counters[i] = 0

				if (i == 0) {
					newborns += decrement
				}
				else {
					counters[i - 1] = decrement
				}
			}

			counters[6] = counters[6] + newborns
			counters[8] = counters[8] + newborns
		}
	}

	fun getFishes(input: String) = input.split(",").map {
		it.toInt()
	}

	fun elapse(days: Int, input: String): Long {
		return Shoal(getFishes(input)).apply {
			for (i in 0 until days) {
				elapseDay()
			}
		}.total
	}

	fun part1(input: List<String>): Long = elapse(80, input[0])

	fun part2(input: List<String>): Long = elapse(256, input[0])

	val testInput = readInput("Day06_test")
	println(testInput)
	check(part1(testInput) == 5934L)
	check(part2(testInput) == 26984457539L)

	val input = readInput("Day06")
	println(part1(input))
	println(part2(input))
}
