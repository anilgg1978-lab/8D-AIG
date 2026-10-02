package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppScreen
import com.example.ui.components.Interactive8DStepper
import com.example.ui.theme.QResolveColors
import com.example.ui.theme.QResolveType

@Composable
fun D5D6PcaScreen(
    batch3Completed: Boolean,
    onCompleteBatch3: () -> Unit,
    onNavigate: (AppScreen) -> Unit,
    onShowToast: (String) -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWide = maxWidth >= 960.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(QResolveColors.Surface)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Incident Header & Action Bar
            D5D6HeaderCard(
                onExportDossier = { onNavigate(AppScreen.EXECUTIVE_DOSSIER) },
                onExportMinitab = { onShowToast("Exported Cpk 1.92 Minitab Dataset (PN8842_PILOT.MTW).") },
                onAdvanceToD7 = {
                    onShowToast("D5/D6 Validation Signed Off! Advancing to D7 Standardization & PFMEA.")
                    onNavigate(AppScreen.D7_STANDARDIZATION)
                }
            )

            // Industrial D1-D8 Progress Stepper Ribbon
            Interactive8DStepper(
                activeStageIndex = 5,
                onNavigate = onNavigate
            )

            // Primary Engineering Split Screen (D5 Left Workbench, D6 Right Validation)
            if (isWide) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(0.58f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        D5PcaSelectionWorkbenchCard()
                        D5EngineeringGateAuthorizationCard(batch3Completed = batch3Completed)
                    }
                    Column(
                        modifier = Modifier.weight(0.42f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        D6PilotValidationRunCard(
                            batch3Completed = batch3Completed,
                            onCompleteBatch3 = onCompleteBatch3
                        )
                        IatfSqeLiveTelemetryFeedCard()
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    D5PcaSelectionWorkbenchCard()
                    D5EngineeringGateAuthorizationCard(batch3Completed = batch3Completed)
                    D6PilotValidationRunCard(
                        batch3Completed = batch3Completed,
                        onCompleteBatch3 = onCompleteBatch3
                    )
                    IatfSqeLiveTelemetryFeedCard()
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun D5D6HeaderCard(
    onExportDossier: () -> Unit,
    onExportMinitab: () -> Unit,
    onAdvanceToD7: () -> Unit
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
            // Breadcrumb & Critical Status
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(QResolveColors.ErrorContainer)
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(QResolveColors.Error))
                        Text("CRITICAL SEVERITY (S=8, O=5, D=6)", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.OnErrorContainer)
                    }
                    Text("8D-20250514-039", style = QResolveType.CodeBadge, color = QResolveColors.OnSurface)
                    Text("IATF CAPA ID: #8491", style = QResolveType.CodeBadge, color = QResolveColors.Secondary)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(QResolveColors.SurfaceContainer)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("PN-8842-B [Rev 04] ECU Engine Management Main Board", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.OnSurfaceVariant)
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = QResolveColors.Tertiary, modifier = Modifier.size(14.dp))
                    Text("Customer: Magna Powertrain / Stellantis N.V. / Plant #4 - SMT Line 2", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                }
            }

            HorizontalDivider(color = QResolveColors.SurfaceContainerHigh)

            // Main Title & Operational Actions
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Pin Solder Bridging Non-Conformance on Automotive ECU Connector (Pins 14–15)",
                            style = QResolveType.HeadlineMd,
                            color = QResolveColors.OnSurface
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(QResolveColors.PrimaryFixed)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text("D5/D6 ACTIVE STAGE", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.OnPrimaryFixed)
                        }
                    }

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("CFT Lead: Marcus Chen (Sr. Quality Engineer)", style = QResolveType.BodySm, color = QResolveColors.OnSurfaceVariant)
                        Text("•", style = QResolveType.BodySm, color = QResolveColors.Outline)
                        Text("Executive Sponsor: Dr. Aris Thorne (VP Operations)", style = QResolveType.BodySm, color = QResolveColors.OnSurfaceVariant)
                        Text("•", style = QResolveType.BodySm, color = QResolveColors.Outline)
                        Text("Opened: May 14, 2025", style = QResolveType.BodySm, color = QResolveColors.OnSurfaceVariant)
                        Text("•", style = QResolveType.BodySm, color = QResolveColors.Outline)
                        Text("Mandatory Target Closure: June 12, 2025 (23 Days left)", style = QResolveType.BodySm, color = QResolveColors.OnSurfaceVariant)
                    }
                }

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(QResolveColors.SurfaceContainer)
                            .clickable { onExportDossier() }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = QResolveColors.OnSurface, modifier = Modifier.size(15.dp))
                        Text("Export AIAG/VDA Dossier", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.OnSurface)
                    }
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(QResolveColors.SurfaceContainer)
                            .clickable { onExportMinitab() }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Analytics, contentDescription = null, tint = QResolveColors.OnSurface, modifier = Modifier.size(15.dp))
                        Text("Minitab Data (.MTW)", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.OnSurface)
                    }
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(QResolveColors.Primary)
                            .clickable { onAdvanceToD7() }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .testTag("advance_to_d7_button"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = QResolveColors.OnPrimary, modifier = Modifier.size(15.dp))
                        Text("Sign-Off & Advance to D7", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.OnPrimary)
                    }
                }
            }

            // Telemetry SLAs & Containment Snapshot Ribbon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TelemetrySnapshotBox(
                    title = "D3 CONTAINMENT ISOLATION",
                    value = "4,200 Pcs Quarantined",
                    valueColor = QResolveColors.Tertiary,
                    badge = "11.4h SLA Met",
                    badgeBg = QResolveColors.SurfaceContainer,
                    badgeFg = QResolveColors.Tertiary
                )
                TelemetrySnapshotBox(
                    title = "D4 ROOT CAUSE VALIDATION",
                    value = "Pallet #8 Flex + AOI 2D Blindspot",
                    valueColor = QResolveColors.OnSurface,
                    badge = "Verified 100%",
                    badgeBg = QResolveColors.SurfaceContainer,
                    badgeFg = QResolveColors.Tertiary
                )
                TelemetrySnapshotBox(
                    title = "D5/D6 VALIDATION SLA CLOCK",
                    value = "4d : 18h : 22m Remaining",
                    valueColor = QResolveColors.Primary,
                    badge = "On Track",
                    badgeBg = QResolveColors.PrimaryFixed,
                    badgeFg = QResolveColors.Primary
                )
                TelemetrySnapshotBox(
                    title = "PILOT PROCESS CAPABILITY",
                    value = "Cpk 1.92 (Target ≥ 1.67)",
                    valueColor = QResolveColors.Tertiary,
                    badge = "0 PPM Defect",
                    badgeBg = QResolveColors.SurfaceContainer,
                    badgeFg = QResolveColors.Tertiary
                )
            }
        }
    }
}

