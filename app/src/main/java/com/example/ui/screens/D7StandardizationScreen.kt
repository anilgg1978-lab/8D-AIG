package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AltRoute
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppScreen
import com.example.model.HotlinkedImages
import com.example.ui.components.HotlinkedNetworkImage
import com.example.ui.components.Interactive8DStepper
import com.example.ui.theme.QResolveColors
import com.example.ui.theme.QResolveType

@Composable
fun D7StandardizationScreen(
    onLockD7AndProceed: () -> Unit,
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
            // D1-D8 Process Stepper Ribbon
            Interactive8DStepper(
                activeStageIndex = 7,
                onNavigate = onNavigate
            )

            // Incident Case Header Bar
            D7HeaderCard(
                onExportD7Packet = { onNavigate(AppScreen.EXECUTIVE_DOSSIER) },
                onSyncToPlm = { onShowToast("PLM ECN #ECN-2025-0418 & MES Terminal Rev 4.0 synchronized.") },
                onAdvanceToD8 = onLockD7AndProceed
            )

            // Key Metrics / Health KPI Row
            D7KpiRow(isWide = isWide)

            // SECTION 1: Dynamic PFMEA Risk Ledger
            PfmeaDynamicRiskLedgerCard()

            // SECTION 2 & 3 Side-by-Side: Control Plan Updates & SOP Verification
            if (isWide) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    ControlPlanRevisionCard(modifier = Modifier.weight(0.58f))
                    StandardWorkTrainingCard(modifier = Modifier.weight(0.42f))
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ControlPlanRevisionCard(modifier = Modifier.fillMaxWidth())
                    StandardWorkTrainingCard(modifier = Modifier.fillMaxWidth())
                }
            }

            // SECTION 4: Horizontal Read-Across Matrix
            HorizontalReadAcrossMatrixCard()

            // SECTION 5: D7 Standardization Sign-Off & Verification Gate
            D7SignOffVerificationGateCard(
                isWide = isWide,
                onLockD7AndProceed = onLockD7AndProceed
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun D7HeaderCard(
    onExportD7Packet: () -> Unit,
    onSyncToPlm: () -> Unit,
    onAdvanceToD8: () -> Unit
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
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(QResolveColors.ErrorContainer)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text("CRITICAL SEVERITY (S=8)", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.OnErrorContainer)
                    }
                    Text("8D-20250514-039", style = QResolveType.HeadlineMd, color = QResolveColors.OnSurface)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(QResolveColors.SurfaceContainer)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("IATF CAPA #8491", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(QResolveColors.SurfaceContainer)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("IATF 16949 §10.2.3 CONFORMANCE", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Primary)
                    }
                }

                Text(
                    text = "Part: PN-8842-B [Rev 04 → Rev 05 Synced] ECU Engine Management Main Board • Customer: Magna Powertrain / Stellantis N.V. • Location: Plant #4 SMT Line 2 (Wave Solder Cell C) • CFT Lead: Marcus Chen (Sr. QE)",
                    style = QResolveType.BodySm,
                    color = QResolveColors.Secondary
                )
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(QResolveColors.SurfaceContainerHigh)
                        .clickable { onExportD7Packet() }
                        .padding(horizontal = 10.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(imageVector = Icons.Default.PictureAsPdf, contentDescription = null, tint = QResolveColors.OnSurface, modifier = Modifier.size(16.dp))
                    Text("Export AIAG/VDA D7 Packet", style = QResolveType.HeadlineSm.copy(fontSize = 12.sp), color = QResolveColors.OnSurface)
                }
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(QResolveColors.SurfaceContainerHigh)
                        .clickable { onSyncToPlm() }
                        .padding(horizontal = 10.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(imageVector = Icons.Default.SyncAlt, contentDescription = null, tint = QResolveColors.OnSurface, modifier = Modifier.size(16.dp))
                    Text("Sync to PLM / MES", style = QResolveType.HeadlineSm.copy(fontSize = 12.sp), color = QResolveColors.OnSurface)
                }
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(QResolveColors.PrimaryContainer)
                        .clickable { onAdvanceToD8() }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                        .testTag("advance_to_d8_button"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(imageVector = Icons.Default.Verified, contentDescription = null, tint = QResolveColors.OnPrimaryContainer, modifier = Modifier.size(16.dp))
                    Text("Advance to D8 Sign-Off", style = QResolveType.HeadlineSm.copy(fontSize = 12.sp), color = QResolveColors.OnPrimaryContainer)
                }
            }
        }
    }
}

