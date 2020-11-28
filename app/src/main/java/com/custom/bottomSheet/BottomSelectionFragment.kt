package com.custom.bottomSheet

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.BaseBottomSheetFragment
import com.base.BaseRecyclerAdapter
import com.utils.Constants.EXTRA_SELECTION_LIST
import com.utils.Constants.EXTRA_SELECTION_TITLE
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kagroup.baseProject.R
import com.kagroup.baseProject.databinding.FragmentBottomSelectionBinding
import kotlinx.android.synthetic.main.fragment_bottom_selection.*
import rx.functions.Action2

class BottomSelectionFragment : BaseBottomSheetFragment<FragmentBottomSelectionBinding,
        BottomSheetViewModel>(),
    BaseRecyclerAdapter.OnITemClickListener<BottomSheetModel>, BottomSheetNavigator {

    lateinit var mViewModel: BottomSheetViewModel
    private var title = ""
    private var list: List<BottomSheetModel> = ArrayList()
    private val mAdapter = BottomSheetAdapter()
    private var selectedPos = -1
    private lateinit var clickAction: Action2<Int, BottomSheetModel>

    override fun getViewModel(): BottomSheetViewModel {
        mViewModel = ViewModelProvider(this).get(BottomSheetViewModel::class.java)
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_bottom_selection
    }

    override fun init() {
        mViewModel.navigator = this
        viewDataBinding!!.vm = mViewModel
        viewDataBinding!!.navigator = this
        getBundles()
        initRecyclerView()
        title_textView.text = title
    }


    override fun subscibeToLiveData() {
    }

    private fun getBundles() {
        if (arguments != null) {
            title = requireArguments().getString(EXTRA_SELECTION_TITLE)!!
            list = Gson().fromJson(
                requireArguments().getString(EXTRA_SELECTION_LIST),
                object : TypeToken<List<BottomSheetModel>>() {}.type
            )
        }
    }

    private fun initRecyclerView() {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mAdapter
        mAdapter.setData(list)
        mAdapter.selectedPos = this.selectedPos
        mAdapter.onITemClickListener = this
    }

    companion object {
        fun newInstance(
            selectionTitle: String, selectionList: List<BottomSheetModel>,
            clickAction: Action2<Int, BottomSheetModel>, selectedPos: Int
        ) =
            BottomSelectionFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_SELECTION_TITLE, selectionTitle)
                    putString(
                        EXTRA_SELECTION_LIST, Gson().toJson(
                            selectionList,
                            object : TypeToken<List<BottomSheetModel>>() {}.type
                        )
                    )
                }
                this.clickAction = clickAction
                this.selectedPos = selectedPos
            }
    }

    override fun onItemClick(pos: Int, item: BottomSheetModel) {
        clickAction.call(pos, item)
        dismiss()
    }


    override fun finish() {
        dismiss()
    }

}