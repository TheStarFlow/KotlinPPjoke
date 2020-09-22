package com.zzs.ppjoke_kotlin_jetpack.ui.sofa

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zzs.ppjoke_kotlin_jetpack.R
import com.zzs.ppjoke_kotlin_jetpack.common.utils.AppConfig
import com.zzs.ppjoke_kotlin_jetpack.databinding.FragmentSofaBinding
import com.zzs.ppjoke_kotlin_jetpack.model.SofaTab
import com.zzs.ppjoke_kotlin_jetpack.model.Tab
import com.zzs.ppjoke_kotlin_jetpack.ui.home.HomeFragment
import kotlinx.android.synthetic.main.fragment_sofa.*

class SofaFragment : Fragment() {

    private lateinit var sofaViewModel: SofaViewModel
    lateinit var binding: FragmentSofaBinding
    lateinit var mSofaTab: SofaTab
    lateinit var mediator: TabLayoutMediator
    val tabs = mutableListOf<Tab>()
    val fragmens = mutableListOf<Fragment?>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sofaViewModel = ViewModelProviders.of(this).get(SofaViewModel::class.java)
        binding = DataBindingUtil.inflate<FragmentSofaBinding>(
            inflater,
            R.layout.fragment_sofa,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSofaTab = getTabs()
        mSofaTab.tabs.filter {
            it.enable
        }.forEach {
            tabs.add(it)
        }
        mViewPager.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
        mViewPager.adapter = object : FragmentStateAdapter(childFragmentManager, lifecycle) {
            override fun getItemCount(): Int {
                return tabs.size

            }

            override fun createFragment(position: Int): Fragment {
//                var fragment = fragmens[position]
//                if (fragment == null) {
//                    fragment = getFragment(position)
//                }
                return getFragment(position)
            }

        }
        mediator = TabLayoutMediator(
            mTabLayout,
            mViewPager
        ) { tab, position -> tab.customView = makeTabView(position) }
        mediator.attach()
        mViewPager.registerOnPageChangeCallback(tabSelect)
        mViewPager.post {
            Runnable {
                mViewPager.setCurrentItem(mSofaTab.select, false)
            }
        }
    }

    private val tabSelect = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            val tabCount: Int = mTabLayout.tabCount
            for (i in 0 until tabCount) {
                val tab: TabLayout.Tab? = mTabLayout?.getTabAt(i)
                val customView = tab?.customView as TextView?
                if (tab?.position == position) {
                    customView!!.textSize = mSofaTab.activeSize.toFloat()
                    customView.typeface = Typeface.DEFAULT_BOLD
                } else {
                    customView!!.textSize = mSofaTab.normalSize.toFloat()
                    customView.typeface = Typeface.DEFAULT
                }
            }
        }
    }

    private fun makeTabView(position: Int): View {
        return TextView(context).apply {
            val states = arrayOfNulls<IntArray>(2)
            states[0] = intArrayOf(android.R.attr.state_selected)
            states[1] = intArrayOf()

            val colors = intArrayOf(
                Color.parseColor(mSofaTab.activeColor),
                Color.parseColor(mSofaTab.normalColor)
            )
            val stateList = ColorStateList(states, colors)
            setTextColor(stateList)
            text = tabs[position].title
            textSize = mSofaTab.normalSize.toFloat()
        }
    }

    fun getFragment(position: Int) = HomeFragment.newInstance(tabs[position].tag)

    private fun getTabs() = AppConfig.getSofaTabs()

    override fun onDestroyView() {
        super.onDestroyView()
        mViewPager.unregisterOnPageChangeCallback(tabSelect)
    }

    override fun onDestroy() {
        mediator.detach()
        //不能在这里调用kotlin Extension找的控件，会报 空指针错误，应该在onDestroyView里面调用
        //   mViewPager.unregisterOnPageChangeCallback(tabSelect)
        super.onDestroy()
        // mViewPager.unregisterOnPageChangeCallback(tabSelect)

    }
}