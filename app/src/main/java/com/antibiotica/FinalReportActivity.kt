package com.antibiotica

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.graphics.toArgb
import com.antibiotica.ui.theme.Primary
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import android.os.Environment
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument

class FinalReportActivity : ComponentActivity() {
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
                FinalReportScreen(
                    pathogenData = pathogenData,
                    onBackClick = { finish() }
                )
            }
        }
    }
}

@Composable
fun FinalReportScreen(pathogenData: PathogenData?, onBackClick: () -> Unit) {
    val scrollState = rememberScrollState()
    var selectedFormat by remember { mutableStateOf("PDF") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                    text = "Final Report",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { /* Share */ }) {
                    Icon(Icons.Default.IosShare, contentDescription = "Share", modifier = Modifier.size(20.dp))
                }
            }
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 16.dp,
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PrimaryButton(
                        text = "Generate & Download Report",
                        onClick = {
                            val fileName = "Analysis_Report_${pathogenData?.id?.replace("#", "") ?: "unknown"}.${selectedFormat.lowercase()}"

                            scope.launch {
                                try {
                                    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                    val file = File(downloadsDir, fileName)

                                    if (selectedFormat == "PDF") {
                                        val pdfDocument = PdfDocument()
                                        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 Size
                                        val page = pdfDocument.startPage(pageInfo)
                                        val canvas = page.canvas
                                        val paint = Paint()

                                        var y = 50f
                                        paint.textSize = 24f
                                        paint.isFakeBoldText = true
                                        canvas.drawText("ANTIBIOTICA ANALYSIS REPORT", 150f, y, paint)

                                        y += 40f
                                        paint.textSize = 16f
                                        paint.isFakeBoldText = true
                                        canvas.drawText("SAMPLE INFORMATION", 50f, y, paint)

                                        y += 25f
                                        paint.isFakeBoldText = false
                                        paint.textSize = 14f
                                        canvas.drawText("Sample ID: ${pathogenData?.id}", 50f, y, paint)
                                        y += 20f
                                        canvas.drawText("Detected: ${pathogenData?.name}", 50f, y, paint)
                                        y += 20f
                                        canvas.drawText("Confidence: ${pathogenData?.similarityScore}%", 50f, y, paint)
                                        y += 20f
                                        canvas.drawText("Method: ${pathogenData?.method ?: "Digital Image Analysis"}", 50f, y, paint)

                                        y += 40f
                                        paint.isFakeBoldText = true
                                        canvas.drawText("ANALYSIS RESULTS", 50f, y, paint)
                                        y += 25f
                                        paint.isFakeBoldText = false
                                        canvas.drawText("Recommended Antibiotic: ${pathogenData?.recommendedAntibiotic}", 50f, y, paint)
                                        y += 20f
                                        canvas.drawText("Therapy: ${pathogenData?.recommendationType}", 50f, y, paint)

                                        y += 40f
                                        paint.isFakeBoldText = true
                                        canvas.drawText("ZONE MEASUREMENTS", 50f, y, paint)
                                        y += 25f
                                        paint.isFakeBoldText = false
                                        canvas.drawText("Inhibition Zone: ${pathogenData?.inhibitionZone}mm", 50f, y, paint)
                                        y += 20f
                                        canvas.drawText("Resistance Breakpoint: ${pathogenData?.resistanceBreakpoint}mm", 50f, y, paint)

                                        y += 60f
                                        paint.color = Color.Gray.toArgb()
                                        canvas.drawText("[Logo Placeholder]", 240f, y, paint)

                                        y += 100f
                                        canvas.drawRect(50f, y, 545f, y + 200f, Paint().apply { color = Color.LightGray.toArgb(); style = Paint.Style.STROKE })
                                        canvas.drawText("Screenshot of Results Placeholder", 180f, y + 110f, paint)

                                        y += 250f
                                        paint.textSize = 10f
                                        canvas.drawText("Disclaimer: This AI-generated report is for informational purposes.", 50f, y, paint)

                                        pdfDocument.finishPage(page)
                                        FileOutputStream(file).use { out ->
                                            pdfDocument.writeTo(out)
                                        }
                                        pdfDocument.close()
                                    } else {
                                        // Simple CSV implementation
                                        FileOutputStream(file).use { out ->
                                            out.write("Attribute,Value\n".toByteArray())
                                            out.write("Sample ID,${pathogenData?.id}\n".toByteArray())
                                            out.write("Detected,${pathogenData?.name}\n".toByteArray())
                                            out.write("Confidence,${pathogenData?.similarityScore}%\n".toByteArray())
                                            out.write("Recommended,${pathogenData?.recommendedAntibiotic}\n".toByteArray())
                                        }
                                    }

                                    snackbarHostState.showSnackbar(
                                        message = "Report saved to Downloads: $fileName",
                                        duration = SnackbarDuration.Short
                                    )
                                } catch (e: Exception) {
                                    snackbarHostState.showSnackbar(
                                        message = "Failed to save report: ${e.message}",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        },
                        icon = Icons.Default.Download
                    )
                    Text(
                        text = "Medical Disclaimer: This report is generated by AI for informational purposes only and should not replace professional laboratory diagnostics.",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        lineHeight = 14.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                Surface(modifier = Modifier.size(48.dp), shape = CircleShape, color = Primary.copy(alpha = 0.1f)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Primary)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = "Analysis Complete", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(text = "Ready to generate details", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                }
            }

            // Preview Card
            InfoCard(modifier = Modifier.padding(0.dp)) {
                Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "STRAIN DETECTED", style = MaterialTheme.typography.labelSmall, color = Primary, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = pathogenData?.name ?: "Unknown Pathogen", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(text = "Sample ID: ${pathogenData?.id ?: "Unknown"} • Micro-scan", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Gray.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (pathogenData != null) {
                            Image(
                                painter = painterResource(id = pathogenData.imageResId),
                                contentDescription = "Pathogen Sample",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(Icons.Default.Biotech, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(48.dp))
                        }
                    }
                }
            }

            // Quick Stats
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ReportStatCard("${pathogenData?.similarityScore ?: 0}%", "CONFIDENCE", Icons.Default.Verified, modifier = Modifier.weight(1f))
                ReportStatCard(pathogenData?.recommendedAntibiotic ?: "N/A", "RECOMMENDED", Icons.Default.Medication, modifier = Modifier.weight(1f))
            }

            // Contents Checklist
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                    Text(text = "Report Contents", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(text = "3 Items Included", style = MaterialTheme.typography.labelSmall, color = Primary, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ChecklistItem("High-res Sample Imagery", "Original & Processed scans", Icons.Default.Image)
                    ChecklistItem("Susceptibility Graph", "Growth curve analysis", Icons.Default.SsidChart)
                    ChecklistItem("Dosage Guidelines", "Based on resistance level", Icons.Default.Vaccines)
                }
            }

            // Format Selector
            Column {
                Text(text = "File Format", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
                ) {
                    Row(modifier = Modifier.padding(4.dp)) {
                        FormatToggleButton("PDF Document", Icons.Default.PictureAsPdf, selectedFormat == "PDF", modifier = Modifier.weight(1f)) { selectedFormat = "PDF" }
                        FormatToggleButton("CSV Data", Icons.Default.TableChart, selectedFormat == "CSV", modifier = Modifier.weight(1f)) { selectedFormat = "CSV" }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ReportStatCard(value: String, label: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, maxLines = 1)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = Primary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
            }
        }
    }
}

@Composable
fun ChecklistItem(title: String, subtitle: String, icon: ImageVector) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(44.dp), shape = CircleShape, color = Primary.copy(alpha = 0.1f)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = Primary, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Primary, modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
fun FormatToggleButton(text: String, icon: ImageVector, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier
            .height(52.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = if (selected) MaterialTheme.colorScheme.surface else Color.Transparent,
        shadowElevation = if (selected) 4.dp else 0.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = if (selected) Color.Red else Color.Gray, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, style = MaterialTheme.typography.bodyMedium, fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium, color = if (selected) MaterialTheme.colorScheme.onBackground else Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FinalReportScreenPreview() {
    AntibioticaTheme {
        FinalReportScreen(null, {})
    }
}
