package org.example.app.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**

 This function contains a welcome dialog box AngryCats

*/

@Composable
fun startScreen(changeScreen: () -> Unit) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(start = 70.dp, end = 70.dp, bottom = 20.dp, top = 350.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("AngryCats", fontSize = 120.sp, fontWeight = FontWeight.Bold)

        Button(
            onClick = changeScreen,
            modifier = Modifier.padding(top = 80.dp).size(width = 400.dp, height = 50.dp),
        ) {
            Text("Go!")
        }
    }
}
