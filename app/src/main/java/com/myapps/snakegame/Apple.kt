package com.myapps.snakegame

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import java.util.Random

class Apple(
    private val sizeOfFieldPart: Int,
    private val field: Field,
    private val snake: Snake,
    resources: Resources
) {

    private val bm = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(resources, R.drawable.apple),
        sizeOfFieldPart,
        sizeOfFieldPart,
        true
    )

    private var x = 0
    private var y = 0

    init {
        update()
    }

    fun getBody(): Rect {
        return Rect(x, y, x + sizeOfFieldPart, y + sizeOfFieldPart)
    }

    fun update() {
        x = field.getFieldPart(Random().nextInt(field.getFieldSize() - 1)).x
        y = field.getFieldPart(Random().nextInt(field.getFieldSize() - 1)).y
        var check = true
        while (check) {
            check = false
            for (i in 0 until snake.getSize()) {
                if (snake.getSnakePart(i).getBody().intersect(getBody())) {
                    check = true
                    x = field.getFieldPart(Random().nextInt(field.getFieldSize() - 1)).x
                    y = field.getFieldPart(Random().nextInt(field.getFieldSize() - 1)).y
                    break
                }
            }
        }
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(bm, x.toFloat(), y.toFloat(), null)
    }
}