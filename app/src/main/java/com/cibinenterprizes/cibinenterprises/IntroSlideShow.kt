package com.cibinenterprizes.cibinenterprises

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.cibinenterprizes.cibinenterprises.Fragments.IntroSlider
import kotlinx.android.synthetic.main.activity_intro_slide_show.*
import java.util.ArrayList

class IntroSlideShow : AppCompatActivity() {

    val fragment1 = IntroSlider()
    val fragment2 = IntroSlider()
    val fragment3 = IntroSlider()
    val fragment4 = IntroSlider()
    val fragment5 = IntroSlider()
    val fragment6 = IntroSlider()
    val fragment7 = IntroSlider()
    lateinit var adapter : myPagerAdapter

    lateinit var preference : SharedPreferences
    val pref_show_intro = "Intro"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_slide_show)

        preference = getSharedPreferences("IntroSlider", Context.MODE_PRIVATE)
        if (!preference.getBoolean(pref_show_intro, true)){
            startActivity(Intent(this@IntroSlideShow, MainActivity::class.java))
            finish()
        }
        fragment1.setImage(R.drawable.slideshow)
        fragment2.setImage(R.drawable.slideshow_1)
        fragment3.setImage(R.drawable.slideshow_2)
        fragment4.setImage(R.drawable.slideshow_3)
        fragment5.setImage(R.drawable.slideshow_4)
        fragment6.setImage(R.drawable.slideshow_5)
        fragment7.setImage(R.drawable.slideshow_6)

        adapter = myPagerAdapter(supportFragmentManager)
        adapter.list.add(fragment1)
        adapter.list.add(fragment2)
        adapter.list.add(fragment3)
        adapter.list.add(fragment4)
        adapter.list.add(fragment5)
        adapter.list.add(fragment6)
        adapter.list.add(fragment7)

        view_pager.adapter = adapter

        btn_skip.setOnClickListener {
            startActivity(Intent(this@IntroSlideShow, MainActivity::class.java))
            finish()
            val editor = preference.edit()
            editor.putBoolean(pref_show_intro,false)
            editor.apply()
        }
        btn_next.setOnClickListener {
            view_pager.currentItem++
        }
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            @SuppressLint("ResourceAsColor")
            override fun onPageSelected(position: Int) {
                if (position == adapter.list.size-1){
                    btn_next.text = "DONE"
                    btn_next.setOnClickListener {
                        startActivity(Intent(this@IntroSlideShow, MainActivity::class.java))
                        finish()
                        val editor = preference.edit()
                        editor.putBoolean(pref_show_intro,false)
                        editor.apply()
                    }
                }else{
                    btn_next.text = "NEXT"
                    btn_next.setOnClickListener {
                        view_pager.currentItem++
                    }
                }
                when(view_pager.currentItem) {
                    0->{
                        indicator1.setTextColor(R.color.green)
                        indicator2.setTextColor(Color.WHITE)
                        indicator3.setTextColor(Color.WHITE)
                        indicator4.setTextColor(Color.WHITE)
                        indicator5.setTextColor(Color.WHITE)
                        indicator6.setTextColor(Color.WHITE)
                        indicator7.setTextColor(Color.WHITE)
                    }
                    1->{
                        indicator1.setTextColor(Color.WHITE)
                        indicator2.setTextColor(R.color.green)
                        indicator3.setTextColor(Color.WHITE)
                        indicator4.setTextColor(Color.WHITE)
                        indicator5.setTextColor(Color.WHITE)
                        indicator6.setTextColor(Color.WHITE)
                        indicator7.setTextColor(Color.WHITE)
                    }
                    2->{
                        indicator1.setTextColor(Color.WHITE)
                        indicator2.setTextColor(Color.WHITE)
                        indicator3.setTextColor(R.color.green)
                        indicator4.setTextColor(Color.WHITE)
                        indicator5.setTextColor(Color.WHITE)
                        indicator6.setTextColor(Color.WHITE)
                        indicator7.setTextColor(Color.WHITE)
                    }
                    3->{
                        indicator1.setTextColor(Color.WHITE)
                        indicator2.setTextColor(Color.WHITE)
                        indicator3.setTextColor(Color.WHITE)
                        indicator4.setTextColor(R.color.green)
                        indicator5.setTextColor(Color.WHITE)
                        indicator6.setTextColor(Color.WHITE)
                        indicator7.setTextColor(Color.WHITE)
                    }
                    4->{
                        indicator1.setTextColor(Color.WHITE)
                        indicator2.setTextColor(Color.WHITE)
                        indicator3.setTextColor(Color.WHITE)
                        indicator4.setTextColor(Color.WHITE)
                        indicator5.setTextColor(R.color.green)
                        indicator6.setTextColor(Color.WHITE)
                        indicator7.setTextColor(Color.WHITE)
                    }
                    5->{
                        indicator1.setTextColor(Color.WHITE)
                        indicator2.setTextColor(Color.WHITE)
                        indicator3.setTextColor(Color.WHITE)
                        indicator4.setTextColor(Color.WHITE)
                        indicator5.setTextColor(Color.WHITE)
                        indicator6.setTextColor(R.color.green)
                        indicator7.setTextColor(Color.WHITE)
                    }
                    6->{
                        indicator1.setTextColor(Color.WHITE)
                        indicator2.setTextColor(Color.WHITE)
                        indicator3.setTextColor(Color.WHITE)
                        indicator4.setTextColor(Color.WHITE)
                        indicator5.setTextColor(Color.WHITE)
                        indicator6.setTextColor(Color.WHITE)
                        indicator7.setTextColor(R.color.green)
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
    }

    class myPagerAdapter(manager : FragmentManager) : FragmentPagerAdapter(manager){

        val list : MutableList<Fragment> = ArrayList()

        override fun getCount(): Int {
            return list.size
        }

        override fun getItem(position: Int): Fragment {
            return list[position]
        }

    }
}