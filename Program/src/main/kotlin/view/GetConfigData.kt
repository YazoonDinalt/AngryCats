package view

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun getConfigData(changeScreen: () -> Unit) {
    var text1 by remember { mutableStateOf("") }
    var text2 by remember { mutableStateOf("") }
    var text3 by remember { mutableStateOf("") }
    var text4 by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = text1,
            onValueChange = { text1 = it },
            label = { Text("Field 1") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = text2,
            onValueChange = { text2 = it },
            label = { Text("Field 2") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // Example: Numeric keyboard
        )
        OutlinedTextField(
            value = text3,
            onValueChange = { text3 = it },
            label = { Text("Field 3") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )
        OutlinedTextField(
            value = text4,
            onValueChange = { text4 = it },
            label = { Text("Field 4") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = changeScreen, modifier = Modifier.fillMaxWidth()) {
            Text("Go to Second Screen")
        }
    }
}
