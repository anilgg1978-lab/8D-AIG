package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.model.CaseSeverity
import com.example.model.LedgerCase
import com.example.model.QuarantineLot
import com.example.ui.theme.QResolveColors
import com.example.ui.theme.QResolveType

@Composable
fun DashboardScreen(
    ledgerCases: List<LedgerCase>,
    quarantineLots: List<QuarantineLot>,
    searchQuery: String,
    selectedLine: String,
    selectedCustomer: String,
    selectedSeverity: String,
    selectedDiscipline: String,
    agingFilterActive: Boolean,
    selectedLedgerRowId: String,
    currentPage: Int,
    emergencyHoldActive: Boolean,
    onLineFilterChange: (String) -> Unit,
    onCustomerFilterChange: (String) -> Unit,
    onSeverityFilterChange: (String) -> Unit,
    onDisciplineFilterChange: (String) -> Unit,
    onToggleAgingFilter: () -> Unit,
    onClearFilters: () -> Unit,
    onSelectRow: (String) -> Unit,
    onInspectCase: (LedgerCase) -> Unit,
    onPageChange: (Int) -> Unit,
    onOpenNewCaseDialog: () -> Unit,
    onToggleEmergencyHold: () -> Unit,
    onNavigate: (AppScreen) -> Unit,
    onShowToast: (String) -> Unit
) {
    val filteredCases = remember(
        ledgerCases,
        searchQuery,
        selectedLine,
        selectedCustomer,
        selectedSeverity,
        selectedDiscipline,
        agingFilterActive
    ) {
        ledgerCases.filter { item ->
            val matchesQuery = searchQuery.isBlank() ||
                item.ticketId.contains(searchQuery, ignoreCase = true) ||
                item.partNumber.contains(searchQuery, ignoreCase = true) ||
                item.partDescription.contains(searchQuery, ignoreCase = true) ||
                item.customer.contains(searchQuery, ignoreCase = true)

            val matchesLine = selectedLine == "All Lines (Plant #4)" ||
                item.lineFilterKey.contains(selectedLine.take(6), ignoreCase = true)

            val matchesCustomer = selectedCustomer == "All OEM Accounts" ||
                selectedCustomer.contains(item.customer.take(5), ignoreCase = true)

            val matchesSeverity = selectedSeverity == "All Severities" ||
                selectedSeverity.startsWith(item.severity.label, ignoreCase = true)

            val matchesDiscipline = selectedDiscipline == "Any Phase (D1-D8)" ||
                selectedDiscipline.startsWith("D${item.activeStage}", ignoreCase = true)

            val matchesAging = !agingFilterActive || item.agingDays > 14

            matchesQuery && matchesLine && matchesCustomer && matchesSeverity && matchesDiscipline && matchesAging
        }
    }

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
            // Emergency Hold Active Banner if triggered
            if (emergencyHoldActive) {
                Surface(
                    color = QResolveColors.ErrorContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, QResolveColors.Error, RoundedCornerShape(8.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PanTool,
                                contentDescription = null,
                                tint = QResolveColors.Error
                            )
                            Column {
                                Text(
                                    text = "EMERGENCY QUARANTINE HOLD ACTIVE — PLANT #4 DOCK DOORS LOCKED",
                                    style = QResolveType.CodeBadge,
                                    color = QResolveColors.OnErrorContainer
                                )
                                Text(
                                    text = "All outbound shipments for PN-8842-B and PN-9014-X halted pending D3 100% sort certification.",
                                    style = QResolveType.BodySm,
                                    color = QResolveColors.OnErrorContainer
                                )
                            }
                        }
                        TextButton(onClick = onToggleEmergencyHold) {
                            Text("RELEASE HOLD", style = QResolveType.CodeBadge, color = QResolveColors.Error)
                        }
                    }
                }
            }

            // Top Section: Plant Header Bar & Quick Action Ribbon
            DashboardHeaderSection(
                emergencyHoldActive = emergencyHoldActive,
                onOpenNewCaseDialog = onOpenNewCaseDialog,
                onToggleEmergencyHold = onToggleEmergencyHold,
                onExportPdf = { onNavigate(AppScreen.EXECUTIVE_DOSSIER) },
                onExportExcel = { onShowToast("Exporting VDA 8D Matrix to Excel (.XLSX)...") },
                onReload = { onShowToast("Plant #4 MES & CAPA Telemetry synchronized (30s cycle).") }
            )

            // Section 1: 4 Executive KPI Cards
            ExecutiveKpiGrid(isWide = isWide)

            // Section 2: Industrial Filter Matrix Ribbon
            IndustrialFilterMatrixRibbon(
                selectedLine = selectedLine,
                selectedCustomer = selectedCustomer,
                selectedSeverity = selectedSeverity,
                selectedDiscipline = selectedDiscipline,
                agingFilterActive = agingFilterActive,
                displayedCount = filteredCases.size,
                totalCount = 18,
                onLineFilterChange = onLineFilterChange,
                onCustomerFilterChange = onCustomerFilterChange,
                onSeverityFilterChange = onSeverityFilterChange,
                onDisciplineFilterChange = onDisciplineFilterChange,
                onToggleAgingFilter = onToggleAgingFilter,
                onClearFilters = onClearFilters
            )

            // Section 3 & 4: Main Layout Split
            if (isWide) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(0.67f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ActiveLedgerTableCard(
                            cases = filteredCases,
                            selectedRowId = selectedLedgerRowId,
                            currentPage = currentPage,
                            onSelectRow = onSelectRow,
                            onInspectCase = onInspectCase,
                            onPageChange = onPageChange
                        )
                        ContainmentVerificationMatrixCard()
                    }
                    Column(
                        modifier = Modifier.weight(0.33f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        RootCauseParetoCard(
                            onLaunchFishbone = { onNavigate(AppScreen.D4_ROOT_CAUSE) }
                        )
                        LiveQuarantineTallyCard(
                            lots = quarantineLots,
                            onPrintTags = {
                                onShowToast("Sent 14 Red/Green Quarantine Labels to Zebra ZT411 (Bay C4).")
                            }
                        )
                        AuditReadinessCard()
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ActiveLedgerTableCard(
                        cases = filteredCases,
                        selectedRowId = selectedLedgerRowId,
                        currentPage = currentPage,
                        onSelectRow = onSelectRow,
                        onInspectCase = onInspectCase,
                        onPageChange = onPageChange
                    )
                    ContainmentVerificationMatrixCard()
                    RootCauseParetoCard(
                        onLaunchFishbone = { onNavigate(AppScreen.D4_ROOT_CAUSE) }
                    )
                    LiveQuarantineTallyCard(
                        lots = quarantineLots,
                        onPrintTags = {
                            onShowToast("Sent 14 Red/Green Quarantine Labels to Zebra ZT411 (Bay C4).")
                        }
                    )
                    AuditReadinessCard()
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DashboardHeaderSection(
    emergencyHoldActive: Boolean,
    onOpenNewCaseDialog: () -> Unit,
    onToggleEmergencyHold: () -> Unit,
    onExportPdf: () -> Unit,
    onExportExcel: () -> Unit,
    onReload: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Breadcrumb
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text("FACILITY #04 DETROIT", style = QResolveType.CodeBadge, color = QResolveColors.Secondary)
            Text("/", style = QResolveType.CodeBadge, color = QResolveColors.Secondary)
            Text("LINE 4 SMT & INVERTER ASSY", style = QResolveType.CodeBadge, color = QResolveColors.Secondary)
            Text("/", style = QResolveType.CodeBadge, color = QResolveColors.Secondary)
            Text("CAPA CONTROL DESK", style = QResolveType.CodeBadge, color = QResolveColors.Primary)
        }

        // Title + Telemetry Pill
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "8D Incident Control & CAPA Command",
                style = QResolveType.HeadlineLg,
                color = QResolveColors.OnSurface
            )
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(QResolveColors.SurfaceContainerHigh)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(QResolveColors.Primary)
                )
                Text(
                    text = "REAL-TIME TELEMETRY ACTIVE",
                    style = QResolveType.CodeBadge,
                    color = QResolveColors.Primary
                )
            }
        }

        // Quick Action Ribbon
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // + Initiate New 8D Case
            Button(
                onClick = onOpenNewCaseDialog,
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = QResolveColors.PrimaryContainer,
                    contentColor = QResolveColors.OnPrimaryContainer
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier
                    .height(36.dp)
                    .testTag("initiate_new_8d_button")
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("+ Initiate New 8D Case", style = QResolveType.HeadlineSm.copy(fontSize = 12.sp))
            }

            // Emergency Quarantine Hold
            Button(
                onClick = onToggleEmergencyHold,
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (emergencyHoldActive) QResolveColors.OnErrorContainer else QResolveColors.Error,
                    contentColor = QResolveColors.OnError
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier
                    .height(36.dp)
                    .testTag("emergency_quarantine_button")
            ) {
                Icon(
                    imageVector = Icons.Default.PanTool,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (emergencyHoldActive) "Quarantine Hold Enforced" else "Emergency Quarantine Hold",
                    style = QResolveType.HeadlineSm.copy(fontSize = 12.sp)
                )
            }

            // AIAG PDF & VDA Excel pill group
            Row(
                modifier = Modifier
                    .height(36.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(QResolveColors.SurfaceContainerLow)
                    .padding(2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { onExportPdf() }
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PictureAsPdf,
                        contentDescription = null,
                        tint = QResolveColors.OnSurfaceVariant,
                        modifier = Modifier.size(15.dp)
                    )
                    Text("AIAG PDF", style = QResolveType.BodySm.copy(fontWeight = FontWeight.Medium), color = QResolveColors.OnSurfaceVariant)
                }
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { onExportExcel() }
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.TableView,
                        contentDescription = null,
                        tint = QResolveColors.OnSurfaceVariant,
                        modifier = Modifier.size(15.dp)
                    )
                    Text("VDA Excel", style = QResolveType.BodySm.copy(fontWeight = FontWeight.Medium), color = QResolveColors.OnSurfaceVariant)
                }
            }

            // Reload Feed
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(QResolveColors.SurfaceContainer)
                    .clickable { onReload() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reload Feed",
                    tint = QResolveColors.OnSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun ExecutiveKpiGrid(isWide: Boolean) {
    if (isWide) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            KpiCard1Active8D(modifier = Modifier.weight(1f))
            KpiCard2MttcD3(modifier = Modifier.weight(1f))
            KpiCard3MttcLifecycle(modifier = Modifier.weight(1f))
            KpiCard4FirstPassRecurrence(modifier = Modifier.weight(1f))
        }
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                KpiCard1Active8D(modifier = Modifier.weight(1f))
                KpiCard2MttcD3(modifier = Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                KpiCard3MttcLifecycle(modifier = Modifier.weight(1f))
                KpiCard4FirstPassRecurrence(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun KpiCard1Active8D(modifier: Modifier = Modifier) {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 1.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("ACTIVE 8D WORKSTREAM", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(QResolveColors.SurfaceContainerHigh)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("+2 this wk", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Primary)
                }
            }
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("18", style = QResolveType.DataMetricLg, color = QResolveColors.OnSurface)
                Text(
                    "Total Investigations",
                    style = QResolveType.BodySm,
                    color = QResolveColors.Secondary,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
            // Segmented Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(QResolveColors.SurfaceContainer)
            ) {
                Box(modifier = Modifier.weight(0.22f).fillMaxHeight().background(QResolveColors.Error))
                Box(modifier = Modifier.weight(0.39f).fillMaxHeight().background(QResolveColors.PrimaryContainer))
                Box(modifier = Modifier.weight(0.28f).fillMaxHeight().background(QResolveColors.TertiaryContainer))
                Box(modifier = Modifier.weight(0.11f).fillMaxHeight().background(QResolveColors.Secondary))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("4 D3 Hold", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Error)
                Text("7 D4 RCA", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Primary)
                Text("5 D6 Valid", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Tertiary)
                Text("2 D8 Sign", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Secondary)
            }
        }
    }
}

