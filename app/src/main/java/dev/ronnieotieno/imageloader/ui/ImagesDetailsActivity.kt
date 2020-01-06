package dev.ronnieotieno.imageloader.ui

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.animation.AnticipateOvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import dev.ronnieotieno.imageloader.R
import dev.ronnieotieno.imageloaderlibrary.ImageLoader
import kotlinx.android.synthetic.main.activity_detail.*


class ImagesDetailsActivity : AppCompatActivity() {


    private var isDetailLayout = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setIntent()

        constraintLayout.setOnClickListener {
            isDetailLayout = if (isDetailLayout) {
                swapFrames(R.layout.activity_detail)
                false
            } else {
                swapFrames(R.layout.activity_details_expanded)
                true
            }

        }

    }

    private fun setIntent() {
        val intent = intent
        val imageUrl = intent.getStringExtra("image")
        ImageLoader(this).load(imageUrl, imvBackground)

        txvTitle.text = intent.getStringExtra("Creator")
        txvDescription.text = intent.getStringExtra("likes")
    }


    private fun swapFrames(layoutId: Int) {

        val constraintSet = ConstraintSet()
        constraintSet.clone(this, layoutId)

        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 1200

        TransitionManager.beginDelayedTransition(constraintLayout, transition)
        constraintSet.applyTo(constraintLayout)

        isDetailLayout = !isDetailLayout
    }
}
