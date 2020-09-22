package com.zzs.ppjoke_kotlin_jetpack.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.zzs.ppjoke_kotlin_jetpack.BasePageListViewModel
import com.zzs.ppjoke_kotlin_jetpack.R
import com.zzs.ppjoke_kotlin_jetpack.common.ext.toast
import com.zzs.ppjoke_kotlin_jetpack.databinding.LayoutRefreshViewBinding
import kotlinx.android.synthetic.main.layout_refresh_view.*
import java.lang.reflect.ParameterizedType

abstract class AbsListFragment<T, VM : BasePageListViewModel<T>> : Fragment(), OnRefreshLoadMoreListener {
    protected var shouldPause: Boolean = true
    lateinit var binding: LayoutRefreshViewBinding
    protected lateinit var mAdapter: PagedListAdapter<T, RecyclerView.ViewHolder>
    lateinit var mViewModel: VM
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!this::binding.isInitialized) {
            binding =
                DataBindingUtil.inflate(inflater, R.layout.layout_refresh_view, container, false)
        }
        if (!this::mAdapter.isInitialized) {
            mAdapter = getPageAdapter()
        }
        binding.apply {
            mRecycler.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                setHasFixedSize(true)
                adapter = mAdapter
                val decoration =
                    DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
                ContextCompat.getDrawable(requireContext(), R.drawable.list_divider)?.let {
                    decoration.setDrawable(
                        it
                    )
                }
                addItemDecoration(decoration)
            }
            mRefreshView.apply {
                setEnableRefresh(true)
                setEnableLoadMore(true)
                setOnLoadMoreListener(this@AbsListFragment)
                setOnRefreshListener(this@AbsListFragment)
            }
        }
        return binding.root
    }

    abstract fun getPageAdapter(): PagedListAdapter<T, RecyclerView.ViewHolder>

    private fun finishRefresh(hasData: Boolean) {
        var nowHasData: Boolean = hasData
        mAdapter.currentList?.also { list ->
            nowHasData = hasData or list.isNotEmpty()
        }
        val state = mRefreshView.state
        if (state.isFooter && state.isOpening) {
            mRefreshView.finishLoadMore()
        } else if (state.isHeader && state.isOpening) {
            mRefreshView.finishRefresh()
        }
        mEmptyView.visibility = if (nowHasData) View.GONE else View.VISIBLE
    }

    fun submitList(list: PagedList<T>) {
        if (list.isNotEmpty()) mAdapter.submitList(list)
        finishRefresh(list.isNotEmpty())
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = javaClass.genericSuperclass as ParameterizedType
        val arguments = type.actualTypeArguments
        if (arguments.size > 1) {
            val modelClass = (arguments[1] as Class<*>).asSubclass(BasePageListViewModel::class.java)
            mViewModel = ViewModelProviders.of(this).get(modelClass) as VM
            afterViewCreatedViewModelCreated()
            mViewModel.fetchPageData().observe(viewLifecycleOwner, Observer {
                submitList(it)
            })
            mViewModel.expHasData.observe(viewLifecycleOwner, Observer {
                finishRefresh(it)
            })
            mViewModel.expOnError.observe(viewLifecycleOwner, Observer {
                it.toast(requireContext())
            })
        }
    }

    open fun afterViewCreatedViewModelCreated() = run { }
}