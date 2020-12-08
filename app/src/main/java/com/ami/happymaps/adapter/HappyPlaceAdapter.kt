package com.ami.happymaps.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ami.happymaps.R
import com.ami.happymaps.activity.AddHappyMapActivity
import com.ami.happymaps.activity.MainActivity
import com.ami.happymaps.database.DataBaseHandler
import com.ami.happymaps.model.HappyMapsModel
import kotlinx.android.synthetic.main.activity_add_happy_place.view.*
import kotlinx.android.synthetic.main.activity_add_happy_place.view.iv_place_image
import kotlinx.android.synthetic.main.item_happy_maps.view.*

class HappyPlaceAdapter (private val context: Context, private var list: ArrayList<HappyMapsModel>):
    RecyclerView.Adapter<HappyPlaceViewHolder>() {


private var onClickListener: View.OnClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HappyPlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_happy_maps, parent, false)
        return HappyPlaceViewHolder(view)
    }


    override fun onBindViewHolder(holder: HappyPlaceViewHolder, position: Int) {
        val model = list[position]

        if (holder is HappyPlaceViewHolder){
            holder.itemView.iv_place_image.setImageURI(Uri.parse(model.image))
            holder.itemView.tvTitle.text = model.title
            holder.itemView.tvDescription.text = model.description

            holder.itemView.setOnClickListener {
                if (onClickListener != null){
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int){
        val intent = Intent(context, AddHappyMapActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, list[position])

        activity.startActivityForResult(
                intent,
                requestCode
        )
        notifyItemChanged(position)
    }

    fun removeAt(position: Int){
        val dbHandler = DataBaseHandler(context)
        val isDelete = dbHandler.deleteHappyMap(list[position])
        if (isDelete > 0){
            list.removeAt(position)
            notifyItemRemoved(position)
        }

    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener : View.OnClickListener {
        fun onClick(position: Int, model: HappyMapsModel)
    }

   
}

private fun View.OnClickListener.onClick(position: Int, model: HappyMapsModel) {

}


class HappyPlaceViewHolder(view: View): RecyclerView.ViewHolder(view){

}

