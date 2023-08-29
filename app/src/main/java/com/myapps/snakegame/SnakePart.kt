package com.myapps.snakegame

import android.graphics.Bitmap
import android.graphics.Rect

data class SnakePart(var x: Int, var y: Int, var sizeOfFieldPart: Int, var bm: Bitmap) {
    fun getBody() : Rect {
        return Rect(x, y, x + sizeOfFieldPart, y + sizeOfFieldPart)
    }

    fun getTopBody() : Rect {
        return Rect(x, y - sizeOfFieldPart, x + sizeOfFieldPart, y)
    }

    fun getBottomBody() : Rect {
        return Rect(x, y + sizeOfFieldPart, x + sizeOfFieldPart, y + 2 * sizeOfFieldPart)
    }

    fun getLeftBody() : Rect {
        return Rect(x - sizeOfFieldPart, y, x, y + sizeOfFieldPart)
    }

    fun getRightBody() : Rect {
        return Rect(x + sizeOfFieldPart, y, x + 2 * sizeOfFieldPart, y + sizeOfFieldPart)
    }
}