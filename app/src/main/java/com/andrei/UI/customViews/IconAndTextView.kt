package com.andrei.UI.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.carrental.R
import com.andrei.carrental.databinding.IconWithTextLayoutBinding
import timber.log.Timber

class IconAndTextView : FrameLayout {

    private val binding:IconWithTextLayoutBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    constructor(context:Context) : super(context){
        initViews(null)
    }
    constructor(context: Context,attributeSet: AttributeSet?) : super(context,attributeSet){
        initViews(attributeSet)
    }
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr:Int):
            super(context,attributeSet,defStyleAttr){
        initViews(attributeSet)
    }

    private fun initViews(attrs:AttributeSet?){
        val attrsArray = context.theme.obtainStyledAttributes(attrs,R.styleable.IconAndTextView, 0 ,0)
        try{
            val iconImage = attrsArray.getResourceId(R.styleable.IconAndTextView_iconImage,0)
            binding.ivIcon.setImageResource(iconImage)
        } finally {
            attrsArray.recycle()
        }
    }


    fun setIconText(text:String){
        binding.tvText.text = text
    }

}