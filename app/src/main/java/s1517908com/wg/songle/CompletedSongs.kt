package s1517908com.wg.songle

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

import kotlinx.android.synthetic.main.activity_completed_songs.*
import kotlinx.android.synthetic.main.content_completed_songs.*
import java.io.File
import java.io.FileInputStream
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

        clearSongsB.setOnClickListener { view ->
            clearCompletedTxtFile()
        }

        var easy: ArrayList<Song>
        var inter: ArrayList<Song>
        var hard: ArrayList<Song>
        var expert: ArrayList<Song>
        var legend: ArrayList<Song>



        val songs = DownloadXML(this)
        songs.execute("http://www.inf.ed.ac.uk/teaching/courses/cslp/data/songs/songs.xml").get()

        val theSongs = songs.getSoList()
        val number :Int = theSongs.size
        Log.d("ListSize", number.toString())

        Log.d("CHECK", "CHECK")
        for(y in 1..5){
            val fileName :String = this.getFilesDir().getAbsolutePath() + "/" + "completedSongsFile"+y.toString()+".txt"
            val cFile : File = File(fileName)
            var i :Int = 1
            var songsArray = arrayListOf<Int>()
            // using extension function readLines
            cFile.readLines().forEach {
                songsArray.add(i)
            }

            for(x in 0..theSongs.size-1){
                Log.d("SONGE1name", theSongs.get(x).num)
                for(z in songsArray){
                    /*if(z == theSongs.get(x).num.toInt()){
                        if(y = 1){
                            legend.add(theSongs)
                        }
                    }*/
                }


         }
        }



        Log.d("CHECK","CHECK")

    }

}


