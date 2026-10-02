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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.automirrored.filled.TrendingFlat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
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
fun D8ClosureScreen(
    oemCounterSigned: Boolean,
    oemSignTimestamp: String,
    memoSent: Boolean,
    caseFormallyClosed: Boolean,
    onExecuteOemCounterSign: () -> Unit,
    onSendExecutiveMemo: () -> Unit,
    onExecuteFormalClosure: () -> Unit,
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
            // TOP CONTEXT & INVESTIGATION STEPPER
            D8TopContextCard(
                onOpenDossier = { onNavigate(AppScreen.EXECUTIVE_DOSSIER) },
                onOpenKnowledgeBase = { onNavigate(AppScreen.LESSONS_LEARNED) },
                onShowPart11Cert = { onShowToast("21 CFR Part 11 Electronic Signature Certificate Verified (SHA-256).") },
                onNavigate = onNavigate
            )

            // EXECUTIVE SUMMARY & ABATEMENT SCORECARD (5 Cards)
            D8ExecutiveScorecardRow()

            // MULTI-TIER EXECUTIVE DIGITAL SIGN-OFF MATRIX
            CryptographicSignOffMatrixCard(
                isWide = isWide,
                oemCounterSigned = oemCounterSigned,
                oemSignTimestamp = oemSignTimestamp,
                onExecuteOemCounterSign = onExecuteOemCounterSign
            )

            // CROSS-FUNCTIONAL TEAM RECOGNITION & KNOWLEDGE BASE ARCHIVE
            if (isWide) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    CftRecognitionPanel(
                        onViewCommendation = { onNavigate(AppScreen.EXECUTIVE_DOSSIER) },
                        modifier = Modifier.weight(0.58f)
                    )
                    LessonsLearnedArchivePanel(
                        onSearchRepository = { onNavigate(AppScreen.LESSONS_LEARNED) },
                        modifier = Modifier.weight(0.42f)
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CftRecognitionPanel(
                        onViewCommendation = { onNavigate(AppScreen.EXECUTIVE_DOSSIER) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    LessonsLearnedArchivePanel(
                        onSearchRepository = { onNavigate(AppScreen.LESSONS_LEARNED) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // AUDIT TRAIL, COMPLIANCE EVIDENCE & FORMAL CASE CLOSURE CTA BANNER
            D8FormalClosureBanner(
                memoSent = memoSent,
                caseFormallyClosed = caseFormallyClosed,
                onSendExecutiveMemo = onSendExecutiveMemo,
                onExecuteFormalClosure = onExecuteFormalClosure
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun D8TopContextCard(
    onOpenDossier: () -> Unit,
    onOpenKnowledgeBase: () -> Unit,
    onShowPart11Cert: () -> Unit,
    onNavigate: (AppScreen) -> Unit
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
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(QResolveColors.Primary).padding(horizontal = 6.dp, vertical = 2.dp)) {
                            Text("CASE 8D-20250514-039", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.OnPrimary)
                        }
                        Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(QResolveColors.TertiaryFixed).padding(horizontal = 6.dp, vertical = 2.dp)) {
                            Text("STATUS: READY FOR CLOSURE", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.OnTertiaryFixedVariant)
                        }
                        Text("IATF 16949 §10.2.3", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                        Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(QResolveColors.SurfaceContainer).padding(horizontal = 6.dp, vertical = 2.dp)) {
                            Text("CRITICALITY: TIER-1 OEM ESCAPE", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.OnSurfaceVariant)
                        }
                    }

                    Text(
                        text = "ECU Engine Management Board Solder Bridging Investigation",
                        style = QResolveType.HeadlineLg,
                        color = QResolveColors.OnSurface
                    )

                    Text(
                        text = "Part Number: PN-8842-B [Rev 05 Synced] • Customer: Magna Powertrain / Stellantis N.V. • Site: Plant #4 SMT Line 2 (Detroit Powertrain & Electronics) • Assigned Champion: Marcus Chen (Staff QA Specialist)",
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
                            .background(QResolveColors.SurfaceContainerLow)
                            .clickable { onOpenDossier() }
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                            .testTag("open_dossier_button"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Description, contentDescription = null, tint = QResolveColors.Primary, modifier = Modifier.size(16.dp))
                        Text("Executive Dossier (PDF)", style = QResolveType.BodySm, color = QResolveColors.OnSurface)
                    }
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(QResolveColors.SurfaceContainerLow)
                            .clickable { onOpenKnowledgeBase() }
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                            .testTag("sync_kb_button"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Hub, contentDescription = null, tint = QResolveColors.Tertiary, modifier = Modifier.size(16.dp))
                        Text("Sync Knowledge Base", style = QResolveType.BodySm, color = QResolveColors.OnSurface)
                    }
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(QResolveColors.SurfaceContainerLow)
                            .clickable { onShowPart11Cert() }
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(imageVector = Icons.Default.VerifiedUser, contentDescription = null, tint = QResolveColors.Secondary, modifier = Modifier.size(16.dp))
                        Text("21 CFR Part 11 Cert", style = QResolveType.BodySm, color = QResolveColors.OnSurface)
                    }
                }
            }

            Interactive8DStepper(
                activeStageIndex = 8,
                onNavigate = onNavigate
            )
        }
    }
}

