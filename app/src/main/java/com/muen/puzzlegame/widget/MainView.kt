package com.muen.puzzlegame.widget

import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.muen.puzzlegame.MainActivity
import com.muen.puzzlegame.entity.Point
import com.muen.puzzlegame.util.MLog.d
import java.io.IOException


class MainView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr){
    private var back: Bitmap? = null
    private val paint: Paint
    private var tileWidth = 0
    private var tileHeight = 0
    private lateinit var bitmapTiles: Array<Bitmap?>
    private var dataTiles: Array<IntArray>? = null
    private var tilesBoard: Board? = null
    private val COL = 3
    private val ROW = 3
    private val dir =
        arrayOf(intArrayOf(-1, 0), intArrayOf(0, -1), intArrayOf(1, 0), intArrayOf(0, 1))
    private var isSuccess = false
    var steps = 0

    init {
        paint = Paint()
        paint.isAntiAlias = true
        init()
        startGame()
    }

    /**
     * 初始化
     */
    private fun init() {
        //载入图像，并将图片切成块
        val assetManager = context.assets
        try {
            val assetInputStream = assetManager.open("back.jpg")
            val bitmap = BitmapFactory.decodeStream(assetInputStream)
            back = Bitmap.createScaledBitmap(
                bitmap,
                MainActivity.getScreenWidth(),
                MainActivity.getScreenHeight(),
                true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        tileWidth = back!!.width / COL
        tileHeight = back!!.height / ROW
        bitmapTiles = arrayOfNulls(COL * ROW)
        var idx = 0
        for (i in 0 until ROW) {
            for (j in 0 until COL) {
                bitmapTiles[idx++] = Bitmap.createBitmap(
                    back!!,
                    j * tileWidth,
                    i * tileHeight,
                    tileWidth,
                    tileHeight
                )
            }
        }
    }

    /**
     * 开始游戏
     */
    private fun startGame() {
        tilesBoard = Board()
        dataTiles = tilesBoard!!.createRandomBoard(ROW, COL)
        isSuccess = false
        steps = 0
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.GRAY)
        for (i in 0 until ROW) {
            for (j in 0 until COL) {
                val idx = dataTiles!![i][j]
                if (idx == ROW * COL - 1 && !isSuccess) continue
                canvas.drawBitmap(
                    bitmapTiles[idx]!!,
                    (j * tileWidth).toFloat(),
                    (i * tileHeight).toFloat(),
                    paint
                )
            }
        }
    }

    /**
     * 将屏幕上的点转换成,对应拼图块的索引
     *
     * @param x
     * @param y
     * @return
     */
    private fun xyToIndex(x: Int, y: Int): Point {
        val extraX = if (x % tileWidth > 0) 1 else 0
        val extraY = if (x % tileWidth > 0) 1 else 0
        val col = x / tileWidth + extraX
        val row = y / tileHeight + extraY
        return Point(col - 1, row - 1)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val point: Point = xyToIndex(event.x.toInt(), event.y.toInt())
            for (i in dir.indices) {
                val newX: Int = point.x + dir[i][0]
                val newY: Int = point.y + dir[i][1]
                if (newX >= 0 && newX < COL && newY >= 0 && newY < ROW) {
                    if (dataTiles!![newY][newX] == COL * ROW - 1) {
                        steps++
                        val temp = dataTiles!![point.y][point.x]
                        dataTiles!![point.y][point.x] = dataTiles!![newY][newX]
                        dataTiles!![newY][newX] = temp
                        invalidate()
                        if (tilesBoard!!.isSuccess(dataTiles!!)) {
                            isSuccess = true
                            invalidate()
                            val successText = String.format("恭喜你拼图成功，移动了%d次", steps)
                            AlertDialog.Builder(context)
                                .setTitle("拼图成功")
                                .setCancelable(false)
                                .setMessage(successText)
                                .setPositiveButton("重新开始",
                                    DialogInterface.OnClickListener { dialog, which -> startGame() })
                                .setNegativeButton("退出游戏",
                                    DialogInterface.OnClickListener { dialog, which -> System.exit(0) })
                                .create()
                                .show()
                        }
                    }
                }
            }
        }
        return true
    }

    private fun printArray(arr: Array<IntArray>) {
        val sb = StringBuilder()
        for (i in arr.indices) {
            for (j in arr[i].indices) {
                sb.append(arr[i][j].toString() + ",")
            }
            sb.append("\n")
        }
        d(TAG, sb.toString())
    }

    companion object {
        private val TAG = MainView::class.java.simpleName
    }
}