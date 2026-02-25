package com.antibiotica.data

import android.os.Parcelable
import com.antibiotica.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class PathogenData(
    val id: String,
    val name: String,
    val imageResId: Int,
    val referenceImageResId: Int,
    val similarityScore: Int,
    val confidence: String,
    val recommendedAntibiotic: String,
    val recommendationType: String, // e.g. First-line therapy
    val recommendationDetails: String,
    val inhibitionZone: Int,
    val totalZone: Int,
    val resistanceBreakpoint: Int,
    val processingTime: String,
    val resistanceMarkers: String,
    val method: String,
    val matchId: String,
    val probabilityList: List<ProbabilityItem>
) : Parcelable

@Parcelize
data class ProbabilityItem(
    val name: String,
    val probability: Float
) : Parcelable

object PathogenSamples {
    val samples = listOf(
        PathogenData(
            id = "#8842",
            name = "Staphylococcus aureus",
            imageResId = R.drawable.s_aureus,
            referenceImageResId = R.drawable.ref_s_aureus,
            similarityScore = 98,
            confidence = "High Confidence",
            recommendedAntibiotic = "Vancomycin",
            recommendationType = "First-line therapy",
            recommendationDetails = "Effective for MRSA strains. Please verify patient history for allergies.",
            inhibitionZone = 28,
            totalZone = 30,
            resistanceBreakpoint = 18,
            processingTime = "1.2s",
            resistanceMarkers = "None detected",
            method = "Disk Diffusion",
            matchId = "#420",
            probabilityList = listOf(
                ProbabilityItem("S. aureus", 0.98f),
                ProbabilityItem("S. epidermidis", 0.12f),
                ProbabilityItem("Micrococcus", 0.05f)
            )
        ),
        PathogenData(
            id = "#8843",
            name = "Pseudomonas aeruginosa",
            imageResId = R.drawable.p_aeruginosa,
            referenceImageResId = R.drawable.ref_p_aeruginosa,
            similarityScore = 94,
            confidence = "High Confidence",
            recommendedAntibiotic = "Ciprofloxacin",
            recommendationType = "Standard therapy",
            recommendationDetails = "Highly effective against most gram-negative aerobes. Monitor renal function.",
            inhibitionZone = 24,
            totalZone = 30,
            resistanceBreakpoint = 20,
            processingTime = "1.5s",
            resistanceMarkers = "Low level Efflux",
            method = "Disk Diffusion",
            matchId = "#421",
            probabilityList = listOf(
                ProbabilityItem("P. aeruginosa", 0.94f),
                ProbabilityItem("P. fluorescens", 0.15f),
                ProbabilityItem("Burkholderia", 0.08f)
            )
        ),
        PathogenData(
            id = "#8844",
            name = "Escherichia coli",
            imageResId = R.drawable.e_coli,
            referenceImageResId = R.drawable.ref_e_coli,
            similarityScore = 99,
            confidence = "Very High Confidence",
            recommendedAntibiotic = "Gentamicin",
            recommendationType = "Standard therapy",
            recommendationDetails = "Aminoglycoside antibiotic. Highly effective for septicemia strains. Note: Resistant to Ampicillin.",
            inhibitionZone = 29,
            totalZone = 30,
            resistanceBreakpoint = 16,
            processingTime = "0.9s",
            resistanceMarkers = "AmpC Beta-lactamase",
            method = "Disk Diffusion",
            matchId = "#422",
            probabilityList = listOf(
                ProbabilityItem("E. coli", 0.99f),
                ProbabilityItem("Klebsiella", 0.10f),
                ProbabilityItem("Enterobacter", 0.04f)
            )
        )
    )
}
