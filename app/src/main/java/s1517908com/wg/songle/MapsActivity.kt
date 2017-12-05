package s1517908com.wg.songle

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import android.content.Context

import android.content.IntentFilter
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.res.Resources
import android.net.ConnectivityManager
//import android.webkit.DownloadListener
import java.io.*
import java.net.URL
import com.google.maps.android.kml.KmlLayer

import android.os.AsyncTask

import android.widget.Toast
import android.widget.Button
import kotlinx.android.synthetic.main.activity_maps.*
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, DownloadListener {


    private lateinit var mMap: GoogleMap
    //var filename = "testMap.kml"
    private var receiver = NetworkReceiver()

    var kmllayer: KmlLayer? = null

    private lateinit var mGoogleApiClient: GoogleApiClient
    val permissionsRequestAccessFineLocation = 1
    var mLocationPermissionGranted = false
    // getLastLocation can return null, so we need the type ”Location?”
    private var mLastLocation: android.location.Location? = null
    val tag = "MapsActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        val intent = intent
        val URL = "http://www.inf.ed.ac.uk/teaching/courses/cslp/data/songs/01/map1.kml"
        val kmldownloader = DownloadKml(this,this)
        kmldownloader.execute(URL)




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



        val res :Resources = getResources()
        val pointsIS: InputStream  = res.openRawResource(R.raw.pointsnumber)
        val bufferedReaderTXT: BufferedReader = BufferedReader(InputStreamReader(pointsIS))
        val points:String = bufferedReaderTXT.readText()



        pointsText.text = points

        guessText.visibility = View.INVISIBLE
        guessSongButton.setOnClickListener { view ->
            Toast.makeText(this, "Incorrect!",
                    Toast.LENGTH_LONG).show()

            if(guessText.visibility == View.VISIBLE){
                guessText.visibility = View.INVISIBLE
            }else{
                guessText.visibility = View.VISIBLE
            }

        }

        freeWordB.setOnClickListener { view ->
            //get free word
            //subtract points
        }

        viewWordsB.setOnClickListener { view ->
            val intent = Intent(this, CollectedWords::class.java)
            startActivity(intent)
        }

        autoCompleteB.setOnClickListener { view ->
            val intent = Intent(this, AutoCompletePage::class.java)
            startActivity(intent)
        }


    }

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

        kmllayer = KmlLayer(mMap, stream, this)
        kmllayer!!.addLayerToMap()

        // Add a marker in Sydney and move the camera
        //val FHill = LatLng(55.946233, -3.192473)
        //mMap.addMarker(MarkerOptions().position(FHill).title("FHill"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(FHill))


        try {  // Visualise current position with a small blue circle
            mMap.isMyLocationEnabled = true
        } catch (se: SecurityException) {
            println("Security exception thrown [onMapReady]")
        }
        // Add ”My location” button to the user interface
        mMap.uiSettings.isMyLocationButtonEnabled = true
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


    val tag = "DownloadKml"
    var cont = context
    //var sng = song
    //var mp = map
    //var filename = "Song ${sng}, map ${mp}"

    //var file = File(cont.getFilesDir(), filename)

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

        val outputStream : FileOutputStream

        try{
            //outputStream = cont.openFileOutput(filename, Context.MODE_PRIVATE)
            //outputStream.write(fileString.toString().toByteArray())
            //outputStream.close()
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
