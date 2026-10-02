package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Rule
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppScreen
import com.example.model.HotlinkedImages
import com.example.model.IshikawaCategory
import com.example.ui.components.HotlinkedNetworkImage
import com.example.ui.components.Interactive8DStepper
import com.example.ui.theme.QResolveColors
import com.example.ui.theme.QResolveType

@Composable
fun D4RootCauseScreen(
    ishikawaCategories: List<IshikawaCategory>,
    selected6MCategory: String,
    onSelect6MCategory: (String) -> Unit,
    onNavigate: (AppScreen) -> Unit,
    onShowToast: (String) -> Unit
) {
    val activeCategory = ishikawaCategories.find { it.name == selected6MCategory }
        ?: ishikawaCategories.first()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWide = maxWidth >= 920.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(QResolveColors.Surface)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Case Header & High-Density Telemetry Banner
            D4CaseHeaderBanner(
                onExportForm = { onNavigate(AppScreen.EXECUTIVE_DOSSIER) },
                onDownloadDossier = { onShowToast("Downloading 8D-20250514-039 Technical Dossier (.PDF)...") },
                onSignOffD4 = {
                    onShowToast("D4 Root Cause Stage Signed Off! Advancing to D5/D6 PCA Workbench.")
                    onNavigate(AppScreen.D5_D6_PCA)
                }
            )

            // 2. Interactive D1-D8 Horizontal Stepper Workflow Pipeline
            Interactive8DStepper(
                activeStageIndex = 4,
                onNavigate = onNavigate
            )

            // 3. Dual-Panel Deep-Dive Investigation Workspace
            if (isWide) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(0.42f),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        D2DefectDefinitionCard()
                        OpticalMicrographViewerCard()
                        IsIsNotBoundaryMatrixCard()
                    }
                    Column(
                        modifier = Modifier.weight(0.58f),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        IshikawaFishboneEngineCard(
                            categories = ishikawaCategories,
                            activeCategory = activeCategory,
                            onSelectCategory = onSelect6MCategory
                        )
                        DualTrack5WhyDrilldownCard()
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    D2DefectDefinitionCard()
                    OpticalMicrographViewerCard()
                    IsIsNotBoundaryMatrixCard()
                    IshikawaFishboneEngineCard(
                        categories = ishikawaCategories,
                        activeCategory = activeCategory,
                        onSelectCategory = onSelect6MCategory
                    )
                    DualTrack5WhyDrilldownCard()
                }
            }

            // 4. Action Footer & Handoff Controls
            D4ActionFooter(
                onLogMicrograph = { onShowToast("Attached SEM EDX Assay & Cpk Study to Case 8D-20250514-039.") },
                onTriggerReadAcross = {
                    onShowToast("D7 Pre-alert Read-Across Notice broadcasted to 4 SMT plants.")
                    onNavigate(AppScreen.D7_STANDARDIZATION)
                },
                onSubmitToD5 = {
                    onShowToast("Root Cause committed to D5 Permanent Corrective Action Matrix.")
                    onNavigate(AppScreen.D5_D6_PCA)
                }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun D4CaseHeaderBanner(
    onExportForm: () -> Unit,
    onDownloadDossier: () -> Unit,
    onSignOffD4: () -> Unit
) {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(QResolveColors.ErrorContainer)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(QResolveColors.Error))
                        Text("CRITICAL SEVERITY", style = QResolveType.CodeBadge, color = QResolveColors.OnErrorContainer)
                    }
                    Text("8D-20250514-039", style = QResolveType.HeadlineLg, color = QResolveColors.OnSurface)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(QResolveColors.SurfaceContainer)
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text("IATF CAPA ID #8491", style = QResolveType.CodeBadge, color = QResolveColors.Secondary)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = QResolveColors.Primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Magna / Stellantis Powertrain",
                            style = QResolveType.BodySm.copy(fontWeight = FontWeight.Medium),
                            color = QResolveColors.OnSurface
                        )
                        Text("/", style = QResolveType.BodySm, color = QResolveColors.Outline)
                        Text("Plant #4 - SMT Line 2", style = QResolveType.CodeBadge, color = QResolveColors.Secondary)
                    }
                }

                // Action Bar
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(QResolveColors.SurfaceContainerLow)
                            .clickable { onExportForm() }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = QResolveColors.OnSurface, modifier = Modifier.size(15.dp))
                        Text("Export AIAG Form", style = QResolveType.BodySm, color = QResolveColors.OnSurface)
                    }
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(QResolveColors.SurfaceContainerLow)
                            .clickable { onDownloadDossier() }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(imageVector = Icons.Default.PictureAsPdf, contentDescription = null, tint = QResolveColors.OnSurface, modifier = Modifier.size(15.dp))
                        Text("Download Dossier", style = QResolveType.BodySm, color = QResolveColors.OnSurface)
                    }
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(QResolveColors.PrimaryContainer)
                            .clickable { onSignOffD4() }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .testTag("sign_off_d4_button"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Verified, contentDescription = null, tint = QResolveColors.OnPrimaryContainer, modifier = Modifier.size(15.dp))
                        Text("Sign-Off D4 Stage", style = QResolveType.HeadlineSm.copy(fontSize = 12.sp), color = QResolveColors.OnPrimaryContainer)
                    }
                }
            }

            // Core Incident Title & Part Designation
            Text(
                text = "Pin Solder Bridging Non-Conformance on Automotive ECU Connector (Pins 14–15)",
                style = QResolveType.HeadlineMd,
                color = QResolveColors.OnSurface
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("PART TARGET:", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Outline)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(QResolveColors.SurfaceContainerHigh)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("PN-8842-B (Rev 04)", style = QResolveType.CodeBadge, color = QResolveColors.OnSecondaryFixed)
                    }
                    Text("High-Density Automotive ECU Bus Interface", style = QResolveType.BodySm, color = QResolveColors.OnSurface)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("CFT LEAD:", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Outline)
                    Text("Marcus Chen (Sr. QA Lead)", style = QResolveType.BodySm.copy(fontWeight = FontWeight.Medium), color = QResolveColors.OnSurface)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("SPONSOR:", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Outline)
                    Text("Dr. Aris Thorne (VP Ops)", style = QResolveType.BodySm.copy(fontWeight = FontWeight.Medium), color = QResolveColors.OnSurface)
                }
            }

            // Telemetry Strip: 4 SLA Boxes
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(QResolveColors.SurfaceContainerLow)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("D3 CONTAINMENT SLA", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Outline)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = QResolveColors.Tertiary, modifier = Modifier.size(14.dp))
                        Text("11.4h (Closed)", style = QResolveType.DataMetricSm.copy(fontWeight = FontWeight.Bold), color = QResolveColors.Tertiary)
                    }
                    Text("4,200 pcs isolated", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.OnSurfaceVariant)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("• D4 ROOT CAUSE SLA", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Error)
                    Text("06h 18m Left", style = QResolveType.DataMetricSm.copy(fontWeight = FontWeight.Bold), color = QResolveColors.Error)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(QResolveColors.ErrorContainer)
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text("IATF L1 Alert", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.OnErrorContainer)
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("FINAL CLOSURE TARGET", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Outline)
                    Text("June 12, 2025", style = QResolveType.DataMetricSm.copy(fontWeight = FontWeight.SemiBold), color = QResolveColors.OnSurface)
                    Text("(24 Days)", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("DEFECT RATE IMPACT", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Outline)
                    Text("4.2% PPM Spike", style = QResolveType.DataMetricSm.copy(fontWeight = FontWeight.Bold), color = QResolveColors.Error)
                    Text("38 Bad / 904 Inspected", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.OnSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun D2DefectDefinitionCard() {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(QResolveColors.SurfaceContainerLow)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Assignment, contentDescription = null, tint = QResolveColors.Primary, modifier = Modifier.size(16.dp))
                    Text("D2: 5W2H DEFECT DEFINITION", style = QResolveType.LabelCaps, color = QResolveColors.OnSurface)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(QResolveColors.TertiaryFixed)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("VERIFIED SPEC", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.OnTertiaryFixedVariant)
                }
            }
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DefectSpecCell(
                        label = "WHAT (DEFECT)",
                        value = "Solder bridge between Pin 14 & Pin 15",
                        isError = false,
                        modifier = Modifier.weight(1f)
                    )
                    DefectSpecCell(
                        label = "WHERE (STATION)",
                        value = "Wave Solder Station 4 (SMT Line 2)",
                        isError = false,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DefectSpecCell(
                        label = "WHEN (DETECTION WINDOW)",
                        value = "Lot 2505-A, Shift 2 (14:32 EST)",
                        isError = false,
                        modifier = Modifier.weight(1f)
                    )
                    DefectSpecCell(
                        label = "HOW MANY (IMPACT)",
                        value = "38 defective units (4.2% PPM Spike)",
                        isError = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun DefectSpecCell(
    label: String,
    value: String,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(QResolveColors.SurfaceContainerLow)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(label, style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Outline)
        Text(
            text = value,
            style = QResolveType.BodySm.copy(fontWeight = FontWeight.SemiBold),
            color = if (isError) QResolveColors.Error else QResolveColors.OnSurface
        )
    }
}

@Composable
private fun OpticalMicrographViewerCard() {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Biotech, contentDescription = null, tint = QResolveColors.Primary, modifier = Modifier.size(16.dp))
                    Text("OPTICAL INSPECTION & MICROGRAPH CROSS-SECTION", style = QResolveType.LabelCaps, color = QResolveColors.OnSurface)
                }
                Text("Mag: 140X SEM", style = QResolveType.CodeBadge, color = QResolveColors.Secondary)
            }

            // Micrograph Visual Specimen Card with Caliper Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0A1912))
            ) {
                HotlinkedNetworkImage(
                    url = HotlinkedImages.SEM_MICROGRAPH,
                    contentDescription = "SEM Micrograph of Pin 14-15 Solder Bridge",
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Rich SEM Micrograph fallback canvas
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawRect(
                            brush = Brush.verticalGradient(
                                listOf(Color(0xFF0B2218), Color(0xFF123524), Color(0xFF07160F))
                            )
                        )
                        // Draw ECU pins 13, 14, 15, 16
                        val pinWidths = size.width / 8f
                        for (i in 0..3) {
                            val x = size.width * 0.16f + i * (pinWidths * 1.55f)
                            drawRect(
                                color = Color(0xFF94A3B8),
                                topLeft = Offset(x, size.height * 0.25f),
                                size = androidx.compose.ui.geometry.Size(pinWidths * 0.8f, size.height * 0.55f)
                            )
                        }
                        // Draw solder bridge between Pin 14 (i=1) and Pin 15 (i=2)
                        val bridgeX = size.width * 0.16f + 1.4f * pinWidths
                        drawOval(
                            color = Color(0xFFE2E8F0),
                            topLeft = Offset(bridgeX, size.height * 0.42f),
                            size = androidx.compose.ui.geometry.Size(pinWidths * 1.4f, size.height * 0.22f)
                        )
                    }
                }

                // Gradient overlay + Caliper vector annotation
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Black.copy(alpha = 0.45f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.65f)
                                )
                            )
                        )
                        .padding(10.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.Black.copy(alpha = 0.65f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("ROI: J3 CONN / ROW-B", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = Color.White)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(QResolveColors.Error)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("SPEC BREACH", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = Color.White)
                        }
                    }

                    // Center Measurement Caliper Callout
                    Column(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(QResolveColors.SurfaceContainerLowest.copy(alpha = 0.95f))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Straighten,
                                contentDescription = null,
                                tint = QResolveColors.Error,
                                modifier = Modifier.size(16.dp)
                            )
                            Column {
                                Text("Bridge Width: 0.12mm", style = QResolveType.CodeBadge, color = QResolveColors.Error)
                                Text("Max Spec: 0.00mm (NO BRIDGE)", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Outline)
                            }
                        }
                        Canvas(modifier = Modifier.width(96.dp).height(14.dp)) {
                            val path = Path().apply {
                                moveTo(0f, size.height * 0.75f)
                                lineTo(0f, 3f)
                                lineTo(size.width, 3f)
                                lineTo(size.width, size.height * 0.75f)
                            }
                            drawPath(path, color = QResolveColors.Error, style = Stroke(width = 2.dp.toPx()))
                            drawCircle(color = QResolveColors.Error, radius = 3.5.dp.toPx(), center = Offset(size.width / 2f, 3f))
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("FOV: 1.8mm x 1.2mm", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = Color.White)
                        Text("FAIL CODE: SMT-IPC-A-610-CL3", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun IsIsNotBoundaryMatrixCard() {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(QResolveColors.SurfaceContainerLow)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Rule, contentDescription = null, tint = QResolveColors.Primary, modifier = Modifier.size(16.dp))
                    Text("D2: IS / IS-NOT BOUNDARY MATRIX", style = QResolveType.LabelCaps, color = QResolveColors.OnSurface)
                }
                Text("Boundary Analysis", style = QResolveType.CodeBadge, color = QResolveColors.Secondary)
            }

            data class BoundaryItem(val dimension: String, val clue: String, val isText: String, val isNotText: String)
            val rows = listOf(
                BoundaryItem(
                    "DIMENSION: WHAT",
                    "Key Clue: Wave Peel Angle",
                    "Solder bridge directly linking adjacent pins 14 & 15.",
                    "Solder balling, dewetting, or open dry circuits."
                ),
                BoundaryItem(
                    "DIMENSION: WHERE",
                    "Key Clue: Carrier #8 Aperture Wear",
                    "Line 2 wave pallet carrier fixture #8 exclusively.",
                    "Line 1 or carrier pallets #1 through #7."
                ),
                BoundaryItem(
                    "DIMENSION: WHEN",
                    "Key Clue: Thermal Profile Delta",
                    "Post-maintenance shift 2 run cycle after 14:00.",
                    "Shift 1 run prior to nozzle cleaning and re-fluxing."
                ),
                BoundaryItem(
                    "DIMENSION: EXTENT",
                    "Key Clue: Pallet Index Repetition",
                    "Sporadic 4.2% rate correlating with Pallet #8 index.",
                    "Continuous 100% board defect rate across the lot."
                )
            )

            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rows.forEach { item ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(QResolveColors.SurfaceContainerLow)
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(item.dimension, style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Primary)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(QResolveColors.TertiaryContainer)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(item.clue, style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.OnTertiaryContainer)
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(QResolveColors.SurfaceContainerLowest)
                                    .padding(8.dp)
                            ) {
                                Text("IS:", style = QResolveType.CodeBadge, color = QResolveColors.Tertiary)
                                Text(item.isText, style = QResolveType.BodySm, color = QResolveColors.OnSurface)
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(QResolveColors.SurfaceContainerLowest)
                                    .padding(8.dp)
                            ) {
                                Text("IS NOT:", style = QResolveType.CodeBadge, color = QResolveColors.Error)
                                Text(item.isNotText, style = QResolveType.BodySm, color = QResolveColors.OnSurface)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun IshikawaFishboneEngineCard(
    categories: List<IshikawaCategory>,
    activeCategory: IshikawaCategory,
    onSelectCategory: (String) -> Unit
) {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(imageVector = Icons.Default.AccountTree, contentDescription = null, tint = QResolveColors.Primary, modifier = Modifier.size(18.dp))
                    Text("D4: ISHIKAWA 6M FISHBONE ANALYSIS ENGINE", style = QResolveType.LabelCaps, color = QResolveColors.OnSurface)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(QResolveColors.PrimaryFixed)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text("6 Hypotheses Evaluated", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Primary)
                }
            }

            // Interactive 6M Category Tabs
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(QResolveColors.SurfaceContainerLow)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                categories.forEach { cat ->
                    val isSelected = cat.name == activeCategory.name
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) QResolveColors.Primary else Color.Transparent)
                            .clickable { onSelectCategory(cat.name) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("tab_6m_${cat.name.lowercase()}"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = cat.name,
                            style = QResolveType.BodySm.copy(fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal),
                            color = if (isSelected) QResolveColors.OnPrimary else QResolveColors.OnSurfaceVariant
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(
                                    when {
                                        isSelected && cat.isHighConfidence -> QResolveColors.Error
                                        isSelected -> QResolveColors.PrimaryContainer
                                        else -> QResolveColors.SurfaceContainerHighest
                                    }
                                )
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = cat.probabilityBadge,
                                style = QResolveType.CodeBadge.copy(fontSize = 9.sp),
                                color = if (isSelected) Color.White else QResolveColors.Secondary
                            )
                        }
                    }
                }
            }

            // Hypotheses Cards
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                activeCategory.hypotheses.forEach { hyp ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(QResolveColors.SurfaceContainerLow)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .heightIn(min = 92.dp)
                                .background(if (hyp.isProbable) QResolveColors.Primary else QResolveColors.Outline)
                        )
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = hyp.id,
                                    style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                                    color = if (hyp.isProbable) QResolveColors.Primary else QResolveColors.Outline
                                )
                                Text(
                                    text = hyp.statusBadge,
                                    style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                                    color = if (hyp.isProbable) QResolveColors.Primary else QResolveColors.Outline
                                )
                            }
                            Text(
                                text = hyp.title,
                                style = QResolveType.BodySm.copy(fontWeight = FontWeight.SemiBold),
                                color = QResolveColors.OnSurface
                            )
                            Text(
                                text = hyp.description,
                                style = QResolveType.BodySm.copy(fontSize = 11.sp),
                                color = QResolveColors.OnSurfaceVariant
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (hyp.isProbable) "✓ ${hyp.footerLeft}" else hyp.footerLeft,
                                    style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                                    color = if (hyp.isProbable) QResolveColors.Tertiary else QResolveColors.Outline
                                )
                                Text(
                                    text = hyp.footerRight,
                                    style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                                    color = QResolveColors.Outline
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DualTrack5WhyDrilldownCard() {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.AccountTree, contentDescription = null, tint = QResolveColors.Primary, modifier = Modifier.size(18.dp))
                    Text("DUAL-TRACK 5-WHY ANALYSIS DRILLDOWN", style = QResolveType.LabelCaps, color = QResolveColors.OnSurface)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(QResolveColors.ErrorContainer)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("Track A: Occurrence", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.OnErrorContainer)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(QResolveColors.SecondaryContainer)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("Track B: Detection Escape", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.OnSecondaryFixed)
                    }
                }
            }

            // TRACK A: Occurrence Root Cause Workflow
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(QResolveColors.SurfaceContainerLow)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Default.PrecisionManufacturing, contentDescription = null, tint = QResolveColors.Primary, modifier = Modifier.size(15.dp))
                        Text(
                            text = "TRACK A: OCCURRENCE ROOT CAUSE (WHY DID THE DEFECT GET GENERATED?)",
                            style = QResolveType.LabelCaps.copy(fontSize = 10.sp),
                            color = QResolveColors.Primary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(QResolveColors.SurfaceContainerLowest)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("Turn Defect ON/OFF Verified", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Tertiary)
                    }
                }

                WhyStepRow("W1", "Molten solder alloy bridged connector pins 14 & 15 during wave immersion.", "Evidence: X-Ray Cross-Section confirms continuous meniscus fillet across 0.12mm gap.", isTrackA = true)
                WhyStepRow("W2", "Wave pallet carrier #8 allowed 0.28mm local PCB substrate sagging in thermal zone 3.", "Evidence: Laser displacement gauge logged 0.28mm vertical dip on Carrier #8 only.", isTrackA = true)
                WhyStepRow("W3", "Titanium hold-down leaf spring lost retention force due to thermal fatigue beyond 15,000 cycles.", "Evidence: Tension scale showed clamp force fell from nominal 4.2N to 1.1N on Carrier #8.", isTrackA = true)
                WhyStepRow("W4", "Preventive maintenance (PM) cadence lacked load-cell force measurement for pallet clamp clips.", "Evidence: Maintenance log PM-SMT-04 checks only visual presence of springs, not gram-force.", isTrackA = true)

                // W5 Occurrence Root Cause
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(QResolveColors.Error),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("W5", style = QResolveType.CodeBadge, color = QResolveColors.OnError)
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(QResolveColors.ErrorContainer)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("OCCURRENCE ROOT CAUSE IDENTIFIED", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Error)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(QResolveColors.Error)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("VERIFIED ROOT", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.OnError)
                            }
                        }
                        Text(
                            text = "PM procedure PM-SMT-04 did not enforce cycle-count replacement limits or calibrated tension verification on wave carrier titanium leaf clamps.",
                            style = QResolveType.BodySm.copy(fontWeight = FontWeight.Bold),
                            color = QResolveColors.OnErrorContainer
                        )
                        HorizontalDivider(color = QResolveColors.Error.copy(alpha = 0.25f))
                        Text(
                            text = "RECREATION TEST (Turn Defect ON / OFF):",
                            style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                            color = QResolveColors.Error
                        )
                        Text(
                            text = "• DEFECT ON: Defective clamp leaf from Carrier #8 mounted to Golden Pallet #1 recreated 100% bridging across 15 trial boards.",
                            style = QResolveType.CodeBadge.copy(fontSize = 10.sp, fontWeight = FontWeight.Normal),
                            color = QResolveColors.OnErrorContainer
                        )
                        Text(
                            text = "• DEFECT OFF: New tension spring (4.5N) installed on Carrier #8 ran 200 consecutive boards with 0 solder bridges detected (CPK > 1.84).",
                            style = QResolveType.CodeBadge.copy(fontSize = 10.sp, fontWeight = FontWeight.Normal),
                            color = QResolveColors.OnErrorContainer
                        )
                    }
                }
            }

            // TRACK B: Detection / Escape Root Cause Workflow
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(QResolveColors.SurfaceContainerLow)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Default.VisibilityOff, contentDescription = null, tint = QResolveColors.Secondary, modifier = Modifier.size(15.dp))
                        Text(
                            text = "TRACK B: DETECTION / ESCAPE ROOT CAUSE (WHY DID THE DEFECT ESCAPE THE LINE?)",
                            style = QResolveType.LabelCaps.copy(fontSize = 10.sp),
                            color = QResolveColors.Secondary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(QResolveColors.SurfaceContainerLowest)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("Escape Gate Analysis", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Secondary)
                    }
                }

                WhyStepRow("W1", "Post-wave in-line Automated Optical Inspection (AOI) Station failed to halt Pallet #8 boards.", "Evidence: AOI Inspection Log 2505-A shows \"PASS\" flag despite bridge presence.", isTrackA = false)
                WhyStepRow("W2", "Vision algorithm co-axial lighting threshold was throttled down to 65% sensitivity.", "Evidence: Engineering change order ECO-2024-11 reduced threshold to suppress false alarms on shiny gold plating.", isTrackA = false)

                // W3 Escape Root Cause
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(QResolveColors.Error),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("W3", style = QResolveType.CodeBadge, color = QResolveColors.OnError)
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(QResolveColors.SurfaceContainerLowest)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("DETECTION ESCAPE ROOT CAUSE", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Error)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(QResolveColors.ErrorContainer)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("ALGORITHM GAP", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.OnErrorContainer)
                            }
                        }
                        Text(
                            text = "AOI inspection program utilized 2D grayscale contrast only and lacked 3D structured light profilometry / laser triangulation to detect low-profile planar bridges in high-density pin pitches (<0.5mm).",
                            style = QResolveType.BodySm.copy(fontWeight = FontWeight.Bold),
                            color = QResolveColors.OnSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WhyStepRow(
    code: String,
    statement: String,
    evidence: String,
    isTrackA: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(if (isTrackA) QResolveColors.PrimaryContainer else QResolveColors.SecondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = code,
                style = QResolveType.CodeBadge,
                color = if (isTrackA) QResolveColors.OnPrimaryContainer else QResolveColors.OnSecondaryFixed
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(6.dp))
                .background(QResolveColors.SurfaceContainerLowest)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(statement, style = QResolveType.BodySm.copy(fontWeight = FontWeight.SemiBold), color = QResolveColors.OnSurface)
            Text(evidence, style = QResolveType.CodeBadge.copy(fontSize = 10.sp, fontWeight = FontWeight.Normal), color = QResolveColors.Secondary)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun D4ActionFooter(
    onLogMicrograph: () -> Unit,
    onTriggerReadAcross: () -> Unit,
    onSubmitToD5: () -> Unit
) {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(QResolveColors.TertiaryContainer))
                    Text(
                        text = "D4 Root Cause Phase Complete: 100% Recreation Validated",
                        style = QResolveType.CodeBadge,
                        color = QResolveColors.OnSurface
                    )
                }
                Text(
                    text = "Evidence attachments: 4 micrographs, 1 SEM EDX assay, 1 PM gauge study.",
                    style = QResolveType.BodySm,
                    color = QResolveColors.OnSurfaceVariant
                )
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(QResolveColors.SurfaceContainerLow)
                        .clickable { onLogMicrograph() }
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(imageVector = Icons.Default.UploadFile, contentDescription = null, tint = QResolveColors.OnSurface, modifier = Modifier.size(16.dp))
                    Text("Log Supporting Micrograph / CPK Study", style = QResolveType.BodySm, color = QResolveColors.OnSurface)
                }
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(QResolveColors.SurfaceContainerLow)
                        .clickable { onTriggerReadAcross() }
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(imageVector = Icons.Default.DynamicFeed, contentDescription = null, tint = QResolveColors.OnSurface, modifier = Modifier.size(16.dp))
                    Text("Trigger Horizontal Read-Across Notice (D7 Pre-alert)", style = QResolveType.BodySm, color = QResolveColors.OnSurface)
                }
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(QResolveColors.PrimaryContainer)
                        .clickable { onSubmitToD5() }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .testTag("submit_to_d5_button"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Submit Root Cause to D5 PCA Selection", style = QResolveType.HeadlineSm.copy(fontSize = 12.sp), color = QResolveColors.OnPrimaryContainer)
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = QResolveColors.OnPrimaryContainer, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