@Composable
private fun KpiCard2MttcD3(modifier: Modifier = Modifier) {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 1.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("MEAN TIME TO CONTAIN (D3)", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(QResolveColors.SurfaceContainer)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("TGT < 18h", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Tertiary)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("14.8", style = QResolveType.DataMetricLg, color = QResolveColors.OnSurface)
                    Text("Hours", style = QResolveType.BodySm, color = QResolveColors.Secondary, modifier = Modifier.padding(bottom = 2.dp))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.TrendingDown,
                        contentDescription = null,
                        tint = QResolveColors.Tertiary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text("-3.2h vs prior", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Tertiary)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Immediate ICA SLA:", style = QResolveType.BodySm, color = QResolveColors.Secondary)
                Text("94.4% on-target", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Tertiary)
            }
        }
    }
}

@Composable
private fun KpiCard3MttcLifecycle(modifier: Modifier = Modifier) {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 1.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("MTTC-8D LIFECYCLE", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(QResolveColors.SurfaceContainer)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("SLA ≤ 30d", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.OnSurfaceVariant)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("24.2", style = QResolveType.DataMetricLg, color = QResolveColors.OnSurface)
                    Text("Days", style = QResolveType.BodySm, color = QResolveColors.Secondary, modifier = Modifier.padding(bottom = 2.dp))
                }
                Text("88.4% in SLA", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Primary)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Fastest closed:", style = QResolveType.BodySm, color = QResolveColors.Secondary)
                Text("9.5d (Line 2)", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.OnSurface)
            }
        }
    }
}

