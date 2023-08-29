package com.myapps.snakegame

import android.content.res.Resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.min

class Game(resources: Resources, private val gameActivity: GameActivity) {

    private var gameStarted = false

    private var appleCount = 0
    private var maxScore = 0

    private val fieldWidth = 15
    private val fieldHeight = 22

    private val sizeOfFieldPart =
        min(
            Constants.screenWidth / (fieldWidth + 2),
            (Constants.screenHeight - Constants.topPanelHeight) / (fieldHeight + 2)
        )

    private val field = Field(sizeOfFieldPart, fieldWidth, fieldHeight, resources)
    private val snake = Snake(sizeOfFieldPart, field, resources)
    private val apple = Apple(sizeOfFieldPart, field, snake, resources)

    init {
        val authentication = FirebaseAuth.getInstance()
        val people = Firebase.firestore.collection("People")
        people.document(authentication.currentUser?.uid!!).get().addOnSuccessListener {
            if (it != null) {
                maxScore = it.data?.get("maxScore").toString().toInt()
            }
        }
    }

    fun getField() = field

    fun getSnake() = snake

    fun getApple() = apple

    fun getAppleCount() = appleCount

    fun getMaxScore() = maxScore

    fun setAppleCount(appleCount: Int) {
        this.appleCount = appleCount
        gameActivity.updateAppleCountTextViews(this.appleCount)
    }

    fun setMaxScore(maxScore: Int) {
        this.maxScore = maxScore
        gameActivity.updateMaxScoreTextViews(this.maxScore)
        gameActivity.updateMaxScoreData(maxScore)
    }

    fun startGame() {
        gameStarted = true
    }

    fun stopGame() {
        gameStarted = false
        gameActivity.showLostDialogWindow()
    }

    fun victory() {
        gameStarted = false
        gameActivity.showWonDialogWindow()
    }

    fun isGameStarted() = gameStarted
}