fun main() {
	data class Octopus(var energy: Int = 0, val row: Int, val col: Int, var chargeStep: Int = 0)

	fun getOctopuses(input: List<String>): List<List<Octopus>> = input.mapIndexed { lineIndex, line ->
		line.mapIndexed { charIndex, char ->
			Octopus(char.digitToInt(), lineIndex, charIndex)
		}
	}

	fun getAdjacentOctopuses(map: List<List<Octopus>>, row: Int, col: Int): List<Octopus> {
		val adj = mutableListOf<Octopus>()

		/*
		 -------------------
		 |     |     |     |
		 |-1,-1|-1,0 |-1,1 |
		 |     |     |     |
		 -------------------
		 |     |     |     |
		 | 0,-1| 0,0 | 0,1 |
		 |     |     |     |
		 -------------------
		 |     |     |     |
		 | 1,-1| 1,0 | 1,1,|
		 |     |     |     |
		 -------------------
		 */

		listOf(Pair(-1, -1), Pair(-1, 0), Pair(-1, 1), Pair(0, -1), Pair(0, 1), Pair(1, -1), Pair(1, 0), Pair(1, 1)).forEach {
			if ((row + it.first) in map.indices && (col + it.second) in map[row].indices) {
				adj.add(map[row + it.first][col + it.second])
			}
		}

		return adj.toList()
	}

	fun charge(list: List<Octopus>, step: Int): MutableList<Octopus> {
		val out = mutableListOf<Octopus>()

		list.forEach {
			if (it.chargeStep < step) {
				it.energy += 1

				if (it.energy > 9) {
					it.energy = 0
					it.chargeStep = step
					out.add(it)
				}
			}
		}

		return out
	}

	fun part1(input: List<String>): Int {
		val octopuses = getOctopuses(input)
		var totalFlashes = 0

		for (step in 0..100) {
			octopuses.forEach { line ->
				var newBatch = charge(line, step)

				while (newBatch.isNotEmpty()) {
					totalFlashes += newBatch.size
					newBatch = charge(newBatch.flatMap { getAdjacentOctopuses(octopuses, it.row, it.col) }, step)
				}
			}
		}

		return totalFlashes
	}

	fun part2(input: List<String>): Int {
		val octopuses = getOctopuses(input)

		for (step in 0..Int.MAX_VALUE) {
			octopuses.forEach { line ->
				var newBatch = charge(line, step)

				while (newBatch.isNotEmpty()) {
					newBatch = charge(newBatch.flatMap { getAdjacentOctopuses(octopuses, it.row, it.col) }, step)
				}
			}

			val synced = octopuses.flatten().run {
				map {
					it.energy == this[0].energy
				}.fold(true) { acc, b ->
					acc && b
				}
			}

			if (synced) {
				return step
			}
		}

		return -1
	}

	val testInput = readInput("Day11_test")
	println(testInput)
	check(part1(testInput) == 1656)
	check(part2(testInput) == 195)

	val input = readInput("Day11")
	println(part1(input))
	println(part2(input))
}
