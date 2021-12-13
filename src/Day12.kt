fun main() {
	data class Cave(val name: String) {
		val isSmall = name.lowercase() == name
		val isStart = name == "start"
		val isEnd = name == "end"
		val neighbours = mutableSetOf<Cave>()

		fun join(other: Cave) {
			this.neighbours.add(other)
			other.neighbours.add(this)
		}

		override fun equals(other: Any?): Boolean {
			if (other !is Cave) {
				return false
			}

			return other.name == this.name
		}

		override fun hashCode(): Int {
			return name.hashCode()
		}
	}

	fun getCaves(input: List<String>): Set<Cave> {
		val map = mutableMapOf<String, Cave>()

		val caves = input.flatMap { line ->
			val adjCaves = line.split("-").map { name ->
				map[name] ?: Cave(name).also { map[name] = it }
			}

			adjCaves[0].join(adjCaves[1])

			return@flatMap adjCaves
		}.toSet()

		return caves
	}

	// I've done this task with help from this solution:
	// https://gist.github.com/tbpaolini/74c3009400a24807eb96aa70a4615c07
	fun traverseAll(caves: Set<Cave>, canVisitTwice: Boolean = false): List<List<Cave>> {
		val paths = mutableListOf<List<Cave>>()

		fun traverse(cave: Cave, path: MutableList<Cave> = mutableListOf(), canVisitTwice: Boolean) {
			if (cave.isStart && path.size > 1) {
				// we have returned to the beginning of the path
				return
			}

			if (cave.isEnd) {
				// we have reached the end
				paths.add(path + cave)
				return
			}

			var extraVisitAllowed = canVisitTwice

			if (cave.isSmall) {
				val visits = path.count { it == cave }

				if (visits >= 1) {
					if (canVisitTwice) {
						extraVisitAllowed = false
					}
					else {
						// we can't visit small cave again
						return
					}
				}
			}

			cave.neighbours.forEach {
				traverse(it, (path + cave).toMutableList(), extraVisitAllowed)
			}
		}

		traverse(caves.find { it.isStart }!!, canVisitTwice = canVisitTwice)

		return paths
	}

	fun part1(input: List<String>): Int {
		val caves = getCaves(input)
		val paths = traverseAll(caves)
		return paths.size
	}

	fun part2(input: List<String>): Int {
		val caves = getCaves(input)
		val paths = traverseAll(caves, canVisitTwice = true)
		return paths.size
	}

	val testInput1 = readInput("Day12_test1")
	println(testInput1)
	check(part1(testInput1) == 10)
	check(part2(testInput1) == 36)

	val testInput2 = readInput("Day12_test2")
	println(testInput2)
	check(part1(testInput2) == 19)
	check(part2(testInput2) == 103)

	val testInput3 = readInput("Day12_test3")
	println(testInput3)
	check(part1(testInput3) == 226)
	check(part2(testInput3) == 3509)

	val input = readInput("Day12")
	println(part1(input))
	println(part2(input))
}