@Composable
private fun D8ExecutiveScorecardRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Card 1: SLA
        ScorecardTile(
            title = "INVESTIGATION SLA",
            icon = Icons.Default.Timelapse,
            iconColor = QResolveColors.Primary,
            mainMetric = "21",
            unitText = "/ 30 Days Target",
            mainColor = QResolveColors.OnSurface,
            footerLeft = "+9 Days Ahead",
            footerLeftColor = QResolveColors.Tertiary,
            footerBadge = "100% On-Time",
            footerBadgeBg = QResolveColors.TertiaryFixed,
            footerBadgeFg = QResolveColors.OnTertiaryFixedVariant
        )
        // Card 2: Financial ROI
        ScorecardTile(
            title = "NET SCRAP ABATEMENT",
            icon = Icons.Default.MonetizationOn,
            iconColor = QResolveColors.Tertiary,
            mainMetric = "$148,500",
            unitText = "/ yr",
            mainColor = QResolveColors.Tertiary,
            footerLeft = "CapEx $12.6k",
            footerLeftColor = QResolveColors.OnSurfaceVariant,
            footerBadge = "11.8x ROI",
            footerBadgeBg = QResolveColors.SurfaceContainerLow,
            footerBadgeFg = QResolveColors.Primary
        )
        // Card 3: Process Cpk
        ScorecardTile(
            title = "PROCESS CPK METRIC",
            icon = Icons.Default.Tune,
            iconColor = QResolveColors.Primary,
            mainMetric = "1.92",
            unitText = "(Target ≥ 1.67)",
            mainColor = QResolveColors.OnSurface,
            footerLeft = "0 Escapes Detected",
            footerLeftColor = QResolveColors.Tertiary,
            footerBadge = "4,200 pcs Run",
            footerBadgeBg = QResolveColors.SurfaceContainerLow,
            footerBadgeFg = QResolveColors.Secondary
        )
        // Card 4: PFMEA RPN Reduction
        Surface(
            color = QResolveColors.SurfaceContainerLowest,
            shape = RoundedCornerShape(8.dp),
            shadowElevation = 1.dp,
            modifier = Modifier.width(210.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("PFMEA RPN REDUCTION", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary)
                    Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = QResolveColors.Primary, modifier = Modifier.size(16.dp))
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("336", style = QResolveType.DataMetricSm.copy(textDecoration = TextDecoration.LineThrough), color = QResolveColors.Error)
                    Icon(imageVector = Icons.AutoMirrored.Filled.TrendingFlat, contentDescription = null, tint = QResolveColors.Secondary, modifier = Modifier.size(16.dp))
                    Text("32", style = QResolveType.DataMetricLg, color = QResolveColors.Tertiary)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("-90.5% Risk Drop", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Tertiary)
                    Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(QResolveColors.SecondaryContainer).padding(horizontal = 6.dp, vertical = 2.dp)) {
                        Text("RPN < 40 Pass", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.OnSecondaryContainer)
                    }
                }
            }
        }
        // Card 5: Horizontal Rollout
        ScorecardTile(
            title = "HORIZONTAL ROLLOUT",
            icon = Icons.Default.Share,
            iconColor = QResolveColors.Primary,
            mainMetric = "4 / 4",
            unitText = "Plants / Lines",
            mainColor = QResolveColors.OnSurface,
            footerLeft = "ECN-2025-0420",
            footerLeftColor = QResolveColors.Primary,
            footerBadge = "100% Deployed",
            footerBadgeBg = QResolveColors.TertiaryFixed,
            footerBadgeFg = QResolveColors.OnTertiaryFixedVariant
        )
    }
}

