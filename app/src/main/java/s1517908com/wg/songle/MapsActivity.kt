package s1517908com.wg.songle

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import android.content.Context

import android.content.IntentFilter
import android.content.Intent
import android.content.BroadcastReceiver

import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager

import java.io.*
import java.net.URL
import com.google.maps.android.kml.KmlLayer

import android.os.AsyncTask

import android.widget.Toast

import kotlinx.android.synthetic.main.activity_maps.*

import android.util.Log
import android.view.KeyEvent
import android.view.View
import com.google.android.gms.maps.model.*

import java.util.*
import kotlin.collections.ArrayList

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, DownloadListener, DownloadCompleteListener, OnMarkerClickListener  {


    private lateinit var mMap: GoogleMap

    private var receiver = NetworkReceiver()

    var kmllayer: KmlLayer? = null

    private lateinit var mGoogleApiClient: GoogleApiClient
    val permissionsRequestAccessFineLocation = 1
    var mLocationPermissionGranted = false
    // getLastLocation can return null, so we need the type ”Location?”
    private var mLastLocation: android.location.Location? = null
    val tag = "MapsActivity"

    var points :String = ""
    var title :String = ""
    var collectedWords: String = ""
    var lyricsList :MutableList<MutableList<String>> = mutableListOf()

    //reads points text file
    fun readTxtFile(filename: String) {
        val fis : FileInputStream= openFileInput(filename)
        val isr = InputStreamReader(fis)
        val bufferedReaderTxt = BufferedReader(isr)
        points = bufferedReaderTxt.readText()
        fis.close()
        isr.close()

    }

    //writes to points text file
    fun writeTxtFile() {
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

    fun createPointsTxtFile(){
        val filename = "pointsnum.txt"
        points = "50"
        val outputStream: FileOutputStream

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE)
            outputStream.write(points.toByteArray())
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //reads completed songs file
    fun readCompFile(difficulty: Int):ArrayList<Int>{
        val fileName :String = this.getFilesDir().getAbsolutePath() + "/" + "completedSongsFile"+difficulty.toString()+".txt"
        Log.d("MAPSCFILE",fileName)
        val cFile : File = File(fileName)
        var i :Int = 1
        var songsArray = arrayListOf<Int>()
        // using extension function readLines
        cFile.readLines().forEach {
            songsArray.add(i)
        }

        return songsArray
    }

    //starts corrected guess activity
    fun openCorrAct(song:String,map:String){
        val intentComp = Intent(this, CorrectGuess::class.java)
        intentComp.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        val bundleComp:Bundle = Bundle()

        bundleComp.putString("map", map)
        bundleComp.putString("song",song)

        intentComp.putExtras(bundleComp)
        startActivity(intentComp)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //val intent = intent

        //figure out what song to play based on difficulty
        val bundle:Bundle = getIntent().getExtras()
        var mapNum:String = bundle.getString("map")
        var diff: Int

        if(mapNum == "map1") {
            diff = 1
        }else if(mapNum == "map2"){
            diff = 2
        }else if(mapNum == "map3"){
            diff = 3
        }else if(mapNum == "map4"){
            diff = 4
        }else{
            diff = 5
        }

        //get songs completed by user
        val songsArray = readCompFile(diff)
        Log.d("PLAYEDSONGSMAP",songsArray.toString())

        //choose song by checking how many songs are, then comparing to songsArray and randomly generating

        //get the maximum song number
        val maxSong = GetMaxSongNumber(this,this)
        val mS = maxSong.execute("http://www.inf.ed.ac.uk/teaching/courses/cslp/data/songs/songs.txt").get()



        //find an unplayed song
        val rand: Random = Random()
        var randNum = 0

        var found:Boolean = true

        do{
            found = true
            randNum = rand.nextInt(mS.toInt()-1) + 1

            for(x in songsArray){
                if(x == randNum){
                    found = false
                }
            }

        }while(found == false)

        var songNum: String

        if(randNum > 9){
            songNum = randNum.toString()
        }else{
            songNum = "0"+randNum.toString()
        }

        //load the lyrics
        val songLyrics = GetSongLyrics(this,this)
        val lyrics = songLyrics.execute("http://www.inf.ed.ac.uk/teaching/courses/cslp/data/songs/"+ songNum+ "/lyrics.txt").get()

        val linesList: List<String> = lyrics.split("\n".toRegex())
        var words : List<String>

        for(x in linesList){
            words = x.split("[\\p{Punct}+ | \\s]".toRegex())
            lyricsList!!.add(words.toMutableList())

        }




        //load the KMLlayer
        val URL = "http://www.inf.ed.ac.uk/teaching/courses/cslp/data/songs/"+ songNum+"/" + mapNum + ".kml"
        val kmldownloader = DownloadKml(this,this)
        kmldownloader.execute(URL).get()

        Log.d("SongNum", songNum)
        Log.d("MapNum",mapNum)




        //get the song title
        val songsList = DownloadXML(this)
        songsList.execute("http://www.inf.ed.ac.uk/teaching/courses/cslp/data/songs/songs.xml").get()
        val theSongsList = songsList.getSoList()





        for(i in theSongsList){
            if(i.num == songNum){
                title = i.title
            }
        }



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Create an instance of GoogleAPIClient.
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()

        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(receiver, filter)

        //check to see if there is a pointsnum.txt in the users directory
        val path :String = this.getFilesDir().getAbsolutePath() + "/" + "pointsnum.txt"
        val pointsFile : File = File(path)
        val pointsFileExists : Boolean? = pointsFile.exists()


        if(pointsFileExists == true){
            readTxtFile("pointsnum.txt")
        }else{
            createPointsTxtFile()
        }

        pointsText.text = points



        //set visibility of collected words
        collectedWordsSV.visibility = View.INVISIBLE

        //have the guessText box listen for the enter key being hit
        guessText.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if(guessText.text.toString().toLowerCase() == title.toLowerCase()){


                    points = Integer.toString(points.toInt() + 10)
                    pointsText.text = points
                    writeTxtFile()

                    openCorrAct(songNum,mapNum)


                }else{
                    Toast.makeText(this, "Incorrect!",
                            Toast.LENGTH_LONG).show()

                }
                return@OnKeyListener true
            }
            false
        })

        //guess song button visibility
        guessText.visibility = View.INVISIBLE
        guessSongButton.setOnClickListener { view ->

            if(guessText.visibility == View.VISIBLE){
                guessText.visibility = View.INVISIBLE
            }else{
                guessText.visibility = View.VISIBLE
            }

        }

        //get free word
        freeWordB.setOnClickListener { view ->
            //get free word
            //subtract points
            if(points.toInt() > 1) {
                points = Integer.toString(points.toInt() - 2)
                pointsText.text = points
                writeTxtFile()

                var fwL = rand.nextInt(lyricsList.size)
                var fwW = rand.nextInt(lyricsList.get(fwL).size )
                var word:String = lyricsList.get(fwL).get(fwW) + "\n"
                Log.d("THEFWORD",word)
                collectedWords += word
                collectedWordsTB.setText(collectedWords)
                Toast.makeText(this, word,
                        Toast.LENGTH_SHORT).show()
            }
        }


        //set visibility for viewing words
        viewWordsB.setOnClickListener { view ->
            if(collectedWordsSV.visibility == View.VISIBLE) {
                collectedWordsSV.visibility = View.INVISIBLE
            }else{
                collectedWordsSV.visibility = View.VISIBLE
            }
        }

        //autocomplete button
        autoCompleteB.setOnClickListener { view ->
            if(points.toInt() > 19) {
                val intent = Intent(this, AutoCompletePage::class.java)
                val bundle: Bundle = Bundle()

                bundle.putString("map", mapNum)
                bundle.putString("song", songNum)

                intent.putExtras(bundle)
                startActivity(intent)
            }
        }


    }

    //stop anything from happening on back pressed
    override fun onBackPressed() {
        //prevent back button working
    }


    var stream : InputStream? = null
    override fun downloadComp(str : String){
        var kstream = str.byteInputStream()
        stream = kstream
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //create kml layer
        kmllayer = KmlLayer(mMap, stream, this)
        kmllayer!!.addLayerToMap()


        kmllayer!!.map.setOnMarkerClickListener(this)



        try {  // Visualise current position with a small blue circle
            mMap.isMyLocationEnabled = true
        } catch (se: SecurityException) {
            println("Security exception thrown [onMapReady]")
        }
        // Add ”My location” button to the user interface
        mMap.uiSettings.isMyLocationButtonEnabled = true
    }

    //marker click listener
    override fun onMarkerClick(marker: Marker):Boolean{
        val tempLoc = Location(LocationManager.GPS_PROVIDER)
        tempLoc.setLatitude(marker.position.latitude)
        tempLoc.setLongitude(marker.position.longitude)
        if(mLastLocation!!.distanceTo(tempLoc) < 30){
            marker.isVisible = false
            var wordNums = marker.title.toString()
            var line = 0
            var pos = 0
            for(char in wordNums){
                if(char == ':'){
                    line = wordNums.substring(0,wordNums.indexOf(':')).toInt()
                    pos = wordNums.substring(wordNums.indexOf(':')+1, wordNums.length).toInt()
                }
            }
            var str = lyricsList.get(line -1).get(pos-1)
            Toast.makeText(this, str,
                            Toast.LENGTH_SHORT).show()
            collectedWords += str +"\n"
            collectedWordsTB.setText(collectedWords)
            //add word to collected words and set text box to equal it
            //do points (plus one point)
            points = Integer.toString(points.toInt() + 1)
            pointsText.text = points
            writeTxtFile()



        }
        return false
    }
    override fun onStart() {
        super.onStart()

        mGoogleApiClient.connect()
    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient.isConnected) {
            mGoogleApiClient.disconnect()
        }
    }

    fun createLocationRequest() {
        // Set the parameters for the location request
        val mLocationRequest = LocationRequest()
        mLocationRequest.interval = 5000 // preferably every 5 seconds
        mLocationRequest.fastestInterval = 1000 // at most every second
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        // Can we access the user’s current location?
        val permissionCheck = android.support.v4.content.ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)

        if (permissionCheck == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this)
        }
    }

    override fun onConnected(connectionHint: Bundle?) {
        try {
            createLocationRequest()
        } catch (ise: IllegalStateException) {
            println("[$tag] [onConnected] IllegalStateException thrown")
        }
        // Can we access the user’s current location?
        if (android.support.v4.content.ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            val api = LocationServices.FusedLocationApi
            mLastLocation = api.getLastLocation(mGoogleApiClient)
            // Caution: getLastLocation can return null
            if (mLastLocation == null) {
                println("[$tag] Warning: mLastLocation is null")
            }
        } else {
            android.support.v4.app.ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    permissionsRequestAccessFineLocation)
        }
    }

    override fun onLocationChanged(current: android.location.Location?) {
        if (current == null) {
            println("[$tag] [onLocationChanged] Location unknown")
        } else {
            println("""[$tag] [onLocationChanged] Lat/long now
             (${current.latitude},
            ${current.longitude})"""
            )
            // Do something with current location
            mLastLocation = current
        }
    }

    override fun onConnectionSuspended(flag: Int) {
        println(" >>>> onConnectionSuspended")
    }

    override fun onConnectionFailed(result: ConnectionResult) {
        // An unresolvable error has occurred and a connection to Google APIs // could not be established. Display an error message, or handle
        // the failure silently
        println(" >>>> onConnectionFailed")
    }




    private inner class NetworkReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val connMgr =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connMgr.activeNetworkInfo
            val networkPref = "wifi"
            if (networkPref == "wifi" &&
                    networkInfo?.type == ConnectivityManager.TYPE_WIFI) { // Wi ́Fi is connected, so use Wi ́Fi
            } else if (networkInfo != null) {
                // Have a network connection and permission, so use data
            }

        }

    }

}

