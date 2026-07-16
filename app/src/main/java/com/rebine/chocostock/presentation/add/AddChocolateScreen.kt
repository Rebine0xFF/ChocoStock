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
            viewModel.analyzePhotos(file.let { coverPhoto!! }, file)
        }

        AddStep.REVIEW -> ReviewStep(
            viewModel = viewModel,
            coverPhoto = coverPhoto,
            expiryPhoto = expiryPhoto,
            onConfirm = { title, expiryDateIso ->
                viewModel.saveChocolate(title, expiryDateIso, coverPhoto, expiryPhoto, onSaved)
            },
            onCancel = onCancel
        )
    }
}

@Composable
private fun ReviewStep(
    viewModel: AddChocolateViewModel,
    coverPhoto: File?,
    expiryPhoto: File?,
    onConfirm: (String, String?) -> Unit,
    onCancel: () -> Unit
) {
    val analysisState by viewModel.analysisState.collectAsState()

    var title by remember { mutableStateOf("") }
    var expiryDateIso by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(analysisState) {
        if (analysisState is AnalysisState.Success) {
            val success = analysisState as AnalysisState.Success
            title = success.title
            expiryDateIso = success.expiryDateIso
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Vérif et corrige si besoin", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = rememberAsyncImagePainter(coverPhoto),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().height(180.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (analysisState) {
            is AnalysisState.Loading -> {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Analyse en cours...")
                }
            }
            is AnalysisState.Error -> {
                Text(
                    "L'analyse automatique a échoué, renseigne les infos manuellement.",
                    color = MaterialTheme.colorScheme.error
                )
            }
            else -> {}
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Titre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = expiryDateIso ?: "",
            onValueChange = { expiryDateIso = it.ifBlank { null } },
            label = { Text("Date de péremption (AAAA-MM-JJ)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                Text("Annuler")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { onConfirm(title, expiryDateIso) },
                enabled = title.isNotBlank(),
                modifier = Modifier.weight(1f)
            ) {
                Text("Ajouter au stock")
            }
        }
    }
}