@Composable
private fun ScorecardTile(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    mainMetric: String,
    unitText: String,
    mainColor: Color,
    footerLeft: String,
    footerLeftColor: Color,
    footerBadge: String,
    footerBadgeBg: Color,
    footerBadgeFg: Color
) {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 1.dp,
        modifier = Modifier.width(210.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(title, style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary)
                Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(16.dp))
            }
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(mainMetric, style = QResolveType.DataMetricLg, color = mainColor)
                Text(unitText, style = QResolveType.BodySm, color = QResolveColors.Secondary, modifier = Modifier.padding(bottom = 2.dp))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(footerLeft, style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = footerLeftColor)
                Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(footerBadgeBg).padding(horizontal = 6.dp, vertical = 2.dp)) {
                    Text(footerBadge, style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = footerBadgeFg)
                }
            }
        }
    }
}

@Composable
private fun CryptographicSignOffMatrixCard(
    isWide: Boolean,
    oemCounterSigned: Boolean,
    oemSignTimestamp: String,
    onExecuteOemCounterSign: () -> Unit
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
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.FactCheck, contentDescription = null, tint = QResolveColors.Primary, modifier = Modifier.size(20.dp))
                        Text("Cryptographic Executive Sign-off Matrix", style = QResolveType.HeadlineMd, color = QResolveColors.OnSurface)
                    }
                    Text(
                        text = "21 CFR Part 11 Electronic Records & Signatures | IATF 16949 §10.2.3 Formal Concurrence",
                        style = QResolveType.BodySm,
                        color = QResolveColors.Secondary
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(QResolveColors.Tertiary))
                    Text("SHA-256 LEDGER SECURE", style = QResolveType.CodeBadge, color = QResolveColors.Tertiary)
                }
            }

            if (isWide) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    TierSignOffCard1(Modifier.weight(1f))
                    TierSignOffCard2(Modifier.weight(1f))
                    TierSignOffCard3(Modifier.weight(1f))
                    TierSignOffCard4Oem(
                        oemCounterSigned = oemCounterSigned,
                        oemSignTimestamp = oemSignTimestamp,
                        onExecuteOemCounterSign = onExecuteOemCounterSign,
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    TierSignOffCard1(Modifier.fillMaxWidth())
                    TierSignOffCard2(Modifier.fillMaxWidth())
                    TierSignOffCard3(Modifier.fillMaxWidth())
                    TierSignOffCard4Oem(
                        oemCounterSigned = oemCounterSigned,
                        oemSignTimestamp = oemSignTimestamp,
                        onExecuteOemCounterSign = onExecuteOemCounterSign,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun TierSignOffCard1(modifier: Modifier = Modifier) {
    ExecutiveTierCard(
        tierBadge = "TIER 1: 8D CHAMPION",
        initials = "MC",
        avatarUrl = null,
        avatarBg = QResolveColors.PrimaryContainer,
        avatarFg = QResolveColors.OnPrimary,
        name = "Marcus Chen",
        role = "Staff Quality Engineer",
        quote = "\"Root cause verified via physical recreation test; 3D AOI profilometry and spring-tension interlocks proven over 3 production runs with 0 PPM.\"",
        status = "SIGNED & VERIFIED",
        timestamp = "2025-05-23 09:15 EST",
        hash = "HASH: 9f8a3c...e81042",
        modifier = modifier
    )
}

@Composable
private fun TierSignOffCard2(modifier: Modifier = Modifier) {
    ExecutiveTierCard(
        tierBadge = "TIER 2: QA AUTHORITY",
        initials = "ER",
        avatarUrl = HotlinkedImages.ELENA_ROSTOVA_AVATAR,
        avatarBg = QResolveColors.TertiaryContainer,
        avatarFg = QResolveColors.OnTertiary,
        name = "Dr. Elena Rostova",
        role = "VP Quality Assurance",
        quote = "\"Audit trail validated against IATF 16949 §10.2.3 and ISO 9001:2015 Clause 10.2. All containment lots purged, certified, and released without customer interruption.\"",
        status = "APPROVED & SEALED",
        timestamp = "2025-05-23 11:30 EST",
        hash = "HASH: 44b7d1...0fa19e",
        modifier = modifier
    )
}

@Composable
private fun TierSignOffCard3(modifier: Modifier = Modifier) {
    ExecutiveTierCard(
        tierBadge = "TIER 3: OPERATIONS VP",
        initials = "DW",
        avatarUrl = null,
        avatarBg = QResolveColors.SecondaryFixed,
        avatarFg = QResolveColors.OnSecondaryFixed,
        name = "David Wu",
        role = "VP Reliability & Operations",
        quote = "\"CapEx $12.6k signed off. ECN-2025-0420 read-across complete at Detroit Line 1, Saltillo plant notification confirmed active.\"",
        status = "AUTHORIZED & CONCURRED",
        timestamp = "2025-05-23 14:05 EST",
        hash = "HASH: 12de99...38ab61",
        modifier = modifier
    )
}

@Composable
private fun ExecutiveTierCard(
    tierBadge: String,
    initials: String,
    avatarUrl: String?,
    avatarBg: Color,
    avatarFg: Color,
    name: String,
    role: String,
    quote: String,
    status: String,
    timestamp: String,
    hash: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(QResolveColors.Surface)
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(QResolveColors.SurfaceContainer).padding(horizontal = 6.dp, vertical = 2.dp)) {
                    Text(tierBadge, style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.OnSurface)
                }
                Icon(imageVector = Icons.Default.Verified, contentDescription = null, tint = QResolveColors.Tertiary, modifier = Modifier.size(16.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (avatarUrl != null) {
                    HotlinkedNetworkImage(
                        url = avatarUrl,
                        contentDescription = name,
                        modifier = Modifier.size(38.dp).clip(CircleShape)
                    ) {
                        Box(modifier = Modifier.size(38.dp).clip(CircleShape).background(avatarBg), contentAlignment = Alignment.Center) {
                            Text(initials, style = QResolveType.CodeBadge, color = avatarFg)
                        }
                    }
                } else {
                    Box(modifier = Modifier.size(38.dp).clip(CircleShape).background(avatarBg), contentAlignment = Alignment.Center) {
                        Text(initials, style = QResolveType.CodeBadge, color = avatarFg)
                    }
                }
                Column {
                    Text(name, style = QResolveType.HeadlineSm, color = QResolveColors.OnSurface)
                    Text(role, style = QResolveType.BodySm, color = QResolveColors.Secondary)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(QResolveColors.SurfaceContainerLowest)
                    .padding(8.dp)
            ) {
                Text(quote, style = QResolveType.BodySm.copy(fontStyle = FontStyle.Italic), color = QResolveColors.OnSurfaceVariant)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(QResolveColors.SurfaceContainerLow)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Status:", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                Text(status, style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Tertiary)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Timestamp:", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                Text(timestamp, style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.OnSurface)
            }
            Text(hash, style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Outline)
        }
    }
}

@Composable
private fun TierSignOffCard4Oem(
    oemCounterSigned: Boolean,
    oemSignTimestamp: String,
    onExecuteOemCounterSign: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(QResolveColors.Surface)
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(QResolveColors.PrimaryFixed).padding(horizontal = 6.dp, vertical = 2.dp)) {
                    Text("TIER 4: OEM SQE SPONSOR", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.OnPrimaryFixedVariant)
                }
                Icon(
                    imageVector = if (oemCounterSigned) Icons.Default.Verified else Icons.Default.PendingActions,
                    contentDescription = null,
                    tint = if (oemCounterSigned) QResolveColors.Tertiary else QResolveColors.Secondary,
                    modifier = Modifier.size(16.dp)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier.size(38.dp).clip(CircleShape).background(QResolveColors.SurfaceContainerHighest),
                    contentAlignment = Alignment.Center
                ) {
                    Text("AT", style = QResolveType.CodeBadge, color = QResolveColors.Primary)
                }
                Column {
                    Text("Dr. Aris Thorne", style = QResolveType.HeadlineSm, color = QResolveColors.OnSurface)
                    Text("Magna / Stellantis SQE", style = QResolveType.BodySm, color = QResolveColors.Secondary)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(QResolveColors.SurfaceContainerLowest)
                    .padding(8.dp)
            ) {
                Text(
                    text = "\"Zero customer yard holds. Level 4 PPAP/PPAC update approved for ECU Main Board Rev 05 with enhanced profiling.\"",
                    style = QResolveType.BodySm.copy(fontStyle = FontStyle.Italic),
                    color = QResolveColors.OnSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(QResolveColors.SurfaceContainerLow)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Status:", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                Text(
                    text = if (oemCounterSigned) "COUNTERSIGNED & SEALED" else "READY TO COUNTER-SIGN",
                    style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                    color = if (oemCounterSigned) QResolveColors.Tertiary else QResolveColors.Primary
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Timestamp:", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                Text(oemSignTimestamp, style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.OnSurface)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (oemCounterSigned) QResolveColors.TertiaryFixed else QResolveColors.Primary)
                    .clickable(enabled = !oemCounterSigned) { onExecuteOemCounterSign() }
                    .padding(vertical = 7.dp)
                    .testTag("btn_countersign_oem"),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (oemCounterSigned) Icons.Default.Lock else Icons.Default.Draw,
                    contentDescription = null,
                    tint = if (oemCounterSigned) QResolveColors.OnTertiaryFixedVariant else QResolveColors.OnPrimary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (oemCounterSigned) "OEM Seal Cryptographically Verified" else "COUNTER-SIGN AS OEM SQE",
                    style = QResolveType.LabelCaps.copy(fontSize = 10.sp),
                    color = if (oemCounterSigned) QResolveColors.OnTertiaryFixedVariant else QResolveColors.OnPrimary
                )
            }
        }
    }
}

