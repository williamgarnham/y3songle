package s1517908com.wg.songle

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View

import kotlinx.android.synthetic.main.activity_completed_songs.*
import kotlinx.android.synthetic.main.content_completed_songs.*
//import jdk.nashorn.internal.runtime.ScriptingFunctions.readLine
import java.io.*


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

        //var easy: ArrayList<Song>? = null
        //var inter: ArrayList<Song>? = null
        //var hard: ArrayList<Song>? = null
        //var expert: ArrayList<Song>? = null
        //var legend: ArrayList<Song>? = null

        var easy = arrayListOf<Song>()
        var inter = arrayListOf<Song>()
        var hard = arrayListOf<Song>()
        var expert = arrayListOf<Song>()
        var legend = arrayListOf<Song>()



        val songs = DownloadXML(this)
        songs.execute("http://www.inf.ed.ac.uk/teaching/courses/cslp/data/songs/songs.xml").get()

        val theSongs = songs.getSoList()
        val number :Int = theSongs.size
        Log.d("ListSize", number.toString())

        Log.d("CHECK", "CHECK")
        for(y in 1..5){
            val fileName :String = this.getFilesDir().getAbsolutePath() + "/" + "completedSongsFile"+y.toString()+".txt"
            //val cFile : File = File(fileName)
            var songsArray = arrayListOf<Int>()
            // using extension function readLines
            //cFile.readLines().forEach {
            //    songsArray.add(i)
            //}

            val bufferedReader = File(fileName).bufferedReader()
            bufferedReader.useLines { lines -> lines.forEach { songsArray.add(it.toInt()) } }




            Log.d("SongsArray",songsArray.toString())

            for(x in 0..songsArray.size-1){
                Log.d("X", songsArray.get(x).toString())
                for(z in theSongs){
                    //Log.d("Songs",z.title)
                    if(songsArray.get(x) == z.num.toInt()){
                        if(y == 1){
                            legend.add(z)
                            Log.d("Songs",z.title)
                        }
                        if(y == 2){
                            expert.add(z)
                            Log.d("Songs",z.title)
                        }
                        if(y == 3){
                            hard.add(z)
                            Log.d("Songs",z.title)
                        }
                        if(y == 4){
                            inter.add(z)
                            Log.d("Songs",z.title)
                        }
                        if(y == 5){
                            easy.add(z)
                            Log.d("Songs",z.title)
                        }
                    }
                }


         }
        }

        var largeString: String = ""

        for(i in 1..5){
            if(i == 1){
                for(x in legend){
                    largeString += x.title + " " + x.link + " Legend\n"
                }
            }
            if(i == 2){
                for(x in expert){
                    largeString += x.title + " " + x.link + " Expert\n"
                }
            }
            if(i == 3){
                for(x in hard){
                    largeString += x.title + " " + x.link + " Hard\n"
                }
            }
            if(i == 4){
                for(x in inter){
                    largeString += x.title + " " + x.link + " Intermediate\n"
                }
            }
            if(i == 5){
                for(x in easy){
                    largeString += x.title + " " + x.link + " Easy\n"
                }
            }
        }
        Log.d("Songs",largeString)
        songsText.text = largeString
        songsText.visibility = View.VISIBLE
        Log.d("CHECK","CHECK")

    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}


