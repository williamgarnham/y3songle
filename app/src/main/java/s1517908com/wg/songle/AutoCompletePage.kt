package s1517908com.wg.songle

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_auto_complete_page.*

import android.view.View
import kotlinx.android.synthetic.main.content_auto_complete_page.*
import android.content.Intent
import java.io.*


class AutoCompletePage : AppCompatActivity() {


    var points :String =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_complete_page)
        setSupportActionBar(toolbar)



        yesB.setOnClickListener { view ->
            //add to completed page
            //subtract points
            readTxtFile()
            if(points.toInt() > 19){

                points = Integer.toString(points.toInt() - 20)
                writeTxtFile()
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)

                val bundle:Bundle = getIntent().getExtras()
                var mapNum:String = bundle.getString("map")
                var songNum:String = bundle.getString("song")


                writeToComplFile(songNum,mapNum)

                startActivity(intent)
                finish()
            }



        }
    }

    fun writeToComplFile(song:String,map:String){

        val filename = "completedSongsFile" + map + ".txt"

        try {
            val fw = FileWriter(filename, true) //the true will append the new data
            fw.write(song+"\n")//appends the string to the file
            fw.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun readTxtFile() {
        val fis : FileInputStream = openFileInput("pointsnum.txt")
        val isr = InputStreamReader(fis)
        val bufferedReaderTxt = BufferedReader(isr)
        points = bufferedReaderTxt.readText()
        isr.close()
        fis.close()

    }
    fun writeTxtFile() {
        //File("pointsnum.txt").bufferedWriter().use { out -> out.write(points) }

        val filename = "pointsnum.txt"
        val outputStream: FileOutputStream

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE)
            outputStream.write(points.toByteArray())
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}
