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
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppScreen
import com.example.ui.theme.QResolveColors
import com.example.ui.theme.QResolveType
import com.example.viewmodel.QResolveViewModel

@Composable
fun LessonsLearnedScreen(
    viewModel: QResolveViewModel,
    isExpandedScreen: Boolean
) {
    val isBookmarked by viewModel.isBookmarked.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. Breadcrumb & Action Bar
        LessonsLearnedHeader(
            isBookmarked = isBookmarked,
            onToggleBookmark = { viewModel.toggleWatchlist() },
            onExportDossier = {
                viewModel.showToast("Opening AIAG/VDA Executive 8D Final Report Dossier...")
                viewModel.navigateTo(AppScreen.EXECUTIVE_DOSSIER)
            },
            onReopenCase = {
                viewModel.showToast("Audit Override Requested: Navigating to D4 Root Cause Review...")
                viewModel.navigateTo(AppScreen.D4_ROOT_CAUSE)
            }
        )

        // 2. Incident Master Identity Banner
        IncidentMasterBanner(
            onNavigateStage = { screen -> viewModel.navigateTo(screen) }
        )

        // 3. Executive KPI & Audit Scorecard Row (4 Cards)
        LessonsLearnedKpiRow(isExpandedScreen = isExpandedScreen)

        // 4. Main Dual-Column Body: Root Cause Summary + Yokoten Read-Across
        if (isExpandedScreen) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(0.58f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ValidatedRootCauseArtifactCard(
                        onInspectD4 = { viewModel.navigateTo(AppScreen.D4_ROOT_CAUSE) }
                    )
                    LessonsLearnedKnowledgeCard(
                        onToast = { viewModel.showToast(it) }
                    )
                }
                Column(
                    modifier = Modifier.weight(0.42f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GlobalYokotenLedgerCard(
                        onToast = { viewModel.showToast(it) }
                    )
                    ImmutableAuditTrailCard(
                        onViewFullDossier = { viewModel.navigateTo(AppScreen.EXECUTIVE_DOSSIER) }
                    )
                }
            }
        } else {
            ValidatedRootCauseArtifactCard(
                onInspectD4 = { viewModel.navigateTo(AppScreen.D4_ROOT_CAUSE) }
            )
            LessonsLearnedKnowledgeCard(
                onToast = { viewModel.showToast(it) }
            )
            GlobalYokotenLedgerCard(
                onToast = { viewModel.showToast(it) }
            )
            ImmutableAuditTrailCard(
                onViewFullDossier = { viewModel.navigateTo(AppScreen.EXECUTIVE_DOSSIER) }
            )
        }
    }
}

