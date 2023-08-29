package com.myapps.snakegame

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.SurfaceView
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GameActivity : AppCompatActivity() {

    private val topPanelHeightDP = 100

    private var surfaceView: SurfaceView? = null

    private var appleCount: TextView? = null
    private var maxScore: TextView? = null
    private var logout: Button? = null

    private var authentication: FirebaseAuth? = null
    private val people = Firebase.firestore.collection("People")
    private var gameEngine: GameEngine? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        Constants.screenWidth = dm.widthPixels
        Constants.screenHeight = dm.heightPixels

        surfaceView = findViewById(R.id.surfaceView)
        Constants.topPanelHeight = topPanelHeightDP * dm.densityDpi / 160

        appleCount = findViewById(R.id.appleCount)
        maxScore = findViewById(R.id.maxScore)
        logout = findViewById(R.id.logout)

        authentication = FirebaseAuth.getInstance()

        people.document(authentication?.currentUser?.uid!!).get().addOnSuccessListener {
            if (it != null) {
                maxScore?.text = it.data?.get("maxScore").toString()
            }
            ////
            ////
        }

        logout?.setOnClickListener {
            authentication?.signOut()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }


        gameEngine = GameEngine(surfaceView!!, this)
    }

    fun updateAppleCountTextViews(appleCount: Int) {
        runOnUiThread {
            this.appleCount?.text = appleCount.toString()
        }
    }

    fun updateMaxScoreTextViews(maxScore: Int) {
        runOnUiThread {
            this.maxScore?.text = maxScore.toString()
        }
    }

    fun showLostDialogWindow() {
        runOnUiThread {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("You Lost")
            dialog.setMessage("Try again")
            dialog.setCancelable(false);
            dialog.setPositiveButton("Restart") { dialog, which ->
                gameEngine?.restartGame()
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    fun showWonDialogWindow() {
        runOnUiThread {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Congratulations")
            dialog.setMessage("You won")
            dialog.setCancelable(false);
            dialog.setPositiveButton("Restart") { dialog, which ->
                gameEngine?.restartGame()
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    fun updateMaxScoreData(maxScore: Int) {
        val userId = authentication?.currentUser?.uid!!
        val map = mapOf<String, Any>("maxScore" to maxScore)
        people.document(userId).set(map, SetOptions.merge())
    }
}