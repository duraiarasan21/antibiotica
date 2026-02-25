package com.antibiotica

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.antibiotica.ui.components.PrimaryButton
import com.antibiotica.ui.theme.AntibioticaTheme
import com.antibiotica.ui.theme.Primary

class AnalysisResultsActivity : ComponentActivity() {
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
                AnalysisResultsScreen(
                    pathogenData = pathogenData,
                    onBackClick = { finish() },
                    onGenerateReportClick = {
                        val intent = Intent(this, FinalReportActivity::class.java)
                        intent.putExtra("pathogen_data", pathogenData)
                        startActivity(intent)
                    },
                    onNewScanClick = {
                        startActivity(Intent(this, UploadSampleActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun AnalysisResultsScreen(
    pathogenData: PathogenData?,
    onBackClick: () -> Unit,
    onGenerateReportClick: () -> Unit,
    onNewScanClick: () -> Unit
) {
    val scrollState = rememberScrollState()

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
                    text = "Analysis Complete",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(48.dp))
            }
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 16.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { /* Save */ },
                        modifier = Modifier.size(56.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.BookmarkBorder, contentDescription = "Save", tint = MaterialTheme.colorScheme.onSurface)
                            Text("Save", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    PrimaryButton(
                        text = "Generate Report",
                        onClick = onGenerateReportClick,
                        icon = Icons.Default.PictureAsPdf,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Identified Pathogen Hero Card
            InfoCard(modifier = Modifier.padding(0.dp)) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Gray.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (pathogenData != null) {
                            Image(
                                painter = painterResource(id = pathogenData.imageResId),
                                contentDescription = "Identified Pathogen",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(Icons.Default.Biotech, contentDescription = null, modifier = Modifier.size(100.dp), tint = Primary.copy(alpha = 0.1f))
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp)
                                .background(Primary.copy(alpha = 0.9f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF102216), modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "IDENTIFIED", color = Color(0xFF102216), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                        Column {
                            Text(text = "CLOSEST MATCH", style = MaterialTheme.typography.labelSmall, color = Primary, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                            Text(text = pathogenData?.name ?: "Unknown Pathogen", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        }
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = Color.Gray)
                        }
                    }
                    Text(text = "Pathogen identified from sample ${pathogenData?.id ?: "#Unknown"}.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Confidence & Stats
            InfoCard {
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                        Column {
                            Text(text = "Similarity Score", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                            Text(text = "${pathogenData?.similarityScore ?: 0}%", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                        }
                        Surface(color = Primary.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)) {
                            Text(text = pathogenData?.confidence ?: "Confidence", modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), color = Primary, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    LinearProgressIndicator(
                        progress = (pathogenData?.similarityScore ?: 0) / 100f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(CircleShape),
                        color = Primary,
                        trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Based on morphological analysis of 12,000 samples.", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                }
            }

            // Recommended Antibiotic
            InfoCard {
                Row(verticalAlignment = Alignment.Top) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "RECOMMENDED ANTIBIOTIC", style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = pathogenData?.recommendedAntibiotic ?: "Antibiotic", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(text = pathogenData?.recommendationType ?: "Therapy", color = Primary, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = pathogenData?.recommendationDetails ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {},
                            modifier = Modifier.fillMaxWidth().height(40.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onBackground, contentColor = MaterialTheme.colorScheme.background),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("View Dosage Guidelines", style = MaterialTheme.typography.labelLarge)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Medication, contentDescription = null, tint = Primary, modifier = Modifier.size(48.dp))
                    }
                }
            }

            // Probability Chart
            InfoCard {
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Match Probability", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Surface(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f), shape = RoundedCornerShape(4.dp)) {
                            Text(text = "Log Scale", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().height(160.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        pathogenData?.probabilityList?.forEach { item ->
                            ProbabilityBar(item.name, item.probability, if (item.probability > 0.5f) Primary else Color.Gray.copy(alpha = 0.3f))
                        }
                    }
                }
            }

            // Inhibition Zone Analysis
            InfoCard {
                Column {
                    Text(text = "Match Analysis", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Inhibition Zone", style = MaterialTheme.typography.bodySmall)
                        Text(text = "${pathogenData?.inhibitionZone}mm / ${pathogenData?.totalZone}mm", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(24.dp).clip(RoundedCornerShape(4.dp)).background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))) {
                        val zoneRatio = (pathogenData?.inhibitionZone ?: 0).toFloat() / (pathogenData?.totalZone ?: 1)
                        Box(modifier = Modifier.fillMaxWidth(zoneRatio).fillMaxHeight().background(Primary.copy(alpha = 0.8f)))

                        val breakpointRatio = (pathogenData?.resistanceBreakpoint ?: 0).toFloat() / (pathogenData?.totalZone ?: 1)
                        Box(modifier = Modifier.fillMaxHeight().width(2.dp).align(Alignment.CenterStart).offset(x = (320 * breakpointRatio).dp).background(Color.Red))
                    }
                    Text(text = "Zone diameter exceeds resistance breakpoint (>${pathogenData?.resistanceBreakpoint}mm)", style = MaterialTheme.typography.labelSmall, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
                }
            }

            // Visual Evidence
            Column {
                Text(text = "Visual Evidence", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    EvidenceItem("Your Sample", "UPLOADED", imageResId = pathogenData?.imageResId, modifier = Modifier.weight(1f))
                    EvidenceItem("Database Ref", "MATCH ${pathogenData?.matchId}", imageResId = pathogenData?.referenceImageResId, isMatch = true, modifier = Modifier.weight(1f))
                }
            }

            // Detailed Metrics
            InfoCard(modifier = Modifier.padding(0.dp)) {
                Column {
                    Text(text = "Detailed Metrics", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                    MetricRow("Detected Strain", pathogenData?.name ?: "Unknown")
                    MetricRow("Processing Time", pathogenData?.processingTime ?: "N/A")
                    MetricRow("Resistance Markers", pathogenData?.resistanceMarkers ?: "None")
                    MetricRow("Method", pathogenData?.method ?: "Disk Diffusion")
                }
            }

            // Disclaimer
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), verticalAlignment = Alignment.Top) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Medical Disclaimer: AI results are for reference only. This tool does not provide a definitive medical diagnosis. Always consult a physician before administering treatment.",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    lineHeight = 14.sp
                )
            }

            PrimaryButton(
                text = "New Scan",
                onClick = onNewScanClick,
                icon = Icons.Default.AddAPhoto
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ProbabilityBar(label: String, probability: Float, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(80.dp)) {
        Text(text = "${(probability * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = if (probability > 0.5f) Primary else Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .width(40.dp)
                .fillMaxHeight(probability)
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label.split(" ").last(), style = MaterialTheme.typography.labelSmall, color = Color.Gray, textAlign = TextAlign.Center)
    }
}

@Composable
fun EvidenceItem(label: String, badge: String, imageResId: Int? = null, isMatch: Boolean = false, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Gray.copy(alpha = 0.1f))
                .border(if (isMatch) androidx.compose.foundation.BorderStroke(2.dp, Primary.copy(alpha = 0.3f)) else androidx.compose.foundation.BorderStroke(0.dp, Color.Transparent), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (imageResId != null) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = label,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(Icons.Default.Science, contentDescription = null, tint = Color.Gray.copy(alpha = 0.5f), modifier = Modifier.size(48.dp))
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .background(if (isMatch) Primary else Color.Black.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(text = badge, color = if (isMatch) Color(0xFF102216) else Color.White, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.ExtraBold)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = Color.Gray, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun MetricRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f), modifier = Modifier.padding(horizontal = 16.dp))
}

@Preview(showBackground = true)
@Composable
fun AnalysisResultsScreenPreview() {
    AntibioticaTheme {
        AnalysisResultsScreen(null, {}, {}, {})
    }
}
