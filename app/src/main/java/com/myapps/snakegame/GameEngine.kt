package com.myapps.snakegame

import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameEngine(
    private val surfaceView: SurfaceView,
    gameActivity: GameActivity
) : SurfaceHolder.Callback {

    private val game = Game(surfaceView.resources, gameActivity)

    private var drawThread: DrawThread? = null

    private var move = false
    private var x = 0F
    private var y = 0F

    init {
        surfaceView.holder.addCallback(this)

        surfaceView.setOnTouchListener { v, event ->
            when (event?.actionMasked) {
                MotionEvent.ACTION_MOVE -> {
                    if (!move) {
                        x = event.x
                        y = event.y
                        move = true
                    } else {
                        if (event.y - y > 100 && !game.getSnake().isTop() && game.getSnake().isUpdated()) {
                            x = event.x
                            y = event.y
                            game.getSnake().moveBottom()
                            game.getSnake().setUpdated(false)
                            game.startGame()
                        } else if (y - event.y > 100 && !game.getSnake().isBottom() && game.getSnake().isUpdated()) {
                            x = event.x
                            y = event.y
                            game.getSnake().moveTop()
                            game.getSnake().setUpdated(false)
                            game.startGame()
                        } else if (event.x - x > 100 && !game.getSnake().isLeft() && game.getSnake().isUpdated()) {
                            x = event.x
                            y = event.y
                            game.getSnake().moveRight()
                            game.getSnake().setUpdated(false)
                            game.startGame()
                        } else if (x - event.x > 100 && !game.getSnake().isRight() && game.getSnake().isUpdated()) {
                            x = event.x
                            y = event.y
                            game.getSnake().moveLeft()
                            game.getSnake().setUpdated(false)
                            game.startGame()
                        }
                    }
                    true
                } MotionEvent.ACTION_UP -> {
                    x = 0F
                    y = 0F
                    move = false
                    true
                } else -> true
            }
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        drawThread = DrawThread(surfaceView.holder, surfaceView.resources, game)
        drawThread?.setRunning(true)
        drawThread?.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        drawThread?.setRunning(false)
        var retry = true
        while (retry) {
            try {
                drawThread?.join()
                retry = false
            } catch (_: InterruptedException) {
            }
        }
    }

    fun restartGame() {
        game.setAppleCount(0)
        game.getSnake().restart()
        game.getApple().update()
    }
}