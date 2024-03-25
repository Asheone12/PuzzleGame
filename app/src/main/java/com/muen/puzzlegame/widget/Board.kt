package com.muen.puzzlegame.widget

import com.muen.puzzlegame.entity.Point
import java.util.Random

/**
 * 用来保存块数据的类
 * 注意：x对应的是col,y对应的row
 */
class Board {
    private var array: Array<IntArray>? = null
    private var row = 0
    private var col = 0

    //四个方向
    private val dir =
        arrayOf(intArrayOf(0, 1), intArrayOf(1, 0), intArrayOf(0, -1), intArrayOf(-1, 0))

    private fun createIntegerArray(row: Int, col: Int) {
        array = Array(row) { IntArray(col) }
        var idx = 0
        for (i in 0 until row) for (j in 0 until col) array!![i][j] = idx++
    }

    /**
     * 移动块的位置
     *
     * @param srcX
     * @param srcY
     * @param xOffset
     * @param yOffset
     * @return 新的位置，错误返回new Point(-1,-1);
     */
    private fun move(srcX: Int, srcY: Int, xOffset: Int, yOffset: Int): Point {
        val x = srcX + xOffset
        val y = srcY + yOffset
        if (x < 0 || y < 0 || x >= col || y >= row) return Point(-1, -1)
        val temp = array!![y][x]
        array!![y][x] = array!![srcY][srcX]
        array!![srcY][srcX] = temp
        return Point(x, y)
    }

    /**
     * 得到下一个可以移动的位置
     *
     * @param src
     * @return
     */
    private fun getNextPoint(src: Point): Point {
        val rd = Random()
        val idx: Int = rd.nextInt(4) //产生0~3的随机数
        val xOffset = dir[idx][0]
        val yOffset = dir[idx][1]
        val newPoint: Point = move(src.x, src.y, xOffset, yOffset)
        return if (newPoint.x != -1 && newPoint.y != -1) {
            newPoint
        } else getNextPoint(src)
    }

    /**
     * 生成拼图数据
     *
     * @param row
     * @param col
     * @return
     */
    fun createRandomBoard(row: Int, col: Int): Array<IntArray>? {
        require(!(row < 2 || col < 2)) { "行和列都不能小于2" }
        this.row = row
        this.col = col
        createIntegerArray(row, col)
        var count = 0
        var tempPoint = Point(col - 1, row - 1)
        val rd = Random()
        val num: Int = rd.nextInt(100) + 20 //产生20~119的随机数
        while (count < num) {
            tempPoint = getNextPoint(tempPoint)
            count++
        }
        return array
    }

    /**
     * 判断是否拼图成功
     *
     * @param arr
     * @return
     */
    fun isSuccess(arr: Array<IntArray>): Boolean {
        var idx = 0
        for (i in arr.indices) {
            var j = 0
            while (j < arr[i].size && idx < row * col - 1) {
                if (arr[idx / row][idx % col] > arr[(idx + 1) / row][(idx + 1) % col]) {
                    return false
                }
                idx++
                j++
            }
        }
        return true
    }

    companion object {
        private const val TAG = "Board"
    }
}