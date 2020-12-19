package com.college.moderunnermvp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SummaryAdapter (fm:FragmentManager) : FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0-> {
                FragmentHome()
            }
            1-> {
                GraphSummaryFragment()
            }
            else -> {
                FragmentPersonalBest()
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0-> "Map"
            1-> "Graph"
            else -> "Stats"
        }
    }
}