package magym.rssreader.image

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import magym.rssreader.R
import magym.rssreader.utils.load

class PhotoActivity : AppCompatActivity() {

    companion object {
        private const val URL_KEY = "URL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_activity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""

        val image = findViewById<View>(R.id.fullscreen_content) as ImageView

        val url = intent.getStringExtra(URL_KEY)

        image.load(url, R.drawable.placeholder)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

}