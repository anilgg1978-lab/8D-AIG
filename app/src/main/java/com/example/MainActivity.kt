package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AppScreen
import com.example.ui.components.InitiateNew8DCaseDialog
import com.example.ui.components.InvestigationWorkstreamSidebar
import com.example.ui.components.QResolveTopHeader
import com.example.ui.screens.*
import com.example.ui.theme.QResolveColors
import com.example.ui.theme.QResolveTheme
import com.example.ui.theme.QResolveType
import com.example.viewmodel.QResolveViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QResolveTheme {
                QResolveApp()
            }
        }
    }
}

@Composable
fun QResolveApp(viewModel: QResolveViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedLine by viewModel.selectedLine.collectAsStateWithLifecycle()
    val selectedCustomer by viewModel.selectedCustomer.collectAsStateWithLifecycle()
    val selectedSeverity by viewModel.selectedSeverity.collectAsStateWithLifecycle()
    val selectedDiscipline by viewModel.selectedDiscipline.collectAsStateWithLifecycle()
    val agingFilterActive by viewModel.agingFilterActive.collectAsStateWithLifecycle()
    val selectedLedgerRowId by viewModel.selectedLedgerRowId.collectAsStateWithLifecycle()
    val currentPage by viewModel.currentPage.collectAsStateWithLifecycle()
    val showNewCaseDialog by viewModel.showNewCaseDialog.collectAsStateWithLifecycle()
    val emergencyHoldActive by viewModel.emergencyHoldActive.collectAsStateWithLifecycle()
    val selected6MCategory by viewModel.selected6MCategory.collectAsStateWithLifecycle()
    val batch3Completed by viewModel.batch3Completed.collectAsStateWithLifecycle()
    val oemCounterSigned by viewModel.oemCounterSigned.collectAsStateWithLifecycle()
    val oemSignTimestamp by viewModel.oemSignTimestamp.collectAsStateWithLifecycle()
    val memoSent by viewModel.memoSent.collectAsStateWithLifecycle()
    val caseFormallyClosed by viewModel.caseFormallyClosed.collectAsStateWithLifecycle()
    val ledgerCases by viewModel.ledgerCases.collectAsStateWithLifecycle()
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
            viewModel.clearToast()
        }
    }

    // BackHandler for secondary screens
    BackHandler(enabled = currentScreen != AppScreen.DASHBOARD) {
        viewModel.navigateBack()
    }

    if (showNewCaseDialog) {
        InitiateNew8DCaseDialog(
            onDismiss = { viewModel.setShowNewCaseDialog(false) },
            onConfirm = { partNumber, partDesc, customer, line, severity ->
                viewModel.createNew8DCase(partNumber, partDesc, customer, line, severity)
            }
        )
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isExpandedScreen = maxWidth >= 840.dp

        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = !isExpandedScreen,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = QResolveColors.SurfaceContainerLowest,
                    modifier = Modifier.width(280.dp)
                ) {
                    InvestigationWorkstreamSidebar(
                        currentScreen = currentScreen,
                        onSelectScreen = { screen ->
                            viewModel.navigateTo(screen)
                            coroutineScope.launch { drawerState.close() }
                        }
                    )
                }
            }
        ) {
            Scaffold(
                containerColor = QResolveColors.Surface,
                contentWindowInsets = WindowInsets.safeDrawing,
                topBar = {
                    QResolveTopHeader(
                        currentScreen = currentScreen,
                        searchQuery = searchQuery,
                        onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                        onOpenDrawer = {
                            coroutineScope.launch { drawerState.open() }
                        },
                        showMenuButton = !isExpandedScreen,
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                },
                bottomBar = {
                    if (!isExpandedScreen) {
                        QResolveCompactBottomRibbon(
                            currentScreen = currentScreen,
                            onSelectScreen = { viewModel.navigateTo(it) }
                        )
                    }
                },
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState) { data ->
                        Snackbar(
                            snackbarData = data,
                            containerColor = QResolveColors.InverseSurface,
                            contentColor = Color.White,
                            shape = RoundedCornerShape(6.dp)
                        )
                    }
                }
            ) { innerPadding ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    if (isExpandedScreen) {
                        InvestigationWorkstreamSidebar(
                            currentScreen = currentScreen,
                            onSelectScreen = { viewModel.navigateTo(it) },
                            modifier = Modifier.width(248.dp)
                        )
                        VerticalDivider(
                            color = QResolveColors.SurfaceContainer,
                            thickness = 1.dp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(QResolveColors.Surface)
                    ) {
                        when (currentScreen) {
                            AppScreen.DASHBOARD -> DashboardScreen(
                                ledgerCases = ledgerCases,
                                quarantineLots = viewModel.quarantineLots,
                                searchQuery = searchQuery,
                                selectedLine = selectedLine,
                                selectedCustomer = selectedCustomer,
                                selectedSeverity = selectedSeverity,
                                selectedDiscipline = selectedDiscipline,
                                agingFilterActive = agingFilterActive,
                                selectedLedgerRowId = selectedLedgerRowId,
                                currentPage = currentPage,
                                emergencyHoldActive = emergencyHoldActive,
                                onLineFilterChange = { viewModel.setLineFilter(it) },
                                onCustomerFilterChange = { viewModel.setCustomerFilter(it) },
                                onSeverityFilterChange = { viewModel.setSeverityFilter(it) },
                                onDisciplineFilterChange = { viewModel.setDisciplineFilter(it) },
                                onToggleAgingFilter = { viewModel.toggleAgingFilter() },
                                onClearFilters = { viewModel.clearAllFilters() },
                                onSelectRow = { viewModel.selectLedgerRow(it) },
                                onInspectCase = { ledgerCase ->
                                    viewModel.selectLedgerRow(ledgerCase.ticketId)
                                    viewModel.navigateTo(ledgerCase.targetScreen)
                                },
                                onPageChange = { viewModel.setPage(it) },
                                onOpenNewCaseDialog = { viewModel.setShowNewCaseDialog(true) },
                                onToggleEmergencyHold = { viewModel.toggleEmergencyQuarantineHold() },
                                onNavigate = { viewModel.navigateTo(it) },
                                onShowToast = { viewModel.showToast(it) }
                            )

                            AppScreen.D4_ROOT_CAUSE -> D4RootCauseScreen(
                                ishikawaCategories = viewModel.ishikawaCategories,
                                selected6MCategory = selected6MCategory,
                                onSelect6MCategory = { viewModel.select6MCategory(it) },
                                onNavigate = { viewModel.navigateTo(it) },
                                onShowToast = { viewModel.showToast(it) }
                            )

                            AppScreen.D5_D6_PCA -> D5D6PcaScreen(
                                batch3Completed = batch3Completed,
                                onCompleteBatch3 = { viewModel.completeBatch3Validation() },
                                onNavigate = { viewModel.navigateTo(it) },
                                onShowToast = { viewModel.showToast(it) }
                            )

                            AppScreen.D7_STANDARDIZATION -> D7StandardizationScreen(
                                onLockD7AndProceed = { viewModel.lockD7AndProceed() },
                                onNavigate = { viewModel.navigateTo(it) },
                                onShowToast = { viewModel.showToast(it) }
                            )

                            AppScreen.D8_CLOSURE -> D8ClosureScreen(
                                oemCounterSigned = oemCounterSigned,
                                oemSignTimestamp = oemSignTimestamp,
                                memoSent = memoSent,
                                caseFormallyClosed = caseFormallyClosed,
                                onExecuteOemCounterSign = { viewModel.executeOemCounterSign() },
                                onSendExecutiveMemo = { viewModel.sendExecutiveMemo() },
                                onExecuteFormalClosure = { viewModel.executeFormalCaseClosure() },
                                onNavigate = { viewModel.navigateTo(it) },
                                onShowToast = { viewModel.showToast(it) }
                            )

                            AppScreen.LESSONS_LEARNED -> LessonsLearnedScreen(
                                viewModel = viewModel,
                                isExpandedScreen = isExpandedScreen
                            )

                            AppScreen.EXECUTIVE_DOSSIER -> ExecutiveDossierScreen(
                                viewModel = viewModel,
                                isExpandedScreen = isExpandedScreen
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QResolveCompactBottomRibbon(
    currentScreen: AppScreen,
    onSelectScreen: (AppScreen) -> Unit
) {
    val tabs = listOf(
        Triple(AppScreen.DASHBOARD, "Dashboard", Icons.Default.GridView),
        Triple(AppScreen.D4_ROOT_CAUSE, "D4 RCA", Icons.Default.AccountTree),
        Triple(AppScreen.D5_D6_PCA, "D5/D6 PCA", Icons.Default.BuildCircle),
        Triple(AppScreen.D7_STANDARDIZATION, "D7 PFMEA", Icons.AutoMirrored.Filled.FactCheck),
        Triple(AppScreen.D8_CLOSURE, "D8 Close", Icons.Default.MilitaryTech),
        Triple(AppScreen.LESSONS_LEARNED, "Yokoten KB", Icons.AutoMirrored.Filled.MenuBook),
        Triple(AppScreen.EXECUTIVE_DOSSIER, "8D PDF", Icons.AutoMirrored.Filled.Assignment)
    )

    Surface(
        color = QResolveColors.SurfaceContainerLowest,
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        Column {
            HorizontalDivider(color = QResolveColors.SurfaceContainer, thickness = 1.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEach { (screen, label, icon) ->
                    val isSelected = currentScreen == screen
                    val bgColor = if (isSelected) QResolveColors.PrimaryContainer else QResolveColors.SurfaceContainerLow
                    val contentColor = if (isSelected) Color.White else QResolveColors.OnSurfaceVariant

                    Row(
                        modifier = Modifier
                            .heightIn(min = 48.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(bgColor)
                            .clickable { onSelectScreen(screen) }
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                            .testTag("bottom_nav_${screen.name.lowercase()}"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = contentColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = label,
                            style = QResolveType.CodeBadge.copy(
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = contentColor
                        )
                    }
                }
            }
        }
    }
}
