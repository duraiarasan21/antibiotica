package com.antibiotica

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antibiotica.ui.components.PrimaryButton
import com.antibiotica.ui.theme.AntibioticaTheme
import com.antibiotica.ui.theme.Primary

class RegistrationSuccessActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AntibioticaTheme {
                RegistrationSuccessScreen(
                    onGetStartedClick = {
                        startActivity(Intent(this, DashboardActivity::class.java))
                        finishAffinity()
                    },
                    onEditProfileClick = {
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun RegistrationSuccessScreen(onGetStartedClick: () -> Unit, onEditProfileClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Background Decorative Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Primary.copy(alpha = 0.1f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Success Illustration Area
            Box(contentAlignment = Alignment.Center) {
                // Glow effect
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .blur(40.dp)
                        .background(Primary.copy(alpha = 0.2f), shape = CircleShape)
                )

                Surface(
                    modifier = Modifier.size(140.dp),
                    shape = CircleShape,
                    color = Primary,
                    shadowElevation = 8.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF102216),
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "All Set!",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Your profile is set up. We are ready to help you find the right antibiotic guidelines.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.width(300.dp)
            )

            Spacer(modifier = Modifier.height(64.dp))

            PrimaryButton(
                text = "Get Started",
                onClick = onGetStartedClick,
                icon = Icons.Default.ArrowForward
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onEditProfileClick,
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = "Edit Profile",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationSuccessScreenPreview() {
    AntibioticaTheme {
        RegistrationSuccessScreen({}, {})
    }
}
