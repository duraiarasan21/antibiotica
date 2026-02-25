package com.antibiotica

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antibiotica.data.PathogenData
import com.antibiotica.ui.components.InfoCard
import com.antibiotica.ui.theme.AntibioticaTheme
import com.antibiotica.ui.theme.Primary
import kotlinx.coroutines.delay

class AnalyzingSampleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pathogenData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("pathogen_data", PathogenData::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("pathogen_data")
        }

        setContent {
            AntibioticaTheme {
                AnalyzingSampleScreen(
                    pathogenData = pathogenData,
                    onBackClick = { finish() },
                    onAnalysisComplete = {
                        val intent = Intent(this, AnalysisResultsActivity::class.java)
                        intent.putExtra("pathogen_data", pathogenData)
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun AnalyzingSampleScreen(pathogenData: PathogenData?, onBackClick: () -> Unit, onAnalysisComplete: () -> Unit) {
    var progress by remember { mutableFloatStateOf(0f) }
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "progress")

    LaunchedEffect(Unit) {
        delay(500)
        progress = 0.25f
        delay(1000)
        progress = 0.5f
        delay(1500)
        progress = 0.8f
        delay(1000)
        progress = 1.0f
        delay(500)
        onAnalysisComplete()
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", modifier = Modifier.size(20.dp))
                }
                Text(
                    text = "Analyzing Sample",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(48.dp))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Scanner Hero
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(2.dp, Primary.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                // Background image
                if (pathogenData != null) {
                    Image(
                        painter = painterResource(id = pathogenData.imageResId),
                        contentDescription = "Sample being analyzed",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        alpha = 0.7f
                    )
                } else {
                    Icon(Icons.Default.Biotech, contentDescription = null, modifier = Modifier.size(120.dp), tint = Primary.copy(alpha = 0.1f))
                }

                // Scanning Line Animation
                val infiniteTransition = rememberInfiniteTransition(label = "scan")
                val scanOffset by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 280f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(2000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "scanLine"
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .offset(y = (scanOffset - 140).dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Primary, Color.Transparent)
                            )
                        )
                )

                // Floating Badge
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = 14.dp),
                    shape = RoundedCornerShape(100.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 6.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Sync, contentDescription = null, tint = Primary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Scanning ID: ${pathogenData?.id ?: "#8821X"}", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(56.dp))

            Text(text = "Processing Image...", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Please wait while our AI analyzes the zone of inhibition.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Progress Bar
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                    Text(text = "Detecting Resistance...", style = MaterialTheme.typography.titleSmall, color = Primary, fontWeight = FontWeight.Bold)
                    Text(text = "${(animatedProgress * 100).toInt()}%", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = animatedProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(CircleShape),
                    color = Primary,
                    trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Timeline
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(28.dp)) {
                TimelineItem("Image Uploaded", "Successfully received high-res sample", true, true)
                TimelineItem(
                    title = if (progress >= 0.25f) "Bacteria Identified" else "Identifying Bacteria...",
                    subtitle = if (progress >= 0.25f) "Detected ${pathogenData?.name ?: "Unknown"}" else "Scanning microbial signatures...",
                    completed = progress >= 0.25f,
                    active = progress < 0.5f && progress >= 0.25f
                )
                TimelineItem("Analyzing Resistance", "Measuring inhibition zones...", progress >= 0.5f, progress < 1f && progress >= 0.5f)
                TimelineItem("Generating Report", "Pending final results", progress >= 1f, false)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Tip Card
            InfoCard {
                Row(verticalAlignment = Alignment.Top) {
                    Surface(modifier = Modifier.size(36.dp), shape = CircleShape, color = Primary.copy(alpha = 0.1f)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Primary, modifier = Modifier.size(20.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = "DID YOU KNOW?", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.ExtraBold, color = Color.Gray, letterSpacing = 1.sp)
                        Text(
                            text = "Antibiotics are not effective against viruses like the common cold or flu. Proper diagnosis is key to effective treatment.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            TextButton(onClick = onBackClick, modifier = Modifier.padding(vertical = 16.dp)) {
                Text(text = "Cancel Analysis", color = Color.Gray, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun TimelineItem(title: String, subtitle: String, completed: Boolean, active: Boolean) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Surface(
            modifier = Modifier.size(44.dp),
            shape = CircleShape,
            color = if (completed) Primary else MaterialTheme.colorScheme.surface,
            border = if (completed) null else androidx.compose.foundation.BorderStroke(2.dp, if (active) Primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = if (completed) Icons.Default.Check else if (active) Icons.Default.Sync else Icons.Default.Description,
                    contentDescription = null,
                    tint = if (completed) Color(0xFF102216) else if (active) Primary else Color.Gray,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(20.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (active || completed) MaterialTheme.colorScheme.onBackground else Color.Gray
            )
            Text(text = subtitle, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnalyzingSampleScreenPreview() {
    AntibioticaTheme {
        AnalyzingSampleScreen(null, {}, {})
    }
}