@Composable
private fun CftRecognitionPanel(
    onViewCommendation: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 1.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(imageVector = Icons.Default.MilitaryTech, contentDescription = null, tint = QResolveColors.Tertiary, modifier = Modifier.size(20.dp))
                    Text("D8 Cross-Functional Team Recognition", style = QResolveType.HeadlineMd, color = QResolveColors.OnSurface)
                }
                Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(QResolveColors.TertiaryFixed).padding(horizontal = 6.dp, vertical = 2.dp)) {
                    Text("+500 Q-POINTS ALLOTTED", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.OnTertiaryFixedVariant)
                }
            }
            Text("Commendation awards formally entered into Plant #4 Operational Excellence Ledger.", style = QResolveType.BodySm, color = QResolveColors.Secondary)

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AwardCard(
                    badge = "Analytical Rigor",
                    badgeBg = QResolveColors.PrimaryFixed,
                    badgeFg = QResolveColors.OnPrimaryFixedVariant,
                    role = "QA Lead",
                    name = "Marcus Chen",
                    desc = "Led dual-track 5-Why analysis isolating dynamic clamp mechanical fatigue from vision blindspot reflection.",
                    footerLeft = "Merit Badge Level III",
                    points = "+150 pts",
                    footerColor = QResolveColors.Primary,
                    modifier = Modifier.weight(1f)
                )
                AwardCard(
                    badge = "Zero-Defect Innovation",
                    badgeBg = QResolveColors.TertiaryFixed,
                    badgeFg = QResolveColors.OnTertiaryFixedVariant,
                    role = "Wave Process",
                    name = "Sarah Jenkins",
                    desc = "Architected the Poka-Yoke beryllium-copper dual-pivot clamp retrofit preventing thermal deflection at 260°C.",
                    footerLeft = "Engineering Patent Filed",
                    points = "+150 pts",
                    footerColor = QResolveColors.Tertiary,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AwardCard(
                    badge = "Rapid Execution",
                    badgeBg = QResolveColors.SecondaryContainer,
                    badgeFg = QResolveColors.OnSecondaryContainer,
                    role = "Tooling Lead",
                    name = "Dave Kowalski",
                    desc = "Fabricated and verified 32 modified pallet fixtures in 72 hours, maintaining unbroken OEM production feed.",
                    footerLeft = "SLA Shield Recipient",
                    points = "+100 pts",
                    footerColor = QResolveColors.Secondary,
                    modifier = Modifier.weight(1f)
                )
                AwardCard(
                    badge = "Frontline Excellence",
                    badgeBg = QResolveColors.SurfaceContainerHighest,
                    badgeFg = QResolveColors.OnSurface,
                    role = "Operators (8)",
                    name = "Shift B Wave Operators",
                    desc = "Maintained 100% adherence to the 200% interim containment sort gate through 24/7 rotations with 0 escapes.",
                    footerLeft = "Operational Star",
                    points = "+100 pts",
                    footerColor = QResolveColors.Primary,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Corporate Recognition Portal: Batch #Q4-8842-HONORS", style = QResolveType.BodySm, color = QResolveColors.Secondary)
                Row(
                    modifier = Modifier.clickable { onViewCommendation() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("VIEW TEAM COMMENDATION LETTER", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Primary)
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = QResolveColors.Primary, modifier = Modifier.size(13.dp))
                }
            }
        }
    }
}