interface DownloadListener {
    fun downloadComp(result: String)

}



class DownloadKml (val caller: DownloadListener, val context: Context) : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg f_url: String): String?{
        val fileString = StringBuilder()

        try{
            val url = URL(f_url[0])
            val input = BufferedReader(InputStreamReader(url.openStream()))

            var line: String? = input.readLine()
            while(line != null){
                fileString.append(line)
                line = input.readLine()
            }



        }catch(e:Exception){

        }

        return fileString.toString()
    }


    override fun onPostExecute(result: String?) {
        if(result != null){
            caller.downloadComp(result)
        }
    }

}


class GetMaxSongNumber (val caller: DownloadListener, val context: Context) : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg f_url: String): String?{
        var str:String = ""


        try{
            val url = URL(f_url[0])
            val input = BufferedReader(InputStreamReader(url.openStream()))


            // Read all the text returned by the server

            val lines = input.readLines()

            str = lines[lines.size -6]
            str = str.removeSuffix("</Number>")
            str = str.removePrefix("    <Number>")
            input.close()


        }catch(e:Exception){

        }

        return str
    }


    override fun onPostExecute(result: String?) {
        if(result != null){
            caller.downloadComp(result)
        }
    }

}

class GetSongLyrics (val caller: DownloadListener, val context: Context) : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg f_url: String): String?{
        var str:String = ""
        val fileString = StringBuilder()

        try{
            val url = URL(f_url[0])
            val input = BufferedReader(InputStreamReader(url.openStream()))

            var line: String? = input.readLine()
            while(line != null){
                str += line
                fileString.append(line + "\n")
                line = input.readLine()
            }
            input.close()


        }catch(e:Exception){

        }

        return fileString.toString()
    }


    override fun onPostExecute(result: String?) {
        if(result != null){
            caller.downloadComp(result)
        }
    }

}