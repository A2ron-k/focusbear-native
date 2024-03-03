package com.example.focusbear

import androidx.fragment.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType

class WelcomeActivity : AppIntro2() {
    private lateinit var manager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manager = PreferencesManager(this)

        isColorTransitionsEnabled = true

//        if (manager.isFirstRun()) {
//            manager.setFirstRun()
            addSlide(AppIntroFragment.createInstance(
                title = "Welcome to FocusBear!",
                description = "This is our home page, where you will be hatching our dear pets and items! For starters, when you focus for more than 15 minutes, you will get a reward! Of course, after a period of time, this time constraint will be adjusted based on your focus habits!",
                imageDrawable = R.drawable.cat,
                titleColorRes = R.color.black,
                descriptionColorRes = R.color.black,
                backgroundColorRes = R.color.pastel_yellow,
            ))
            addSlide(AppIntroFragment.createInstance(
                title = "Desk",
                description = "Here you can decorate your desk with all your rewards that you have bought/earned!",
                titleColorRes = R.color.black,
                descriptionColorRes = R.color.black,
                backgroundColorRes = R.color.pastel_blue,
            ))
            addSlide(AppIntroFragment.createInstance(
                title = "Shop",
                description = "Here you can buy all our different items to decorate your desk!",
                titleColorRes = R.color.black,
                descriptionColorRes = R.color.black,
                backgroundColorRes = R.color.pastel_pink,
            ))
            addSlide(AppIntroFragment.createInstance(
                title = "Profile",
                description = "Here you can find all your past focus sessions, along with statistics on how much you have focused on!",
                titleColorRes = R.color.black,
                descriptionColorRes = R.color.black,
                backgroundColorRes = R.color.pastel_peach,
            ))
//        } else {
//            goToMain()
//        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        goToMain()
    }

     override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        goToMain()
    }

     override fun onSlideChanged(oldFragment: Fragment?, newFragment: Fragment?) {
        super.onSlideChanged(oldFragment, newFragment)
        Log.d("Hello", "Changed")
    }


}