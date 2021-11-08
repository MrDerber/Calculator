package com.example.calculator.presentation.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.calculator.domain.HistoryRepository
import com.example.calculator.domain.SettingsDao
import com.example.calculator.domain.entity.FormatResultTypeEnum
import com.example.calculator.domain.entity.HistoryItem
import com.example.calculator.domain.entity.ResultOutputPanelType
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {
    @get:Rule
    var instantExecutorRuler = InstantTaskExecutorRule()
    private val settingsDao: SettingsDao = SettingsDaoFake()
    private val historyDao: HistoryRepository = HistoryDaoFake()


    @Test
    fun testPlus() {
        val viewModel = MainViewModel(settingsDao, historyDao)
        viewModel.onDigitalButtonClick(2, 0)
        viewModel.onOperationClick(Operators.PLUS, 1)
        viewModel.onDigitalButtonClick(2, 2)

        Assert.assertEquals("2+2", viewModel.expressionState.value?.expression)
        Assert.assertEquals("4", viewModel.resultState.value)
    }

    @Test
    fun testDelete() {
        val viewModel = MainViewModel(settingsDao, historyDao)
        viewModel.onDigitalButtonClick(2, 0)
        viewModel.onDigitalButtonClick(2, 1)
        viewModel.onDigitalButtonClick(3, 2)

        viewModel.onBackButtonClick(2)
        Assert.assertEquals("22", viewModel.expressionState.value?.expression)
        Assert.assertEquals("22", viewModel.resultState.value)

    }

    class HistoryDaoFake : HistoryRepository {
        override suspend fun getAll(): List<HistoryItem> {
            return emptyList()
        }

        override suspend fun add(historyItem: HistoryItem) {
            TODO("Not yet implemented")
        }
    }

    class SettingsDaoFake : SettingsDao {
        private var resultPanelType: ResultOutputPanelType = ResultOutputPanelType.LEFT
        override suspend fun getResultPanelType(): ResultOutputPanelType {
            return resultPanelType
        }

        override suspend fun setResultPanelType(resultPanelType: ResultOutputPanelType) {
            this.resultPanelType = ResultOutputPanelType.LEFT
        }

        override suspend fun getFormatResultType(): FormatResultTypeEnum? {
            return FormatResultTypeEnum.MANY
        }
    }

}