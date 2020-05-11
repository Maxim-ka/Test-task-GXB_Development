package com.reschikov.gxbdevelopment.testtask.di

import com.reschikov.gxbdevelopment.testtask.calculation.Calculator
import com.reschikov.gxbdevelopment.testtask.network.Request
import com.reschikov.gxbdevelopment.testtask.parser.ExpressionParsing
import com.reschikov.gxbdevelopment.testtask.parser.ModuleParser
import com.reschikov.gxbdevelopment.testtask.ui.Calculable
import com.reschikov.gxbdevelopment.testtask.ui.Computationable
import com.reschikov.gxbdevelopment.testtask.ui.Derivable
import com.reschikov.gxbdevelopment.testtask.ui.MainModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module


val appModule = module {
    factory<Calculable> { Calculator(ModuleParser(ExpressionParsing())) }
}

val viewModelModule = module {
    viewModel { MainModel(get(), get()) } bind Computationable::class
}

val netModule = module {
    factory<Derivable> { Request() }
}