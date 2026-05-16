package com.example.quiz_app_wvssim

import android.app.Application
import com.example.quiz_app_wvssim.di.AppContainer

class QuizApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}

