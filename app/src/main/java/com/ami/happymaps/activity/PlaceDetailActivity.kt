package com.ami.happymaps.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ami.happymaps.R
import com.ami.happymaps.model.HappyMapsModel
import kotlinx.android.synthetic.main.activity_place_detail.*

class PlaceDetailActivity : AppCompatActivity() {

    private var happyMapDetailModel: HappyMapsModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        happyMapDetailModel = intent.getParcelableExtra(MainActivity.EXTRA_PLACE_DETAILS)

        happyMapDetailModel?.let{
            setSupportActionBar(toolbar_detail_place)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)


            toolbar_detail_place.setNavigationOnClickListener {
                onBackPressed()
            }

            iv_place_image.setImageURI(Uri.parse(it.image))
            tv_description.text = it.description
            tv_location.text = it.location

            btn_view_on_map.setOnClickListener {
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, happyMapDetailModel)
                startActivity(intent)
            }

        }
    }
}