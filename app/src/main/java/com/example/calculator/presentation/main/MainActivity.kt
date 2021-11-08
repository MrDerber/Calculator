package com.example.calculator.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.calculator.R
import com.example.calculator.databinding.MainActivityBinding
import com.example.calculator.di.HistoryRepositoryProvider
import com.example.calculator.di.SettingsDaoProvider
import com.example.calculator.domain.entity.ForceVibrationTypeEnum.*
import com.example.calculator.domain.entity.ResultOutputPanelType.*
import com.example.calculator.presentation.common.BaseActivity
import com.example.calculator.presentation.history.Result
import com.example.calculator.presentation.main.Operators.*
import com.example.calculator.presentation.settings.SettingsActivity


class MainActivity : BaseActivity() {

    private val viewModel by viewModels<MainViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainViewModel(
                    SettingsDaoProvider.getDao(this@MainActivity),
                    HistoryRepositoryProvider.get(this@MainActivity)
                ) as T
            }
        }
    }
    private val viewBinding by viewBinding(MainActivityBinding::bind)

    private val resultLauncher = registerForActivityResult(Result()) { item ->
        viewModel.onHistoryResult(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        viewBinding.settings.setOnClickListener() { openSettings() }
        viewBinding.history.setOnClickListener() { openHistory() }

        viewBinding.mainInput.apply { showSoftInputOnFocus = false }
        viewBinding.mainEquals.setOnClickListener {}

        listOf(
            viewBinding.mainZero,
            viewBinding.mainOne,
            viewBinding.mainTwo,
            viewBinding.mainThree,
            viewBinding.mainFour,
            viewBinding.mainFive,
            viewBinding.mainSix,
            viewBinding.mainSeven,
            viewBinding.mainEight,
            viewBinding.mainNine,
        ).forEachIndexed { index, textView ->
            textView.setOnClickListener {
                viewModel.onDigitalButtonClick(index)
            }
        }

        viewBinding.mainBack.setOnClickListener { viewModel.onBackButtonClick() }
        viewBinding.mainClear.setOnClickListener { viewModel.onClearButtonClick() }


        viewBinding.mainPlus.setOnClickListener {
            viewModel.onOperationClick(PLUS)
        }

        viewBinding.mainMinus.setOnClickListener {
            viewModel.onOperationClick(MINUS)
        }

        viewBinding.mainDivision.setOnClickListener {
            viewModel.onOperationClick(DIVIDE)
        }

        viewBinding.mainMultiply.setOnClickListener {
            viewModel.onOperationClick(MULTIPLY)
        }

        viewBinding.mainDegree.setOnClickListener {
            viewModel.onOperationClick(DEGREE)
        }

        viewBinding.mainEquals.setOnClickListener { viewModel.onEqualsButtonClick() }

        viewModel.expressionState.observe(this) { state ->
            viewBinding.mainInput.setText(state.expression)
        }

        viewModel.resultState.observe(this) { state ->
            viewBinding.mainResult.setText(state)
        }

        viewModel.resultPanelState.observe(this) {
            with(viewBinding.mainResult) {
                gravity = when (it) {
                    LEFT -> Gravity.START.or(Gravity.CENTER_VERTICAL)
                    RIGHT -> Gravity.END.or(Gravity.CENTER_VERTICAL)
                    HIDE -> gravity
                }
                isVisible = it != HIDE
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    private fun openSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun openHistory() {
        resultLauncher.launch()
    }
}



