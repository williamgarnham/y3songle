package s1517908com.wg.songle
import android.os.AsyncTask
import android.content.res.Resources
import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.IOException
import org.xmlpull.v1.XmlPullParserException
import java.io.InputStream
import java.net.URL
import java.net.HttpURLConnection

/**
 * Created by Buggs on 09/11/2017.
 */

data class Song(val num: String, val artist: String, val title: String, val link: String)


class DownloadXML(val caller : DownloadCompleteListener) :
        AsyncTask<String, Void, String>() {


    var solist = emptyList<Song>()

    fun getSoList():List<Song>{
        return solist
    }

    override fun doInBackground(vararg urls: String): String {
        return try {
            loadXmlFromNetwork(urls[0])
        } catch (e: IOException) {
             "Unable to load content. Check your network connection"
    } catch (e: XmlPullParserException) {
            "Error parsing XML"

        }
    }

    private fun loadXmlFromNetwork(urlString: String): String { val result = StringBuilder()
        val stream = downloadUrl(urlString)
        // Do something with stream e.g. parse as XML, build result
        solist = parse(stream)
        return stream.toString()
    }


    // Given a string representation of a URL, sets up a connection and gets // an input stream.
    @Throws(IOException::class)
    private fun downloadUrl(urlString: String): InputStream {
        val url = URL(urlString)
        val conn = url.openConnection() as HttpURLConnection // Also available: HttpsURLConnection
        conn.readTimeout = 10000 // milliseconds
        conn.connectTimeout = 15000 // milliseconds
        conn.requestMethod = "GET"
        conn.doInput = true
        // Starts the query
        conn.connect()
        return conn.inputStream
    }
    private val ns: String? = null
    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(input: InputStream): List<Song> {
        input.use {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,
                    false)
            parser.setInput(input, null)
            parser.nextTag()
            return readSongs(parser)
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readSongs(parser: XmlPullParser): List<Song> {
        val songs = ArrayList<Song>()
        parser.require(XmlPullParser.START_TAG, ns, "Songs")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            // Starts by looking for the entry tag
            if (parser.name == "Song") {
                songs.add(readSong(parser))
            } else {

            }
        }
        return songs
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readSong(parser: XmlPullParser): Song {
        parser.require(XmlPullParser.START_TAG, ns, "Placemark")
        var num = ""
        var artist = ""
        var title = ""
        var link = ""
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG)
                continue
            when (parser.name) {
                "Number" -> num = readNumber(parser)
                "Artist" -> artist = readArtist(parser)
                "Title" -> title = readTitle(parser)
                "Link" -> link = readLink(parser)

            }
        }
        return Song(num, artist,title,link)
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readNumber(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "Number")
        val num = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "Number")
        return num
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readArtist(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "Artist")
        val artist = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "Artist")
        return artist
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readTitle(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "Title")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "Title")
        return title
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readLink(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "Link")
        val link = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "Link")
        return link
    }


    @Throws(IOException::class, XmlPullParserException::class)
    private fun readXXXX(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "Song")
        val Point = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "Point")
        return Point
    }



    @Throws(IOException::class, XmlPullParserException::class)
    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }




    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
        caller.downloadComplete(result)
    }


}

interface DownloadCompleteListener {
    fun downloadComplete(result: String){

    }
}