@Composable
private fun D7KpiRow(isWide: Boolean) {
    @Composable
    fun Kpi1(modifier: Modifier) {
        Surface(color = QResolveColors.SurfaceContainerLowest, shape = RoundedCornerShape(10.dp), shadowElevation = 1.dp, modifier = modifier) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("PFMEA RISK ABATEMENT", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                    Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(QResolveColors.TertiaryFixed).padding(horizontal = 6.dp, vertical = 2.dp)) {
                        Text("-90.5% RPN", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.OnTertiaryFixedVariant)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("252", style = QResolveType.DataMetricLg.copy(textDecoration = TextDecoration.LineThrough), color = QResolveColors.Error)
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = QResolveColors.Secondary, modifier = Modifier.size(16.dp))
                    Text("24", style = QResolveType.DataMetricLg, color = QResolveColors.Tertiary)
                    Text("(S8 • O2 • D2)", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Action Priority: Low (L)", style = QResolveType.BodySm, color = QResolveColors.Tertiary)
                    Text("AIAG-VDA 1st Ed", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.OnSurfaceVariant)
                }
            }
        }
    }

    @Composable
    fun Kpi2(modifier: Modifier) {
        Surface(color = QResolveColors.SurfaceContainerLowest, shape = RoundedCornerShape(10.dp), shadowElevation = 1.dp, modifier = modifier) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("CONTROL PLAN STATUS", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                    Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(QResolveColors.SurfaceContainerHigh).padding(horizontal = 6.dp, vertical = 2.dp)) {
                        Text("Rev 4.0 Active", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Primary)
                    }
                }
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("100%", style = QResolveType.DataMetricLg, color = QResolveColors.OnSurface)
                    Text("Pushed to Shop Floor", style = QResolveType.BodySm, color = QResolveColors.Secondary, modifier = Modifier.padding(bottom = 2.dp))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Doc: CP-DET-SMT2-WSC", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.OnSurface)
                    Text("3 Line Terminals Live", style = QResolveType.BodySm.copy(fontWeight = FontWeight.SemiBold), color = QResolveColors.Tertiary)
                }
            }
        }
    }

    @Composable
    fun Kpi3(modifier: Modifier) {
        Surface(color = QResolveColors.SurfaceContainerLowest, shape = RoundedCornerShape(10.dp), shadowElevation = 1.dp, modifier = modifier) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("SOP / OPERATOR TRAINING", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                    Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(QResolveColors.TertiaryFixed).padding(horizontal = 6.dp, vertical = 2.dp)) {
                        Text("24 / 24 Certified", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.OnTertiaryFixedVariant)
                    }
                }
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("3 of 3", style = QResolveType.DataMetricLg, color = QResolveColors.OnSurface)
                    Text("Work Instructions Live", style = QResolveType.BodySm, color = QResolveColors.Secondary, modifier = Modifier.padding(bottom = 2.dp))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Shifts A, B, C Completed", style = QResolveType.BodySm, color = QResolveColors.Secondary)
                    Text("LMS Audit Synced", style = QResolveType.BodySm.copy(fontWeight = FontWeight.SemiBold), color = QResolveColors.Tertiary)
                }
            }
        }
    }

    @Composable
    fun Kpi4(modifier: Modifier) {
        Surface(color = QResolveColors.SurfaceContainerLowest, shape = RoundedCornerShape(10.dp), shadowElevation = 1.dp, modifier = modifier) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("READ-ACROSS DIFFUSION", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                    Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(QResolveColors.SecondaryFixed).padding(horizontal = 6.dp, vertical = 2.dp)) {
                        Text("4 Facilities Scoped", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.OnSecondaryFixed)
                    }
                }
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("2 ECNs", style = QResolveType.DataMetricLg, color = QResolveColors.Primary)
                    Text("Spawned Across Fleet", style = QResolveType.BodySm, color = QResolveColors.Secondary, modifier = Modifier.padding(bottom = 2.dp))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Detroit Line 1 Retrofit (90%)", style = QResolveType.BodySm, color = QResolveColors.Secondary)
                    Text("Saltillo QA-Alert", style = QResolveType.BodySm, color = QResolveColors.Secondary)
                }
            }
        }
    }

    if (isWide) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            Kpi1(Modifier.weight(1f))
            Kpi2(Modifier.weight(1f))
            Kpi3(Modifier.weight(1f))
            Kpi4(Modifier.weight(1f))
        }
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Kpi1(Modifier.weight(1f))
                Kpi2(Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Kpi3(Modifier.weight(1f))
                Kpi4(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun PfmeaDynamicRiskLedgerCard() {
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
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(QResolveColors.PrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.AccountTree, contentDescription = null, tint = QResolveColors.OnPrimaryContainer, modifier = Modifier.size(18.dp))
                    }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("1. Process FMEA (PFMEA) Dynamic Risk Ledger", style = QResolveType.HeadlineSm, color = QResolveColors.OnSurface)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(QResolveColors.SurfaceContainerHighest)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("AIAG & VDA Harmonized", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Secondary)
                            }
                        }
                        Text("Root-cause failure mode mitigation, risk re-rating, and direct Enovia/Teamcenter ECN linkages", style = QResolveType.BodySm, color = QResolveColors.Secondary)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("PLM ECN:", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(QResolveColors.SurfaceContainerLowest)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("#ECN-2025-0418", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Primary)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(QResolveColors.TertiaryFixed)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("SYNC VALIDATED", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.OnTertiaryFixedVariant)
                    }
                }
            }

            // Comparative Table
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier
                        .background(QResolveColors.SurfaceContainerLow)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("PROCESS STEP / FUNCTION", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary, modifier = Modifier.width(155.dp))
                    Text("POTENTIAL FAILURE MODE & EFFECTS", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary, modifier = Modifier.width(225.dp))
                    Text("PRE S", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.OnSurface, modifier = Modifier.width(42.dp))
                    Text("PRE O", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.OnSurface, modifier = Modifier.width(42.dp))
                    Text("PRE D", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.OnSurface, modifier = Modifier.width(42.dp))
                    Text("PRE RPN", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Error, modifier = Modifier.width(65.dp))
                    Text("PERMANENT CORRECTIVE ACTIONS (STANDARDIZED IN D7)", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary, modifier = Modifier.width(280.dp))
                    Text("POST S", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.OnSurface, modifier = Modifier.width(46.dp))
                    Text("POST O", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.OnSurface, modifier = Modifier.width(46.dp))
                    Text("POST D", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.OnSurface, modifier = Modifier.width(46.dp))
                    Text("POST RPN", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Tertiary, modifier = Modifier.width(68.dp))
                    Text("AP", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary, modifier = Modifier.width(70.dp))
                }
                HorizontalDivider(color = QResolveColors.SurfaceContainer)

                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.width(155.dp).padding(end = 8.dp)) {
                        Text("Station 40", style = QResolveType.HeadlineSm, color = QResolveColors.OnSurface)
                        Text("Wave Solder Pallet Conveyance & Pre-Heat", style = QResolveType.BodySm, color = QResolveColors.Secondary)
                        Text("Operation ID: OP-040-WSC2", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Outline)
                    }
                    Column(modifier = Modifier.width(225.dp).padding(end = 8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("FAILURE MODE:", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Error)
                        Text("Carrier leaf spring tension decay > 0.28mm deflection into solder wave.", style = QResolveType.BodySm, color = QResolveColors.OnSurface)
                        Text("EFFECT:", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Secondary)
                        Text("Pin 14-15 solder bridging causing ECU intermittent CAN bus short circuit.", style = QResolveType.BodySm, color = QResolveColors.Secondary)
                    }
                    Text("8", style = QResolveType.CodeBadge, color = QResolveColors.OnSurface, modifier = Modifier.width(42.dp))
                    Text("6", style = QResolveType.CodeBadge, color = QResolveColors.Error, modifier = Modifier.width(42.dp))
                    Text("7", style = QResolveType.CodeBadge, color = QResolveColors.Error, modifier = Modifier.width(42.dp))
                    Column(modifier = Modifier.width(65.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(QResolveColors.ErrorContainer).padding(horizontal = 6.dp, vertical = 2.dp)) {
                            Text("336", style = QResolveType.DataMetricSm.copy(fontWeight = FontWeight.Bold), color = QResolveColors.OnErrorContainer)
                        }
                        Text("High (H)", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Error)
                    }
                    Column(modifier = Modifier.width(280.dp).padding(horizontal = 8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(QResolveColors.SurfaceContainerLow).padding(6.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(modifier = Modifier.clip(RoundedCornerShape(3.dp)).background(QResolveColors.PrimaryContainer).padding(horizontal = 4.dp, vertical = 1.dp)) {
                                Text("POKA-YOKE", style = QResolveType.CodeBadge.copy(fontSize = 8.sp), color = Color.White)
                            }
                            Text("Option 1A: Beryllium-copper toggle clamp + RFID cycle-life interlock (15,000 cycles).", style = QResolveType.BodySm.copy(fontSize = 11.sp), color = QResolveColors.OnSurface)
                        }
                        Row(
                            modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(QResolveColors.SurfaceContainerLow).padding(6.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(modifier = Modifier.clip(RoundedCornerShape(3.dp)).background(QResolveColors.Tertiary).padding(horizontal = 4.dp, vertical = 1.dp)) {
                                Text("DETECTION", style = QResolveType.CodeBadge.copy(fontSize = 8.sp), color = Color.White)
                            }
                            Text("Option 2A: 3D Multi-Frequency AOI Profilometry (0.008mm Z-accuracy) with 100% SPC.", style = QResolveType.BodySm.copy(fontSize = 11.sp), color = QResolveColors.OnSurface)
                        }
                    }
                    Text("8", style = QResolveType.CodeBadge, color = QResolveColors.OnSurface, modifier = Modifier.width(46.dp))
                    Text("2", style = QResolveType.CodeBadge, color = QResolveColors.Tertiary, modifier = Modifier.width(46.dp))
                    Text("2", style = QResolveType.CodeBadge, color = QResolveColors.Tertiary, modifier = Modifier.width(46.dp))
                    Column(modifier = Modifier.width(68.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(QResolveColors.TertiaryFixed).padding(horizontal = 6.dp, vertical = 2.dp)) {
                            Text("32", style = QResolveType.DataMetricSm.copy(fontWeight = FontWeight.Bold), color = QResolveColors.OnTertiaryFixedVariant)
                        }
                        Text("Low (L)", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Tertiary)
                    }
                    Box(
                        modifier = Modifier
                            .width(70.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(QResolveColors.TertiaryFixed)
                            .padding(horizontal = 6.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("PASSED", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.OnTertiaryFixedVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun ControlPlanRevisionCard(modifier: Modifier = Modifier) {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 1.dp,
        modifier = modifier
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(QResolveColors.SurfaceContainerLow)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier.size(30.dp).clip(RoundedCornerShape(6.dp)).background(QResolveColors.PrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.AssignmentTurnedIn, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                    Column {
                        Text("2. Control Plan Revision & Verification", style = QResolveType.HeadlineSm, color = QResolveColors.OnSurface)
                        Text("Document: CP-DET-SMT2-WSC Rev 4.0 (Supercedes Rev 3.2)", style = QResolveType.BodySm, color = QResolveColors.Secondary)
                    }
                }
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(QResolveColors.PrimaryFixed).padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text("PUBLISHED • LIVE", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.OnPrimaryFixedVariant)
                }
            }

            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ControlPlanStationItem(
                    title = "Station 20: Pallet Hold-Down Spring Tension",
                    badge = "100% In-Line Interlock",
                    badgeBg = QResolveColors.SurfaceContainer,
                    badgeFg = QResolveColors.Primary,
                    spec = "14.2N ± 1.0N",
                    method = "Auto PLC Conveyor Interlock",
                    reaction = "Auto-divert to Maintenance Cell D"
                )
                ControlPlanStationItem(
                    title = "Station 45: Post-Wave Solder Bridging Detection",
                    badge = "3D AOI Profilometry",
                    badgeBg = QResolveColors.TertiaryFixed,
                    badgeFg = QResolveColors.OnTertiaryFixedVariant,
                    spec = "Min 0.45mm pin clearance",
                    method = "Closed-loop laser triangulation",
                    reaction = "Instant line halt + Supervisor lock"
                )
                ControlPlanStationItem(
                    title = "PM-SMT-04: Preventive Maintenance Protocol",
                    badge = "Mandatory Refresh",
                    badgeBg = QResolveColors.SurfaceContainer,
                    badgeFg = QResolveColors.Secondary,
                    spec = "Max 15,000 cycles / 45 days",
                    method = "MES ticket auto-generation",
                    reaction = "RFID rejection at line infeed"
                )
            }
        }
    }
}

@Composable
private fun ControlPlanStationItem(
    title: String,
    badge: String,
    badgeBg: Color,
    badgeFg: Color,
    spec: String,
    method: String,
    reaction: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(QResolveColors.SurfaceContainerLow)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, style = QResolveType.HeadlineSm.copy(fontSize = 13.sp), color = QResolveColors.OnSurface, modifier = Modifier.weight(1f))
            Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(badgeBg).padding(horizontal = 6.dp, vertical = 2.dp)) {
                Text(badge, style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = badgeFg)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text("SPECIFICATION", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Outline)
                Text(spec, style = QResolveType.CodeBadge.copy(fontSize = 11.sp), color = QResolveColors.OnSurface)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("CONTROL METHOD", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Outline)
                Text(method, style = QResolveType.BodySm.copy(fontWeight = FontWeight.Medium), color = QResolveColors.OnSurface)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("REACTION PLAN", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Outline)
                Text(reaction, style = QResolveType.BodySm.copy(fontWeight = FontWeight.Medium), color = QResolveColors.Error)
            }
        }
    }
}

