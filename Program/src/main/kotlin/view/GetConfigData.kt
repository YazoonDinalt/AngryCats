package view

import Config
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 70.dp, end = 70.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Text("AngryCats", fontSize = 70.sp, fontWeight = FontWeight.Bold)
            Text(
                "Enter the number of cats",
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            OutlinedTextField(
                value = amountCats,
                onValueChange = {
                    amountCats = it
                    Config.amountCats.value = amountCats.toIntOrNull() ?: Config.amountCats.value
                },
                shape = RoundedCornerShape(20.dp),
                label = { Text("Default: ${Config.amountCats.value}") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
            Text(
                "Enter height",
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            OutlinedTextField(
                value = height,
                onValueChange = {
                    height = it
                    Config.height.value = height.toIntOrNull() ?: Config.height.value
                },
                shape = RoundedCornerShape(20.dp),
                label = { Text("Default: ${Config.height.value}") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Text(
                "Enter width",
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            OutlinedTextField(
                value = width,
                onValueChange = {
                    width = it
                    Config.width.value = width.toIntOrNull() ?: Config.width.value
                },
                shape = RoundedCornerShape(20.dp),
                label = { Text("Default: ${Config.width.value}")},
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
            Text(
                "Enter r0",
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            OutlinedTextField(
                value = r0,
                onValueChange = {
                    r0 = it
                    Config.r0.value = r0.toDoubleOrNull() ?: Config.r0.value
                },
                shape = RoundedCornerShape(20.dp),
                label = { Text("Default: ${Config.r0.value}") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
            Text(
                "Enter r1",
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            OutlinedTextField(
                value = r1,
                onValueChange = {
                    r1 = it
                    Config.r1.value = r1.toDoubleOrNull() ?: Config.r1.value
                },
                shape = RoundedCornerShape(20.dp),
                label = { Text("Default: ${Config.r1.value}") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
            Text(
                "Enter time",
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            OutlinedTextField(
                value = time,
                onValueChange = {
                    time = it
                },
                shape = RoundedCornerShape(20.dp),
                label = { Text("Default: 1s") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))
            Button(onClick = changeScreen, modifier = Modifier.fillMaxWidth()) {
                Text("Compute!")
            }
        }
    }
}
