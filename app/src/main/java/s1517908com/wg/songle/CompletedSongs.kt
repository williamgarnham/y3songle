package s1517908com.wg.songle

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_completed_songs.*
import android.content.Intent
import kotlinx.android.synthetic.main.content_completed_songs.*

class CompletedSongs : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_songs)
        setSupportActionBar(toolbar)

        clearSongsB.setOnClickListener { view ->
            //get free word
        }

    }

}
