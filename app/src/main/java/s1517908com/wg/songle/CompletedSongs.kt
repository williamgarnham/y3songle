package s1517908com.wg.songle

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_completed_songs.*
import android.content.Intent
import kotlinx.android.synthetic.main.content_completed_songs.*
import java.io.FileOutputStream

class CompletedSongs : AppCompatActivity() {

    fun clearCompletedTxtFile(){
        val filename = "completedSongsFile.txt"
        var emptyString = ""
        val outputStream: FileOutputStream

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE)
            outputStream.write(emptyString.toByteArray())
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_songs)
        setSupportActionBar(toolbar)



        clearSongsB.setOnClickListener { view ->
            clearCompletedTxtFile()
        }

    }

}
