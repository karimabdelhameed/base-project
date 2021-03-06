package com.base

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluecrunch.base.BaseViewModel
import com.custom.PaginationScrollListener
import com.kagroup.baseProject.R
import io.reactivex.functions.Action

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel<*>> : Fragment() {

    var baseActivity: BaseActivity<*, *>? = null
        private set
    private var mRootView: View? = null
    var viewDataBinding: T? = null
        private set
    protected var viewModel: V? = null
    var loadingLayout: View? = null

    protected abstract fun getMyViewModel(): V

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun init(): Unit


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getMyViewModel()
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(
            inflater, getLayoutId(), container,
            false
        )
        mRootView = viewDataBinding!!.root
        return mRootView
        //        return super.onCreateView(inflater, container, savedInstanceState);
    }

    fun paginate(recyclerView: RecyclerView?, layoutManager: LinearLayoutManager, action: Action?) {
        recyclerView?.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return viewModel?.isLastPage!!
            }

            override fun isLoading(): Boolean {
                return viewModel?.isPagingLoading?.get()!!
            }

            override fun loadMoreItems() {
                viewModel?.setIsPagingLoading(true)
                action?.run()
            }
        })
    }

    fun paginateWithScrollView(scrollView: NestedScrollView?, action: Action?) {
        scrollView?.setOnScrollChangeListener { v, scrollX: Int, scrollY: Int,
                                                oldScrollX: Int, oldScrollY: Int ->


            val lastChild = scrollView.getChildAt(scrollView.childCount - 1)

            if (lastChild != null) {

                if ((scrollY >=
                            (lastChild.measuredHeight - v.measuredHeight))
                    && scrollY > oldScrollY && !viewModel?.isPagingLoading?.get()!!
                    && !viewModel?.isLastPage!!
                ) {

                    viewModel?.setIsPagingLoading(true)
                    action?.run()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        viewDataBinding!!.executePendingBindings()
        handleError()
        handleProgressLoading()
    }


    private fun handleProgressLoading() {
//        viewModel!!.showLoadingLayout.observe(viewLifecycleOwner, Observer { showLoading ->
//            if(showLoading)
//                showProgressLayout()
//            else
//                hideProgressLayout()
//        })
    }

    private fun handleError() {
        viewModel!!.errorMessage.observe(viewLifecycleOwner, Observer {
            (activity as BaseActivity<*, *>).showPopUp(
                "",
                it,
                getString(android.R.string.ok),
                false
            )
        })
        viewModel!!.networkErrorMessage.observe(viewLifecycleOwner, Observer {
            (activity as BaseActivity<*, *>).showPopUp("", it, R.string.try_again,
                DialogInterface.OnClickListener { _, _ ->
                    if (viewModel!!.action != null) {
                        viewModel!!.action!!.run()
                    }

                }
                , getString(android.R.string.cancel), false)
        })
    }


    private fun showProgressLayout() {
        if (loadingLayout == null)
            loadingLayout = view?.findViewById(R.id.progress_bar_layout)

        loadingLayout?.visibility = View.VISIBLE
    }

    private fun hideProgressLayout() {
        if (loadingLayout == null)
            loadingLayout = view?.findViewById(R.id.progress_bar_layout)

        if (loadingLayout == null) return

        loadingLayout?.visibility = View.GONE
    }


    fun hideKeyboard() {
        if (baseActivity != null) {
            baseActivity!!.hideKeyboard()
        }
    }


    fun showPopUp(
        @StringRes messageId: Int,
        @StringRes posActionName: Int,
        positiveAction: DialogInterface.OnClickListener,
        @StringRes negAction: Int,
        isCancelable: Boolean
    ): AlertDialog {
        return baseActivity!!.showPopUp(
            messageId,
            posActionName,
            positiveAction,
            negAction,
            isCancelable
        )
    }

    fun showPopUp(
        @StringRes messageId: Int,
        @StringRes posAction: Int,
        positiveAction: DialogInterface.OnClickListener,
        @StringRes negActioname: Int,
        negAction: DialogInterface.OnClickListener,
        isCancelable: Boolean
    ): AlertDialog {
        return showPopUp(
            messageId,
            posAction,
            positiveAction,
            negActioname,
            negAction,
            isCancelable
        )
    }

    fun showPopUp(
        @StringRes messageId: Int,
        posActionName: String,
        positiveAction: DialogInterface.OnClickListener,
        negActionName: String,
        negAction: DialogInterface.OnClickListener,
        isCancelable: Boolean
    ): AlertDialog {
        return baseActivity!!.showPopUp(
            messageId,
            posActionName,
            positiveAction,
            negActionName,
            negAction,
            isCancelable
        )
    }

    fun showPopUp(
        @StringRes messageId: Int,
        @StringRes posAction: Int,
        positiveAction: DialogInterface.OnClickListener,
        isCancelable: Boolean
    ): AlertDialog {
        return baseActivity!!.showPopUp(messageId, posAction, positiveAction, isCancelable)
    }


    fun showPopUp(
        message: String,
        @StringRes posActionName: Int,
        positiveAction: DialogInterface.OnClickListener,
        @StringRes negAction: Int,
        isCancelable: Boolean
    ): AlertDialog {
        return baseActivity!!.showPopUp(
            message,
            posActionName,
            positiveAction,
            negAction,
            isCancelable
        )
    }

    fun showPopUp(
        @StringRes messageId: Int,
        @StringRes posAction: Int,
        isCancelable: Boolean
    ): AlertDialog {
        return baseActivity!!.showPopUp(messageId, posAction, isCancelable)
    }

    fun showPopUp(
        title: String, message: String,
        posAction: Int,
        positiveAction: DialogInterface.OnClickListener,
        negAction: String,
        isCancelable: Boolean
    ): AlertDialog {
        return baseActivity!!.showPopUp(
            title,
            message,
            posAction,
            positiveAction,
            negAction,
            isCancelable
        )
    }

    fun showPopUp(
        title: String,
        message: String,
        posAction: String,
        isCancelable: Boolean
    ): AlertDialog {
        return baseActivity!!.showPopUp(title, message, posAction, isCancelable)
    }

    fun showPopUp(
        message: String,
        @StringRes posAction: Int,
        positiveAction: DialogInterface.OnClickListener,
        isCancelable: Boolean
    ): AlertDialog {
        return baseActivity!!.showPopUp(message, posAction, positiveAction, isCancelable)
    }

    fun showPopUp(
        message: String,
        @StringRes posAction: Int,
        isCancelable: Boolean
    ): AlertDialog {
        return baseActivity!!.showPopUp(message, posAction, isCancelable)
    }

}