@Composable
private fun TelemetrySnapshotBox(
    title: String,
    value: String,
    valueColor: Color,
    badge: String,
    badgeBg: Color,
    badgeFg: Color
) {
    Row(
        modifier = Modifier
            .width(245.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(QResolveColors.SurfaceContainerLow)
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, style = QResolveType.DataMetricSm.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold), color = valueColor)
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(badgeBg)
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(badge, style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = badgeFg)
        }
    }
}

@Composable
private fun D5PcaSelectionWorkbenchCard() {
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
                    Icon(imageVector = Icons.Default.Architecture, contentDescription = null, tint = QResolveColors.Primary, modifier = Modifier.size(18.dp))
                    Text("D5: Permanent Corrective Action (PCA) Selection", style = QResolveType.HeadlineSm, color = QResolveColors.OnSurface)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(QResolveColors.SurfaceContainer)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("ISO/IATF 16949 §10.2.3 Validated", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.OnSurfaceVariant)
                }
            }

            HorizontalDivider(color = QResolveColors.SurfaceContainerHigh)

            Text(
                text = "Permanent Countermeasures address dual root cause vectors confirmed in D4: Vector 1 (Occurrence) pallet flexure leading to 0.42mm solder meniscus bridge, and Vector 2 (Detection/Escape) 2D AOI grayscale threshold limitation under solder flux glare.",
                style = QResolveType.BodyMd,
                color = QResolveColors.OnSurfaceVariant
            )

            // Focus 1: Occurrence Countermeasures
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(imageVector = Icons.Default.Build, contentDescription = null, tint = QResolveColors.Primary, modifier = Modifier.size(14.dp))
                    Text("OCCURRENCE COUNTERMEASURES (CARRIER #8 FLEXURE & SPRING FATIGUE)", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                }
                Text("Selected: Option 1A", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Tertiary)
            }

            // Option 1A: Selected
            PcaOptionCard(
                isSelected = true,
                isRejected = false,
                title = "Option 1A: Mechanical Poka-Yoke Toggle Clamp + RFID Cycle Interlock",
                statusBadge = "SELECTED FOR PILOT",
                scoreText = "SCORE: 9.4 / 10",
                subScoreText = "Projected Cpk: 2.14",
                description = "Retrofit pallet hold-downs with hardened beryllium-copper dual-pivot toggle clamps (14.2 N constant downward force) and integrate RFID cycle counter locked to 15,000 cycles preventive refurbishment.",
                metrics = listOf(
                    "CapEx: $3,200 (Tooling)",
                    "Lead Time: 3 Working Days",
                    "Side Effect: Negligible (RPN < 24)",
                    "Recreation Proof: 100% Clearance"
                )
            )

            // Option 1B: Ruled Out
            PcaOptionCard(
                isSelected = false,
                isRejected = false,
                title = "Option 1B: Pneumatic Hold-Down Cylinder Retrofit",
                statusBadge = "RULED OUT",
                scoreText = "SCORE: 6.8 / 10",
                subScoreText = "High Line Dependency",
                description = "Requires continuous shop-air umbilical coupling during wave conveyor transfer; high leak vulnerability and $18,400 overhaul cost across 42 carrier pallets."
            )

            // Option 1C: Rejected
            PcaOptionCard(
                isSelected = false,
                isRejected = true,
                title = "Option 1C: Shave Connector Body Clearance by 0.30mm",
                statusBadge = "REJECTED (OEM PPAP)",
                scoreText = "SCORE: 3.2 / 10",
                subScoreText = "Rejected by OEM SQE",
                description = "Modifies OEM Stellantis connector mold injection tooling. Requires Level 4 PPAP customer resubmission, 12-week lead time, and $45,000 re-validation fee."
            )

            // Focus 2: Escape / Detection Countermeasures
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(imageVector = Icons.Default.Visibility, contentDescription = null, tint = QResolveColors.Primary, modifier = Modifier.size(14.dp))
                    Text("DETECTION / ESCAPE COUNTERMEASURES (AOI POST-WAVE STATION 4)", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                }
                Text("Selected: Option 2A", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Tertiary)
            }

            // Option 2A: Selected
            PcaOptionCard(
                isSelected = true,
                isRejected = false,
                title = "Option 2A: 3D Multi-Frequency Phase-Shift Profilometry & Laser Triangulation",
                statusBadge = "DEPLOYED ON LINE 2",
                scoreText = "SCORE: 9.6 / 10",
                subScoreText = "Gauge R&R: 6.4%",
                description = "Replaces 2D monochrome camera inspection with 3D topographic volume and height sensor (0.008mm Z-accuracy). Detects micro-bridging regardless of solder alloy reflectivity or meniscus gloss."
            )

            // Option 2B: Ruled Out
            PcaOptionCard(
                isSelected = false,
                isRejected = false,
                title = "Option 2B: Dedicated Human Visual Sorter with 40x Stereo Microscope",
                statusBadge = "RULED OUT",
                scoreText = "SCORE: 5.1 / 10",
                subScoreText = "High Escape Residual",
                description = "Subject to human optical fatigue (>18% escape risk in high-speed 1.4s takt time). Violates zero-defect IATF requirement for automated automotive safety components."
            )

            // Comparative Multi-Criteria Decision Matrix Table
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("CFT TRADE-OFF DECISION MATRIX (AIAG STANDARD)", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Outline)
                Text("Weights: Risk (35%), Cpk (30%), Timing (20%), Cost (15%)", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Secondary)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(QResolveColors.SurfaceContainerLow)
                    .horizontalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier
                        .background(QResolveColors.SurfaceContainer)
                        .padding(8.dp)
                ) {
                    Text("COUNTERMEASURE ID", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary, modifier = Modifier.width(175.dp))
                    Text("FEASIBILITY", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary, modifier = Modifier.width(80.dp))
                    Text("SIDE EFFECTS", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary, modifier = Modifier.width(125.dp))
                    Text("COST IMPACT", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary, modifier = Modifier.width(90.dp))
                    Text("TIME", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary, modifier = Modifier.width(70.dp))
                    Text("WEIGHTED RANK", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary, modifier = Modifier.width(95.dp))
                    Text("ACTION", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary, modifier = Modifier.width(80.dp))
                }
                DecisionMatrixRow("1A + 2A (Bespoke Clamp + 3D AOI)", "9.8 / 10", "Zero (Tested)", "$9,400 Net", "3 Days", "9.50 #1", "ADOPTED", isWinner = true, isError = false)
                DecisionMatrixRow("1B + 2A (Pneumatic + 3D AOI)", "7.0 / 10", "Line Hose Trip Risk", "$24,600", "18 Days", "6.95 #2", "REJECT", isWinner = false, isError = false)
                DecisionMatrixRow("1C + 2B (Tooling Shave + Manual)", "4.2 / 10", "Customer Re-homologation", "$52,000+", "84 Days", "3.80 #3", "DISMISSED", isWinner = false, isError = true)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PcaOptionCard(
    isSelected: Boolean,
    isRejected: Boolean,
    title: String,
    statusBadge: String,
    scoreText: String,
    subScoreText: String,
    description: String,
    metrics: List<String> = emptyList()
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) QResolveColors.SurfaceContainerLowest else QResolveColors.SurfaceContainerLow)
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .heightIn(min = 90.dp)
                    .background(QResolveColors.Primary)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = when {
                            isSelected -> Icons.Default.CheckBox
                            isRejected -> Icons.Default.Close
                            else -> Icons.Default.RadioButtonUnchecked
                        },
                        contentDescription = null,
                        tint = if (isSelected) QResolveColors.Primary else QResolveColors.Outline,
                        modifier = Modifier.size(18.dp).padding(top = 2.dp)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = title,
                                style = QResolveType.HeadlineSm.copy(
                                    textDecoration = if (isRejected) TextDecoration.LineThrough else TextDecoration.None
                                ),
                                color = QResolveColors.OnSurface
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        when {
                                            isSelected -> QResolveColors.TertiaryFixed
                                            isRejected -> QResolveColors.ErrorContainer
                                            else -> QResolveColors.SurfaceContainerHighest
                                        }
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = statusBadge,
                                    style = QResolveType.CodeBadge.copy(fontSize = 9.sp),
                                    color = when {
                                        isSelected -> QResolveColors.OnTertiaryFixed
                                        isRejected -> QResolveColors.OnErrorContainer
                                        else -> QResolveColors.Secondary
                                    }
                                )
                            }
                        }
                        Text(description, style = QResolveType.BodySm, color = QResolveColors.OnSurfaceVariant)
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = scoreText,
                        style = QResolveType.CodeBadge,
                        color = when {
                            isSelected -> QResolveColors.Primary
                            isRejected -> QResolveColors.Error
                            else -> QResolveColors.Secondary
                        }
                    )
                    Text(subScoreText, style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary)
                }
            }

            if (metrics.isNotEmpty()) {
                HorizontalDivider(color = QResolveColors.SurfaceContainer)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    metrics.forEach { m ->
                        Text(m, style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                    }
                }
            }
        }
    }
}

