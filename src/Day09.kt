fun main() {
	fun loadMap(input: List<String>): List<List<Int>> = input.map { line ->
		line.map { point ->
			point.digitToInt()
		}
	}

	fun getAdjacentPoints(map: List<List<Int>>, row: Int, col: Int): List<Pair<Int, Int>> {
		val adj = mutableListOf<Pair<Int, Int>>()

		listOf(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0)).forEach {
			if ((row + it.first) in map.indices && (col + it.second) in map[row].indices) {
				adj.add(Pair(row + it.first, col + it.second))
			}
		}

		return adj.toList()
	}

	fun isLowest(map: List<List<Int>>, row: Int, col: Int): Boolean {
		var low = true

		getAdjacentPoints(map, row, col).forEach {
			val point = map[row][col]

			if (point == 9 || map[it.first][it.second] < point) {
				low = false
			}
		}

		return low
	}

	fun getLows(map: List<List<Int>>): List<Pair<Int, Int>> {
		val heightMap = mutableListOf<Pair<Int, Int>>()

		map.forEachIndexed { row, line ->
			line.forEachIndexed { col, _ ->
				if (isLowest(map, row, col)) {
					heightMap.add(Pair(row, col))
				}
			}
		}

		return heightMap.toList()
	}

	fun getBasin(map: List<List<Int>>, row: Int, col: Int): List<Pair<Int, Int>> {
		val basin = mutableListOf<Pair<Int, Int>>()
		val visited = mutableSetOf<Pair<Int, Int>>()
		val queue = mutableListOf(Pair(row, col))

		while (queue.isNotEmpty()) {
			val point = queue.removeFirst()

			if (visited.contains(point)) {
				continue
			}

			visited.add(point)

			if (map[point.first][point.second] != 9) {
				basin.add(point)
				queue.addAll(getAdjacentPoints(map, point.first, point.second).filter { !visited.contains(it) })
			}
		}

		return basin
	}

	fun part1(input: List<String>): Int {
		val map = loadMap(input)
		val lows = getLows(map)
		return lows.sumOf { map[it.first][it.second] + 1 }
	}

	fun part2(input: List<String>): Int {
		val map = loadMap(input)
		val lows = getLows(map)

		val basins = lows.map {
			getBasin(map, it.first, it.second)
		}

		return basins.sortedByDescending { it.size }.take(3).fold(1) { acc, points ->
			acc * points.size
		}
	}

	val testInput = readInput("Day09_test")
	println(testInput)
	check(part1(testInput) == 15)
	check(part2(testInput) == 1134)

	val input = readInput("Day09")
	println(part1(input))
	println(part2(input))
}
