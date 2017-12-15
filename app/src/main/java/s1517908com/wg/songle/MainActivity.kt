package s1517908com.wg.songle

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.content.Intent
import android.util.Log
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {


    //create the completed songs text files
    fun createCompletedTxtFile(){
        val filename = "completedSongsFile"
        var emptystring = ""
        var outputStream: FileOutputStream
        for(x in 1..5) {
            try {
                var newFileName = filename + x.toString() + ".txt"
                Log.d("FILENAMES",newFileName)

                outputStream = openFileOutput(newFileName, Context.MODE_PRIVATE)
                outputStream.write(emptystring.toByteArray())
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        Log.d("COMPSONGFILE","created")

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //check if the complatedTxt files exist, if they don't, make them
        val path :String = this.getFilesDir().getAbsolutePath() + "/" + "completedSongsFile1.txt";
        val completedFile : File = File(path)
        val completedFileExists : Boolean? = completedFile.exists()

        if(completedFileExists == false){
            createCompletedTxtFile()
        }else{
            Log.d("COMPSONGFILE","exists")
        }


        //set up buttons (some with bundles to pass information from one activity to another)
        helpB.setOnClickListener { view ->
            val intent = Intent(this, HelpPage::class.java)
            startActivity(intent)
        }
        completedSongs.setOnClickListener { view ->
            val intent = Intent(this, CompletedSongs::class.java)
            startActivity(intent)
        }
        easyButton.setOnClickListener { view ->
            val intent = Intent(this, MapsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            val bundle:Bundle = Bundle()

            bundle.putString("map", "map5")

            intent.putExtras(bundle)
            startActivity(intent)
        }
        interButton.setOnClickListener { view ->
            val intent = Intent(this, MapsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            val bundle:Bundle = Bundle()

            bundle.putString("map", "map4")

            intent.putExtras(bundle)
            startActivity(intent)
        }
        hardButton.setOnClickListener { view ->
            val intent = Intent(this, MapsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            val bundle:Bundle = Bundle()

            bundle.putString("map", "map3")

            intent.putExtras(bundle)
            startActivity(intent)
        }
        expertButton.setOnClickListener { view ->
            val intent = Intent(this, MapsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            val bundle:Bundle = Bundle()

            bundle.putString("map", "map2")

            intent.putExtras(bundle)
            startActivity(intent)
        }
        legendaryButton.setOnClickListener { view ->
            val intent = Intent(this, MapsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            val bundle:Bundle = Bundle()

            bundle.putString("map", "map1")

            intent.putExtras(bundle)
            startActivity(intent)
        }

    }


}


