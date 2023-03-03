package com.pixcanvas

import android.graphics.*
import android.util.Log
import android.view.View
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.IntentUtils
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReadableMap
import java.io.File
import kotlin.math.*

inline fun <T1: Any, T2: Any, R: Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2)->R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

class PixCanvasView(val context: ReactContext): View(context) {
    private var radio: Int? = null
    private var size: Int? = null
    private var points: List<Point>? = null
    private var bitmap: Bitmap? = null
    private val density = resources.displayMetrics.density
    private var gridSize: Int = 0
    private var canvasSize: Int = 0
    private var hideGridLine: Boolean? = false
    private val borderWeight: Int
      get() = if (hideGridLine == true) 0 else 1
    private var savedFile: File? = null


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        safeLet(bitmap, size) { bitmap, size ->
            val imgSize = size * density
            canvas?.drawBitmap(bitmap, null, RectF(0f, 0f, imgSize, imgSize), null)
        }
    }

    private fun drawPix(data: ReadableMap) {
        bitmap = Bitmap.createBitmap(canvasSize, canvasSize, Bitmap.Config.RGB_565).also {
            Log.i("img", "---->")
            val canvas = Canvas(it)
            val paint = Paint()
            paint.style = Paint.Style.FILL
            points?.forEach { p ->
                val left = (borderWeight * 2 + gridSize) * p.x
                val top = (borderWeight * 2 + gridSize) * p.y
                if (hideGridLine == false) {
                    paint.color = Color.parseColor(getColorWithCover(p.color, radio!!))
                    canvas.drawRect(left.toFloat(), top.toFloat(),
                        (left + gridSize + borderWeight * 2).toFloat(), (top + gridSize + borderWeight * 2).toFloat(), paint)
                }
                paint.color = Color.parseColor(p.color)
                val ileft = borderWeight + left
                val itop = borderWeight + top
                canvas.drawRect(ileft.toFloat(), itop.toFloat(), (ileft + gridSize).toFloat(), (itop + gridSize).toFloat(), paint)
            }
        }
        invalidate()
    }

    fun setData(data: ReadableMap) {
        if (points != null) {
            return
        }

        radio = data.getInt("radio")
        size = data.getInt("size")
        hideGridLine = data.getBoolean("hideGridLine")
        safeLet(size, radio) {size, radio ->
            val cSize = ceil(size * density).toInt()
            gridSize = ((cSize.toFloat() - borderWeight * 2 * radio) / radio).toInt()
            canvasSize = (gridSize + borderWeight * 2) * radio

        }

        radio?.let {radio ->
            val radioDelta = (MAX_RADIO - radio) / 2
            points = (data.getArray("points")?.toArrayList() as ArrayList<HashMap<String, Any>>).map {
                Point(it["x"] as Double - radioDelta, it["y"] as Double - radioDelta, it["color"] as String)
            }.filter {
                it.x >= 0 && it.x < radio && it.y >= 0 && it.y < radio
            }
        }
        drawPix(data)
    }

    fun saveToAlbum(): File? {
        bitmap?.let {
            savedFile = ImageUtils.save2Album(bitmap, Bitmap.CompressFormat.JPEG)
        }
        Log.d("wangcong", "${savedFile}")
        return savedFile
    }

    fun share() {
        bitmap?.let {
            if (savedFile == null) {
                saveToAlbum()
            }
            val intent = IntentUtils.getShareImageIntent(savedFile)
            context.startActivity(intent)
        }
    }

    companion object {
        // private const val GRID_SIZE = 10
        private const val MAX_RADIO = 32
        private const val BORDER_COLOR = "#CCCCCC"
        private const val EMPTY_COLOR = "#ffffff"

        private fun transHexToRgb(hex: String): Triple<Int, Int, Int> {
            val r = hex.substring(1, 3).toInt(16)
            val g = hex.substring(3, 5).toInt(16)
            val b = hex.substring(5, 7).toInt(16)
            return Triple(r, g, b)
        }

        private fun getHexWithCover(n: Int, p: Double): String {
            val decrease = (n * p).roundToInt()
            return max(n - decrease, 0).toString(16).padStart(2, '0')
        }

        private fun getColorWithCover(color: String, radio: Int): String {
            val (r, g, b) = transHexToRgb(color)

            val percent = when (radio) {
                32 -> 0.06
                24 -> 0.08
                else -> 0.1
            }
            return "#${getHexWithCover(r, percent)}${getHexWithCover(g, percent)}${getHexWithCover(b, percent)}"
        }
    }
}
