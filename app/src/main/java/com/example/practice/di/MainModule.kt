package com.example.practice.di

import com.example.practice.Movies
import com.example.practice.navigation.Route
import com.example.practice.navigation.TopLevelBackStack
import org.koin.dsl.module

val mainModule = module {
    single { TopLevelBackStack<Route>(Movies) }
}