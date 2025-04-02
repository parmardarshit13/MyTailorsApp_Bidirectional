package com.example.mytailorsapp.ui.customer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mytailorsapp.R
import kotlin.math.PI
import kotlin.math.sin
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.ImageShader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VirtualClothingScreen(
    materialName: String,
    materialResId: Int,
    navController: NavController
) {
    // State variables for fabric customization
    var fabricColor by remember { mutableStateOf(Color.White) }
    var scaleFactor by remember { mutableFloatStateOf(1.0f) }
    var stretchX by remember { mutableFloatStateOf(1.0f) }
    var stretchY by remember { mutableFloatStateOf(1.0f) }
    var rotation by remember { mutableFloatStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var showDrapingEffect by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Virtual Clothing Preview") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Preview of $materialName",
                style = MaterialTheme.typography.headlineSmall
            )

            // Interactive Fabric Preview Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .pointerInput(Unit) {
                        detectTransformGestures(
                            onGesture = { centroid, pan, zoom, rotationDelta ->
                                offset += pan
                                scaleFactor *= zoom
                                rotation += rotationDelta
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                // Human Silhouette (Optional)
                Image(
                    painter = painterResource(id = R.drawable.human_silhouette),
                    contentDescription = "Human Silhouette",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )

                // Fabric Visualization with Draping Effect
                FabricVisualization(
                    fabricResId = materialResId,
                    fabricColor = fabricColor,
                    scaleFactor = scaleFactor,
                    stretchX = stretchX,
                    stretchY = stretchY,
                    rotation = rotation,
                    offset = offset,
                    showDrapingEffect = showDrapingEffect
                )
            }

            // Controls Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Color Selection
                Text("Fabric Color", style = MaterialTheme.typography.labelLarge)
                ColorPalette { fabricColor = it }

                // Stretch Controls
                Text("Horizontal Stretch", style = MaterialTheme.typography.labelLarge)
                Slider(
                    value = stretchX,
                    onValueChange = { stretchX = it },
                    valueRange = 0.5f..2.5f,
                    steps = 10,
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Vertical Stretch", style = MaterialTheme.typography.labelLarge)
                Slider(
                    value = stretchY,
                    onValueChange = { stretchY = it },
                    valueRange = 0.5f..2.5f,
                    steps = 10,
                    modifier = Modifier.fillMaxWidth()
                )

                // Advanced Options
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { showDrapingEffect = !showDrapingEffect }) {
                        Text(if (showDrapingEffect) "Hide Draping" else "Show Draping")
                    }
                    Button(onClick = {
                        // Reset all transformations
                        scaleFactor = 1.0f
                        stretchX = 1.0f
                        stretchY = 1.0f
                        rotation = 0f
                        offset = Offset.Zero
                    }) {
                        Text("Reset")
                    }
                }
            }
        }
    }
}

@Composable
private fun FabricVisualization(
    fabricResId: Int,
    fabricColor: Color,
    scaleFactor: Float,
    stretchX: Float,
    stretchY: Float,
    rotation: Float,
    offset: Offset,
    showDrapingEffect: Boolean
) {
    val fabricBitmap = ImageBitmap.imageResource(id = fabricResId)
    val colorFilter = remember(fabricColor) {
        ColorFilter.tint(fabricColor)
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = this.center
        val scaledWidth = fabricBitmap.width * scaleFactor * stretchX
        val scaledHeight = fabricBitmap.height * scaleFactor * stretchY

        withTransform({
            translate(offset.x, offset.y)
            rotate(rotation, pivot = center)
        }) {
            if (showDrapingEffect) {
                drawDrapedFabric(
                    bitmap = fabricBitmap,
                    colorFilter = colorFilter,
                    center = center,
                    width = scaledWidth,
                    height = scaledHeight,
                    rotation = rotation
                )
            } else {
                val shaderBrush = ShaderBrush(
                    ImageShader(
                        fabricBitmap,
                        TileMode.Repeated,
                        TileMode.Repeated
                    )
                )

                drawRect(
                    brush = shaderBrush,
                    topLeft = Offset(
                        center.x - scaledWidth / 2,
                        center.y - scaledHeight / 2
                    ),
                    size = Size(scaledWidth, scaledHeight)
                )

            }
        }
    }
}

private fun DrawScope.drawDrapedFabric(
    bitmap: ImageBitmap,
    colorFilter: ColorFilter?,
    center: Offset,
    width: Float,
    height: Float,
    rotation: Float
) {
    val points = mutableListOf<Offset>()
    val steps = 20
    val amplitude = height * 0.1f

    // Generate points for the draped fabric
    for (i in 0..steps) {
        val x = width * (i.toFloat() / steps) - width / 2
        val angle = (i.toFloat() / steps) * 2f * PI.toFloat()
        val y = amplitude * sin(angle + rotation * 0.1f)
        points.add(Offset(x, y))
    }

    // Draw the fabric with draping effect
    drawIntoCanvas { canvas ->
        val path = Path().apply {
            moveTo(points.first().x + center.x, points.first().y + center.y - height / 2)
            points.forEachIndexed { index, point ->
                if (index > 0) {
                    lineTo(point.x + center.x, point.y + center.y - height / 2)
                }
            }
            lineTo(points.last().x + center.x, points.last().y + center.y + height / 2)
            points.reversed().forEachIndexed { index, point ->
                if (index > 0) {
                    lineTo(point.x + center.x, point.y + center.y + height / 2)
                }
            }
            close()
        }

        canvas.nativeCanvas.drawPath(
            path.asAndroidPath(),
            android.graphics.Paint().apply {
                shader = android.graphics.BitmapShader(
                    bitmap.asAndroidBitmap(),
                    android.graphics.Shader.TileMode.REPEAT,
                    android.graphics.Shader.TileMode.REPEAT
                )
                colorFilter?.let {
                    this.colorFilter = it.asAndroidColorFilter()
                }
            }
        )
    }
}

@Composable
private fun ColorPalette(onColorSelected: (Color) -> Unit) {
    val colors = listOf(
        Color.Red,
        Color.Blue,
        Color.Green,
        Color.Yellow,
        Color.White,
        Color.Black,
        Color(0xFF8B4513), // Brown
        Color(0xFF800080)  // Purple
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        colors.forEach { color ->
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(color)
                    .clickable { onColorSelected(color) }
            )
        }
    }
}