@Composable
private fun StandardWorkTrainingCard(modifier: Modifier = Modifier) {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 1.dp,
        modifier = modifier
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(QResolveColors.SurfaceContainerLow)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier.size(30.dp).clip(RoundedCornerShape(6.dp)).background(QResolveColors.PrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                    Column {
                        Text("3. Standard Work & Training", style = QResolveType.HeadlineSm, color = QResolveColors.OnSurface)
                        Text("Operator LMS certifications & shop floor directives", style = QResolveType.BodySm, color = QResolveColors.Secondary)
                    }
                }
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(QResolveColors.TertiaryFixed).padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text("100% VERIFIED", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.OnTertiaryFixedVariant)
                }
            }

            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // SOP-WSC-014 with Hotlinked Image
                SopWorkInstructionRow(
                    imageUrl = HotlinkedImages.SOP_WSC_014_PHOTO,
                    revBadge = "REV 5.1",
                    code = "SOP-WSC-014",
                    description = "Wave Solder Carrier Setup & RFID Verification. Boundary photos for go/no-go pin gauge verification installed on station monitors.",
                    checkNote = "Pushed to Line 2 HMI Terminals"
                )
                // WI-MAINT-088 with Hotlinked Image
                SopWorkInstructionRow(
                    imageUrl = HotlinkedImages.WI_MAINT_088_PHOTO,
                    revBadge = "REV 2.0",
                    code = "WI-MAINT-088",
                    description = "Beryllium-Copper Toggle Clamp Tension Calibration Standard (14.2N gauge calibration protocol).",
                    checkNote = "Maintenance Tooling Shop Certified"
                )

                // Shift Training Roll-up Bar
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(QResolveColors.SurfaceContainer)
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("OPERATOR CERTIFICATION TRACKER", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary)
                        Text("24 of 24 Technicians Certified", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Tertiary)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(50))
                            .background(QResolveColors.TertiaryContainer)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Shift A (8/8) • Shift B (8/8) • Shift C (8/8)", style = QResolveType.BodySm.copy(fontSize = 11.sp), color = QResolveColors.Secondary)
                        Text("LMS ID: CERT-8D-039", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Tertiary)
                    }
                }
            }
        }
    }
}

