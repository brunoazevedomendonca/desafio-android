package com.picpay.desafio.android.presentation.common

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.picpay.desafio.android.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

@BindingAdapter("app:showIfLoadingState")
fun showIfLoadingState(view: View, screenState: ScreenState) {
    view.visibility = if(screenState == ScreenState.LOADING) View.VISIBLE else View.GONE
}

@BindingAdapter("app:showIfSuccessState")
fun showIfSuccessState(view: View, screenState: ScreenState) {
    view.visibility = if(screenState == ScreenState.SUCCESS) View.VISIBLE else View.GONE
}

@BindingAdapter("app:showIfErrorState")
fun showIfErrorState(view: View, screenState: ScreenState) {
    view.visibility = if(screenState == ScreenState.ERROR) View.VISIBLE else View.GONE
}

@BindingAdapter(value = ["app:imageUrl", "app:progressBar"], requireAll = true)
fun showNetworkImage(circleImageView: CircleImageView, url: String, progressBar: ProgressBar) {
    progressBar.visibility = View.VISIBLE
    Picasso.get()
        .load(url)
        .error(R.drawable.ic_round_account_circle)
        .into(circleImageView, object : Callback {
            override fun onSuccess() {
                progressBar.visibility = View.GONE
            }

            override fun onError(e: Exception?) {
                progressBar.visibility = View.GONE
            }
        })
}