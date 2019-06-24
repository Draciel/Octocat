package pl.draciel.octocat.app.ui.userdetails.pager

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

typealias PageListFragmentFactory<T> = () -> PageListFragment<T>

const val EXTRA_FRAGMENTS_TAGS = "UserDetailsViewPagerAdapter::FragmentsTags"

class UserDetailsViewPagerAdapter(
    private val fragmentManager: FragmentManager, private val context: Context,
    private val factories: List<PageListFragmentFactory<*>>
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    // Internal cache of fragments to avoid findFragmentByTag lookup
    private val cachedFragments: Array<PageListFragment<*>?> = arrayOfNulls(factories.size)
    private val fragmentTags: Array<String?> = arrayOfNulls<String?>(factories.size)

    override fun getItem(position: Int): PageListFragment<*> {
        var fragment = cachedFragments[position]
        if (fragment != null) {
            return fragment
        }
        val tag = fragmentTags[position]
        if (tag != null) {
            fragment = fragmentManager.findFragmentByTag(tag) as? PageListFragment<*>
            if (fragment != null) {
                return fragment
            }
        }
        return factories[position]()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Fragment {
        val fragment = super.instantiateItem(container, position) as PageListFragment<*>
        fragmentTags[position] = fragment.tag
        cachedFragments[position] = fragment
        return fragment
    }

    override fun saveState(): Parcelable? {
        val bundle = Bundle()
        bundle.putStringArray(EXTRA_FRAGMENTS_TAGS, fragmentTags)
        return bundle
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
        if (state != null && state is Bundle) {
            val tags = state.getStringArray(EXTRA_FRAGMENTS_TAGS)
            tags?.copyInto(fragmentTags)
        }
    }

    override fun getCount(): Int = cachedFragments.size

    override fun getPageTitle(position: Int): CharSequence = context.getString(getItem(position).getPageTitle())
}
