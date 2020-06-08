package com.androdude.chatwho.Mainscreen.Adapters

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.androdude.chatwho.Mainscreen.Fragments.*

class pageAdapter(val fm: FragmentManager, val numberOfTabs : Int) : FragmentPagerAdapter(fm) {



    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        if(position==0)
        {
            return  ChatFragment()
        }
        else if(position==1)
        {
            return  StatusFragment()
        }
       else
        {
            return  ContactFragment()
        }

    }

    override fun getCount(): Int {
        return numberOfTabs
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }
}