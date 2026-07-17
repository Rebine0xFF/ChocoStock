package com.rebine.chocostock.presentation.add

import androidx.compose.runtime.*
import com.rebine.chocostock.presentation.camera.CameraCaptureScreen
import java.io.File

private enum class AddStep { COVER_PHOTO, EXPIRY_PHOTO }

@Composable
fun AddChocolateScreen(
    viewModel: AddChocolateViewModel,
    onSaved: () -> Unit
) {
    var step by remember { mutableStateOf(AddStep.COVER_PHOTO) }
    var coverPhoto by remember { mutableStateOf<File?>(null) }

    when (step) {
        AddStep.COVER_PHOTO -> CameraCaptureScreen(
            instructionText = "Photo du devant de l'emballage"
        ) { file ->
            coverPhoto = file
            step = AddStep.EXPIRY_PHOTO
        }

        AddStep.EXPIRY_PHOTO -> CameraCaptureScreen(
            instructionText = "Zoom sur la date limite"
        ) { file ->
            viewModel.createChocolateAndAnalyze(coverPhoto!!, file)
            onSaved() // immediate return to the list, analysis continues in the background
        }
    }
}