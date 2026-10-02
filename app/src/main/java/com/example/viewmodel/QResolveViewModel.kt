package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class QResolveViewModel : ViewModel() {

    private val _currentScreen = MutableStateFlow(AppScreen.DASHBOARD)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _screenHistory = mutableListOf<AppScreen>()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Dashboard Filters
    private val _selectedLine = MutableStateFlow("All Lines (Plant #4)")
    val selectedLine: StateFlow<String> = _selectedLine.asStateFlow()

    private val _selectedCustomer = MutableStateFlow("All OEM Accounts")
    val selectedCustomer: StateFlow<String> = _selectedCustomer.asStateFlow()

    private val _selectedSeverity = MutableStateFlow("All Severities")
    val selectedSeverity: StateFlow<String> = _selectedSeverity.asStateFlow()

    private val _selectedDiscipline = MutableStateFlow("Any Phase (D1-D8)")
    val selectedDiscipline: StateFlow<String> = _selectedDiscipline.asStateFlow()

    private val _agingFilterActive = MutableStateFlow(false)
    val agingFilterActive: StateFlow<Boolean> = _agingFilterActive.asStateFlow()

    private val _selectedLedgerRowId = MutableStateFlow("8D-20250514-039")
    val selectedLedgerRowId: StateFlow<String> = _selectedLedgerRowId.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    // Modals
    private val _showNewCaseDialog = MutableStateFlow(false)
    val showNewCaseDialog: StateFlow<Boolean> = _showNewCaseDialog.asStateFlow()

    private val _emergencyHoldActive = MutableStateFlow(false)
    val emergencyHoldActive: StateFlow<Boolean> = _emergencyHoldActive.asStateFlow()

    // D4 State
    private val _selected6MCategory = MutableStateFlow("Machine")
    val selected6MCategory: StateFlow<String> = _selected6MCategory.asStateFlow()

    // D5/D6 State
    private val _selectedOccurrenceOption = MutableStateFlow("1A")
    val selectedOccurrenceOption: StateFlow<String> = _selectedOccurrenceOption.asStateFlow()

    private val _batch3Completed = MutableStateFlow(false)
    val batch3Completed: StateFlow<Boolean> = _batch3Completed.asStateFlow()

    // D7 State
    private val _d7Locked = MutableStateFlow(false)
    val d7Locked: StateFlow<Boolean> = _d7Locked.asStateFlow()

    // D8 State
    private val _oemCounterSigned = MutableStateFlow(false)
    val oemCounterSigned: StateFlow<Boolean> = _oemCounterSigned.asStateFlow()

    private val _oemSignTimestamp = MutableStateFlow("Awaiting Signature")
    val oemSignTimestamp: StateFlow<String> = _oemSignTimestamp.asStateFlow()

    private val _memoSent = MutableStateFlow(false)
    val memoSent: StateFlow<Boolean> = _memoSent.asStateFlow()

    private val _caseFormallyClosed = MutableStateFlow(false)
    val caseFormallyClosed: StateFlow<Boolean> = _caseFormallyClosed.asStateFlow()

    // Lessons Learned State
    private val _isBookmarked = MutableStateFlow(false)
    val isBookmarked: StateFlow<Boolean> = _isBookmarked.asStateFlow()

    // Toast feedback
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    private val _ledgerCases = MutableStateFlow(
        listOf(
            LedgerCase(
                ticketId = "8D-20250514-039",
                loggedTime = "Logged: May 14, 07:15",
                partNumber = "PN-8842-B",
                partDescription = "ECU Bus Connector Header (12-Pin)",
                customer = "Stellantis",
                workcell = "Line 4 SMT Station 3",
                lineFilterKey = "Line 4 - SMT Inverter",
                severity = CaseSeverity.CRITICAL,
                completedStages = 5,
                activeStage = 6,
                activeStageError = true,
                phaseStatusNote = "PCA Trial Validation Fail",
                slaStatusText = "Overdue D6: +1d",
                slaIsUrgent = true,
                containmentTitle = "100% Quarantined",
                containmentSub = "2,450 pcs sorted | 14 NG",
                leadName = "M. Reyes",
                leadRole = "QA Lead",
                leadInitials = "MR",
                agingDays = 16,
                targetScreen = AppScreen.D4_ROOT_CAUSE
            ),
            LedgerCase(
                ticketId = "8D-20250518-041",
                loggedTime = "Logged: Today, 03:40",
                partNumber = "PN-9014-X",
                partDescription = "DC-DC Converter Housing Gasket",
                customer = "General Motors",
                workcell = "Line 2 Press & Seal",
                lineFilterKey = "Line 2 - Stamping & Housing",
                severity = CaseSeverity.CRITICAL,
                completedStages = 2,
                activeStage = 3,
                activeStageError = false,
                phaseStatusNote = "Warehouse Gate Hold in Progress",
                slaStatusText = "D3: 04h 22m remaining",
                slaIsUrgent = true,
                containmentTitle = "WIP Purge Ongoing",
                containmentSub = "820 / 1,400 inspected",
                containmentIsAlert = true,
                leadName = "T. Kowalski",
                leadRole = "Mfg Eng",
                leadInitials = "TK",
                agingDays = 2,
                targetScreen = AppScreen.D5_D6_PCA
            ),
            LedgerCase(
                ticketId = "8D-20250512-038",
                loggedTime = "Logged: May 12, 14:20",
                partNumber = "PN-3100-F",
                partDescription = "IGBT Driver Gate Resistor Array",
                customer = "Ford Electrification",
                workcell = "Line 4 Reflow Oven 2",
                lineFilterKey = "Line 4 - SMT Inverter",
                severity = CaseSeverity.MAJOR,
                completedStages = 3,
                activeStage = 4,
                activeStageError = false,
                phaseStatusNote = "Dual-Track 5-Why in review",
                slaStatusText = "D4: 3d remaining",
                slaIsUrgent = false,
                containmentTitle = "Verified (200% Gate)",
                containmentSub = "Customer stock buffered",
                leadName = "A. Chen",
                leadRole = "Chief Metallurgist",
                leadInitials = "AC",
                agingDays = 18,
                targetScreen = AppScreen.D4_ROOT_CAUSE
            ),
            LedgerCase(
                ticketId = "8D-20250508-035",
                loggedTime = "Logged: May 08, 11:00",
                partNumber = "PN-1922-A",
                partDescription = "Shielded Sensor Ground Harness",
                customer = "Tesla Tier-1",
                workcell = "Line 7 Automated Crimper",
                lineFilterKey = "Line 7 - High-Voltage Harness",
                severity = CaseSeverity.MINOR,
                completedStages = 6,
                activeStage = 7,
                activeStageError = false,
                phaseStatusNote = "PFMEA & Control Plan Update",
                slaStatusText = "D7: On Schedule",
                slaIsUrgent = false,
                slaIsGood = true,
                containmentTitle = "Released from Hold",
                containmentSub = "Zero escape verification ok",
                leadName = "D. Scott",
                leadRole = "Lead SQE",
                leadInitials = "DS",
                agingDays = 22,
                targetScreen = AppScreen.D7_STANDARDIZATION
            )
        )
    )
    val ledgerCases: StateFlow<List<LedgerCase>> = _ledgerCases.asStateFlow()

    val ishikawaCategories = listOf(
        IshikawaCategory(
            name = "Machine",
            probabilityBadge = "94%",
            isHighConfidence = true,
            hypotheses = listOf(
                IshikawaHypothesis(
                    id = "HYPOTHESIS M-01",
                    statusBadge = "94% PROBABLE",
                    isProbable = true,
                    title = "Carrier #8 Fixture Thermal Deflection & Clamp Play",
                    description = "Warpage measured at 0.28mm under 260°C wave solder bath.",
                    footerLeft = "Validated",
                    footerRight = "Test: RECREATE-01"
                ),
                IshikawaHypothesis(
                    id = "HYPOTHESIS M-02",
                    statusBadge = "12% RULED OUT",
                    isProbable = false,
                    title = "Solder Pot Alloy Contamination / Copper Dissolution",
                    description = "SAC305 assay shows Cu at 0.72% (well within 0.5-0.9% spec limit).",
                    footerLeft = "ICP-OES Test Cleared",
                    footerRight = "Sample #490"
                ),
                IshikawaHypothesis(
                    id = "HYPOTHESIS M-03",
                    statusBadge = "08% RULED OUT",
                    isProbable = false,
                    title = "Conveyor Wave Peel Velocity Variation",
                    description = "Encoder logs confirm conveyor speed steady at 1.15 m/min ±0.01.",
                    footerLeft = "Telemetry In-Spec",
                    footerRight = "PLC S7-1500"
                )
            )
        ),
        IshikawaCategory(
            name = "Method",
            probabilityBadge = "34%",
            hypotheses = listOf(
                IshikawaHypothesis(
                    id = "HYPOTHESIS MT-01",
                    statusBadge = "34% CONTRIBUTING",
                    isProbable = true,
                    title = "PM-SMT-04 Visual-Only Spring Check",
                    description = "Maintenance schedule lacked gram-force tension load cell check at 15,000 cycles.",
                    footerLeft = "Linked to W4/W5",
                    footerRight = "Doc: PM-SMT-04"
                ),
                IshikawaHypothesis(
                    id = "HYPOTHESIS MT-02",
                    statusBadge = "05% RULED OUT",
                    isProbable = false,
                    title = "Pre-Heater Ramp Slope Deviation",
                    description = "Thermocouple profiling confirmed 1.8°C/s ramp rate across all 3 zones.",
                    footerLeft = "Profile Verified",
                    footerRight = "KIC-2000"
                )
            )
        ),
        IshikawaCategory(
            name = "Material",
            probabilityBadge = "12%",
            hypotheses = listOf(
                IshikawaHypothesis(
                    id = "HYPOTHESIS MA-01",
                    statusBadge = "12% RULED OUT",
                    isProbable = false,
                    title = "Flux Specific Gravity Drift (ORM0)",
                    description = "Inline hydrometer logged 0.812 g/cm³ within ±0.005 spec window.",
                    footerLeft = "Assay Passed",
                    footerRight = "Lot #FLX-88"
                )
            )
        ),
        IshikawaCategory(
            name = "Measurement",
            probabilityBadge = "62%",
            isHighConfidence = true,
            hypotheses = listOf(
                IshikawaHypothesis(
                    id = "HYPOTHESIS MS-01",
                    statusBadge = "62% ESCAPE CAUSE",
                    isProbable = true,
                    title = "2D AOI Grayscale Threshold Throttled to 65%",
                    description = "Lacked 3D structured light profilometry to separate bridge meniscus from gold glare.",
                    footerLeft = "Track B Verified",
                    footerRight = "ECO-2024-11"
                )
            )
        ),
        IshikawaCategory(
            name = "Man",
            probabilityBadge = "08%",
            hypotheses = listOf(
                IshikawaHypothesis(
                    id = "HYPOTHESIS MN-01",
                    statusBadge = "08% RULED OUT",
                    isProbable = false,
                    title = "Operator Board Loading Orientation",
                    description = "Asymmetric poka-yoke locating pins prevent reverse PCB placement.",
                    footerLeft = "Poka-Yoke Active",
                    footerRight = "Station 10"
                )
            )
        ),
        IshikawaCategory(
            name = "Environment",
            probabilityBadge = "04%",
            hypotheses = listOf(
                IshikawaHypothesis(
                    id = "HYPOTHESIS EN-01",
                    statusBadge = "04% RULED OUT",
                    isProbable = false,
                    title = "Cleanroom Humidity & ESD Ambient Shift",
                    description = "BMS telemetry recorded 44% RH ±2% and 21.5°C throughout Shift 2.",
                    footerLeft = "BMS Log Verified",
                    footerRight = "Zone C-4"
                )
            )
        )
    )

    val quarantineLots = listOf(
        QuarantineLot(
            lotId = "LOT-2025-05-9921",
            locationAndCustomer = "Bay C4 • ECU Pins • Stellantis",
            pieces = "2,450 pcs",
            tagStatus = "RED TAG HOLD",
            isRedTag = true,
            isGreenTag = false
        ),
        QuarantineLot(
            lotId = "LOT-2025-05-9844",
            locationAndCustomer = "WIP Buffer 4 • Inverter Seals",
            pieces = "1,400 pcs",
            tagStatus = "SORTING IN-PROGRESS",
            isRedTag = false,
            isGreenTag = false
        ),
        QuarantineLot(
            lotId = "LOT-2025-05-9762",
            locationAndCustomer = "Customer Dock Hub • Ford EV",
            pieces = "3,120 pcs",
            tagStatus = "GREEN TAG PASS",
            isRedTag = false,
            isGreenTag = true
        )
    )

    fun navigateTo(screen: AppScreen) {
        if (_currentScreen.value != screen) {
            _screenHistory.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        return if (_screenHistory.isNotEmpty()) {
            _currentScreen.value = _screenHistory.removeAt(_screenHistory.lastIndex)
            true
        } else if (_currentScreen.value != AppScreen.DASHBOARD) {
            _currentScreen.value = AppScreen.DASHBOARD
            true
        } else {
            false
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setLineFilter(line: String) {
        _selectedLine.value = line
    }

    fun setCustomerFilter(customer: String) {
        _selectedCustomer.value = customer
    }

    fun setSeverityFilter(severity: String) {
        _selectedSeverity.value = severity
    }

    fun setDisciplineFilter(discipline: String) {
        _selectedDiscipline.value = discipline
    }

    fun toggleAgingFilter() {
        _agingFilterActive.value = !_agingFilterActive.value
    }

    fun clearAllFilters() {
        _selectedLine.value = "All Lines (Plant #4)"
        _selectedCustomer.value = "All OEM Accounts"
        _selectedSeverity.value = "All Severities"
        _selectedDiscipline.value = "Any Phase (D1-D8)"
        _agingFilterActive.value = false
        _searchQuery.value = ""
        showToast("All industrial matrix filters reset.")
    }

    fun selectLedgerRow(ticketId: String) {
        _selectedLedgerRowId.value = ticketId
    }

    fun setPage(page: Int) {
        _currentPage.value = page.coerceIn(1, 3)
    }

    fun setShowNewCaseDialog(show: Boolean) {
        _showNewCaseDialog.value = show
    }

    fun createNew8DCase(partNumber: String, partDesc: String, customer: String, line: String, severity: CaseSeverity) {
        val newId = "8D-20250519-04${_ledgerCases.value.size + 2}"
        val newCase = LedgerCase(
            ticketId = newId,
            loggedTime = "Logged: Just Now",
            partNumber = partNumber.ifBlank { "PN-4410-C" },
            partDescription = partDesc.ifBlank { "High-Voltage Busbar Insulator Sleeve" },
            customer = customer,
            workcell = line,
            lineFilterKey = line,
            severity = severity,
            completedStages = 1,
            activeStage = 2,
            activeStageError = false,
            phaseStatusNote = "5W2H Problem Spec Initiated",
            slaStatusText = "D3: 23h 59m remaining",
            slaIsUrgent = severity == CaseSeverity.CRITICAL,
            containmentTitle = "Dock Audit Staged",
            containmentSub = "0 / 1,200 pcs sorted",
            containmentIsAlert = severity == CaseSeverity.CRITICAL,
            leadName = "E. Vance",
            leadRole = "VP Quality",
            leadInitials = "EV",
            agingDays = 0,
            targetScreen = AppScreen.D4_ROOT_CAUSE
        )
        _ledgerCases.value = listOf(newCase) + _ledgerCases.value
        _selectedLedgerRowId.value = newId
        _showNewCaseDialog.value = false
        showToast("Initiated New 8D Case $newId • D1 CFT Assigned")
    }

    fun toggleEmergencyQuarantineHold() {
        _emergencyHoldActive.value = !_emergencyHoldActive.value
        if (_emergencyHoldActive.value) {
            showToast("EMERGENCY QUARANTINE HOLD ENFORCED: Dock doors & MES conveyors locked.")
        } else {
            showToast("Emergency Quarantine Hold released to standard D3 protocol.")
        }
    }

    fun select6MCategory(category: String) {
        _selected6MCategory.value = category
    }

    fun selectOccurrenceOption(option: String) {
        _selectedOccurrenceOption.value = option
    }

    fun completeBatch3Validation() {
        _batch3Completed.value = true
        showToast("Batch 3 (Lot #2505-D) 1,400/1,400 Verified 0 Defects! Containment Exit Gate UNLOCKED.")
    }

    fun lockD7AndProceed() {
        _d7Locked.value = true
        showToast("D7 Phase Formally Locked. Dossier bundled for D8 Executive Closure.")
        navigateTo(AppScreen.D8_CLOSURE)
    }

    fun executeOemCounterSign() {
        if (!_oemCounterSigned.value) {
            _oemCounterSigned.value = true
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss 'UTC'", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            _oemSignTimestamp.value = sdf.format(Date())
            showToast("OEM Seal Cryptographically Verified (SHA-256: 77a1fc...9902bd)")
        }
    }

    fun sendExecutiveMemo() {
        _memoSent.value = true
        showToast("Memo Sent & Logged (Receipt #8842-EM-29)")
    }

    fun executeFormalCaseClosure() {
        _caseFormallyClosed.value = true
        showToast("Case 8D-20250514-039 Formally Closed & Archived in Enterprise ERP.")
    }

    fun toggleWatchlist() {
        _isBookmarked.value = !_isBookmarked.value
        if (_isBookmarked.value) {
            showToast("Added 8D-20250514-039 to Plant #4 Engineering Watchlist.")
        } else {
            showToast("Removed from Watchlist.")
        }
    }

    fun showToast(message: String) {
        _toastMessage.value = message
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
