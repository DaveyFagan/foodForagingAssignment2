package ie.wit.foraging.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import ie.wit.foraging.R
import ie.wit.foraging.ui.auth.Login

class SplashscreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        supportActionBar?.hide()

        Handler().postDelayed({
            val intent = Intent(this@SplashscreenActivity, Login::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}