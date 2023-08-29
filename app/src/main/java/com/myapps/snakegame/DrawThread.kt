package com.myapps.snakegame

import android.content.res.Resources
import android.graphics.Canvas
import android.view.SurfaceHolder
import java.lang.Exception

class DrawThread(private val holder: SurfaceHolder, private val resources: Resources, private val game: Game) : Thread() {

    private var running = false
    private var prevTime = System.currentTimeMillis()

    fun setRunning(running: Boolean) {
        this.running = running
    }

    override fun run() {
        var canvas: Canvas? = null
        while (running) {
            val now = System.currentTimeMillis()

            if (game.getSnake().getSnakePart(0).getBody().intersect(game.getApple().getBody())) {
                game.getApple().update()
                game.getSnake().raise()
                game.setAppleCount(game.getAppleCount() + 1)
                if (game.getAppleCount() > game.getMaxScore()) {
                    game.setMaxScore(game.getAppleCount())
                }
            }

            if (now - prevTime > 150 && game.isGameStarted()) {
                game.getSnake().update()
                prevTime = now
            }

            try {
                canvas = holder.lockCanvas()
                canvas.drawColor(resources.getColor(R.color.game_background))
                game.getField().draw(canvas)
                game.getSnake().draw(canvas)
                game.getApple().draw(canvas)
            } catch (_: Exception) {
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas)
                }
            }

            if (game.getSnake().isBlocked() && game.isGameStarted()) {
                game.stopGame()
            }

            if (game.getSnake().getSize() == game.getField().getFieldSize()) {
                game.victory()
            }
        }
    }
}