@Composable
private fun SopWorkInstructionRow(
    imageUrl: String,
    revBadge: String,
    code: String,
    description: String,
    checkNote: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(QResolveColors.SurfaceContainerLow)
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(84.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(QResolveColors.SurfaceContainer)
        ) {
            HotlinkedNetworkImage(
                url = imageUrl,
                contentDescription = code,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().background(QResolveColors.InverseSurface),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.PrecisionManufacturing, contentDescription = null, tint = Color.White)
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(QResolveColors.InverseSurface.copy(alpha = 0.85f))
                    .padding(horizontal = 4.dp, vertical = 1.dp)
            ) {
                Text(revBadge, style = QResolveType.CodeBadge.copy(fontSize = 8.sp), color = QResolveColors.InverseOnSurface)
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(code, style = QResolveType.HeadlineSm, color = QResolveColors.OnSurface)
                Text("Active", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Tertiary)
            }
            Text(description, style = QResolveType.BodySm, color = QResolveColors.Secondary)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = QResolveColors.Tertiary, modifier = Modifier.size(13.dp))
                Text(checkNote, style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
            }
        }
    }
}

@Composable
private fun HorizontalReadAcrossMatrixCard() {
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
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier.size(32.dp).clip(RoundedCornerShape(6.dp)).background(QResolveColors.PrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.AltRoute, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("4. Horizontal Read-Across Matrix", style = QResolveType.HeadlineSm, color = QResolveColors.OnSurface)
                            Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(QResolveColors.SurfaceContainerHighest).padding(horizontal = 6.dp, vertical = 2.dp)) {
                                Text("Proactive Fleet Deployment", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Secondary)
                            }
                        }
                        Text("Preventative risk containment across identical or similar wave solder and pallet systems corporate-wide", style = QResolveType.BodySm, color = QResolveColors.Secondary)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Global Alert:", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                    Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(QResolveColors.SurfaceContainerLowest).padding(horizontal = 6.dp, vertical = 2.dp)) {
                        Text("QA-2025-019 Broadcasted", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Primary)
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.background(QResolveColors.SurfaceContainerLow).padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text("LINE / MANUFACTURING FACILITY", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary, modifier = Modifier.width(190.dp))
                    Text("PRODUCT / PROCESS SCOPED", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary, modifier = Modifier.width(175.dp))
                    Text("APPLICABILITY ASSESSMENT", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary, modifier = Modifier.width(165.dp))
                    Text("PREVENTATIVE ACTION / ECN", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary, modifier = Modifier.width(240.dp))
                    Text("TARGET DATE", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary, modifier = Modifier.width(105.dp))
                    Text("IMPLEMENTATION STATUS", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary, modifier = Modifier.width(150.dp))
                }
                HorizontalDivider(color = QResolveColors.SurfaceContainer)

                ReadAcrossRow(
                    facility = "Plant #4 (Detroit)",
                    line = "SMT Line 1 (Sibling Wave Line)",
                    part = "PN-8842-A",
                    partSub = "Identical carrier leaf spring geometry",
                    appBadge = "APPLICABLE (Direct Match)",
                    appBg = QResolveColors.ErrorContainer,
                    appFg = QResolveColors.OnErrorContainer,
                    action = "ECN-2025-0420: 32 Pallet Fixtures retrofitted",
                    actionSub = "RFID interlock deployed",
                    date = "May 28, 2025",
                    status = "90% COMPLETE",
                    statusBg = QResolveColors.SurfaceContainerHigh,
                    statusFg = QResolveColors.Primary
                )
                ReadAcrossRow(
                    facility = "Plant #4 (Detroit)",
                    line = "Selective Solder Cell 3",
                    part = "PN-8890-C",
                    partSub = "ECU Secondary Power Module",
                    appBadge = "NOT APPLICABLE",
                    appBg = QResolveColors.SurfaceContainerHigh,
                    appFg = QResolveColors.Secondary,
                    action = "Uses nitrogen inert selective fountain with zero pallet flexure risk.",
                    actionSub = "",
                    date = "—",
                    status = "CLOSED / N/A",
                    statusBg = QResolveColors.SurfaceContainer,
                    statusFg = QResolveColors.Secondary
                )
                ReadAcrossRow(
                    facility = "Plant #2 (Saltillo Assembly)",
                    line = "SMT Line 4 (ECU Body Controller)",
                    part = "PN-7720-D",
                    partSub = "Similar wave solder carrier clamps",
                    appBadge = "APPLICABLE",
                    appBg = QResolveColors.ErrorContainer,
                    appFg = QResolveColors.OnErrorContainer,
                    action = "Global Quality Alert QA-2025-019 issued",
                    actionSub = "Engineering review scheduled May 24",
                    date = "Jun 06, 2025",
                    status = "IN REVIEW",
                    statusBg = QResolveColors.SurfaceContainerHigh,
                    statusFg = QResolveColors.Primary
                )
                ReadAcrossRow(
                    facility = "Plant #7 (Bratislava Electronics)",
                    line = "SMT Line 2 (Inverter Gate Board)",
                    part = "PN-9910-E",
                    partSub = "Magnetic pneumatic carrier clamp",
                    appBadge = "REVIEW COMPLETE",
                    appBg = QResolveColors.TertiaryFixed,
                    appFg = QResolveColors.OnTertiaryFixedVariant,
                    action = "Low risk verified; magnetic pneumatic holding mechanism immune to spring decay.",
                    actionSub = "",
                    date = "—",
                    status = "VERIFIED SAFE",
                    statusBg = QResolveColors.TertiaryFixed,
                    statusFg = QResolveColors.OnTertiaryFixedVariant
                )
            }
        }
    }
}

