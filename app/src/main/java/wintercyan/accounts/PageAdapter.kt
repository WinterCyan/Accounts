package wintercyan.accounts

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class PageAdapter(fm: FragmentManager, private val context: Context, var tabs: ArrayList<String>): FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(p0: Int): Fragment? {
        when (p0) {
            0 -> return AddFragment.newInstance(context)
            1 -> return ItemFragment().newInstance(context)
            3 -> return SummaryFragment().newInstance(context)
        }
        return SummaryFragment().newInstance(context)
    }

    override fun getPageTitle(position: Int): CharSequence = tabs[position]

}