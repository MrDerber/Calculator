package com.example.calculator.domain

import com.example.calculator.domain.entity.ForceVibrationTypeEnum
import com.example.calculator.domain.entity.FormatResultTypeEnum
import com.example.calculator.domain.entity.ResultOutputPanelType

interface SettingsDao {

    suspend fun getResultPanelType(): ResultOutputPanelType
    suspend fun setResultPanelType(resultPanelType: ResultOutputPanelType)

    suspend fun getFormatResultType(): FormatResultTypeEnum
    suspend fun setFormatResultType(formatResultType: FormatResultTypeEnum)

    suspend fun getForceVibrationType(): ForceVibrationTypeEnum
    suspend fun setForceVibrationType(forceVibrationType: ForceVibrationTypeEnum)

}