@Composable
private fun DecisionMatrixRow(
    id: String,
    feasibility: String,
    sideEffects: String,
    cost: String,
    time: String,
    rank: String,
    action: String,
    isWinner: Boolean,
    isError: Boolean
) {
    Row(
        modifier = Modifier
            .background(if (isWinner) QResolveColors.SurfaceContainerLowest else QResolveColors.SurfaceContainerLow)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = id,
            style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
            color = if (isWinner) QResolveColors.Primary else QResolveColors.OnSurfaceVariant,
            modifier = Modifier.width(175.dp)
        )
        Text(
            text = feasibility,
            style = QResolveType.BodySm,
            color = if (isWinner) QResolveColors.Tertiary else QResolveColors.OnSurfaceVariant,
            modifier = Modifier.width(80.dp)
        )
        Text(
            text = sideEffects,
            style = QResolveType.BodySm,
            color = when {
                isWinner -> QResolveColors.Tertiary
                isError -> QResolveColors.Error
                else -> QResolveColors.Outline
            },
            modifier = Modifier.width(125.dp)
        )
        Text(cost, style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.OnSurface, modifier = Modifier.width(90.dp))
        Text(time, style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.OnSurface, modifier = Modifier.width(70.dp))
        Text(
            text = rank,
            style = QResolveType.CodeBadge,
            color = when {
                isWinner -> QResolveColors.Tertiary
                isError -> QResolveColors.Error
                else -> QResolveColors.Secondary
            },
            modifier = Modifier.width(95.dp)
        )
        Box(
            modifier = Modifier
                .width(80.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(if (isWinner) QResolveColors.Tertiary else QResolveColors.SurfaceContainer)
                .padding(horizontal = 6.dp, vertical = 2.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = action,
                style = QResolveType.CodeBadge.copy(fontSize = 9.sp),
                color = if (isWinner) QResolveColors.OnTertiary else QResolveColors.Secondary
            )
        }
    }
    HorizontalDivider(color = QResolveColors.SurfaceContainer)
}

