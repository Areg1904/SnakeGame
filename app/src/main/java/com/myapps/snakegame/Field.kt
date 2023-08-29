package com.myapps.snakegame

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Field(
    private val sizeOfFieldPart: Int,
    private val fieldWidth: Int,
    private val fieldHeight: Int,
    resources: Resources
) {

    private val fieldParts = arrayListOf<FieldPart>()

    init {
        val paint1 = Paint()
        paint1.color = resources.getColor(R.color.game_field_part_1)
        val paint2 = Paint()
        paint2.color = resources.getColor(R.color.game_field_part_2)
        for (i in 0 until fieldHeight) {
            for (j in 0 until fieldWidth) {
                val x = Constants.screenWidth / 2 + (j - fieldWidth / 2) * sizeOfFieldPart - (fieldWidth % 2) * (sizeOfFieldPart / 2)
                val y = Constants.topPanelHeight + (Constants.screenHeight - Constants.topPanelHeight) / 2 + (i - fieldHeight / 2) * sizeOfFieldPart - (fieldHeight % 2) * (sizeOfFieldPart / 2)
                if ((i + j) % 2 == 0) {
                    fieldParts.add(FieldPart(x, y, sizeOfFieldPart, paint1))
                } else {
                    fieldParts.add(FieldPart(x, y, sizeOfFieldPart, paint2))
                }
            }
        }
    }

    fun draw(canvas: Canvas) {
        for (fieldPart in fieldParts) {
            canvas.drawRect(
                fieldPart.x.toFloat(),
                fieldPart.y.toFloat(),
                (fieldPart.x + sizeOfFieldPart).toFloat(),
                (fieldPart.y + sizeOfFieldPart).toFloat(),
                fieldPart.paint
            )
        }
    }

    fun getFieldPart(i: Int): FieldPart {
        return fieldParts[i]
    }

    fun getFieldWidth() = fieldWidth

    fun getFieldHeight() = fieldHeight

    fun getFieldSize() = fieldWidth * fieldHeight
}