@Composable
private fun LessonsLearnedHeader(
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onExportDossier: () -> Unit,
    onReopenCase: () -> Unit
) {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(6.dp),
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.LibraryBooks,
                        contentDescription = null,
                        tint = QResolveColors.Primary,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = "YOKOTEN KNOWLEDGE BASE / ARCHIVED 8D DOSSIERS / LL-2025-089",
                        style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                        color = QResolveColors.Primary
                    )
                }
                Surface(
                    color = QResolveColors.SuccessBg,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = QResolveColors.Tertiary,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "IATF 16949 AUDIT READY",
                            style = QResolveType.CodeBadge.copy(fontSize = 9.sp),
                            color = QResolveColors.Tertiary
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Executive Audits & Lessons Learned Repository",
                        style = QResolveType.HeadlineLg,
                        color = QResolveColors.OnSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Standardized engineering memory, cross-plant Yokoten read-across verification, and immutable CAPA audit trail.",
                        style = QResolveType.BodySm,
                        color = QResolveColors.Secondary
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onToggleBookmark,
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isBookmarked) QResolveColors.PrimaryFixed else QResolveColors.SurfaceContainerLow
                    ),
                    modifier = Modifier
                        .heightIn(min = 48.dp)
                        .testTag("bookmark_watchlist_button")
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = QResolveColors.Primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isBookmarked) "Bookmarked in Watchlist" else "Add to Plant Watchlist",
                        style = QResolveType.HeadlineSm.copy(fontSize = 12.sp),
                        color = QResolveColors.Primary
                    )
                }

                OutlinedButton(
                    onClick = onReopenCase,
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    modifier = Modifier
                        .heightIn(min = 48.dp)
                        .testTag("audit_inspect_d4_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountTree,
                        contentDescription = null,
                        tint = QResolveColors.Secondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Inspect D4 Evidence",
                        style = QResolveType.HeadlineSm.copy(fontSize = 12.sp),
                        color = QResolveColors.OnSurface
                    )
                }

                Button(
                    onClick = onExportDossier,
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = QResolveColors.PrimaryContainer),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                    modifier = Modifier
                        .heightIn(min = 48.dp)
                        .testTag("open_executive_dossier_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.PictureAsPdf,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "View AIAG/VDA Executive 8D Dossier",
                        style = QResolveType.HeadlineSm.copy(fontSize = 12.sp),
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun IncidentMasterBanner(
    onNavigateStage: (AppScreen) -> Unit
) {
    Surface(
        color = QResolveColors.InverseSurface,
        shape = RoundedCornerShape(6.dp),
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        color = QResolveColors.TertiaryContainer,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "D8 CLOSED & VERIFIED",
                            style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    Text(
                        text = "8D-20250514-039",
                        style = QResolveType.CodeLg.copy(color = Color(0xFF93C5FD)),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "• PN-8842-B (ECU Bus Connector Header)",
                        style = QResolveType.BodySm,
                        color = Color(0xFFCBD5E1),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = "Cycle Time: 9.4 Days (SLA Target: 14d)",
                    style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                    color = Color(0xFF6EE7B7)
                )
            }

            // Completed 8D Ribbon inside dark banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val stages = listOf(
                    Triple("D1", "Team Setup", AppScreen.DASHBOARD),
                    Triple("D2", "5W2H Spec", AppScreen.DASHBOARD),
                    Triple("D3", "Containment", AppScreen.DASHBOARD),
                    Triple("D4", "Root Cause", AppScreen.D4_ROOT_CAUSE),
                    Triple("D5", "PCA Choice", AppScreen.D5_D6_PCA),
                    Triple("D6", "Validation", AppScreen.D5_D6_PCA),
                    Triple("D7", "PFMEA/SOP", AppScreen.D7_STANDARDIZATION),
                    Triple("D8", "Sign-Off", AppScreen.D8_CLOSURE)
                )
                stages.forEach { (code, name, screen) ->
                    Surface(
                        color = Color(0xFF1E2D40),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.clickable { onNavigateStage(screen) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(QResolveColors.TertiaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(10.dp)
                                )
                            }
                            Text(
                                text = "$code $name",
                                style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonsLearnedKpiRow(isExpandedScreen: Boolean) {
    val kpis = listOf(
        LLKpiItem(
            label = "POST-PCA PROCESS CAPABILITY",
            value = "1.84 Cpk",
            badge = "+234% vs Baseline (0.55)",
            subtext = "4,220 consecutive zero-defect parts across 3 shifts",
            accentColor = QResolveColors.Tertiary
        ),
        LLKpiItem(
            label = "PFMEA RISK REDUCTION (RPN)",
            value = "336 → 32",
            badge = "-90.5% RPN DELTA",
            subtext = "Action Priority downgraded from HIGH (H) to LOW (L)",
            accentColor = QResolveColors.Primary
        ),
        LLKpiItem(
            label = "YOKOTEN GLOBAL ADOPTION",
            value = "4 / 5 Plants",
            badge = "80% DEPLOYED",
            subtext = "Plant #9 Monterrey pending final fixture calibration",
            accentColor = QResolveColors.AmberWarning
        ),
        LLKpiItem(
            label = "NET WARRANTY AVOIDANCE",
            value = "$418,500",
            badge = "9.8x CAPA ROI",
            subtext = "Prevented OEM yard Campaign Hold on 14,500 vehicles",
            accentColor = QResolveColors.Tertiary
        )
    )

    if (isExpandedScreen) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            kpis.forEach { item ->
                LLKpiCard(item = item, modifier = Modifier.weight(1f))
            }
        }
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                LLKpiCard(item = kpis[0], modifier = Modifier.weight(1f))
                LLKpiCard(item = kpis[1], modifier = Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                LLKpiCard(item = kpis[2], modifier = Modifier.weight(1f))
                LLKpiCard(item = kpis[3], modifier = Modifier.weight(1f))
            }
        }
    }
}

private data class LLKpiItem(
    val label: String,
    val value: String,
    val badge: String,
    val subtext: String,
    val accentColor: Color
)

@Composable
private fun LLKpiCard(item: LLKpiItem, modifier: Modifier = Modifier) {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(6.dp),
        shadowElevation = 1.dp,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = item.label,
                style = QResolveType.LabelCaps.copy(fontSize = 9.sp),
                color = QResolveColors.Secondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.value,
                    style = QResolveType.DisplaySm.copy(fontSize = 20.sp),
                    color = QResolveColors.OnSurface
                )
                Surface(
                    color = item.accentColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = item.badge,
                        style = QResolveType.CodeBadge.copy(fontSize = 9.sp),
                        color = item.accentColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = item.subtext,
                style = QResolveType.BodySm.copy(fontSize = 11.sp),
                color = QResolveColors.OnSurfaceVariant
            )
        }
    }
}

