import kotlin.random.Random

val table = Array(9) { charArrayOf('.', '.', '.', '.', '.', '.', '.', '.', '.') }
val table2 = Array(9) { charArrayOf('.', '.', '.', '.', '.', '.', '.', '.', '.') }
var totalMines = 0
fun main() {
    print("How many mines do you want on the field? ")
    totalMines = readLine()!!.toInt()
    printTable(false)
    mineMarks()
}

fun mineMarks() {
    var firstTime = true
    while (true) {
        println("Set/delete mine marks (x and y coordinates):")
        val (a, b, c) = readLine()!!.split(" ")
        val x = a.toInt() - 1
        val y = b.toInt() - 1

        when {
            c == "free" && firstTime -> {
                randomX(x, y)
                searchMines()
                floodFillUtil(x, y, '.', '/')
                firstTime = false
            }
            table[y][x] == 'X' && c == "free" -> {
                printTable(true)
                print("You stepped on a mine and failed!")
                return
            }
            table2[y][x] == '.' && c == "free" -> {
                floodFillUtil(x, y, '.', '/')
            }
            table2[y][x] == '.' && c == "mine" -> table2[y][x] = '*'
            table2[y][x].isDigit() -> {
                println("There is a number here!")
                continue
            }
            table2[y][x] == '*' && c == "mine" -> table2[y][x] = '.'
        }

        printTable(false)
        if (noMines()) {
            print("Congratulations! You found all the mines!")
            return
        }
    }
}

fun noMines(): Boolean {
    var num = totalMines
    var con = 0
    for (i in 0..8) {
        for (j in 0..8) {
            if (table[i][j] == '.') con++
            if (table2[i][j] == '*' && table[i][j] == '.') num++
            if (table[i][j] == 'X' && table2[i][j] == '*') num--
        }
    }
    return num == 0 || con == totalMines
}

fun randomX(x: Int, y: Int) {
    var num = totalMines
    table[y][x] = '/'
    while (num != 0) {
        val row = Random.nextInt(0, 9)
        val colum = Random.nextInt(0, 9)
        if (table[row][colum] == '.') {
            table[row][colum] = 'X'
            num--
        }
    }
    table[y][x] = '.'
}

fun printTable(show: Boolean) {
    var con = 0
    println(" │123456789│\n—│—————————│")
    for (i in if (show) table else table2) {
        println("${++con}${i.joinToString("", postfix = "│", prefix = "│")}")
    }
    println("—│—————————│")
}

fun searchMines() {
    var con = 0
    for (i in table.indices) {
        for (j in table[i].indices) {
            if (table[i][j] != 'X') {
                if (i - 1 >= 0 && j - 1 >= 0) if (table[i - 1][j - 1] == 'X') con++
                if (i - 1 >= 0 && j >= 0) if (table[i - 1][j] == 'X') con++
                if (i >= 0 && j - 1 >= 0) if (table[i][j - 1] == 'X') con++
                if (i <= 8 && j + 1 <= 8) if (table[i][j + 1] == 'X') con++
                if (i + 1 <= 8 && j + 1 <= 8) if (table[i + 1][j + 1] == 'X') con++
                if (i + 1 <= 8 && j <= 8) if (table[i + 1][j] == 'X') con++
                if (i + 1 <= 8 && j - 1 >= 0) if (table[i + 1][j - 1] == 'X') con++
                if (i - 1 >= 0 && j + 1 <= 8) if (table[i - 1][j + 1] == 'X') con++
                if (con != 0) {
                    table[i][j] = con.toString().first()
                    con = 0
                }
            }
        }
    }
}

fun floodFillUtil(x: Int, y: Int, prevC: Char, newC: Char) {
    if (x < 0 || x >= 9 || y < 0 || y >= 9) return
    if (table[y][x].isDigit()) {
        table2[y][x] = table[y][x]
        return
    }
    if (table[y][x] != prevC) return

    table[y][x] = newC
    table2[y][x] = newC

    floodFillUtil(x + 1, y, prevC, newC)
    floodFillUtil(x - 1, y, prevC, newC)
    floodFillUtil(x, y + 1, prevC, newC)
    floodFillUtil(x, y - 1, prevC, newC)
    floodFillUtil(x + 1, y + 1, prevC, newC)
    floodFillUtil(x - 1, y - 1, prevC, newC)
    floodFillUtil(x + 1, y - 1, prevC, newC)
    floodFillUtil(x - 1, y + 1, prevC, newC)
}