package com.example.singleinstance

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.singleinstance.databinding.ActivityAnimatorBinding
import kotlinx.android.synthetic.main.activity_animator.*


class AnimatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animLayout = ActivityAnimatorBinding.inflate(layoutInflater)
        setContentView(animLayout.root)
        animLayout.tvAnimatorObject.setOnClickListener {
            Toast.makeText(this, "点击了文字", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 位移动画
     */
    fun clickTranslation(view: View) {
        val animator =
            ObjectAnimator.ofFloat(tv_animator_object, "translationX", 0f, 300f, -300f, 0f)
        animator.start()
        animator.duration = 2000
        animator.start()
    }

    /**
     * 旋转动画
     */
    fun clickRotation(view: View) {
        val rotation =
            ObjectAnimator.ofFloat(tv_animator_object, "rotation", 0f, 90f, 180f, 270f, 360f)
        rotation.duration = 2000
        rotation.start()
    }

    /**
     * 缩放动画
     */
    fun clickScaleX(view: View) {
        val scaleX = ObjectAnimator.ofFloat(tv_animator_object, "scaleX", 1f, 2.0f, 3.0f)
        scaleX.duration = 2000
        scaleX.start()
    }

    /**
     * 渐变动画
     */
    fun clickAlpha(view: View) {
        val alpha =
            ObjectAnimator.ofFloat(tv_animator_object, "alpha", 1.0f, 0.5f, 0.1f, 0.5f, 1.0f)
        alpha.duration = 3000
        alpha.start()
    }

    /**
     * 一起来的组合动画
     */
    fun together(view: View) {
        val animator =
            ObjectAnimator.ofFloat(tv_animator_object, "translationX", 0f, 300f, -300f, 0f)
        val rotation =
            ObjectAnimator.ofFloat(tv_animator_object, "rotation", 0f, 90f, 180f, 270f, 360f)
        val scaleX = ObjectAnimator.ofFloat(tv_animator_object, "scaleX", 1f, 2.0f, 3.0f)
        val scaleY = ObjectAnimator.ofFloat(tv_animator_object, "scaleY", 1f, 2.0f, 3.0f)
        val alpha =
            ObjectAnimator.ofFloat(tv_animator_object, "alpha", 1.0f, 0.5f, 0.1f, 0.5f, 1.0f)
        //组合动画
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animator, rotation, scaleX, scaleY, alpha)
        animatorSet.duration = 3000
        animatorSet.start()

    }
}