@Composable
private fun KpiCard4FirstPassRecurrence(modifier: Modifier = Modifier) {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 1.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("FIRST-PASS RECURRENCE", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(QResolveColors.TertiaryFixed)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("0 Escapes Q2", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.OnTertiaryFixedVariant)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("1.2%", style = QResolveType.DataMetricLg, color = QResolveColors.Tertiary)
                    Text("12-Mo", style = QResolveType.BodySm, color = QResolveColors.Secondary, modifier = Modifier.padding(bottom = 2.dp))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = QResolveColors.Tertiary,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text("< 2.0% IATF", style = QResolveType.CodeBadge.copy(fontSize = 9.sp), color = QResolveColors.Tertiary)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Horizontal (D7):", style = QResolveType.BodySm, color = QResolveColors.Secondary)
                Text("42 PFMEAs Synced", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.OnSurface)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun IndustrialFilterMatrixRibbon(
    selectedLine: String,
    selectedCustomer: String,
    selectedSeverity: String,
    selectedDiscipline: String,
    agingFilterActive: Boolean,
    displayedCount: Int,
    totalCount: Int,
    onLineFilterChange: (String) -> Unit,
    onCustomerFilterChange: (String) -> Unit,
    onSeverityFilterChange: (String) -> Unit,
    onDisciplineFilterChange: (String) -> Unit,
    onToggleAgingFilter: () -> Unit,
    onClearFilters: () -> Unit
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
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterDropdownPill(
                    label = "LINE:",
                    selected = selectedLine,
                    options = listOf(
                        "All Lines (Plant #4)",
                        "Line 4 - SMT Inverter",
                        "Line 2 - Stamping & Housing",
                        "Line 7 - High-Voltage Harness"
                    ),
                    onSelect = onLineFilterChange
                )
                FilterDropdownPill(
                    label = "OEM / CUSTOMER:",
                    selected = selectedCustomer,
                    options = listOf(
                        "All OEM Accounts",
                        "Stellantis Powertrain",
                        "General Motors (BEV3)",
                        "Ford Electrification",
                        "Tesla Energy Tier-1"
                    ),
                    onSelect = onCustomerFilterChange
                )
                FilterDropdownPill(
                    label = "SEVERITY:",
                    selected = selectedSeverity,
                    options = listOf(
                        "All Severities",
                        "Critical (Containment < 24h)",
                        "Major",
                        "Minor"
                    ),
                    onSelect = onSeverityFilterChange
                )
                FilterDropdownPill(
                    label = "DISCIPLINE:",
                    selected = selectedDiscipline,
                    options = listOf(
                        "Any Phase (D1-D8)",
                        "D3 - Containment Active",
                        "D4 - 5-Why & Fishbone",
                        "D6 - PCA Validation",
                        "D7 - Horizontal Standardization"
                    ),
                    onSelect = onDisciplineFilterChange
                )

                // Aging > 14 Days Pill
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (agingFilterActive) QResolveColors.Error else QResolveColors.ErrorContainer)
                        .clickable { onToggleAgingFilter() }
                        .padding(horizontal = 8.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = if (agingFilterActive) QResolveColors.OnError else QResolveColors.OnErrorContainer,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "Aging > 14 Days (4)",
                        style = QResolveType.CodeBadge,
                        color = if (agingFilterActive) QResolveColors.OnError else QResolveColors.OnErrorContainer
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Displaying: $displayedCount of $totalCount Cases",
                    style = QResolveType.CodeBadge,
                    color = QResolveColors.Secondary
                )
                IconButton(
                    onClick = onClearFilters,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterListOff,
                        contentDescription = "Clear Filters",
                        tint = QResolveColors.Secondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterDropdownPill(
    label: String,
    selected: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(QResolveColors.SurfaceContainerLow)
                .clickable { expanded = true }
                .padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(label, style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
            Text(selected, style = QResolveType.BodySm, color = QResolveColors.OnSurface)
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = QResolveColors.Secondary,
                modifier = Modifier.size(14.dp)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = QResolveColors.SurfaceContainerLowest
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, style = QResolveType.BodySm, color = QResolveColors.OnSurface) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ActiveLedgerTableCard(
    cases: List<LedgerCase>,
    selectedRowId: String,
    currentPage: Int,
    onSelectRow: (String) -> Unit,
    onInspectCase: (LedgerCase) -> Unit,
    onPageChange: (Int) -> Unit
) {
    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Table Header Toolbar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(QResolveColors.SurfaceContainerLow)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Active Containment & Investigation Ledger",
                        style = QResolveType.HeadlineSm,
                        color = QResolveColors.OnSurface
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(QResolveColors.SurfaceContainerHighest)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("REV 4.8 LIVE", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Auto-sync: 30s", style = QResolveType.BodySm, color = QResolveColors.Secondary)
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(QResolveColors.Tertiary)
                    )
                }
            }

            // Horizontally Scrollable Industrial Data Table
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                // Table Header Row
                Row(
                    modifier = Modifier
                        .background(QResolveColors.SurfaceContainerLow)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("TICKET ID & TIME", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Secondary, modifier = Modifier.width(136.dp))
                    Text("PART & ASSEMBLY", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Secondary, modifier = Modifier.width(185.dp))
                    Text("CUSTOMER / WORKCELL", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Secondary, modifier = Modifier.width(145.dp))
                    Text("SEVERITY", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Secondary, modifier = Modifier.width(95.dp))
                    Text("8D PHASE TRACKER", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Secondary, modifier = Modifier.width(210.dp))
                    Text("SLA STATUS", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Secondary, modifier = Modifier.width(155.dp))
                    Text("CONTAINMENT (D3)", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Secondary, modifier = Modifier.width(150.dp))
                    Text("CFT LEAD", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Secondary, modifier = Modifier.width(120.dp))
                    Text("ACTION", style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Secondary, modifier = Modifier.width(90.dp))
                }
                HorizontalDivider(color = QResolveColors.SurfaceContainer)

                cases.forEach { item ->
                    val isSelected = item.ticketId == selectedRowId
                    Row(
                        modifier = Modifier
                            .background(
                                if (isSelected) QResolveColors.SurfaceContainerHigh.copy(alpha = 0.45f)
                                else QResolveColors.SurfaceContainerLowest
                            )
                            .clickable { onSelectRow(item.ticketId) }
                            .padding(horizontal = 12.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Ticket ID & Time
                        Column(modifier = Modifier.width(136.dp)) {
                            Text(item.ticketId, style = QResolveType.CodeBadge, color = QResolveColors.Primary)
                            Text(item.loggedTime, style = QResolveType.BodySm.copy(fontSize = 11.sp), color = QResolveColors.Secondary)
                        }
                        // Part & Assembly
                        Column(modifier = Modifier.width(185.dp).padding(end = 8.dp)) {
                            Text(item.partNumber, style = QResolveType.BodyMd.copy(fontWeight = FontWeight.SemiBold), color = QResolveColors.OnSurface)
                            Text(
                                item.partDescription,
                                style = QResolveType.BodySm.copy(fontSize = 11.sp),
                                color = QResolveColors.Secondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        // Customer / Workcell
                        Column(modifier = Modifier.width(145.dp)) {
                            Text(item.customer, style = QResolveType.BodyMd.copy(fontWeight = FontWeight.Medium), color = QResolveColors.OnSurface)
                            Text(item.workcell, style = QResolveType.BodySm.copy(fontSize = 11.sp), color = QResolveColors.Secondary)
                        }
                        // Severity Badge
                        Box(modifier = Modifier.width(95.dp)) {
                            val (bg, fg, dot) = when (item.severity) {
                                CaseSeverity.CRITICAL -> Triple(QResolveColors.ErrorContainer, QResolveColors.OnErrorContainer, QResolveColors.Error)
                                CaseSeverity.MAJOR -> Triple(QResolveColors.SurfaceContainerHigh, QResolveColors.Primary, QResolveColors.Primary)
                                CaseSeverity.MINOR -> Triple(QResolveColors.SurfaceContainer, QResolveColors.Secondary, QResolveColors.Secondary)
                            }
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(bg)
                                    .padding(horizontal = 6.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(dot))
                                Text(item.severity.label, style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = fg)
                            }
                        }
                        // 8D Phase Tracker
                        Column(modifier = Modifier.width(210.dp)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                for (stage in 1..8) {
                                    val isDone = stage <= item.completedStages
                                    val isAct = stage == item.activeStage
                                    val pillBg = when {
                                        isAct && item.activeStageError -> QResolveColors.Error
                                        isAct && stage == 7 -> QResolveColors.TertiaryContainer
                                        isAct -> QResolveColors.PrimaryContainer
                                        else -> QResolveColors.SurfaceContainer
                                    }
                                    val pillFg = when {
                                        isAct -> Color.White
                                        isDone -> QResolveColors.Tertiary
                                        else -> QResolveColors.Secondary
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(pillBg)
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "D$stage",
                                            style = QResolveType.CodeBadge.copy(fontSize = 9.sp),
                                            color = pillFg
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = item.phaseStatusNote,
                                style = QResolveType.BodySm.copy(fontSize = 10.sp, fontWeight = FontWeight.Medium),
                                color = when {
                                    item.activeStageError -> QResolveColors.Error
                                    item.activeStage == 7 -> QResolveColors.Tertiary
                                    item.activeStage == 3 -> QResolveColors.Primary
                                    else -> QResolveColors.Secondary
                                }
                            )
                        }
                        // SLA Status
                        Box(modifier = Modifier.width(155.dp)) {
                            val slaBg = when {
                                item.slaIsUrgent -> QResolveColors.ErrorContainer
                                item.slaIsGood -> QResolveColors.TertiaryFixed
                                else -> QResolveColors.SurfaceContainer
                            }
                            val slaFg = when {
                                item.slaIsUrgent -> QResolveColors.Error
                                item.slaIsGood -> QResolveColors.OnTertiaryFixedVariant
                                else -> QResolveColors.OnSurface
                            }
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(slaBg)
                                    .padding(horizontal = 6.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = when {
                                        item.slaIsUrgent -> Icons.Default.Alarm
                                        item.slaIsGood -> Icons.Default.CheckCircle
                                        else -> Icons.Default.HourglassTop
                                    },
                                    contentDescription = null,
                                    tint = slaFg,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(item.slaStatusText, style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = slaFg)
                            }
                        }
                        // Containment (D3)
                        Column(modifier = Modifier.width(150.dp)) {
                            Text(
                                text = item.containmentTitle,
                                style = QResolveType.BodySm.copy(fontWeight = FontWeight.SemiBold),
                                color = if (item.containmentIsAlert) QResolveColors.Error else QResolveColors.Tertiary
                            )
                            Text(item.containmentSub, style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                        }
                        // CFT Lead
                        Row(
                            modifier = Modifier.width(120.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(1f)) {
                                Text(item.leadName, style = QResolveType.BodySm.copy(fontWeight = FontWeight.SemiBold), color = QResolveColors.OnSurface)
                                Text(item.leadRole, style = QResolveType.BodySm.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                            }
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(QResolveColors.PrimaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(item.leadInitials, style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = Color.White)
                            }
                        }
                        // Action button
                        Box(modifier = Modifier.width(90.dp), contentAlignment = Alignment.Center) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(QResolveColors.SurfaceContainer)
                                    .clickable { onInspectCase(item) }
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                                    .testTag("inspect_${item.ticketId}"),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text("Inspect", style = QResolveType.BodySm.copy(fontWeight = FontWeight.Medium), color = QResolveColors.Primary)
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Inspect",
                                    tint = QResolveColors.Primary,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }
                    }
                    HorizontalDivider(color = QResolveColors.SurfaceContainer)
                }
            }

            // Table Footer / Pagination
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(QResolveColors.SurfaceContainerLow)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Showing rows 1–${cases.size} of 18 active records •",
                        style = QResolveType.BodySm,
                        color = QResolveColors.Secondary
                    )
                    Text(
                        text = "100% digital trace verified",
                        style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                        color = QResolveColors.Tertiary
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    PaginationPill("PREV", enabled = currentPage > 1, isActive = false) { onPageChange(currentPage - 1) }
                    for (p in 1..3) {
                        PaginationPill(p.toString(), enabled = true, isActive = currentPage == p) { onPageChange(p) }
                    }
                    PaginationPill("NEXT", enabled = currentPage < 3, isActive = false) { onPageChange(currentPage + 1) }
                }
            }
        }
    }
}

@Composable
private fun PaginationPill(text: String, enabled: Boolean, isActive: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(if (isActive) QResolveColors.PrimaryContainer else QResolveColors.SurfaceContainer)
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
            color = when {
                isActive -> QResolveColors.OnPrimary
                enabled -> QResolveColors.OnSurface
                else -> QResolveColors.Outline
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ContainmentVerificationMatrixCard() {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                            .size(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(QResolveColors.PrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.RuleFolder,
                            contentDescription = null,
                            tint = QResolveColors.OnPrimaryContainer
                        )
                    }
                    Column {
                        Text(
                            text = "5-Tier Containment Verification Matrix (D3 Protocol)",
                            style = QResolveType.HeadlineSm,
                            color = QResolveColors.OnSurface
                        )
                        Text(
                            text = "Mandatory certified containment before authorization to D4 root-cause experimentation",
                            style = QResolveType.BodySm,
                            color = QResolveColors.Secondary
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(QResolveColors.SurfaceContainer)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("ISO 9001:2015 CLAUSE 10.2", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                }
            }

            val tiers = listOf(
                Triple("1. IN-TRANSIT", "2,800 pcs", "Dock Locked" to QResolveColors.Tertiary),
                Triple("2. FG WAREHOUSE", "4,150 pcs", "Red Tag Staged" to QResolveColors.Tertiary),
                Triple("3. WIP BUFFER", "820 pcs", "Purge at 58%" to QResolveColors.Error),
                Triple("4. RAW SUPPLIER", "11,200 pcs", "Supplier Hold" to QResolveColors.Tertiary),
                Triple("5. 200% QA GATE", "Active", "3 Shifts Enforced" to QResolveColors.Primary)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tiers.forEach { (title, metric, statusPair) ->
                    Column(
                        modifier = Modifier
                            .width(142.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(QResolveColors.SurfaceContainerLow)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(title, style = QResolveType.LabelCaps.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                        Text(metric, style = QResolveType.DataMetricSm.copy(fontWeight = FontWeight.Bold), color = QResolveColors.OnSurface)
                        Text(statusPair.first, style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = statusPair.second)
                    }
                }
            }
        }
    }
}

@Composable
private fun RootCauseParetoCard(onLaunchFishbone: () -> Unit) {
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = null,
                        tint = QResolveColors.Primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text("Root Cause Pareto (D4)", style = QResolveType.HeadlineSm, color = QResolveColors.OnSurface)
                }
                Text("LAST 90 DAYS", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
            }

            Text(
                text = "Aggregated Ishikawa 6M classification across all production lines",
                style = QResolveType.BodySm,
                color = QResolveColors.Secondary
            )

            val paretoRows = listOf(
                Triple("Tooling & Die Wear (Machine)", "34% (28 cases)", 0.34f to QResolveColors.Primary),
                Triple("Solder Temperature Drift (Method)", "26% (21 cases)", 0.26f to QResolveColors.PrimaryContainer),
                Triple("Material Lot Variance (Material)", "18% (15 cases)", 0.18f to QResolveColors.SecondaryFixedDim),
                Triple("Drawing Spec Ambiguity (Measurement)", "12% (10 cases)", 0.12f to QResolveColors.OutlineVariant),
                Triple("Operator Fixture Setup (Man)", "10% (8 cases)", 0.10f to QResolveColors.OutlineVariant)
            )

            paretoRows.forEach { (label, valueText, barInfo) ->
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(label, style = QResolveType.BodySm.copy(fontWeight = FontWeight.Medium), color = QResolveColors.OnSurface)
                        Text(
                            valueText,
                            style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                            color = if (barInfo.first >= 0.25f) QResolveColors.Primary else QResolveColors.Secondary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(7.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(QResolveColors.SurfaceContainer)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(barInfo.first)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(4.dp))
                                .background(barInfo.second)
                        )
                    }
                }
            }

            HorizontalDivider(color = QResolveColors.SurfaceContainer)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Pareto 80/20 Cutoff: 2 Drivers", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Secondary)
                Text(
                    text = "Launch 6M Fishbone Engine →",
                    style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                    color = QResolveColors.Primary,
                    modifier = Modifier
                        .clickable { onLaunchFishbone() }
                        .padding(vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun LiveQuarantineTallyCard(
    lots: List<QuarantineLot>,
    onPrintTags: () -> Unit
) {
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Emergency,
                        contentDescription = null,
                        tint = QResolveColors.Error,
                        modifier = Modifier.size(18.dp)
                    )
                    Text("Live Quarantine Tally", style = QResolveType.HeadlineSm, color = QResolveColors.OnSurface)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(QResolveColors.Error))
                    Text("14 ACTIVE LOTS", style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Error)
                }
            }

            lots.forEach { lot ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(QResolveColors.SurfaceContainerLow)
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(lot.lotId, style = QResolveType.CodeBadge, color = QResolveColors.OnSurface)
                        Text(lot.locationAndCustomer, style = QResolveType.BodySm.copy(fontSize = 11.sp), color = QResolveColors.Secondary)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = lot.pieces,
                            style = QResolveType.DataMetricSm.copy(fontWeight = FontWeight.Bold),
                            color = when {
                                lot.isRedTag -> QResolveColors.Error
                                lot.isGreenTag -> QResolveColors.Tertiary
                                else -> QResolveColors.OnSurface
                            }
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    when {
                                        lot.isRedTag -> QResolveColors.ErrorContainer
                                        lot.isGreenTag -> QResolveColors.TertiaryFixed
                                        else -> QResolveColors.SurfaceContainerHighest
                                    }
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = lot.tagStatus,
                                style = QResolveType.CodeBadge.copy(fontSize = 9.sp),
                                color = when {
                                    lot.isRedTag -> QResolveColors.OnErrorContainer
                                    lot.isGreenTag -> QResolveColors.OnTertiaryFixedVariant
                                    else -> QResolveColors.Secondary
                                }
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(QResolveColors.SurfaceContainer)
                    .clickable { onPrintTags() }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Print,
                    contentDescription = null,
                    tint = QResolveColors.OnSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Print Physical Red / Green Tags (Zebra ZT411)",
                    style = QResolveType.BodySm.copy(fontWeight = FontWeight.Medium),
                    color = QResolveColors.OnSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun AuditReadinessCard() {
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.FactCheck,
                        contentDescription = null,
                        tint = QResolveColors.Tertiary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text("IATF / ISO Audit Readiness", style = QResolveType.HeadlineSm, color = QResolveColors.OnSurface)
                }
                Text("98.4%", style = QResolveType.DataMetricSm.copy(fontWeight = FontWeight.Bold), color = QResolveColors.Tertiary)
            }

            val rows = listOf(
                "D3 24h Containment Sign-Off:" to "100% Compliant",
                "D4 Occurrence vs Escape Proof:" to "96.8% Compliant",
                "D7 Horizontal PFMEA Read-Across:" to "98.2% Compliant",
                "D8 Executive Digital Signatures:" to "100% Compliant"
            )
            rows.forEachIndexed { idx, (label, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(label, style = QResolveType.BodySm, color = QResolveColors.Secondary)
                    Text(value, style = QResolveType.CodeBadge.copy(fontSize = 10.sp), color = QResolveColors.Tertiary)
                }
                if (idx < rows.lastIndex) {
                    HorizontalDivider(color = QResolveColors.SurfaceContainer)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(QResolveColors.SurfaceContainerLow)
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.VerifiedUser,
                    contentDescription = null,
                    tint = QResolveColors.Tertiary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Next Surveillance Audit: Detroit Plant #4 scheduled in 18 days (Bureau Veritas ISO 9001:2015).",
                    style = QResolveType.BodySm.copy(fontSize = 11.sp),
                    color = QResolveColors.Secondary
                )
            }
        }
    }
}