@Composable
private fun ReadAcrossRow(
    facility: String,
    line: String,
    part: String,
    partSub: String,
    appBadge: String,
    appBg: Color,
    appFg: Color,
    action: String,
    actionSub: String,
    date: String,
    status: String,
    statusBg: Color,
    statusFg: Color
) {
    Row(
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.width(190.dp)) {
            Text(facility, style = QResolveType.HeadlineSm.copy(fontSize = 13.sp), color = QResolveColors.OnSurface)
            Text(line, style = QResolveType.BodySm, color = QResolveColors.Secondary)
        }
        Column(modifier = Modifier.width(175.dp)) {
            Text(part, style = QResolveType.CodeBadge, color = QResolveColors.OnSurface)
            Text(partSub, style = QResolveType.BodySm.copy(fontSize = 11.sp), color = QResolveColors.Secondary)
        }
        Box(modifier = Modifier.width(165.dp)) {
            Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(appBg).padding(horizontal = 6.dp, vertical = 2.dp)) {
                Text(appBadge, style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = appFg)
            }
        }
        Column(modifier = Modifier.width(240.dp).padding(end = 8.dp)) {
            Text(action, style = QResolveType.BodySm.copy(fontWeight = FontWeight.Medium), color = QResolveColors.OnSurface)
            if (actionSub.isNotEmpty()) {
                Text(actionSub, style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Secondary)
            }
        }
        Text(date, style = QResolveType.CodeBadge, color = QResolveColors.OnSurface, modifier = Modifier.width(105.dp))
        Box(modifier = Modifier.width(150.dp)) {
            Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(statusBg).padding(horizontal = 8.dp, vertical = 3.dp)) {
                Text(status, style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = statusFg)
            }
        }
    }
    HorizontalDivider(color = QResolveColors.SurfaceContainer)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun D7SignOffVerificationGateCard(
    isWide: Boolean,
    onLockD7AndProceed: () -> Unit
) {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier.size(32.dp).clip(RoundedCornerShape(6.dp)).background(QResolveColors.TertiaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.VerifiedUser, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Column {
                        Text("5. D7 Standardization Sign-Off & Verification Gate", style = QResolveType.HeadlineSm, color = QResolveColors.OnSurface)
                        Text("Cross-functional sign-off required prior to D8 closure and executive lessons-learned release", style = QResolveType.BodySm, color = QResolveColors.Secondary)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = QResolveColors.Tertiary, modifier = Modifier.size(14.dp))
                    Text("IATF 16949 §10.2 AUDIT-PROOF GATE", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Secondary)
                }
            }
            HorizontalDivider(color = QResolveColors.SurfaceContainer)

            if (isWide) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    D7SignBox("CFT QUALITY LEAD", "Marcus Chen", "Sr. Quality Engineer", "SIGNED: 2025-05-21 14:32 EST", isSigned = true, Modifier.weight(1f))
                    D7SignBox("PROCESS ENG. MANAGER", "Sarah Jenkins", "Wave Solder Specialist", "SIGNED: 2025-05-21 16:04 EST", isSigned = true, Modifier.weight(1f))
                    D7SignBox("CORPORATE FMEA CHAMPION", "David Wu", "VP Reliability Engineering", "SIGNED: 2025-05-22 09:12 EST", isSigned = true, Modifier.weight(1f))
                    D7SignBox("CUSTOMER SQE LIAISON", "Dr. Aris Thorne (Exec Sponsor)", "Magna / Stellantis Resident SQE", "READY FOR D8 DOSSIER PRESENTATION", isSigned = false, Modifier.weight(1f))
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        D7SignBox("CFT QUALITY LEAD", "Marcus Chen", "Sr. Quality Engineer", "SIGNED: 2025-05-21 14:32 EST", isSigned = true, Modifier.weight(1f))
                        D7SignBox("PROCESS ENG. MANAGER", "Sarah Jenkins", "Wave Solder Specialist", "SIGNED: 2025-05-21 16:04 EST", isSigned = true, Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        D7SignBox("CORPORATE FMEA CHAMPION", "David Wu", "VP Reliability Engineering", "SIGNED: 2025-05-22 09:12 EST", isSigned = true, Modifier.weight(1f))
                        D7SignBox("CUSTOMER SQE LIAISON", "Dr. Aris Thorne (Exec Sponsor)", "Magna / Stellantis Resident SQE", "READY FOR D8 DOSSIER PRESENTATION", isSigned = false, Modifier.weight(1f))
                    }
                }
            }

            // Final Approval Action Strip
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(QResolveColors.SurfaceContainerLow)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Box(
                        modifier = Modifier.size(38.dp).clip(CircleShape).background(QResolveColors.TertiaryFixed),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.TaskAlt, contentDescription = null, tint = QResolveColors.OnTertiaryFixedVariant)
                    }
                    Column {
                        Text("D7 Standards & Controls Formally Validated", style = QResolveType.HeadlineSm, color = QResolveColors.OnSurface)
                        Text("PFMEA, Control Plan, and LMS Work Instructions are permanently published into production environment.", style = QResolveType.BodySm, color = QResolveColors.Secondary)
                    }
                }

                Button(
                    onClick = onLockD7AndProceed,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = QResolveColors.PrimaryContainer,
                        contentColor = QResolveColors.OnPrimaryContainer
                    ),
                    modifier = Modifier.testTag("lock_d7_button")
                ) {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Lock D7 & Proceed to D8 Team Sign-Off", style = QResolveType.HeadlineSm.copy(fontSize = 12.sp))
                }
            }
        }
    }
}

@Composable
private fun D7SignBox(
    header: String,
    name: String,
    role: String,
    footer: String,
    isSigned: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSigned) QResolveColors.SurfaceContainerLow else QResolveColors.SurfaceContainerHigh)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(header, style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary)
            Icon(
                imageVector = if (isSigned) Icons.Default.CheckCircle else Icons.Default.Pending,
                contentDescription = null,
                tint = if (isSigned) QResolveColors.Tertiary else QResolveColors.Primary,
                modifier = Modifier.size(16.dp)
            )
        }
        Column {
            Text(name, style = QResolveType.HeadlineSm, color = QResolveColors.OnSurface)
            Text(role, style = QResolveType.BodySm, color = QResolveColors.Secondary)
        }
        Text(
            text = footer,
            style = QResolveType.CodeBadge.copy(fontSize = 9.sp),
            color = if (isSigned) QResolveColors.Tertiary else QResolveColors.Primary
        )
    }
}