@Composable
private fun AwardCard(
    badge: String,
    badgeBg: Color,
    badgeFg: Color,
    role: String,
    name: String,
    desc: String,
    footerLeft: String,
    points: String,
    footerColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(QResolveColors.SurfaceContainerLow)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(badgeBg).padding(horizontal = 6.dp, vertical = 2.dp)) {
                Text(badge, style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = badgeFg)
            }
            Text(role, style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Secondary)
        }
        Text(name, style = QResolveType.HeadlineSm, color = QResolveColors.OnSurface)
        Text(desc, style = QResolveType.BodySm, color = QResolveColors.OnSurfaceVariant)
        Row(modifier = Modifier.fillMaxWidth().padding(top = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(footerLeft, style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = footerColor)
            Text(points, style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = footerColor)
        }
    }
}

@Composable
private fun LessonsLearnedArchivePanel(
    onSearchRepository: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 1.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, tint = QResolveColors.Primary, modifier = Modifier.size(20.dp))
                    Text("Organizational Lessons Learned", style = QResolveType.HeadlineMd, color = QResolveColors.OnSurface)
                }
                Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(QResolveColors.SurfaceContainer).padding(horizontal = 6.dp, vertical = 2.dp)) {
                    Text("GLOBAL REPOSITORIES", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.OnSurfaceVariant)
                }
            }
            Text("Standardized knowledge assets exported to AIAG / VDA Knowledge Base.", style = QResolveType.BodySm, color = QResolveColors.Secondary)

            LessonMiniItem(
                code = "PM-SMT-04 • Tooling Maintenance",
                tag = "PREVENTIVE MAINT",
                body = "Dynamic leaf spring fatigue cannot be visually inspected; cyclic thermal strain requires load-cell force calibration every 15,000 passes.",
                checkText = "Updated in SAP PM Plant #1-6"
            )
            LessonMiniItem(
                code = "AOI-STD-09 • Vision Systems",
                tag = "DESIGN RULE",
                body = "2D grayscale contrast cameras fail on high-density pin solder bridges (<0.5mm pitch) due to specular glare. 3D structured light profilometry is mandatory.",
                checkText = "Corporate Standard D-990 Updated"
            )
            LessonMiniItem(
                code = "MAT-SPEC-11 • Fixture Alloys",
                tag = "SUPPLIER SPEC",
                body = "Titanium spring clips replaced by beryllium-copper alloy for 3x thermal fatigue resistance at 260°C wave temperatures.",
                checkText = "Vendor Approved List Refreshed"
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("3 Global Rules Integrated", style = QResolveType.CodeBadge, color = QResolveColors.Secondary)
                Row(
                    modifier = Modifier.clickable { onSearchRepository() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("SEARCH REPOSITORY", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Primary)
                    Icon(imageVector = Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, tint = QResolveColors.Primary, modifier = Modifier.size(13.dp))
                }
            }
        }
    }
}

