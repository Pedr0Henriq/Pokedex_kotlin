package com.example.testekotlin.sampledata

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class PreviewP : PreviewParameterProvider<Int>{
    override val values: Sequence<Int>
        get() = sequenceOf(1,2,3)
}