package s1517908com.wg.songle

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

import kotlinx.android.synthetic.main.activity_completed_songs.*
import kotlinx.android.synthetic.main.content_completed_songs.*
import java.io.FileOutputStream



class CompletedSongs : AppCompatActivity(), DownloadCompleteListener {

    fun clearCompletedTxtFile(){
        val filename = "completedSongsFile"
        var emptystring = ""
        var outputStream: FileOutputStream
        for(x in 1..5) {
            try {
                var newFileName = filename + x.toString() + ".txt"

                outputStream = openFileOutput(newFileName, Context.MODE_PRIVATE)
                outputStream.write(emptystring.toByteArray())
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_songs)
        setSupportActionBar(toolbar)

        val songs = DownloadXML(this)
        songs.execute("http://www.inf.ed.ac.uk/teaching/courses/cslp/data/songs/songs.xml")
        val theSongs = songs.getSoList()

        Log.d("CHECK", "CHECK")
        for(x in 1..theSongs.size){
            Log.d("SONGE1name", theSongs.get(x).num)
        }
        Log.d("CHECK","CHECK")

        //Log.d("SONGS",thesongs)
        clearSongsB.setOnClickListener { view ->
            clearCompletedTxtFile()
        }

    }

}