@Composable
private fun D5EngineeringGateAuthorizationCard(batch3Completed: Boolean) {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(imageVector = Icons.Default.VerifiedUser, contentDescription = null, tint = QResolveColors.Secondary, modifier = Modifier.size(18.dp))
                    Text("D5 Engineering Gate Authorization", style = QResolveType.HeadlineSm, color = QResolveColors.OnSurface)
                }
                Text(
                    text = if (batch3Completed) "✓ 4 of 4 Signatures Affixed" else "✓ 3 of 4 Signatures Affixed",
                    style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                    color = QResolveColors.Tertiary
                )
            }
            HorizontalDivider(color = QResolveColors.SurfaceContainerHigh)

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                GateSignCard("DK", "Dave Kowalski", "Senior Tooling Engineer", "FAT APPROVED", "May 19, 14:15 EST", isPending = false, modifier = Modifier.weight(1f))
                GateSignCard("ER", "Elena Rostova", "Automation Controls Specialist", "PLC INTERLOCK PASS", "May 20, 09:30 EST", isPending = false, modifier = Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                GateSignCard("MC", "Marcus Chen", "CFT Lead / Plant QA Lead", "STATS VALIDATED", "May 21, 08:00 EST", isPending = false, modifier = Modifier.weight(1f))
                GateSignCard(
                    initials = "ST",
                    name = "Stellantis SQE Audit Desk",
                    role = "External OEM Approval Gate",
                    badge = if (batch3Completed) "LOT 3 APPROVED" else "PENDING LOT 3",
                    time = if (batch3Completed) "Just Now • API Verified" else "Auto-Notified via API",
                    isPending = !batch3Completed,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun GateSignCard(
    initials: String,
    name: String,
    role: String,
    badge: String,
    time: String,
    isPending: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isPending) QResolveColors.PrimaryFixed else QResolveColors.SurfaceContainerLow)
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(if (isPending) QResolveColors.Primary else QResolveColors.TertiaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(initials, style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = Color.White)
            }
            Column {
                Text(name, style = QResolveType.BodyMd.copy(fontWeight = FontWeight.SemiBold), color = QResolveColors.OnSurface)
                Text(role, style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary)
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (isPending) QResolveColors.Primary else QResolveColors.Tertiary)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(badge, style = QResolveType.CodeBadge.copy(fontSize = 8.sp), color = Color.White)
            }
            Text(time, style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = if (isPending) QResolveColors.Primary else QResolveColors.Outline)
        }
    }
}

