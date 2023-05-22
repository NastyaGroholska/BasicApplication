package com.shpp.ahrokholska.basicapplication.utils.ext

import android.widget.ImageView
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.shpp.ahrokholska.basicapplication.R
import com.squareup.picasso.Picasso
import com.bumptech.glide.request.target.Target

private enum class ImageLoader {
    Glide, Picasso, Coil
}

private val currentLoader = ImageLoader.Glide

fun ImageView.loadFromURL(picture: String) {
    when (currentLoader) {
        ImageLoader.Glide -> Glide.with(this).load(picture)
            .apply(
                RequestOptions.placeholderOf(R.mipmap.ic_unknown_user)
                    .override(Target.SIZE_ORIGINAL).dontTransform()
            ).into(this)

        ImageLoader.Picasso -> Picasso.get().load(picture).fit().centerCrop().into(this)
        ImageLoader.Coil -> load(picture) {
            transformations(CircleCropTransformation())
            scale(Scale.FILL)
        }
    }
}