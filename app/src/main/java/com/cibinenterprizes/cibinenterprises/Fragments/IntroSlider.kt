package com.cibinenterprizes.cibinenterprises.Fragments

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.cibinenterprizes.cibinenterprises.R
import io.grpc.internal.SharedResourceHolder
import kotlinx.android.synthetic.main.fragment_intro_slider.*

class IntroSlider : Fragment() {

    var imageView: Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro_slider, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragment_image_view.setImageResource(imageView!!)
    }

    fun setImage(imageViews: Int){
        imageView = imageViews
    }

}


