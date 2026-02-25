package com.antibiotica

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antibiotica.ui.theme.AntibioticaTheme
import com.antibiotica.ui.theme.Primary
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AntibioticaTheme {
                SplashScreen {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Decorative Glows
        Box(
            modifier = Modifier
                .size(256.dp)
                .offset(x = 200.dp, y = (-80).dp)
                .blur(100.dp)
                .background(Primary.copy(alpha = 0.1f), shape = RoundedCornerShape(128.dp))
        )
        Box(
            modifier = Modifier
                .size(320.dp)
                .offset(x = (-80).dp, y = 600.dp)
                .blur(100.dp)
                .background(Primary.copy(alpha = 0.05f), shape = RoundedCornerShape(160.dp))
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Logo Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Surface(
                    modifier = Modifier.size(128.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 8.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "App Logo",
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "AntibioGuide",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Precision prescribing made simple.",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Loading Indicator
            Spacer(modifier = Modifier.height(48.dp))
            LoadingIndicator()

            Spacer(modifier = Modifier.weight(1.5f))

            // Footer
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(bottom = 40.dp)
            ) {
                Text(
                    text = "POWERED BY MEDTECH SOLUTIONS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp
                    ),
                    color = Primary.copy(alpha = 0.7f)
                )
                Text(
                    text = "v1.0",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                    color = Primary.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")

    @Composable
    fun BouncingDot(delayMillis: Int) {
        val animation = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(600, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
                initialStartOffset = StartOffset(delayMillis)
            ),
            label = "dot"
        )
        Box(
            modifier = Modifier
                .size(10.dp)
                .offset(y = (animation.value * (-10)).dp)
                .background(Primary.copy(alpha = 1f - (animation.value * 0.7f)), shape = RoundedCornerShape(5.dp))
        )
    }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        BouncingDot(0)
        BouncingDot(150)
        BouncingDot(300)
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    AntibioticaTheme {
        SplashScreen {}
    }
}
