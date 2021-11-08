package com.example.calculator.presentation.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculator.domain.SettingsDao
import com.example.calculator.domain.entity.ForceVibrationTypeEnum
import com.example.calculator.domain.entity.FormatResultTypeEnum
import com.example.calculator.domain.entity.ResultOutputPanelType
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsDao: SettingsDao) : ViewModel() {

    private val _resultPanelState = MutableLiveData<ResultOutputPanelType>(ResultOutputPanelType.LEFT)
    val resultPanelState: LiveData<ResultOutputPanelType> = _resultPanelState

    private val _openResultPanelAction = SingleLiveEvent<ResultOutputPanelType>()
    val openResultPanelAction: LiveData<ResultOutputPanelType> = _openResultPanelAction

    private val _formatResultState =
        MutableLiveData<FormatResultTypeEnum>(FormatResultTypeEnum.MANY)
    val formatResultState: LiveData<FormatResultTypeEnum> = _formatResultState

    private val _openFormatResultAction = SingleLiveEvent<FormatResultTypeEnum>()
    val openFormatResultAction: LiveData<FormatResultTypeEnum> = _openFormatResultAction


    fun onFormatResultChanged(formatResultType: FormatResultTypeEnum) {
        _formatResultState.value = formatResultType
        viewModelScope.launch {
            settingsDao.setFormatResultType(formatResultType)
        }
    }

    fun onFormatResultPanelTypeClicked() {
        _openFormatResultAction.value = _formatResultState.value
    }


    init {
        viewModelScope.launch {
            _resultPanelState.value = settingsDao.getResultPanelType()
            _formatResultState.value = settingsDao.getFormatResultType()
        }
    }

    fun onResultPanelTypeChanged(resultPanelType: ResultOutputPanelType) {
        _resultPanelState.value = resultPanelType
        viewModelScope.launch {
            settingsDao.setResultPanelType(resultPanelType)
        }
    }

    fun onResultPanelTypeClicked() {
        _openResultPanelAction.value = _resultPanelState.value
    }


}


