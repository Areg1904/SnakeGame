package com.myapps.snakegame

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas

class Snake(private val sizeOfFieldPart: Int, private val field: Field, resources: Resources) {

    private val bm = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(resources, R.drawable.snake),
        14 * sizeOfFieldPart,
        sizeOfFieldPart,
        true
    )

    private var bmBodyHorizontal =
        Bitmap.createBitmap(bm, 2 * sizeOfFieldPart, 0, sizeOfFieldPart, sizeOfFieldPart)
    private var bmBodyVertical =
        Bitmap.createBitmap(bm, 5 * sizeOfFieldPart, 0, sizeOfFieldPart, sizeOfFieldPart)
    private var bmHeadUp =
        Bitmap.createBitmap(bm, 9 * sizeOfFieldPart, 0, sizeOfFieldPart, sizeOfFieldPart)
    private var bmHeadDown =
        Bitmap.createBitmap(bm, 6 * sizeOfFieldPart, 0, sizeOfFieldPart, sizeOfFieldPart)
    private var bmHeadLeft =
        Bitmap.createBitmap(bm, 7 * sizeOfFieldPart, 0, sizeOfFieldPart, sizeOfFieldPart)
    private var bmHeadRight =
        Bitmap.createBitmap(bm, 8 * sizeOfFieldPart, 0, sizeOfFieldPart, sizeOfFieldPart)
    private var bmTailUp =
        Bitmap.createBitmap(bm, 13 * sizeOfFieldPart, 0, sizeOfFieldPart, sizeOfFieldPart)
    private var bmTailDown =
        Bitmap.createBitmap(bm, 10 * sizeOfFieldPart, 0, sizeOfFieldPart, sizeOfFieldPart)
    private var bmTailLeft =
        Bitmap.createBitmap(bm, 11 * sizeOfFieldPart, 0, sizeOfFieldPart, sizeOfFieldPart)
    private var bmTailRight =
        Bitmap.createBitmap(bm, 12 * sizeOfFieldPart, 0, sizeOfFieldPart, sizeOfFieldPart)
    private var bmTopLeft =
        Bitmap.createBitmap(bm, 3 * sizeOfFieldPart, 0, sizeOfFieldPart, sizeOfFieldPart)
    private var bmTopRight =
        Bitmap.createBitmap(bm, 4 * sizeOfFieldPart, 0, sizeOfFieldPart, sizeOfFieldPart)
    private var bmBottomLeft = Bitmap.createBitmap(bm, 0, 0, sizeOfFieldPart, sizeOfFieldPart)
    private var bmBottomRight =
        Bitmap.createBitmap(bm, sizeOfFieldPart, 0, sizeOfFieldPart, sizeOfFieldPart)

    private var snakeParts = arrayListOf<SnakePart>()

    private var moveTop = false
    private var moveBottom = false
    private var moveLeft = false
    private var moveRight = false

    private var updated = true

    init {
        createBaseSnake()
    }

    fun draw(canvas: Canvas) {
        for (i in snakeParts.size - 1 downTo 0) {
            canvas.drawBitmap(
                snakeParts[i].bm,
                snakeParts[i].x.toFloat(),
                snakeParts[i].y.toFloat(),
                null
            )
        }
    }

    private fun createBaseSnake() {
        snakeParts.clear()
        val fieldPart = field.getFieldPart(field.getFieldHeight() / 2 * field.getFieldWidth() + 4)
        snakeParts.add(SnakePart(fieldPart.x, fieldPart.y, sizeOfFieldPart, bmHeadRight))
        snakeParts.add(
            SnakePart(
                fieldPart.x - sizeOfFieldPart,
                fieldPart.y,
                sizeOfFieldPart,
                bmBodyHorizontal
            )
        )
        snakeParts.add(
            SnakePart(
                fieldPart.x - 2 * sizeOfFieldPart,
                fieldPart.y,
                sizeOfFieldPart,
                bmTailLeft
            )
        )
        moveRight()
    }

    fun isBlocked(): Boolean {
        var isBlocked = false

        for (i in 4 until snakeParts.size) {
            if (snakeParts[0].getBody().intersect(snakeParts[i].getBody())) {
                isBlocked = true
                break
            }
        }

        if (snakeParts[0].x < field.getFieldPart(0).x || snakeParts[0].x > field.getFieldPart(field.getFieldSize() - 1).x || snakeParts[0].y < field.getFieldPart(
                0
            ).y || snakeParts[0].y > field.getFieldPart(field.getFieldSize() - 1).y
        ) {
            isBlocked = true
        }

        return isBlocked
    }

    fun restart() {
        setUpdated(true)
        createBaseSnake()
    }

    private fun stop() {
        moveTop = false
        moveBottom = false
        moveLeft = false
        moveRight = false
    }

    fun moveTop() {
        stop()
        moveTop = true
    }

    fun moveBottom() {
        stop()
        moveBottom = true
    }

    fun moveLeft() {
        stop()
        moveLeft = true
    }

    fun moveRight() {
        stop()
        moveRight = true
    }

    fun isTop() = moveTop

    fun isBottom() = moveBottom

    fun isLeft() = moveLeft

    fun isRight() = moveRight

    fun isUpdated() = updated

    fun setUpdated(updated: Boolean) {
        this.updated = updated
    }

    fun getSize() = snakeParts.size

    fun getSnakePart(x: Int) = snakeParts[x]

    fun update() {
        setUpdated(true)

        for (i in (snakeParts.size - 1) downTo 1) {
            snakeParts[i].x = snakeParts[i - 1].x
            snakeParts[i].y = snakeParts[i - 1].y
        }

        if (moveTop) {
            snakeParts[0].y -= sizeOfFieldPart
            snakeParts[0].bm = bmHeadUp
        } else if (moveBottom) {
            snakeParts[0].y += sizeOfFieldPart
            snakeParts[0].bm = bmHeadDown
        } else if (moveLeft) {
            snakeParts[0].x -= sizeOfFieldPart
            snakeParts[0].bm = bmHeadLeft
        } else if (moveRight) {
            snakeParts[0].x += sizeOfFieldPart
            snakeParts[0].bm = bmHeadRight
        }

        for (i in 1 until (snakeParts.size - 1)) {
            if (snakeParts[i].getBody()
                    .intersect(snakeParts[i - 1].getBottomBody()) && snakeParts[i].getBody()
                    .intersect(snakeParts[i + 1].getTopBody()) || snakeParts[i].getBody()
                    .intersect(snakeParts[i + 1].getBottomBody()) && snakeParts[i].getBody()
                    .intersect(snakeParts[i - 1].getTopBody())
            ) {
                snakeParts[i].bm = bmBodyVertical
            } else if (snakeParts[i].getBody()
                    .intersect(snakeParts[i - 1].getLeftBody()) && snakeParts[i].getBody()
                    .intersect(snakeParts[i + 1].getRightBody()) || snakeParts[i].getBody()
                    .intersect(snakeParts[i + 1].getLeftBody()) && snakeParts[i].getBody()
                    .intersect(snakeParts[i - 1].getRightBody())
            ) {
                snakeParts[i].bm = bmBodyHorizontal
            } else if (snakeParts[i].getBody()
                    .intersect(snakeParts[i - 1].getTopBody()) && snakeParts[i].getBody()
                    .intersect(snakeParts[i + 1].getLeftBody()) || snakeParts[i].getBody()
                    .intersect(snakeParts[i + 1].getTopBody()) && snakeParts[i].getBody()
                    .intersect(snakeParts[i - 1].getLeftBody())
            ) {
                snakeParts[i].bm = bmBottomRight
            } else if (snakeParts[i].getBody()
                    .intersect(snakeParts[i - 1].getTopBody()) && snakeParts[i].getBody()
                    .intersect(snakeParts[i + 1].getRightBody()) || snakeParts[i].getBody()
                    .intersect(snakeParts[i + 1].getTopBody()) && snakeParts[i].getBody()
                    .intersect(snakeParts[i - 1].getRightBody())
            ) {
                snakeParts[i].bm = bmBottomLeft
            } else if (snakeParts[i].getBody()
                    .intersect(snakeParts[i - 1].getBottomBody()) && snakeParts[i].getBody()
                    .intersect(snakeParts[i + 1].getLeftBody()) || snakeParts[i].getBody()
                    .intersect(snakeParts[i + 1].getBottomBody()) && snakeParts[i].getBody()
                    .intersect(snakeParts[i - 1].getLeftBody())
            ) {
                snakeParts[i].bm = bmTopRight
            } else if (snakeParts[i].getBody()
                    .intersect(snakeParts[i - 1].getBottomBody()) && snakeParts[i].getBody()
                    .intersect(snakeParts[i + 1].getRightBody()) || snakeParts[i].getBody()
                    .intersect(snakeParts[i + 1].getBottomBody()) && snakeParts[i].getBody()
                    .intersect(snakeParts[i - 1].getRightBody())
            ) {
                snakeParts[i].bm = bmTopLeft
            }
        }

        if (snakeParts.last().getBody().intersect(snakeParts[snakeParts.size - 2].getTopBody())) {
            snakeParts.last().bm = bmTailUp
        } else if (snakeParts.last().getBody()
                .intersect(snakeParts[snakeParts.size - 2].getBottomBody())
        ) {
            snakeParts.last().bm = bmTailDown
        } else if (snakeParts.last().getBody()
                .intersect(snakeParts[snakeParts.size - 2].getLeftBody())
        ) {
            snakeParts[snakeParts.size - 1].bm = bmTailLeft
        } else if (snakeParts.last().getBody()
                .intersect(snakeParts[snakeParts.size - 2].getRightBody())
        ) {
            snakeParts.last().bm = bmTailRight
        }
    }

    fun raise() {
        val snakePart = snakeParts.last()
        when (snakePart.bm) {
            bmTailUp -> {
                snakeParts.last().bm = bmBodyVertical
                snakeParts.add(
                    SnakePart(
                        snakePart.x,
                        snakePart.y - sizeOfFieldPart,
                        sizeOfFieldPart,
                        bmTailUp
                    )
                )
            }

            bmTailDown -> {
                snakeParts.last().bm = bmBodyVertical
                snakeParts.add(
                    SnakePart(
                        snakePart.x,
                        snakePart.y + sizeOfFieldPart,
                        sizeOfFieldPart,
                        bmTailDown
                    )
                )
            }

            bmTailLeft -> {
                snakeParts.last().bm = bmBodyHorizontal
                snakeParts.add(
                    SnakePart(
                        snakePart.x - sizeOfFieldPart,
                        snakePart.y,
                        sizeOfFieldPart,
                        bmTailLeft
                    )
                )
            }

            bmTailRight -> {
                snakeParts.last().bm = bmBodyHorizontal
                snakeParts.add(
                    SnakePart(
                        snakePart.x + sizeOfFieldPart,
                        snakePart.y,
                        sizeOfFieldPart,
                        bmTailRight
                    )
                )
            }
        }
    }
}