package com.rebine.chocostock.presentation.edit

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EditChocolateScreen(
    chocolateId: String,
    viewModel: EditChocolateViewModel,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
    val chocolate by viewModel.chocolate.collectAsState()

    LaunchedEffect(chocolateId) {
        viewModel.load(chocolateId)
    }

    var title by remember { mutableStateOf("") }
    var expiryDateIso by remember { mutableStateOf("") }

    LaunchedEffect(chocolate) {
        chocolate?.let {
            title = it.title
            expiryDateIso = it.expiryDateIso ?: ""
        }
    }

    if (chocolate == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Modifier le chocolat", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Titre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = expiryDateIso,
            onValueChange = { expiryDateIso = it },
            label = { Text("Date limite (AAAA-MM-JJ)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                Text("Annuler")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { viewModel.save(title, expiryDateIso.ifBlank { null }, onSaved) },
                enabled = title.isNotBlank(),
                modifier = Modifier.weight(1f)
            ) {
                Text("Enregistrer")
            }
        }
    }
}