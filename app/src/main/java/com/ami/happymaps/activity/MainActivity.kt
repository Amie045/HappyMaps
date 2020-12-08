package com.ami.happymaps.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ami.happymaps.R
import com.ami.happymaps.adapter.HappyPlaceAdapter
import com.ami.happymaps.callback.SwipeToDeleteCallback
import com.ami.happymaps.callback.SwipeToEditCallBack
import com.ami.happymaps.database.DataBaseHandler
import com.ami.happymaps.model.HappyMapsModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    companion object{
        private const val ADD_PLACE_ACTIVITY_REQUEST_CODE = 1
        val EXTRA_PLACE_DETAILS = "extra_place_details"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab_add_happy_place.setOnClickListener {
            val intent = Intent (this@MainActivity,AddHappyMapActivity::class.java)
            startActivityForResult(intent, ADD_PLACE_ACTIVITY_REQUEST_CODE)
        }

        getHappyMapFromLocalDB()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_PLACE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                getHappyMapFromLocalDB()
            } else {
                Log.e("Activity", "Cancelled or Back Pressed")
            }
        }
    }

    private fun getHappyMapFromLocalDB(){

        val dbHandler = DataBaseHandler(this)

        val getHappyMapList = dbHandler.getHappyMapList()

        if (getHappyMapList.size > 0){
            rv_happy_places_list.visibility = View.VISIBLE
            tv_no_records.visibility = View.GONE

            setupHappyPlaceRecyclerView(getHappyMapList)
        } else {
            rv_happy_places_list.visibility = View.GONE
            tv_no_records.visibility = View.VISIBLE
        }

    }


    private fun setupHappyPlaceRecyclerView(happyMapList: ArrayList<HappyMapsModel>){
        rv_happy_places_list.layoutManager = LinearLayoutManager(this)
        rv_happy_places_list.setHasFixedSize(true)

        val placeAdapter = HappyPlaceAdapter(this, happyMapList)
        rv_happy_places_list.adapter = placeAdapter

        placeAdapter.setOnClickListener(object  : HappyPlaceAdapter.OnClickListener{
            override fun onClick(position: Int, model: HappyMapsModel) {
                val intent = Intent (this@MainActivity, PlaceDetailActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS, model)
                startActivity(intent)
            }

            override fun onClick(p0: View?) {
                TODO("Not yet implemented")
            }

        })

        val editSwipeHandler = object : SwipeToEditCallBack(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_happy_places_list.adapter as HappyPlaceAdapter
                adapter.notifyEditItem(
                    this@MainActivity,
                    viewHolder.adapterPosition,
                    ADD_PLACE_ACTIVITY_REQUEST_CODE
                )
            }
        }

        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(rv_happy_places_list)

        val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_happy_places_list.adapter as HappyPlaceAdapter
                adapter.removeAt(viewHolder.adapterPosition)

                getHappyMapFromLocalDB()
            }
        }

        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(rv_happy_places_list)

    }


}