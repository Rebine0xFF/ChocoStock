package com.rebine.chocostock.presentation.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rebine.chocostock.presentation.common.DateFormatUtils
import com.rebine.chocostock.domain.ChocolateValidator

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
    var showDatePicker by remember { mutableStateOf(false) }

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

        Box {
            OutlinedTextField(
                value = DateFormatUtils.formatForEditField(expiryDateIso.ifBlank { null }),
                onValueChange = {},
                readOnly = true,
                label = { Text("Date limite") },
                trailingIcon = {
                    Icon(Icons.Default.DateRange, contentDescription = "Choisir une date")
                },
                modifier = Modifier.fillMaxWidth()
            )
            // Capte les clics sur toute la zone du champ pour ouvrir le sélecteur
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { showDatePicker = true }
            )
        }

        if (showDatePicker) {
            val initialMillis = DateFormatUtils.parseIsoOrNull(expiryDateIso.ifBlank { null })
                ?.atStartOfDay(java.time.ZoneOffset.UTC)
                ?.toInstant()
                ?.toEpochMilli()
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            expiryDateIso = java.time.Instant.ofEpochMilli(millis)
                                .atZone(java.time.ZoneOffset.UTC)
                                .toLocalDate()
                                .toString() // format ISO AAAA-MM-JJ, garanti valide
                        }
                        showDatePicker = false
                    }) { Text("Valider") }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) { Text("Annuler") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                Text("Annuler")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { viewModel.save(ChocolateValidator.sanitizeTitle(title), expiryDateIso.ifBlank { null }, onSaved) },
                enabled = ChocolateValidator.isTitleValid(title),
                modifier = Modifier.weight(1f)
            ) {
                Text("Enregistrer")
            }
        }
    }
}