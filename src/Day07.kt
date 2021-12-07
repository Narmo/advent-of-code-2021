import kotlin.math.abs

fun main() {
	fun getPositions(input: String): IntArray = input.split(",").map { it.toInt() }.toIntArray()

	fun IntArray.median(): Int {
		val sorted = this.sortedArray()

		return if (sorted.size % 2 == 0) {
			((sorted[(sorted.size / 2)] + sorted[(sorted.size / 2) - 1])) / 2
		}
		else {
			(sorted[(sorted.size - 1) / 2])
		}
	}

	fun fuelConsumption(steps: Int): Int = steps * (steps + 1) / 2

	fun part1(input: List<String>): Int {
		val positions = getPositions(input[0]).toMutableList()
		val median = positions.toIntArray().median()

//		val width = positions.maxOrNull()!!
//		val height = positions.size
//
//		FileOutputStream(File("the-treachery-of-whales-part1.csv")).use { fos ->
//			fos.bufferedWriter().use {
//				val iterPositions = positions.toMutableList()
//				var step = 0
//
//				it.write("step,${(0 until iterPositions.size).joinToString(",")}\n")
//
//				while (true) {
//					val bi = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
//					val ig2 = bi.createGraphics()
//					ig2.paint = Color.black
//					ig2.drawRect(0, 0, width, height)
//					ig2.paint = Color.yellow
//
//					var medianCounter = 0
//
//					it.write(step.toString())
//
//					for (i in iterPositions.indices) {
//						it.write(",")
//
//						val curValue = iterPositions[i]
//
//						if (curValue > median) {
//							iterPositions[i] = curValue - 1
//						}
//						else if (curValue < median) {
//							iterPositions[i] = curValue + 1
//						}
//						else {
//							medianCounter += 1
//						}
//
//						it.write(iterPositions[i].toString())
//
//						ig2.drawRect(iterPositions[i], i, 1, 1)
//					}
//
//					ImageIO.write(bi, "PNG", File("images/images${"%05d".format(step)}.png"))
//
//					ig2.dispose()
//
//					it.write("\n")
//
//					step += 1
//
//					if (medianCounter == iterPositions.size) {
//						break
//					}
//				}
//			}
//		}

		return positions.fold(0) { acc, i ->
			acc + abs(i - median)
		}
	}

	fun part2(input: List<String>): Int {
		val positions = getPositions(input[0])

		return (0..positions.maxOrNull()!!).minOf { pos ->
			positions.fold(0) { acc, i ->
				acc + fuelConsumption(abs(i - pos))
			}
		}
	}

	val testInput = readInput("Day07_test")
	println(testInput)
	check(part1(testInput) == 37)
	check(part2(testInput) == 168)

	val input = readInput("Day07")
	println(part1(input))
	println(part2(input))
}
