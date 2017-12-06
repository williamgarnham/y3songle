package s1517908com.wg.songle

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_auto_complete_page.*

import android.view.View
import kotlinx.android.synthetic.main.content_auto_complete_page.*
import android.content.Intent
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader


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
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
            }



        }
    }

    fun readTxtFile() {
        val fis : FileInputStream = openFileInput("pointsnum.txt")
        val isr = InputStreamReader(fis)
        val bufferedReaderTxt = BufferedReader(isr)
        points = bufferedReaderTxt.readText()

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
