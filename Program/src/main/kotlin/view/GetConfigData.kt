package view

import Config
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun getConfigData(changeScreen: () -> Unit) {
    var amountCats by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var width by remember { mutableStateOf("") }
    var r0 by remember { mutableStateOf("") }
    var r1 by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 70.dp, end = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("AngryCats", fontSize = 50.sp)
        OutlinedTextField(
            value = amountCats,
            onValueChange = {
                amountCats = it
                Config.amountCats.value = amountCats.toIntOrNull() ?: Config.amountCats.value
            },
            label = { Text("Enter the number of cats") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = height,
            onValueChange = {
                height = it
                Config.height.value = height.toIntOrNull() ?: Config.height.value
                            },
            label = { Text("Enter height") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = width,
            onValueChange = {
                width = it
                Config.width.value = width.toIntOrNull() ?: Config.width.value
                            },
            label = { Text("Enter width") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = r0,
            onValueChange = {
                r0 = it
                Config.r0.value = r0.toDoubleOrNull() ?: Config.r0.value
                            },
            label = { Text("Enter r0") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = r1,
            onValueChange = {
                r1 = it
                Config.r1.value = r1.toDoubleOrNull() ?: Config.r1.value
                            },
            label = { Text("Enter r1") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = time,
            onValueChange = {
                time = it
                            },
            label = { Text("Enter time") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(40.dp))
        Button(onClick = changeScreen, modifier = Modifier.fillMaxWidth()) {
            Text("Go to Second Screen")
        }
    }
}
