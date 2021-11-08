package com.example.calculator.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculator.data.calculateExpressions
import com.example.calculator.domain.HistoryRepository
import com.example.calculator.domain.SettingsDao
import com.example.calculator.domain.entity.FormatResultTypeEnum
import com.example.calculator.domain.entity.HistoryItem
import com.example.calculator.domain.entity.ResultOutputPanelType
import kotlinx.coroutines.launch

class MainViewModel(
    private val settingsDao: SettingsDao,
    private val historyRepository: HistoryRepository
) : ViewModel() {
    override fun onCleared() {
        super.onCleared()
    }

    private var expression: String = ""

    private val _expressionState = MutableLiveData<ExpressionState>(ExpressionState(expression))
    val expressionState: LiveData<ExpressionState> = _expressionState

    private val _resultState = MutableLiveData<String>()
    val resultState: LiveData<String> = _resultState

    private val _resultPanelState = MutableLiveData<ResultOutputPanelType>(ResultOutputPanelType.LEFT)
    val resultPanelState: LiveData<ResultOutputPanelType> = _resultPanelState

    private val _formatResultState =
        MutableLiveData<FormatResultTypeEnum>(FormatResultTypeEnum.MANY)
    val formatResultState: LiveData<FormatResultTypeEnum> = _formatResultState


    init {
        viewModelScope.launch {
            _resultPanelState.value = settingsDao.getResultPanelType()
            _formatResultState.value = settingsDao.getFormatResultType()
        }
    }

    fun onDigitalButtonClick(number: Int) {
        expression += number.toString()
        _expressionState.value = ExpressionState(expression)
    }

    fun onOperationClick(operators: Operators) {
        expression += operators.symbol
        _expressionState.value = ExpressionState(expression)
    }

    fun onBackButtonClick() {
        expression = expression.dropLast(1)
        _expressionState.value = ExpressionState(expression)
    }

    fun onEqualsButtonClick() {
        val result = calculateExpressions(expression, _formatResultState.value)
        _resultState.value = result
        viewModelScope.launch {
            historyRepository.add(HistoryItem(expression, result))
        }
        _expressionState.value = ExpressionState(result)
        expression = result
    }

    fun onClearButtonClick() {
        expression = ""
        _expressionState.value = ExpressionState(expression)
    }

    fun resultUpdate() {
        val result = calculateExpressions(expression, _formatResultState.value)
        _resultState.value = result
    }

    fun onStart() {
        viewModelScope.launch {
            _resultPanelState.value = settingsDao.getResultPanelType()
            _formatResultState.value = settingsDao.getFormatResultType()
            resultUpdate()
        }

    }

    fun onHistoryResult(item: HistoryItem?) {
        if (item != null) {
            expression = item.exp
            _expressionState.value = ExpressionState(expression)
            _resultState.value = item.result
        }
    }


}

enum class Operators(var symbol: String) {
    MINUS("-"), PLUS("+"), MULTIPLY("*"), DIVIDE("/"), SQRT("√"), DEGREE("^")
}

class ExpressionState(val expression: String)

