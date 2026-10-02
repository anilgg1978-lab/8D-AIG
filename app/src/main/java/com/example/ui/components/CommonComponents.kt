package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.model.AppScreen
import com.example.model.CaseSeverity
import com.example.model.HotlinkedImages
import com.example.ui.theme.QResolveColors
import com.example.ui.theme.QResolveType

@Composable
fun QResolveLogoIcon(modifier: Modifier = Modifier.size(30.dp)) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(QResolveColors.PrimaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(20.dp)) {
            val w = size.width
            val h = size.height
            val outerDiamond = Path().apply {
                moveTo(w / 2f, 0f)
                lineTo(w, h / 2f)
                lineTo(w / 2f, h)
                lineTo(0f, h / 2f)
                close()
            }
            drawPath(outerDiamond, color = Color.White, style = Stroke(width = 2.2.dp.toPx()))
            val innerDiamond = Path().apply {
                moveTo(w / 2f, h * 0.25f)
                lineTo(w * 0.75f, h / 2f)
                lineTo(w / 2f, h * 0.75f)
                lineTo(w * 0.25f, h / 2f)
                close()
            }
            drawPath(innerDiamond, color = Color(0xFF60A5FA))
        }
    }
}

@Composable
fun HotlinkedNetworkImage(
    url: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    fallback: @Composable () -> Unit
) {
    SubcomposeAsyncImage(
        model = url,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        loading = { fallback() },
        error = { fallback() }
    )
}

@Composable
fun QResolveTopHeader(
    currentScreen: AppScreen,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onOpenDrawer: () -> Unit,
    showMenuButton: Boolean,
    onNavigate: (AppScreen) -> Unit
) {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Menu + Brand + Plant Pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    if (showMenuButton) {
                        IconButton(
                            onClick = onOpenDrawer,
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("open_drawer_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Open Workstream Navigation",
                                tint = QResolveColors.OnSurface
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .clickable { onNavigate(AppScreen.DASHBOARD) }
                            .padding(vertical = 4.dp)
                    ) {
                        QResolveLogoIcon()
                        Column {
                            Row(verticalAlignment = Alignment.Top) {
                                Text(
                                    text = "Q-Resolve 8D",
                                    style = QResolveType.HeadlineSm,
                                    color = QResolveColors.OnSurface
                                )
                                Text(
                                    text = " 8D",
                                    style = QResolveType.CodeBadge.copy(fontSize = 9.sp),
                                    color = QResolveColors.PrimaryContainer
                                )
                            }
                            Text(
                                text = "ENTERPRISE CAPA V4.8",
                                style = QResolveType.CodeBadge.copy(fontSize = 9.sp),
                                color = QResolveColors.Secondary
                            )
                        }
                    }

                    // Plant #4 Selector Pill on medium/wide layouts
                    Box(
                        modifier = Modifier
                            .height(22.dp)
                            .width(1.dp)
                            .background(QResolveColors.SurfaceContainer)
                    )

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(QResolveColors.SurfaceContainerLow)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PrecisionManufacturing,
                            contentDescription = null,
                            tint = QResolveColors.Secondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Plant #4 - Detroit Powertrain",
                            style = QResolveType.BodySm.copy(fontWeight = FontWeight.Medium),
                            color = QResolveColors.OnSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = QResolveColors.Outline,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                // Right: Escalations alert + Notifications + Profile Avatar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Escalation or Ready badge
                    val isD8OrLater = currentScreen == AppScreen.D8_CLOSURE || currentScreen == AppScreen.LESSONS_LEARNED
                    if (isD8OrLater) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(QResolveColors.TertiaryFixed)
                                .padding(horizontal = 6.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = QResolveColors.Tertiary,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "READY FOR CLOSURE",
                                style = QResolveType.CodeBadge.copy(fontSize = 9.sp),
                                color = QResolveColors.OnTertiaryFixedVariant
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(QResolveColors.ErrorContainer)
                                .clickable { onNavigate(AppScreen.D5_D6_PCA) }
                                .padding(horizontal = 6.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = QResolveColors.Error,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "2 ESCALATIONS D3",
                                style = QResolveType.CodeBadge.copy(fontSize = 9.sp),
                                color = QResolveColors.OnErrorContainer
                            )
                        }
                    }

                    // Notification Bell with 3 badge
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { onNavigate(AppScreen.EXECUTIVE_DOSSIER) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = QResolveColors.OnSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = (-2).dp, y = 2.dp)
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(QResolveColors.Error),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "3",
                                style = QResolveType.CodeBadge.copy(fontSize = 9.sp),
                                color = QResolveColors.OnError
                            )
                        }
                    }

                    // Elena Vance Avatar (hotlinked from HTML)
                    HotlinkedNetworkImage(
                        url = HotlinkedImages.ELENA_VANCE_AVATAR,
                        contentDescription = "Elena Vance VP Quality & CAPA",
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .border(1.dp, QResolveColors.OutlineVariant, CircleShape)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(QResolveColors.PrimaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "EV",
                                style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                                color = QResolveColors.OnPrimary
                            )
                        }
                    }
                }
            }

            // Compact Search & IATF bar row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Search Box
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(32.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(QResolveColors.SurfaceContainerLow)
                        .border(1.dp, QResolveColors.OutlineVariant, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = QResolveColors.Outline,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Search 8D #, Part Rev, Lot Code...",
                                style = QResolveType.BodySm,
                                color = QResolveColors.Outline,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            singleLine = true,
                            textStyle = QResolveType.BodySm.copy(color = QResolveColors.OnSurface),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("global_search_input")
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(QResolveColors.SurfaceContainerHighest)
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "Ctrl+K",
                            style = QResolveType.CodeBadge.copy(fontSize = 9.sp),
                            color = QResolveColors.Outline
                        )
                    }
                }

                // IATF 16949 / ISO 9001 badge
                Row(
                    modifier = Modifier
                        .height(32.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(QResolveColors.SurfaceContainer)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = null,
                        tint = QResolveColors.Tertiary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "IATF 16949 / ISO 9001",
                        style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                        color = QResolveColors.TertiaryContainer
                    )
                }
            }
            HorizontalDivider(color = QResolveColors.SurfaceContainer, thickness = 1.dp)
        }
    }
}

