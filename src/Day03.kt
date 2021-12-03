fun main() {
	fun IntArray.toDecimal(): Int {
		var result = 0

		for (i in indices) {
			val pos = size - 1 - i

			if (this[i] == 1) {
				result += 2.pow(pos)
			}
		}

		return result
	}

	fun String.toIntArray(): IntArray {
		val array = IntArray(length) { 0 }

		forEachIndexed { index, c ->
			when (c) {
				'0' -> array[index] = 0
				'1' -> array[index] = 1
			}
		}

		return array
	}

	fun getZeroesAndOnes(input: List<String>): Pair<IntArray, IntArray> {
		val seqLength = input.first().length
		val zeroes = IntArray(seqLength) { 0 }
		val ones = IntArray(seqLength) { 0 }

		input.forEach {
			it.forEachIndexed { index, c ->
				when (c) {
					'0' -> zeroes[index] += 1
					'1' -> ones[index] += 1
				}
			}
		}

		return Pair(zeroes, ones)
	}

	fun part1(input: List<String>): Int {
		val seqLength = input.first().length
		val stats = getZeroesAndOnes(input)
		val zeroes = stats.first
		val ones = stats.second
		var gammaRate = 0
		var epsilonRate = 0

		for (i in 0 until seqLength) {
			val pos = seqLength - 1 - i

			if (ones[i] > zeroes[i]) {
				gammaRate += 2.pow(pos)
			}
			else {
				epsilonRate += 2.pow(pos)
			}
		}

		return gammaRate * epsilonRate
	}

	fun part2(input: List<String>): Int {
		val seqLength = input.first().length
		val oxygenEntries = input.toMutableList()
		val coEntries = input.toMutableList()

		for (i in 0 until seqLength) {
			val oxygenStats = getZeroesAndOnes(oxygenEntries)
			val oxygenZeroes = oxygenStats.first
			val oxygenOnes = oxygenStats.second

			if (oxygenEntries.size > 1) {
				val needOne = oxygenOnes[i] >= oxygenZeroes[i]

				oxygenEntries.removeAll {
					if (needOne) {
						it[i] != '1'
					}
					else {
						it[i] == '1'
					}
				}
			}

			val coStats = getZeroesAndOnes(coEntries)
			val coZeroes = coStats.first
			val coOnes = coStats.second

			if (coEntries.size > 1) {
				val needOne = coOnes[i] >= coZeroes[i]

				coEntries.removeAll {
					if (needOne) {
						it[i] == '1'
					}
					else {
						it[i] != '1'
					}
				}
			}
		}

		val oxygenGeneratorRating = oxygenEntries.first().toIntArray().toDecimal()
		val coScrubberRating = coEntries.first().toIntArray().toDecimal()

		return oxygenGeneratorRating * coScrubberRating
	}

	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day03_test")
	println(testInput)
	check(part1(testInput) == 198)
	check(part2(testInput) == 230)

	val input = readInput("Day03")
	println(part1(input))
	println(part2(input))
}