@Composable
private fun D6PilotValidationRunCard(
    batch3Completed: Boolean,
    onCompleteBatch3: () -> Unit
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
                    Icon(imageVector = Icons.Default.Science, contentDescription = null, tint = QResolveColors.Primary, modifier = Modifier.size(18.dp))
                    Text("D6: Pilot Validation Run", style = QResolveType.HeadlineSm, color = QResolveColors.OnSurface)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(QResolveColors.PrimaryContainer)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text("PILOT-2025-0520-A", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.OnPrimaryContainer)
                }
            }
            HorizontalDivider(color = QResolveColors.SurfaceContainerHigh)

            // Pilot Parameters 2x2 Grid
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(QResolveColors.SurfaceContainerLow)
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("TEST PARAMETER", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary)
                        Text("500 Board Production Trial", style = QResolveType.BodySm.copy(fontWeight = FontWeight.SemiBold), color = QResolveColors.OnSurface)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("TOOLING CONFIG", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary)
                        Text("Pallet #8 Mod + 3D Profiler", style = QResolveType.BodySm.copy(fontWeight = FontWeight.SemiBold), color = QResolveColors.OnSurface)
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("SPEED / TAKT TIME", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary)
                        Text("1.38 sec / board (100% load)", style = QResolveType.BodySm.copy(fontWeight = FontWeight.SemiBold), color = QResolveColors.OnSurface)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("PRE-HEATER TEMP CURVE", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary)
                        Text("122°C ± 2°C (Calibrated)", style = QResolveType.BodySm.copy(fontWeight = FontWeight.SemiBold), color = QResolveColors.OnSurface)
                    }
                }
            }

            // Statistical Process Capability (Cpk) Graph & Metric Box
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(QResolveColors.SurfaceContainerLow)
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ShowChart, contentDescription = null, tint = QResolveColors.Primary, modifier = Modifier.size(15.dp))
                        Text("PROCESS CAPABILITY (CPK) VERIFICATION", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(QResolveColors.SurfaceContainer)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("TARGET EXCEEDED: Cpk = 1.92", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Tertiary)
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(QResolveColors.SurfaceContainerLowest)
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("PRE-FIX BASELINE", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Error)
                        Text("0.84", style = QResolveType.DataMetricLg, color = QResolveColors.Error)
                        Text("High Meniscus Breach (4.2% PPM)", style = QResolveType.BodySm.copy(fontSize = 11.sp), color = QResolveColors.Error)
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(QResolveColors.SurfaceContainerLowest)
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("POST-PCA PILOT", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Tertiary)
                        Text("1.92", style = QResolveType.DataMetricLg, color = QResolveColors.Tertiary)
                        Text("0 Defectives across 500 Pcs", style = QResolveType.BodySm.copy(fontSize = 11.sp), color = QResolveColors.Tertiary)
                    }
                }

                // Bell Curve SVG Representation (Normal Distribution Cpk Shift)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(QResolveColors.SurfaceContainerLowest)
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("LSL (0.15mm)", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Outline)
                        Text("Nominal (0.45mm)", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Tertiary)
                        Text("USL (0.75mm)", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Outline)
                    }

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(82.dp)
                            .padding(vertical = 4.dp)
                    ) {
                        val w = size.width
                        val h = size.height
                        val dashEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)

                        // Vertical spec lines
                        listOf(0.14f, 0.5f, 0.86f).forEach { frac ->
                            drawLine(
                                color = Color(0xFFC3C6D7),
                                start = Offset(w * frac, h * 0.08f),
                                end = Offset(w * frac, h * 0.9f),
                                strokeWidth = 1.dp.toPx(),
                                pathEffect = dashEffect
                            )
                        }

                        // Baseline curve (red dashed, shifted left)
                        val baselinePath = Path().apply {
                            moveTo(w * 0.04f, h * 0.88f)
                            quadraticTo(w * 0.20f, h * 0.84f, w * 0.28f, h * 0.44f)
                            quadraticTo(w * 0.35f, h * 0.16f, w * 0.42f, h * 0.44f)
                            quadraticTo(w * 0.52f, h * 0.84f, w * 0.68f, h * 0.88f)
                        }
                        drawPath(
                            path = baselinePath,
                            color = Color(0xFFBA1A1A),
                            style = Stroke(width = 1.8.dp.toPx(), pathEffect = dashEffect)
                        )

                        // Post-PCA curve (green filled + solid stroke, centered at 0.5)
                        val postPath = Path().apply {
                            moveTo(w * 0.25f, h * 0.88f)
                            quadraticTo(w * 0.42f, h * 0.86f, w * 0.47f, h * 0.25f)
                            quadraticTo(w * 0.50f, h * 0.04f, w * 0.53f, h * 0.25f)
                            quadraticTo(w * 0.58f, h * 0.86f, w * 0.75f, h * 0.88f)
                        }
                        drawPath(path = postPath, color = Color(0x22006243))
                        drawPath(path = postPath, color = Color(0xFF006243), style = Stroke(width = 2.4.dp.toPx()))
                        drawCircle(color = Color(0xFF006243), radius = 3.5.dp.toPx(), center = Offset(w * 0.5f, h * 0.12f))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text("— Prior Shift Baseline (Cpk 0.84)", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Error)
                        Text("— New Validated Process (Cpk 1.92)", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Tertiary)
                    }
                }

                // Gauge R&R Verification Tag
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(QResolveColors.SurfaceContainer)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("AOI 3D Gauge R&R: 6.4% of Tolerance", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.OnSurface)
                    Text("< 10% AIAG Standard (Passed)", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Tertiary)
                }
            }

            // 3-Batch Zero-Defect Production Gate
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.FactCheck, contentDescription = null, tint = QResolveColors.Primary, modifier = Modifier.size(15.dp))
                    Text("3-BATCH ZERO-DEFECT GATE", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                }
                Text(
                    text = if (batch3Completed) "3 of 3 BATCHES VERIFIED" else "2 of 3 BATCHES VERIFIED",
                    style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                    color = if (batch3Completed) QResolveColors.Tertiary else QResolveColors.Primary
                )
            }

            BatchGateRow("Batch 1: Lot #2505-B", "1,400 / 1,400 Units Processed", "0 DEFECTS (PASS)", "Verified 100% 3D AOI")
            BatchGateRow("Batch 2: Lot #2505-C", "1,400 / 1,400 Units Processed", "0 DEFECTS (PASS)", "Verified 100% 3D AOI")

            // Batch 3 (In-Progress or Completed)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (batch3Completed) QResolveColors.SurfaceContainerLow else QResolveColors.PrimaryFixed)
                    .clickable { if (!batch3Completed) onCompleteBatch3() }
                    .padding(10.dp)
                    .testTag("batch_3_gate_card"),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(
                            imageVector = if (batch3Completed) Icons.Default.CheckCircle else Icons.Default.Sync,
                            contentDescription = null,
                            tint = if (batch3Completed) QResolveColors.Tertiary else QResolveColors.Primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Column {
                            Text(
                                text = if (batch3Completed) "Batch 3: Lot #2505-D (COMPLETE)" else "Batch 3: Lot #2505-D (RUNNING)",
                                style = QResolveType.BodySm.copy(fontWeight = FontWeight.Bold),
                                color = QResolveColors.OnPrimaryFixed
                            )
                            Text(
                                text = if (batch3Completed) "1,400 / 1,400 Units Complete" else "820 of 1,400 Units Complete (Tap to Verify Final 580)",
                                style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                                color = QResolveColors.Secondary
                            )
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (batch3Completed) QResolveColors.Tertiary else QResolveColors.Primary)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (batch3Completed) "0 DEFECTS (PASS)" else "0 DEFECTS SO FAR",
                                style = QResolveType.CodeBadge.copy(fontSize = 9.sp),
                                color = Color.White
                            )
                        }
                        Text(
                            text = if (batch3Completed) "Verified 100% 3D AOI" else "Line 2 Running Takt",
                            style = QResolveType.CodeBadge.copy(fontSize = 9.sp),
                            color = if (batch3Completed) QResolveColors.Outline else QResolveColors.Primary
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(50))
                        .background(QResolveColors.SurfaceContainerLowest)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(if (batch3Completed) 1f else 0.585f)
                            .fillMaxHeight()
                            .background(if (batch3Completed) QResolveColors.Tertiary else QResolveColors.Primary)
                    )
                }
            }

            // Quarantine Disassembly Lock Warning Box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (batch3Completed) QResolveColors.TertiaryFixed else QResolveColors.SurfaceContainer)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = if (batch3Completed) Icons.Default.LockOpen else Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (batch3Completed) QResolveColors.Tertiary else QResolveColors.Secondary,
                    modifier = Modifier.size(18.dp)
                )
                Column {
                    Text(
                        text = if (batch3Completed) "Containment Exit Gate Status: UNLOCKED & AUTHORIZED" else "Containment Exit Gate Status: LOCKED",
                        style = QResolveType.BodySm.copy(fontWeight = FontWeight.Bold),
                        color = QResolveColors.OnSurface
                    )
                    Text(
                        text = if (batch3Completed) {
                            "All 3 production batches (4,200 units) passed with 0 defects. 200% secondary sorting station authorized for dismantling."
                        } else {
                            "Authorization to release the 4,200 quarantined ECU assemblies and dismantle the 200% secondary sorting station remains locked until Batch 3 completes its final 580 units with zero bridging defects."
                        },
                        style = QResolveType.BodySm,
                        color = QResolveColors.OnSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun BatchGateRow(
    title: String,
    sub: String,
    badge: String,
    badgeSub: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(QResolveColors.SurfaceContainerLow)
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = QResolveColors.Tertiary, modifier = Modifier.size(18.dp))
            Column {
                Text(title, style = QResolveType.BodySm.copy(fontWeight = FontWeight.Bold), color = QResolveColors.OnSurface)
                Text(sub, style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(QResolveColors.Tertiary)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(badge, style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.OnTertiary)
            }
            Text(badgeSub, style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Outline)
        }
    }
}

