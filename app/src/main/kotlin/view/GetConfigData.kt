package org.example.app.view

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
import org.example.lib.utils.IAppConfig

/**

 This is the screen where the user enters the basic startup parameters

 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun getConfigData(config: IAppConfig, changeScreen: () -> Unit) {
    var amountCats by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var width by remember { mutableStateOf("") }
    var r0 by remember { mutableStateOf("") }
    var r1 by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(start = 70.dp, end = 70.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        item {
            Text("AngryCats", fontSize = 70.sp, fontWeight = FontWeight.Bold)
            Text(
                "Enter the number of cats",
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
            )
            OutlinedTextField(
                value = amountCats,
                onValueChange = {
                    amountCats = it
                    config.amountCats = amountCats.toIntOrNull() ?: config.amountCats
                },
                shape = RoundedCornerShape(20.dp),
                label = { Text("Default: ${config.amountCats}") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            )
            Text(
                "Enter height",
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
            )
            OutlinedTextField(
                value = height,
                onValueChange = {
                    height = it
                    config.height = height.toIntOrNull() ?: config.height
                },
                shape = RoundedCornerShape(20.dp),
                label = { Text("Default: ${config.height}") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            Text(
                "Enter width",
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
            )

            OutlinedTextField(
                value = width,
                onValueChange = {
                    width = it
                    config.width = width.toIntOrNull() ?: config.width
                },
                shape = RoundedCornerShape(20.dp),
                label = { Text("Default: ${config.width}") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            )
            Text(
                "Enter r0",
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
            )

            OutlinedTextField(
                value = r0,
                onValueChange = {
                    r0 = it
                    config.r0 = r0.toDoubleOrNull() ?: config.r0
                },
                shape = RoundedCornerShape(20.dp),
                label = { Text("Default: ${config.r0}") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            )
            Text(
                "Enter r1",
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
            )

            OutlinedTextField(
                value = r1,
                onValueChange = {
                    r1 = it
                    config.r1 = r1.toDoubleOrNull() ?: config.r1
                },
                shape = RoundedCornerShape(20.dp),
                label = { Text("Default: ${config.r1}") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            )
            Text(
                "Enter time",
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
            )

            OutlinedTextField(
                value = time,
                onValueChange = {
                    time = it
                    config.time =
                        try {
                            ((it.toDoubleOrNull() ?: 1.0) * 1000L).toLong()
                        } catch (e: Exception) {
                            500L
                        }
                },
                shape = RoundedCornerShape(20.dp),
                label = { Text("Default: 0.5s") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            )

            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = changeScreen,
                modifier = Modifier.padding(top = 80.dp).size(width = 400.dp, height = 50.dp),
            ) {
                Text("Compute!")
            }
        }
    }
}
