package s1517908com.wg.songle

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View

import kotlinx.android.synthetic.main.activity_completed_songs.*
import kotlinx.android.synthetic.main.content_completed_songs.*
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

        //clear completed songs text files
        clearSongsB.setOnClickListener { view ->
            clearCompletedTxtFile()
        }


        //create lists to hold the songs for each difficulty
        var easy = arrayListOf<Song>()
        var inter = arrayListOf<Song>()
        var hard = arrayListOf<Song>()
        var expert = arrayListOf<Song>()
        var legend = arrayListOf<Song>()


        //download the xml of the songs
        val songs = DownloadXML(this)
        songs.execute("http://www.inf.ed.ac.uk/teaching/courses/cslp/data/songs/songs.xml").get()

        val theSongs = songs.getSoList()
        val number :Int = theSongs.size
        Log.d("ListSize", number.toString())

        //loop through each list and check if the song is in the list
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


        //print out the information for each song
        var largeString: String = ""


        for(x in legend){
            largeString += x.title + " " + x.link + " Legend\n"
        }

        for(x in expert){
            largeString += x.title + " " + x.link + " Expert\n"
        }

        for(x in hard){
            largeString += x.title + " " + x.link + " Hard\n"
        }

        for(x in inter){
            largeString += x.title + " " + x.link + " Intermediate\n"
        }

        for(x in easy){
            largeString += x.title + " " + x.link + " Easy\n"
        }



        songsText.text = largeString
        songsText.visibility = View.VISIBLE


    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
        finish()
    }

}


