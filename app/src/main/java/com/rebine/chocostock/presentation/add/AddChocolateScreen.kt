package com.rebine.chocostock.presentation.add

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.rebine.chocostock.presentation.camera.CameraCaptureScreen
import java.io.File

private enum class AddStep { COVER_PHOTO, EXPIRY_PHOTO, REVIEW }

@Composable
fun AddChocolateScreen(
    viewModel: AddChocolateViewModel,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
    var step by remember { mutableStateOf(AddStep.COVER_PHOTO) }
    var coverPhoto by remember { mutableStateOf<File?>(null) }
    var expiryPhoto by remember { mutableStateOf<File?>(null) }

    when (step) {
        AddStep.COVER_PHOTO -> CameraCaptureScreen(
            instructionText = "Photo du devant de l'emballage"
        ) { file ->
            coverPhoto = file
            step = AddStep.EXPIRY_PHOTO
        }

        AddStep.EXPIRY_PHOTO -> CameraCaptureScreen(
            instructionText = "Zoom sur la date de péremption"
        ) { file ->
            expiryPhoto = file
            step = AddStep.REVIEW
        }

        AddStep.REVIEW -> ReviewStep(
            coverPhoto = coverPhoto,
            expiryPhoto = expiryPhoto,
            onConfirm = { 
                viewModel.saveChocolate(coverPhoto, expiryPhoto, onSaved)
            },
            onCancel = onCancel
        )
    }
}

@Composable
private fun ReviewStep(
    coverPhoto: File?,
    expiryPhoto: File?,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Vérifie tes photos", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Devant de l'emballage")
        Image(
            painter = rememberAsyncImagePainter(coverPhoto),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().height(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Date de péremption")
        Image(
            painter = rememberAsyncImagePainter(expiryPhoto),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().height(150.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                Text("Annuler")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onConfirm, modifier = Modifier.weight(1f)) {
                Text("Ajouter au stock")
            }
        }
    }
}