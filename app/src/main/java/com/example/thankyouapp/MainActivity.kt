package com.example.thankyouapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Random

class MainActivity : AppCompatActivity() {
    
    private lateinit var thankYouTextView: TextView
    private lateinit var versionTextView: TextView
    private val random = Random()
    private val colors = arrayOf(
        Color.parseColor("#3F51B5"), // Indigo
        Color.parseColor("#4CAF50"), // Green
        Color.parseColor("#FF9800"), // Orange
        Color.parseColor("#9C27B0"), // Purple
        Color.parseColor("#F44336")  // Red
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // تعريف العناصر
        thankYouTextView = findViewById(R.id.thankYouTextView)
        versionTextView = findViewById(R.id.versionTextView)
        
        // تطبيق رسوم متحركة على النص عند بدء التطبيق
        animateThankYouText()
        
        // تعيين المستمع للنقر على النص
        thankYouTextView.setOnClickListener {
            playTapAnimation()
            showRandomToast()
        }
        
        // تعيين المستمع للنقر الطويل على النص
        thankYouTextView.setOnLongClickListener {
            changeTextColor()
            true
        }
    }
    
    /**
     * تطبيق رسوم متحركة على نص الشكر
     */
    private fun animateThankYouText() {
        // تحريك النص من أسفل الشاشة
        thankYouTextView.translationY = 1000f
        thankYouTextView.alpha = 0f
        
        // رسوم متحركة للظهور والتكبير
        val translateAnimator = ObjectAnimator.ofFloat(thankYouTextView, View.TRANSLATION_Y, 0f).apply {
            duration = 1000
            interpolator = OvershootInterpolator(0.7f)
        }
        
        val fadeAnimator = ObjectAnimator.ofFloat(thankYouTextView, View.ALPHA, 1f).apply {
            duration = 800
        }
        
        // رسوم متحركة للتكبير
        val scaleXAnimator = ObjectAnimator.ofFloat(thankYouTextView, View.SCALE_X, 0.5f, 1.0f).apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
        }
        
        val scaleYAnimator = ObjectAnimator.ofFloat(thankYouTextView, View.SCALE_Y, 0.5f, 1.0f).apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
        }
        
        // تطبيق مجموعة الرسوم المتحركة
        AnimatorSet().apply {
            playTogether(translateAnimator, fadeAnimator, scaleXAnimator, scaleYAnimator)
            start()
            
            // إظهار نص الإصدار بعد انتهاء الرسوم المتحركة
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    versionTextView.visibility = View.VISIBLE
                    versionTextView.alpha = 0f
                    ObjectAnimator.ofFloat(versionTextView, View.ALPHA, 1f).apply {
                        duration = 500
                        start()
                    }
                }
            })
        }
    }
    
    /**
     * تغيير لون النص بشكل عشوائي
     */
    private fun changeTextColor() {
        val randomColor = colors[random.nextInt(colors.size)]
        
        ObjectAnimator.ofArgb(
            thankYouTextView,
            "textColor",
            (thankYouTextView.currentTextColor),
            randomColor
        ).apply {
            duration = 500
            start()
        }
        
        // عرض رسالة منبثقة عن تغيير اللون
        Toast.makeText(this, "تم تغيير اللون", Toast.LENGTH_SHORT).show()
    }
    
    /**
     * تطبيق رسوم متحركة عند النقر على النص
     */
    private fun playTapAnimation() {
        val scaleDown = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(thankYouTextView, View.SCALE_X, 0.9f),
                ObjectAnimator.ofFloat(thankYouTextView, View.SCALE_Y, 0.9f)
            )
            duration = 100
        }
        
        val scaleUp = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(thankYouTextView, View.SCALE_X, 1.0f),
                ObjectAnimator.ofFloat(thankYouTextView, View.SCALE_Y, 1.0f)
            )
            duration = 100
        }
        
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(scaleDown, scaleUp)
        animatorSet.start()
    }
    
    /**
     * عرض رسالة منبثقة عشوائية
     */
    private fun showRandomToast() {
        val messages = arrayOf(
            "شكرا لك!",
            "نقدر دعمكم!",
            "أنتم الأفضل!",
            "شكرا جزيلا!",
            "نشكركم على الثقة!"
        )
        
        val randomMessage = messages[random.nextInt(messages.size)]
        Toast.makeText(this, randomMessage, Toast.LENGTH_SHORT).show()
    }
}
