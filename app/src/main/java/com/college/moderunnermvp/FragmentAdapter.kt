package com.college.moderunnermvp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class FragmentAdapter (fm:FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0-> {
                FragmentHome()
            }
            1-> {
                FragmentPersonalBest()
            }
            else -> {
                FragmentRunningHistory()
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0-> "Start New Run"
            1-> "Running History"
            else -> "Personal Best"
        }
    }
}