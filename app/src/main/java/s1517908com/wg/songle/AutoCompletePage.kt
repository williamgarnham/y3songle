package s1517908com.wg.songle

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_auto_complete_page.*

import android.view.View
import kotlinx.android.synthetic.main.content_auto_complete_page.*
import android.content.Intent


class AutoCompletePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_complete_page)
        setSupportActionBar(toolbar)



        yesB.setOnClickListener { view ->
            //add to completed page
            //subtract points

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
        }
    }

}