@Composable
private fun LessonMiniItem(
    code: String,
    tag: String,
    body: String,
    checkText: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(QResolveColors.Surface)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(code, style = QResolveType.CodeBadge, color = QResolveColors.Primary)
            Text(tag, style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.Secondary)
        }
        Text(body, style = QResolveType.BodySm, color = QResolveColors.OnSurface)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = QResolveColors.Tertiary, modifier = Modifier.size(13.dp))
            Text(checkText, style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun D8FormalClosureBanner(
    memoSent: Boolean,
    caseFormallyClosed: Boolean,
    onSendExecutiveMemo: () -> Unit,
    onExecuteFormalClosure: () -> Unit
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
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(QResolveColors.TertiaryFixed).padding(horizontal = 6.dp, vertical = 2.dp)) {
                        Text("IATF 16949:2016 COMPLIANT", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.OnTertiaryFixedVariant)
                    }
                    Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(QResolveColors.SecondaryContainer).padding(horizontal = 6.dp, vertical = 2.dp)) {
                        Text("ISO 9001:2015 CLAUSE 10.2", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.OnSecondaryContainer)
                    }
                    Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(QResolveColors.SurfaceContainer).padding(horizontal = 6.dp, vertical = 2.dp)) {
                        Text("21 CFR PART 11 IMMUTABLE", style = QResolveType.LabelCaps.copy(fontSize = 9.sp), color = QResolveColors.OnSurfaceVariant)
                    }
                }
                Text("Audit Gate Verified: Ready for Formal Institutional Archive", style = QResolveType.HeadlineMd, color = QResolveColors.OnSurface)
                Text(
                    text = "Executing formal closure permanently locks all investigation fields (D1 through D8), freezes the electronic ledger, transmits the closing statement to Stellantis SQE, and tags the case as resolved in enterprise ERP.",
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
                        .background(QResolveColors.SurfaceContainerLow)
                        .clickable { onSendExecutiveMemo() }
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                        .testTag("btn_send_memo"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(imageVector = Icons.Default.MailOutline, contentDescription = null, tint = QResolveColors.OnSurface, modifier = Modifier.size(16.dp))
                    Text(
                        text = if (memoSent) "Memo Sent & Logged (Receipt #8842-EM-29)" else "Send Wrap-Up Memo to OEM",
                        style = QResolveType.BodySm,
                        color = QResolveColors.OnSurface
                    )
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (caseFormallyClosed) QResolveColors.TertiaryContainer else QResolveColors.Primary)
                        .clickable(enabled = !caseFormallyClosed) { onExecuteFormalClosure() }
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .testTag("btn_formal_close"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(imageVector = Icons.Default.LockPerson, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Text(
                        text = if (caseFormallyClosed) "Case 8D-20250514-039 Formally Closed & Archived" else "Execute Formal 8D Case Closure",
                        style = QResolveType.HeadlineSm,
                        color = Color.White
                    )
                }
            }
        }
    }
}