@Composable
fun InvestigationWorkstreamSidebar(
    currentScreen: AppScreen,
    onSelectScreen: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val navItems = listOf(
        Triple(AppScreen.DASHBOARD, "8D Dashboard & Incident Control", Icons.Default.GridView),
        Triple(AppScreen.D4_ROOT_CAUSE, "Active Investigations & D4 RCA", Icons.Default.AccountTree),
        Triple(AppScreen.D5_D6_PCA, "PCA & Validation (D5/D6)", Icons.Default.BuildCircle),
        Triple(AppScreen.D7_STANDARDIZATION, "Standardization & FMEA (D7)", Icons.AutoMirrored.Filled.FactCheck),
        Triple(AppScreen.D8_CLOSURE, "D8: Closure & Team Recognition", Icons.Default.MilitaryTech),
        Triple(AppScreen.EXECUTIVE_DOSSIER, "AIAG / VDA Executive Dossier", Icons.AutoMirrored.Filled.Assignment),
        Triple(AppScreen.LESSONS_LEARNED, "Executive Audits & Lessons KB", Icons.AutoMirrored.Filled.MenuBook)
    )

    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(QResolveColors.SurfaceContainerLowest)
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "INVESTIGATION WORKSTREAM",
                    style = QResolveType.LabelCaps,
                    color = QResolveColors.Outline
                )
            }
            HorizontalDivider(color = QResolveColors.SurfaceContainer)
            Spacer(modifier = Modifier.height(2.dp))

            navItems.forEach { (screen, label, icon) ->
                val isSelected = currentScreen == screen
                val bgColor = if (isSelected) QResolveColors.PrimaryContainer else Color.Transparent
                val contentColor = if (isSelected) QResolveColors.OnPrimaryContainer else QResolveColors.OnSurfaceVariant

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(bgColor)
                        .clickable { onSelectScreen(screen) }
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                        .testTag("nav_${screen.name.lowercase()}"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = contentColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = label,
                        style = QResolveType.BodyMd.copy(
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        ),
                        color = contentColor,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Bottom Plant #4 Telemetry Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(QResolveColors.SurfaceContainerLow)
                .border(1.dp, QResolveColors.SurfaceContainer, RoundedCornerShape(6.dp))
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PLANT #4 TELEMETRY",
                    style = QResolveType.LabelCaps.copy(fontSize = 10.sp),
                    color = QResolveColors.Outline
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(QResolveColors.TertiaryContainer)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Active Containments:",
                    style = QResolveType.CodeBadge,
                    color = QResolveColors.OnSurfaceVariant
                )
                Text(
                    text = "14 Lots",
                    style = QResolveType.CodeBadge.copy(fontWeight = FontWeight.Bold),
                    color = QResolveColors.Error
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Open CAPA SLA:",
                    style = QResolveType.CodeBadge,
                    color = QResolveColors.OnSurfaceVariant
                )
                Text(
                    text = "98.4%",
                    style = QResolveType.CodeBadge.copy(fontWeight = FontWeight.Bold),
                    color = QResolveColors.Primary
                )
            }
        }
    }
}

