package s1517908com.wg.songle

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_correct_guess.*
import kotlinx.android.synthetic.main.content_correct_guess.*
import android.view.View
import android.content.Intent
import android.util.Log
import java.io.BufferedWriter
import java.io.FileWriter


class CorrectGuess : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_correct_guess)
        setSupportActionBar(toolbar)

        val bundle:Bundle = getIntent().getExtras()
        var mapNum:String = bundle.getString("map")
        var songNum:String = bundle.getString("song")

        writeToComplFile(songNum,mapNum)


        contB.setOnClickListener{ view ->
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
        }
    }

    fun writeToComplFile(song:String,map:String){

        val filename = this.getFilesDir().getAbsolutePath() + "/" +"completedSongsFile" + map.removePrefix("map")+ ".txt"
        Log.d("AUTOCOMPLETE FILENAME",filename)

        try {
            val fw = FileWriter(filename, true) //the true will append the new data
            val bw = BufferedWriter(fw)
            bw.append(song)
            bw.newLine()
            //fw.write(song+"\n")//appends the string to the file
            bw.close()
            fw.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.d("SONG AND MAP ADDED2: ", song +" "+ map)

    }

}
