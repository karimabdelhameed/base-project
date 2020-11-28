package com.base

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.bluecrunch.base.BaseViewModel
import com.kagroup.baseProject.R
import rx.functions.Action1
import java.util.*

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel<*>> :
    AppCompatActivity() {

    var viewDataBinding: T? = null
        private set
    private var mViewModel: V? = null
    private var dialog: AlertDialog? = null
    private var loadingLayout: View? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
        init()
        viewDataBinding?.executePendingBindings()
        subscribeToLiveData()
        handleError()
        handleProgressLoading()
    }

    fun updateConfig(newLang: String) {
        val local = Locale(newLang)
        Locale.setDefault(local)
        val config = Configuration()
        config.locale = local
//        Domain.application.resources.updateConfiguration(config,null)
    }

    private fun handleProgressLoading() {
        mViewModel!!.progressLoading.observe(this, Observer { showLoading ->
            if (mViewModel != null) {
                if (showLoading)
                    window.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    )
                else
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                mViewModel!!.isLoading.set(showLoading)
            }
        })

//        mViewModel!!.showLoadingLayout.observe(this, Observer { showLoading ->
//            if (showLoading)
//                showProgressLayout()
//            else
//                hideProgressLayout()
//        })
    }

    private fun handleError() {
        mViewModel!!.errorMessage.observe(this, Observer {
            showPopUp("", it, getString(R.string.ok_), false)
        })
        mViewModel!!.networkErrorMessage.observe(this, Observer {
            showPopUp("", it, R.string.try_again,
                DialogInterface.OnClickListener { _, _ ->
                    if (mViewModel!!.action != null) {
                        mViewModel!!.action!!.run()
                    }

                }
                , getString(R.string.cancel_), false)
        })
    }

    private fun showProgressLayout() {
        if (loadingLayout == null)
            loadingLayout = findViewById(R.id.progress_bar_layout)

        loadingLayout?.visibility = View.VISIBLE
    }

    private fun hideProgressLayout() {
        if (loadingLayout == null)
            loadingLayout = findViewById(R.id.progress_bar_layout)

        if (loadingLayout == null) return

        loadingLayout?.visibility = View.GONE
    }

    protected abstract fun getViewModel(): V

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun init(): Unit

    protected abstract fun subscribeToLiveData(): Unit


    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(permission)
                == PackageManager.PERMISSION_GRANTED)
    }

    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        this.mViewModel = if (mViewModel == null) getViewModel() else mViewModel