@Composable
fun Interactive8DStepper(
    activeStageIndex: Int, // 4 = D4, 5 = D5/D6, 7 = D7, 8 = D8
    onNavigate: (AppScreen) -> Unit
) {
    data class StepItem(
        val code: String,
        val title: String,
        val subtitle: String,
        val stageNum: Int,
        val targetScreen: AppScreen
    )

    val steps = listOf(
        StepItem("D1", "CFT Assembled", "6 Members", 1, AppScreen.EXECUTIVE_DOSSIER),
        StepItem("D2", "Problem Spec", "5W2H Locked", 2, AppScreen.D4_ROOT_CAUSE),
        StepItem("D3", "Containment", "4,200 Secured", 3, AppScreen.DASHBOARD),
        StepItem("D4", "Root Cause Engine", "Dual 5-Why", 4, AppScreen.D4_ROOT_CAUSE),
        StepItem("D5", "PCA Select", "Poka-Yoke BeCu", 5, AppScreen.D5_D6_PCA),
        StepItem("D6", "Pilot Validation", "Cpk 1.92 Gate", 6, AppScreen.D5_D6_PCA),
        StepItem("D7", "FMEA & Standard", "PFMEA RPN 32", 7, AppScreen.D7_STANDARDIZATION),
        StepItem("D8", "Team Sign-Off", "Exec Closure", 8, AppScreen.D8_CLOSURE)
    )

    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            steps.forEachIndexed { index, step ->
                val isCompleted = step.stageNum < activeStageIndex ||
                    (activeStageIndex == 5 && step.stageNum < 5)
                val isActive = step.stageNum == activeStageIndex ||
                    (activeStageIndex == 5 && (step.stageNum == 5 || step.stageNum == 6))

                val bgColor = when {
                    isActive -> QResolveColors.PrimaryContainer
                    isCompleted -> QResolveColors.SurfaceContainerLow
                    else -> QResolveColors.SurfaceContainerLow.copy(alpha = 0.6f)
                }
                val titleColor = when {
                    isActive -> QResolveColors.OnPrimaryContainer
                    isCompleted -> QResolveColors.OnSurface
                    else -> QResolveColors.OnSurfaceVariant
                }
                val subColor = when {
                    isActive -> QResolveColors.PrimaryFixed
                    isCompleted -> QResolveColors.Tertiary
                    else -> QResolveColors.Outline
                }

                Row(
                    modifier = Modifier
                        .width(126.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(bgColor)
                        .border(
                            width = if (isActive) 1.dp else 0.dp,
                            color = if (isActive) QResolveColors.Primary else Color.Transparent,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clickable { onNavigate(step.targetScreen) }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isActive -> QResolveColors.SurfaceContainerLowest
                                            isCompleted -> QResolveColors.Tertiary
                                            else -> QResolveColors.SurfaceContainerHighest
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isCompleted) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Completed",
                                        tint = QResolveColors.OnTertiary,
                                        modifier = Modifier.size(12.dp)
                                    )
                                } else {
                                    Text(
                                        text = step.code,
                                        style = QResolveType.CodeBadge.copy(fontSize = 9.sp),
                                        color = if (isActive) QResolveColors.Primary else QResolveColors.Secondary
                                    )
                                }
                            }
                            Text(
                                text = if (isActive) "${step.code} ACTIVE" else step.code,
                                style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                                color = when {
                                    isActive -> QResolveColors.OnPrimaryContainer
                                    isCompleted -> QResolveColors.Tertiary
                                    else -> QResolveColors.Secondary
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = step.title.uppercase(),
                            style = QResolveType.LabelCaps.copy(fontSize = 10.sp),
                            color = titleColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = step.subtitle,
                            style = QResolveType.CodeBadge.copy(fontSize = 9.sp),
                            color = subColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                if (index < steps.lastIndex) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = QResolveColors.OutlineVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun InitiateNew8DCaseDialog(
    onDismiss: () -> Unit,
    onConfirm: (partNumber: String, partDesc: String, customer: String, line: String, severity: CaseSeverity) -> Unit
) {
    var partNumber by remember { mutableStateOf("PN-8894-C") }
    var partDesc by remember { mutableStateOf("SiC Inverter Gate Driver Daughterboard") }
    var customer by remember { mutableStateOf("Stellantis") }
    var line by remember { mutableStateOf("Line 4 - SMT Inverter") }
    var severity by remember { mutableStateOf(CaseSeverity.CRITICAL) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = QResolveColors.SurfaceContainerLowest,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = null,
                    tint = QResolveColors.Primary
                )
                Column {
                    Text(
                        text = "Initiate New 8D CAPA Investigation",
                        style = QResolveType.HeadlineSm,
                        color = QResolveColors.OnSurface
                    )
                    Text(
                        text = "Auto-ID: 8D-20250519-042 • IATF 16949 §10.2.3",
                        style = QResolveType.CodeBadge,
                        color = QResolveColors.Secondary
                    )
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = partNumber,
                    onValueChange = { partNumber = it },
                    label = { Text("Part Number & Rev", style = QResolveType.CodeBadge) },
                    singleLine = true,
                    textStyle = QResolveType.CodeBadge,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = partDesc,
                    onValueChange = { partDesc = it },
                    label = { Text("Part & Assembly Description", style = QResolveType.BodySm) },
                    singleLine = true,
                    textStyle = QResolveType.BodyMd,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = customer,
                    onValueChange = { customer = it },
                    label = { Text("OEM / Customer Account", style = QResolveType.BodySm) },
                    singleLine = true,
                    textStyle = QResolveType.BodyMd,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "NON-CONFORMANCE SEVERITY:",
                    style = QResolveType.LabelCaps,
                    color = QResolveColors.Secondary
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CaseSeverity.entries.forEach { sev ->
                        FilterChip(
                            selected = severity == sev,
                            onClick = { severity = sev },
                            label = { Text(sev.label, style = QResolveType.CodeBadge) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(partNumber, partDesc, customer, line, severity) },
                colors = ButtonDefaults.buttonColors(containerColor = QResolveColors.PrimaryContainer)
            ) {
                Text("Launch D1-D3 Protocol", style = QResolveType.HeadlineSm, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", style = QResolveType.BodyMd, color = QResolveColors.Secondary)
            }
        }
    )
}
