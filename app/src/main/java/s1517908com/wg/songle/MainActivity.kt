package s1517908com.wg.songle

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.content.Intent
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {

    fun createCompletedTxtFile(){
        val filename = "completedSongsFile.txt"
        var emptystring = ""
        val outputStream: FileOutputStream

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE)
            outputStream.write(emptystring.toByteArray())
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val path :String = this.getFilesDir().getAbsolutePath() + "/" + "completedSongsFile.txt";
        val completedFile : File = File(path)
        val completedFileExists : Boolean? = completedFile.exists()

        if(completedFileExists == false){
            createCompletedTxtFile()
        }

        helpB.setOnClickListener { view ->
            val intent = Intent(this, HelpPage::class.java)
            startActivity(intent)
        }
        completedSongs.setOnClickListener { view ->
            val intent = Intent(this, CompletedSongs::class.java)
            startActivity(intent)
        }
        contButton.setOnClickListener { view ->
            val intent = Intent(this, MapsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
        }
        easyButton.setOnClickListener { view ->
            val intent = Intent(this, MapsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
        }
        interButton.setOnClickListener { view ->
            val intent = Intent(this, MapsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
        }
        hardButton.setOnClickListener { view ->
            val intent = Intent(this, MapsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
        }
        expertButton.setOnClickListener { view ->
            val intent = Intent(this, MapsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
        }
        legendaryButton.setOnClickListener { view ->
            val intent = Intent(this, MapsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
        }

    }


}