@Composable
private fun IatfSqeLiveTelemetryFeedCard() {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("IATF SQE LIVE TELEMETRY FEED", style = QResolveType.LabelCaps, color = QResolveColors.Secondary)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(QResolveColors.Tertiary))
                    Text("Sync Active", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Tertiary)
                }
            }
            HorizontalDivider(color = QResolveColors.SurfaceContainerHigh)

            val logs = listOf(
                Triple("[11:42:01 EST]", "Carrier #8 clamp strain gauge: 14.18N (Nominal 14.2N)", "OK" to QResolveColors.Tertiary),
                Triple("[11:45:18 EST]", "3D AOI Pin 14-15 solder clearance: 0.448mm (Std: 0.450mm)", "OK" to QResolveColors.Tertiary),
                Triple("[11:48:30 EST]", "Stellantis SQE Portal acknowledged interim D5 submission", "ACK" to QResolveColors.Primary)
            )

            logs.forEach { (time, msg, statusPair) ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(time, style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                    Text(
                        text = msg,
                        style = QResolveType.CodeBadge.copy(fontSize = 10.sp, fontWeight = FontWeight.Normal),
                        color = QResolveColors.OnSurfaceVariant,
                        modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                        maxLines = 1
                    )
                    Text(statusPair.first, style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = statusPair.second)
                }
            }
        }
    }
}
