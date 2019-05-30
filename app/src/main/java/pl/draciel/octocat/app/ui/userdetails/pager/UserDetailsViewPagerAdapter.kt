package pl.draciel.octocat.app.ui.userdetails.pager

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class UserDetailsViewPagerAdapter(
    manager: FragmentManager, private val context: Context,
    val pages: List<PageListFragment<*>>
) :
    FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = pages[position]

    override fun getCount(): Int = pages.size

    override fun getPageTitle(position: Int): CharSequence = context.getString(pages[position].getPageTitle())
}
