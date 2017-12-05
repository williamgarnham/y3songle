package s1517908com.wg.songle

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.content.Intent
import android.widget.Button




class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        completedSongs.setOnClickListener { view ->
            val intent = Intent(this, CompletedSongs::class.java)
            startActivity(intent)
        }
        contButton.setOnClickListener { view ->
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
        easyButton.setOnClickListener { view ->
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
        interButton.setOnClickListener { view ->
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
        hardButton.setOnClickListener { view ->
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
        expertButton.setOnClickListener { view ->
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
        legendaryButton.setOnClickListener { view ->
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

    }


}


