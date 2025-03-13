package com.example.mytailorsapp.ui.customer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mytailorsapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VirtualClothingScreen(materialName: String, materialResId: Int, navController: NavController) {
    var fabricColor by remember { mutableStateOf(Color.White) }
    var scaleFactor by remember { mutableFloatStateOf(1.0f) } // Adjust fabric scale

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Virtual Clothing Preview") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Preview of $materialName on Shirt", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                // Shirt Outline
                Image(
                    painter = painterResource(id = R.drawable.shirt_outline),
                    contentDescription = "Shirt Outline",
                    modifier = Modifier.fillMaxSize()
                )

                // Fabric Texture Wrapped on Shirt
                FabricWrapCanvas(
                    fabricResId = materialResId,
                    fabricColor = fabricColor,
                    scaleFactor = scaleFactor
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🔹 Fabric Color Picker
            Text("Select Fabric Color")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ColorOption(Color.Red) { fabricColor = it }
                ColorOption(Color.Blue) { fabricColor = it }
                ColorOption(Color.Green) { fabricColor = it }
                ColorOption(Color.Yellow) { fabricColor = it }
                ColorOption(Color.White) { fabricColor = it }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Fabric Scaling Slider
            Text("Adjust Fabric Scaling")
            Slider(
                value = scaleFactor,
                onValueChange = { scaleFactor = it },
                valueRange = 0.5f..2.0f,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.popBackStack() }) {
                Text("Go Back")
            }
        }
    }
}

/**
 * 🔹 This composable uses Canvas to wrap the fabric texture on the Shirt.
 */
@Composable
fun FabricWrapCanvas(fabricResId: Int, fabricColor: Color, scaleFactor: Float) {
    val fabricBitmap = ImageBitmap.imageResource(id = fabricResId)

    Canvas(modifier = Modifier.size(180.dp)) {
        drawIntoCanvas { canvas ->
            val paint = Paint().apply {
                colorFilter = ColorFilter.tint(fabricColor) // Apply color tint
            }

            val scaledWidth = fabricBitmap.width * scaleFactor
            val scaledHeight = fabricBitmap.height * scaleFactor

            canvas.drawImageRect(
                image = fabricBitmap,
                srcOffset = IntOffset.Zero,
                srcSize = IntSize(fabricBitmap.width, fabricBitmap.height),
                dstOffset = IntOffset(0, 0),
                dstSize = IntSize(scaledWidth.toInt(), scaledHeight.toInt()),
                paint = paint
            )
        }
    }
}

/**
 * 🔹 Generates a Color Option Button for the Fabric
 */
@Composable
fun ColorOption(color: Color, onClick: (Color) -> Unit) {
    Button(
        onClick = { onClick(color) },
        modifier = Modifier
            .size(40.dp)
            .background(color, shape = RoundedCornerShape(50))
    ) { }
}