@Composable
private fun ValidatedRootCauseArtifactCard(
    onInspectD4: () -> Unit
) {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(6.dp),
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
                Column {
                    Text(
                        text = "ARCHIVED ROOT CAUSE SYNOPSIS (D4 → D6)",
                        style = QResolveType.LabelCaps,
                        color = QResolveColors.Primary
                    )
                    Text(
                        text = "Dual-Track Systemic Failure Mechanism & Permanent Fix",
                        style = QResolveType.HeadlineMd,
                        color = QResolveColors.OnSurface
                    )
                }
                TextButton(onClick = onInspectD4) {
                    Text("Open D4 Tree", style = QResolveType.CodeBadge, color = QResolveColors.Primary)
                }
            }

            // Track A Box
            Surface(
                color = QResolveColors.SurfaceContainerLow,
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "TRACK A: OCCURRENCE ROOT CAUSE (W5)",
                            style = QResolveType.CodeBadge,
                            color = QResolveColors.Error
                        )
                        Text(
                            text = "PCA-01 • IMPLEMENTED",
                            style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                            color = QResolveColors.Tertiary
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Titanium Wave-Solder Carrier #8 thermal bow (0.28mm @ 260°C) caused by 304-SS spring fatigue after 18,400 thermal cycles without quantitative load-cell PM.",
                        style = QResolveType.BodyMd,
                        color = QResolveColors.OnSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Countermeasure: Inconel X-750 spring conversion + Active closed-loop Kistler piezo strain interlock (<18.5N auto line-stop).",
                        style = QResolveType.BodySm,
                        color = QResolveColors.Tertiary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Track B Box
            Surface(
                color = QResolveColors.SurfaceContainerLow,
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "TRACK B: DETECTION ESCAPE ROOT CAUSE (W5)",
                            style = QResolveType.CodeBadge,
                            color = QResolveColors.AmberWarning
                        )
                        Text(
                            text = "PCA-02 • IMPLEMENTED",
                            style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                            color = QResolveColors.Tertiary
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "2D Orthographic AOI algorithm lacked Z-axis height profiling and was throttled to 65% sensitivity to suppress gold-flash false calls.",
                        style = QResolveType.BodyMd,
                        color = QResolveColors.OnSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Countermeasure: 3D Dual-Laser Coplanarity Profilometry at Station 45 + cryptographic recipe lock (IATF Tier-3 QA Key).",
                        style = QResolveType.BodySm,
                        color = QResolveColors.Tertiary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun LessonsLearnedKnowledgeCard(
    onToast: (String) -> Unit
) {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(6.dp),
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
                Column {
                    Text(
                        text = "INSTITUTIONAL DESIGN & PROCESS STANDARDS",
                        style = QResolveType.LabelCaps,
                        color = QResolveColors.Secondary
                    )
                    Text(
                        text = "Codified Lessons Learned Ruleset (DFMEA / PFMEA)",
                        style = QResolveType.HeadlineMd,
                        color = QResolveColors.OnSurface
                    )
                }
                Surface(
                    color = QResolveColors.PrimaryFixed,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "3 GLOBAL RULES",
                        style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                        color = QResolveColors.Primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            val lessons = listOf(
                Triple(
                    "LL-RULE-089A • FIXTURE METALLURGY",
                    "Never specify 304 Austenitic Stainless Steel hold-down springs in wave or selective solder pallets operating >220°C. Always mandate precipitation-hardened Inconel X-750 or René 41.",
                    "Updated Global Tooling Standard GTS-402 Rev 9"
                ),
                Triple(
                    "LL-RULE-089B • AOI CHANGE CONTROL",
                    "Production Engineering is prohibited from modifying optical inspection threshold parameters to mitigate false-reject rates without a formal Gage R&R & SQE hardware lock.",
                    "Enforced in Siemens Opcenter MES v14.2"
                ),
                Triple(
                    "LL-RULE-089C • PM CYCLE TELEMETRY",
                    "Replace time-based calendar preventive maintenance on wave carriers with RFID cycle-counter gate locks (hard halt at 10,000 thermal cycles).",
                    "Linked to SAP PM Module #PM-8841"
                )
            )

            lessons.forEach { (code, rule, ref) ->
                Surface(
                    color = QResolveColors.SurfaceContainerLow,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToast("Copied $code citation to clipboard.") }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = code,
                                style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                                color = QResolveColors.Primary
                            )
                            Text(
                                text = ref,
                                style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                                color = QResolveColors.Secondary
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = rule,
                            style = QResolveType.BodySm,
                            color = QResolveColors.OnSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GlobalYokotenLedgerCard(
    onToast: (String) -> Unit
) {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(6.dp),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "YOKOTEN HORIZONTAL DEPLOYMENT MATRIX",
                style = QResolveType.LabelCaps,
                color = QResolveColors.Primary
            )
            Text(
                text = "Global Plant Read-Across Verification",
                style = QResolveType.HeadlineMd,
                color = QResolveColors.OnSurface
            )

            val plants = listOf(
                YokotenPlantRow("Plant #04 Detroit (Lead)", "Line 4 & Line 6 SMT", "VERIFIED CLOSED", true, "100%"),
                YokotenPlantRow("Plant #02 Stuttgart, DE", "HV Inverter Cell 2", "VERIFIED CLOSED", true, "100%"),
                YokotenPlantRow("Plant #07 Nagoya, JP", "e-Axle Power Stage", "VERIFIED CLOSED", true, "100%"),
                YokotenPlantRow("Plant #11 Kokomo, IN", "BMS Header Line 1", "VERIFIED CLOSED", true, "100%"),
                YokotenPlantRow("Plant #09 Monterrey, MX", "Wave Solder Line 3", "IN-PROGRESS (DUE MAY 24)", false, "75%")
            )

            plants.forEach { plant ->
                Surface(
                    color = QResolveColors.SurfaceContainerLow,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToast("Viewing Yokoten audit package for ${plant.plantName}") }
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = plant.plantName,
                                style = QResolveType.HeadlineSm,
                                color = QResolveColors.OnSurface
                            )
                            Text(
                                text = plant.lineScope,
                                style = QResolveType.BodySm,
                                color = QResolveColors.Secondary
                            )
                        }
                        Surface(
                            color = if (plant.isComplete) QResolveColors.SuccessBg else QResolveColors.WarningBg,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "${plant.progress} • ${plant.status}",
                                style = QResolveType.CodeBadge.copy(fontSize = 9.sp),
                                color = if (plant.isComplete) QResolveColors.Tertiary else QResolveColors.WarningText,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class YokotenPlantRow(
    val plantName: String,
    val lineScope: String,
    val status: String,
    val isComplete: Boolean,
    val progress: String
)

@Composable
private fun ImmutableAuditTrailCard(
    onViewFullDossier: () -> Unit
) {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(6.dp),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "21 CFR PART 11 / IATF CRYPTOGRAPHIC LEDGER",
                style = QResolveType.LabelCaps,
                color = QResolveColors.Secondary
            )
            Text(
                text = "Executive Sign-Off & Audit Checkpoints",
                style = QResolveType.HeadlineMd,
                color = QResolveColors.OnSurface
            )

            val checkpoints = listOf(
                Triple("2025-05-14 07:42 UTC", "D3 Warehouse & WIP Quarantine Locked", "M. Reyes (QA Lead)"),
                Triple("2025-05-16 15:18 UTC", "D4 SEM & Dual 5-Why Validated (94% Prob)", "Dr. A. Chen (Metallurgy)"),
                Triple("2025-05-19 21:05 UTC", "D6 3-Batch Zero-Defect Gate Passed (Cpk 1.84)", "T. Kowalski (Mfg Eng)"),
                Triple("2025-05-21 11:30 UTC", "D7 PFMEA Rev 14.2 & Control Plan Locked", "Dr. E. Rostova (Corp QA)"),
                Triple("2025-05-22 16:45 UTC", "D8 OEM Customer Counter-Sign & Closure", "Dr. A. Thorne (Stellantis SQE)")
            )

            checkpoints.forEach { (ts, event, actor) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = null,
                        tint = QResolveColors.Tertiary,
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .size(14.dp)
                    )
                    Column {
                        Text(
                            text = event,
                            style = QResolveType.HeadlineSm.copy(fontSize = 12.sp),
                            color = QResolveColors.OnSurface
                        )
                        Text(
                            text = "$ts • $actor",
                            style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                            color = QResolveColors.Secondary
                        )
                    }
                }
            }

            HorizontalDivider(color = QResolveColors.OutlineVariant.copy(alpha = 0.4f))

            Button(
                onClick = onViewFullDossier,
                colors = ButtonDefaults.buttonColors(containerColor = QResolveColors.InverseSurface),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Open Complete Printable 8D Executive Dossier",
                    style = QResolveType.HeadlineSm.copy(fontSize = 12.sp),
                    color = Color.White
                )
            }
        }
    }
}
