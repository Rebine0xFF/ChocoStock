package com.rebine.chocostock.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val storedKey by viewModel.apiKey.collectAsState()
    var input by remember { mutableStateOf("") }
    var saved by remember { mutableStateOf(false) }

    LaunchedEffect(storedKey) {
        storedKey?.let { input = it }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Réglages", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "ChocoStock utilise l'API Gemini pour identifier tes chocolats rapidement. " +
                    "Crée une clé gratuite sur aistudio.google.com/apikey et colle-la ci-dessous. " +
                    "Elle reste stockée uniquement sur ton téléphone.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = input,
            onValueChange = { input = it; saved = false },
            label = { Text("Clé API Gemini") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { viewModel.saveApiKey(input); saved = true },
            enabled = input.isNotBlank()
        ) {
            Text("Enregistrer")
        }

        if (saved) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Clé enregistrée.", color = MaterialTheme.colorScheme.tertiary)
        }
    }
}