package com.cibinenterprizes.cibinenterprises.Adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import com.cibinenterprizes.cibinenterprises.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindowForGoogleMap(context: Context) : GoogleMap.InfoWindowAdapter{

    var mContext = context
    var mWindow = (context as Activity).layoutInflater.inflate(R.layout.custom_info_window, null)

    private fun rendowWindowText(marker: Marker, view: View){

        val title = view.findViewById<TextView>(R.id.textTitle)
        val snippet = view.findViewById<TextView>(R.id.textSnippet)

        title.text = marker.title
        snippet.text = marker.snippet
    }

    override fun getInfoWindow(p0: Marker): View? {
        rendowWindowText(p0, mWindow)
        return mWindow
    }

    override fun getInfoContents(p0: Marker): View? {
        rendowWindowText(p0, mWindow)
        return mWindow
    }
}