package com.custom

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.annotation.NonNull
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.base.BaseRecyclerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.kagroup.baseProject.R
import rx.functions.Action1
import timber.log.Timber
import java.util.*
import javax.annotation.Nullable
import kotlin.collections.ArrayList


object BindingAdapters {
    @JvmStatic
    @BindingAdapter("imageURL", "progressLoading", requireAll = false)
    fun bindImageURL(view: ImageView, url: String?, @Nullable progressLoading: ProgressBar?) {
        progressLoading?.visibility = VISIBLE
        if (url != null && url.isNotEmpty()) {
            Glide.with(view.context)
                .load(url)
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressLoading?.visibility = GONE
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressLoading?.visibility = GONE
                        return false
                    }
                })
                .apply(
                    RequestOptions()
//                        .placeholder(R.drawable.ic_logo_large)
                        .centerInside()
                )
                .into(view)
        } else {
//            Glide.with(view.context)
//                .load(R.drawable.ic_logo_large)
//                .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("imageURL", "progressLoading", requireAll = false)
    fun bindClientCommentImageURL(
        view: ImageView, url: String?,
        @Nullable progressLoading: ProgressBar?
    ) {
        progressLoading?.visibility = VISIBLE
        if (url != null && url.isNotEmpty()) {
            Glide.with(view.context)
                .load(url)
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressLoading?.visibility = GONE
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressLoading?.visibility = GONE
                        return false
                    }
                })
                .apply(
                    RequestOptions()
//                        .placeholder(R.drawable.ic_logo_large)
                        .centerInside()
                )
                .into(view)
        } else {
//            Glide.with(view.context)
//                .load(R.drawable.ic_avatar)
////                .apply(
////                    RequestOptions()
////                        .placeholder(com.bluecrunch.dareyourbody.R.drawable.ic_logo_large)
////                        .centerInside()
////                )
//                .into(view)
        }
    }


    @JvmStatic
    @BindingAdapter("imageSrc")
    fun bindImageSource(view: ImageView, resource: Int) {
        if (resource != 0 && resource != -1) {
            view.setImageResource(resource)
        }
    }

    @JvmStatic
    @BindingAdapter("imageBitmap")
    fun bindImageBitmap(view: ImageView, resource: Bitmap?) {
        if (resource != null) {
            view.setImageBitmap(resource)
        }
    }

    @JvmStatic
    @BindingAdapter("imageUri")
    fun bindImageUri(view: ImageView, resource: Uri?) {
        if (resource != null) {
            if(resource != Uri.EMPTY){
                Glide.with(view.context)
                    .load(resource)
                    .apply(RequestOptions().centerCrop())
                    .into(view)
            }
            else {
                Glide.with(view.context)
                    .load(R.drawable.ic_launcher_background)
                    .apply(RequestOptions().centerInside())
                    .into(view)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("imageDrawableSrc")
    fun bindImageDrawableSource(view: ImageView, resource: Drawable) {
        if (resource != null) {
            view.setImageDrawable(resource)
        }
    }


//    @JvmStatic
//    @InverseBindingAdapter(attribute = "recycler_items",event = "recycler_itemsAttrChanged")
//    fun <T> getRecyclerViewAdapterItems(view:RecyclerView) : List<T>? {
//        val x = (view.adapter as BaseRecyclerAdapter<*,*>).items as List<T>?
//        return x
//    }


    @JvmStatic
    @BindingAdapter("recycler_adapter", "recycler_items")
    fun <T> setRecyclerViewAdapterItems(
        view: RecyclerView,
        @NonNull adapter: BaseRecyclerAdapter<T, *>,
        @Nullable items: ObservableField<List<T>>?
    ) {
        Objects.requireNonNull(adapter)
        if (view.adapter == null) {
            view.adapter = adapter
            Timber.w(
                "%s has no adapter attached so the supplied adapter was added.",
                view.javaClass.simpleName
            )
        }
        if (items?.get() == null || items.get()?.isEmpty()!!) {

            adapter.setData(ArrayList())
            Timber.w("Only cleared adapter because items is null")
            return
        }

        adapter.setData(items.get()!!)
        Timber.i("list_adapter_items added %s.", items.toString())
    }

    @JvmStatic
    @BindingAdapter("imageURLWithDrawable", "imageWithDrawable")
    fun bindImageURLWithDrawable(
        view: ImageView, url: String?,
        drawable: Drawable?
    ) {
        if (url != null && !url.contentEquals("-1")) {
            Glide.with(view.context)
                .load(url)
                .apply(
                    RequestOptions()
                        .centerInside()
                )
                .into(view)
        } else if (url != null && url.contentEquals("-1")) {
            Glide.with(view.context)
                .load(drawable)
                .into(view)
        } else {
            Glide.with(view.context)
                .load("")
                .apply(
                    RequestOptions()
                        .centerInside()
                )
                .into(view)
        }
    }


    @JvmStatic
    @BindingAdapter("onTextChangeListener")
    fun setOnTextChangeListener(editText: EditText?, action: Action1<String>?) {
        if (editText != null && action != null) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    action.call(charSequence.toString())
                }

                override fun afterTextChanged(editable: Editable) {}
            })
        }
    }

    @JvmStatic
    @BindingAdapter("onTogglePassword")
    fun setOnTogglePassword(editText: EditText?, shouldShow: Boolean) {
        if (editText != null) {
            if (shouldShow)
                editText.transformationMethod = null
            else
                editText.transformationMethod = PasswordTransformationMethod()
        }
    }

    @JvmStatic
    @BindingAdapter("onSearchClick")
    fun setOnEditorActionListener(textView: TextView?, action: Action1<String>?) {
        if (textView != null && action != null) {
            textView.setOnEditorActionListener { mTextView, i, keyEvent ->
                action.call(mTextView.text.toString())
                true
            }
        }
    }

    @JvmStatic
    @BindingAdapter("onDoneClick")
    fun setOnEditorDoneActionListener(editText: EditText?, action: Action1<String>?) {
        if (editText != null && action != null) {
            editText.setOnEditorActionListener { mTextView, i, keyEvent ->
                action.call(mTextView.text.toString())
                true
            }
        }
    }

    @JvmStatic
    @BindingAdapter("disableSeekBar")
    fun setSeekBarStatus(seekBar: SeekBar?, disable: Boolean?) {
        if (seekBar != null) {
            seekBar.isEnabled = !disable!!
        }
    }


    @JvmStatic
    @BindingAdapter("setHexTextColor")
    fun setTextHexColor(textView: TextView?, hexColor: String) {
        if (hexColor.isNotEmpty())
            textView?.setTextColor(Color.parseColor(hexColor))
        else
            textView?.setTextColor(Color.parseColor("#ffffff"))
    }

}

