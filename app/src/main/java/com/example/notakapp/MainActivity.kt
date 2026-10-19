package com.example.notakapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notakapp.ui.theme.NotakAPPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotakAPPTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NotakKalkulatu(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun NotakKalkulatu(modifier: Modifier = Modifier) {
    // 7 irakasgaien zerrenda
    val irakasgaiak = listOf("PMDM", "DI", "SIS", "EIE", "AD", "H1", "H2")

    // Bi dimentsiotako array-a egoera gisa gorde (7 irakasgai x 2 nota)
    var notakArray by remember { mutableStateOf(Array(7) { Array(2) { "" } }) }

    // Mediana/Batazbesteko emaitza gordetzeko egoera
    var medianaResult by remember { mutableStateOf("Batazbesteko Nota: -") }

    Box(modifier = modifier.fillMaxSize()) {
        // Fondo irudia (ziurtatu res/drawable/fondo.png duzula, bestela aldatu izena)
        Image(
            painter = painterResource(id = R.drawable.fondoa),
            contentDescription = "Fondoa",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Izenburua
            Text(
                text = "PAG2: Notak",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Irakasgai bakoitzeko eremuak sortu
            irakasgaiak.forEachIndexed { rowIndex, subject ->
                val nota1Str = notakArray[rowIndex][0]
                val nota2Str = notakArray[rowIndex][1]

                // Irakasgai bakoitzaren batazbestekoa kalkulatu (ikusteko)
                val n1 = nota1Str.toDoubleOrNull()
                val n2 = nota2Str.toDoubleOrNull()
                val averageSubjectStr = if (n1 != null && n2 != null) {
                    String.format("%.2f", (n1 + n2) / 2.0)
                } else {
                    "-"
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Irakasgaiaren izena
                    Text(
                        text = subject,
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.SemiBold
                    )

                    // Nota 1 TextField
                    OutlinedTextField(
                        value = nota1Str,
                        onValueChange = { newValue ->
                            val updated = notakArray.map { it.clone() }.toTypedArray()
                            updated[rowIndex][0] = newValue
                            notakArray = updated
                        },
                        label = { Text("Nota 1") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1.2f)
                            .padding(horizontal = 2.dp),
                        singleLine = true
                    )

                    // Nota 2 TextField
                    OutlinedTextField(
                        value = nota2Str,
                        onValueChange = { newValue ->
                            val updated = notakArray.map { it.clone() }.toTypedArray()
                            updated[rowIndex][1] = newValue
                            notakArray = updated
                        },
                        label = { Text("Nota 2") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1.2f)
                            .padding(horizontal = 2.dp),
                        singleLine = true
                    )

                    // Irakasgaiaren batazbesteko nota
                    Text(
                        text = averageSubjectStr,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Mediana Kalkulatu Botoia
            Button(
                onClick = {
                    val allNotes = mutableListOf<Double>()
                    for (row in notakArray) {
                        for (value in row) {
                            value.toDoubleOrNull()?.let { allNotes.add(it) }
                        }
                    }

                    // Kendu notarik baxuena eta altuena, ondoren promedioa kalkulatu
                    if (allNotes.size > 2) {
                        allNotes.sort()
                        val trimmedNotes = allNotes.subList(1, allNotes.size - 1)
                        val result = trimmedNotes.average()
                        medianaResult = "Batazbesteko Nota: ${String.format("%.2f", result)}"
                    } else if (allNotes.isNotEmpty()) {
                        val result = allNotes.average()
                        medianaResult = "Batazbesteko Nota: ${String.format("%.2f", result)}"
                    } else {
                        medianaResult = "Batazbesteko Nota: -"
                    }
                },
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text(text = "Mediana Kalkulatu")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Emaitzaren Text
            Text(
                text = medianaResult,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NotakAPPTheme {
        NotakKalkulatu()
    }
}