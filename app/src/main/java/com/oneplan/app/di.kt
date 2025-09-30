package com.oneplan.app

import android.app.Application
import android.content.Context
import androidx.room.Room
import org.koin.androidx.compose.get
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun appModule(app: Application) = module {
    single {
        Room.databaseBuilder(app, OnePlanDb::class.java, "oneplan.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<OnePlanDb>().budget() }
    single { get<OnePlanDb>().meals() }
    single { Repos(app) }
}
