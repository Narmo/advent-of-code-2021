fun main() {
	val boardSide = 5

	class Board(set: List<Int>) {
		val matrix = Array(boardSide) { IntArray(boardSide) { 0 } }
		val marks = Array(boardSide) { BooleanArray(boardSide) { false } }

		val isWinner: Boolean
			get() {
				marks.forEach { row ->
					val isWinner = row.fold(true) { acc, value ->
						acc && value
					}

					if (isWinner) {
						return true
					}
				}

				for (i in 0 until boardSide) {
					val isWinner = marks.fold(true) { acc, row ->
						acc && row[i]
					}

					if (isWinner) {
						return true
					}
				}

				return false
			}

		init {
			set.forEachIndexed { index, i ->
				val r = index / boardSide
				val c = index % boardSide

				matrix[r][c] = i
			}
		}

		fun check(number: Int): Boolean {
			for (row in 0 until boardSide) {
				for (column in 0 until boardSide) {
					if (matrix[row][column] == number) {
						marks[row][column] = true
					}
				}
			}

			return isWinner
		}

		fun unmarkedSum(): Int {
			var result = 0

			for (row in 0 until boardSide) {
				for (column in 0 until boardSide) {
					if (!marks[row][column]) {
						result += matrix[row][column]
					}
				}
			}

			return result
		}
	}

	fun fillBoards(input: List<String>): Pair<List<Int>, List<Board>> {
		val order = input[0].split(",").map { it.toInt() }
		val boards = mutableListOf<Board>()
		var set = mutableListOf<Int>()

		for (i in 1 until input.size) {
			val elements = input[i].trim().split(Regex("\\s+"))

			if (elements.size == boardSide) {
				set.addAll(elements.map { it.toInt() })
			}

			if (set.size == boardSide * boardSide) {
				boards.add(Board(set))
				set = mutableListOf()
			}
		}

		return Pair(order, boards)
	}

	fun part1(input: List<String>): Int {
		val (order, boards) = fillBoards(input)
		var winner: Board? = null
		var winNumber: Int? = null

		run loop@{
			order.forEach forEachOrder@{ number ->
				boards.forEach forEachBoard@{ board ->
					val isWinner = board.check(number)

					if (isWinner) {
						winner = board
						winNumber = number
						return@loop
					}
				}
			}
		}

		val unmarkedSum = winner!!.unmarkedSum()

		return winNumber!! * unmarkedSum
	}

	fun part2(input: List<String>): Int {
		val (order, boards) = fillBoards(input)
		var winner: Board? = null
		var winNumber: Int? = null
		val winners = BooleanArray(boards.size) { false }

		run loop@{
			order.forEach forEachOrder@{ number ->
				boards.forEachIndexed forEachBoard@{ index, board ->
					val isWinner = board.check(number)

					if (isWinner) {
						winners[index] = true

						if (winners.fold(true) { acc, b -> acc && b }) {
							winner = board
							winNumber = number
							return@loop
						}
					}
				}
			}
		}

		val unmarkedSum = winner!!.unmarkedSum()

		return winNumber!! * unmarkedSum
	}

	// test if implementation meets criteria from the description, like:
	val testInput = readInput("Day04_test")
	println(testInput)
	check(part1(testInput) == 4512)
	check(part2(testInput) == 1924)

	val input = readInput("Day04")
	println(part1(input))
	println(part2(input))
}