//        viewDataBinding!!.setVariable(getBindingVariable(), mViewModel)
//        viewDataBinding!!.executePendingBindings()
    }


    fun showPopUp(
        @StringRes messageId: Int,
        @StringRes posActionName: Int,
        positiveAction: DialogInterface.OnClickListener,
        @StringRes negAction: Int,
        isCancelable: Boolean
    ): AlertDialog {
        val builder: AlertDialog.Builder
        builder = AlertDialog.Builder(this)
        return builder.setCancelable(isCancelable)
            .setMessage(getString(messageId))
            .setPositiveButton(getString(posActionName), positiveAction)
            .setNegativeButton(negAction) { dialog, which -> dialog.dismiss() }.show()
    }


    fun showPopUp(
        message: String,
        @StringRes posActionName: Int,
        positiveAction: DialogInterface.OnClickListener,
        @StringRes negAction: Int,
        isCancelable: Boolean
    ): AlertDialog {
        val builder: AlertDialog.Builder
        builder = AlertDialog.Builder(this)
        return builder.setCancelable(isCancelable)
            .setMessage(message)
            .setPositiveButton(getString(posActionName), positiveAction)
            .setNegativeButton(negAction) { dialog, which -> dialog.dismiss() }.show()
    }

    fun showPopUpWithTheme(
        message: String,
        @StringRes posActionName: Int,
        positiveAction: DialogInterface.OnClickListener,
        @StringRes negAction: Int,
        isCancelable: Boolean
    ): AlertDialog {
        val builder: AlertDialog.Builder
        builder = AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
        return builder.setCancelable(isCancelable)
            .setMessage(message)
            .setPositiveButton(getString(posActionName), positiveAction)
            .setNegativeButton(negAction) { dialog, which -> dialog.dismiss() }.show()
    }

    fun showPopUp(
        @StringRes messageId: Int,
        @StringRes posAction: Int,
        positiveAction: DialogInterface.OnClickListener,
        @StringRes negActioname: Int,
        negAction: DialogInterface.OnClickListener,
        isCancelable: Boolean
    ): AlertDialog {
        val builder: AlertDialog.Builder
        builder = AlertDialog.Builder(this)
        return builder.setCancelable(isCancelable)
            .setMessage(getString(messageId))
            .setPositiveButton(getString(posAction), positiveAction)
            .setNegativeButton(getString(negActioname), negAction).show()
    }

    fun showPopUp(
        @StringRes messageId: Int,
        posActionName: String,
        positiveAction: DialogInterface.OnClickListener,
        negActionName: String,
        negAction: DialogInterface.OnClickListener,
        isCancelable: Boolean
    ): AlertDialog {
        val builder: AlertDialog.Builder
        builder = AlertDialog.Builder(this)
        return builder.setCancelable(isCancelable)
            .setMessage(getString(messageId))
            .setPositiveButton(posActionName, positiveAction)
            .setNegativeButton(negActionName, negAction).show()
    }

    fun showPopUp(
        @StringRes messageId: Int,
        @StringRes posAction: Int,
        positiveAction: DialogInterface.OnClickListener,
        isCancelable: Boolean
    ): AlertDialog {
        val builder: AlertDialog.Builder
        builder = AlertDialog.Builder(this)
        return builder.setCancelable(isCancelable)
            .setMessage(getString(messageId))
            .setPositiveButton(getString(posAction), positiveAction)
            .show()
    }

    fun showPopUp(
        @StringRes messageId: Int,
        @StringRes posAction: Int,
        isCancelable: Boolean
    ): AlertDialog {
        val builder: AlertDialog.Builder
        builder = AlertDialog.Builder(this)
        return builder.setCancelable(isCancelable)
            .setMessage(getString(messageId))
            .setPositiveButton(getString(posAction), null)
            .show()
    }

    fun showPopUp(
        title: String, message: String,
        posAction: Int,
        positiveAction: DialogInterface.OnClickListener,
        negAction: String,
        isCancelable: Boolean
    ): AlertDialog {
        val builder: AlertDialog.Builder
        builder = AlertDialog.Builder(this)
        return builder.setCancelable(isCancelable)
            .setMessage(message)
            .setPositiveButton(getString(posAction), positiveAction)
            .setNegativeButton(negAction) { dialog, which -> dialog.dismiss() }.show()
    }

    fun showPopUp(
        title: String,
        message: String,
        posAction: String,
        isCancelable: Boolean
    ): AlertDialog {
        val builder: AlertDialog.Builder
        builder = AlertDialog.Builder(this)
        return builder.setCancelable(isCancelable)
            .setMessage(message)
            .setPositiveButton(posAction) { dialog, which -> dialog.dismiss() }
            .show()
    }

    fun showPopUp(
        title: String,
        message: String,
        posAction: String,
        positiveAction: DialogInterface.OnClickListener,
        isCancelable: Boolean
    ): AlertDialog {
        val builder: AlertDialog.Builder
        builder = AlertDialog.Builder(this)
        return builder.setCancelable(isCancelable)
            .setMessage(message)
            .setPositiveButton(posAction, positiveAction)
            .show()
    }


    fun showPopUp(
        message: String,
        @StringRes posAction: Int,
        positiveAction: DialogInterface.OnClickListener,
        isCancelable: Boolean
    ): AlertDialog {
        val builder: AlertDialog.Builder
        builder = AlertDialog.Builder(this)
        return builder.setCancelable(isCancelable)
            .setMessage(message)
            .setPositiveButton(getString(posAction), positiveAction)
            .show()
    }

    fun showPopUp(
        message: String,
        @StringRes posAction: Int,
        isCancelable: Boolean
    ): AlertDialog {
        val builder: AlertDialog.Builder
        builder = AlertDialog.Builder(this)
        return builder.setCancelable(isCancelable)
            .setMessage(message)
            .setPositiveButton(getString(posAction), null)
            .show()
    }

    fun showChoicesDialog(
        list: Array<String>, isCancelable: Boolean,
        action1: Action1<Int>
    ) {

        AlertDialog.Builder(this)
            .setCancelable(isCancelable)
            .setItems(
                list
            ) { dialog, which ->
                when (which) {
                    0 -> action1.call(0)
                    1 -> action1.call(1)
                }
            }
            .show()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
//            val newLocale = "ar"
//            val context: Context = MyContextWrapper.wrap(newBase, newLocale)
//            super.attachBaseContext(context)
//        } else {
//            super.attachBaseContext(newBase)
//        }
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm != null) {
                imm!!.hideSoftInputFromWindow(view!!.windowToken, 0)
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    fun hideKeypadWhenClickingOut(view: View) {

        // Set up touch listener for non-text box views endDate hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                hideSoftKeyboard(this@BaseActivity)
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until (view as ViewGroup).childCount) {
                val innerView = (view as ViewGroup).getChildAt(i)
                hideKeypadWhenClickingOut(innerView)
            }
        }
    }


    fun showProgressDialog(context: Context, message: String): AlertDialog {
        val llPadding = 30
        val ll = LinearLayout(context)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam

        val progressBar = ProgressBar(context)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam

        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(context)
        tvText.text = message
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 20.toFloat()
        tvText.layoutParams = llParam

        ll.addView(progressBar)
        ll.addView(tvText)

        val builder = AlertDialog.Builder(context)
        builder.setCancelable(true)
        builder.setView(ll)

        dialog = builder.create()
        val window = dialog!!.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog!!.window?.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog!!.window?.attributes = layoutParams
        }
        return dialog!!
    }

    companion object {
        fun hideSoftKeyboard(activity: Activity) {
            val inputMethodManager = activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            if ((activity.currentFocus != null && activity.currentFocus!!.windowToken != null)) {
                inputMethodManager!!.hideSoftInputFromWindow(
                    activity.currentFocus!!.windowToken, 0
                )
            }

        }
    }
}

