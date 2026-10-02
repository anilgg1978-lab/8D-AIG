package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppScreen
import com.example.ui.theme.QResolveColors
import com.example.ui.theme.QResolveType
import com.example.viewmodel.QResolveViewModel

@Composable
fun ExecutiveDossierScreen(
    viewModel: QResolveViewModel,
    isExpandedScreen: Boolean
) {
    val oemCounterSigned by viewModel.oemCounterSigned.collectAsState()
    val oemSignTimestamp by viewModel.oemSignTimestamp.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Top Action Control Strip
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { viewModel.navigateTo(AppScreen.D8_CLOSURE) },
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .heightIn(min = 48.dp)
                    .testTag("dossier_back_to_d8_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = QResolveColors.OnSurface,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Back to D8 Workspace",
                    style = QResolveType.HeadlineSm.copy(fontSize = 12.sp),
                    color = QResolveColors.OnSurface
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = {
                        viewModel.showToast("Cryptographic XML Package (AIAG-VDA Schema v2.4) exported.")
                    },
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .heightIn(min = 48.dp)
                        .testTag("export_xml_schema_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Code,
                        contentDescription = null,
                        tint = QResolveColors.Primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Export AIAG XML",
                        style = QResolveType.HeadlineSm.copy(fontSize = 12.sp),
                        color = QResolveColors.Primary
                    )
                }

                Button(
                    onClick = {
                        viewModel.showToast("Signed Executive 8D Final Report PDF queued for print/download.")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = QResolveColors.PrimaryContainer),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .heightIn(min = 48.dp)
                        .testTag("download_signed_pdf_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Print,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Print / Download Signed PDF",
                        style = QResolveType.HeadlineSm.copy(fontSize = 12.sp),
                        color = Color.White
                    )
                }
            }
        }

        // Formal AIAG / VDA Harmonized Report Sheet
        Surface(
            color = QResolveColors.SurfaceContainerLowest,
            shape = RoundedCornerShape(6.dp),
            shadowElevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, QResolveColors.OutlineVariant, RoundedCornerShape(6.dp))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Report Title Banner
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "AIAG / VDA HARMONIZED EXECUTIVE 8D FINAL REPORT",
                            style = QResolveType.LabelCaps.copy(letterSpacing = 1.sp),
                            color = QResolveColors.Primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "CASE DOSSIER: 8D-20250514-039",
                            style = QResolveType.DisplaySm,
                            color = QResolveColors.OnSurface
                        )
                        Text(
                            text = "Part: PN-8842-B (Rev D) • ECU Bus Connector Header (12-Pin) • Customer: Stellantis North America",
                            style = QResolveType.BodySm,
                            color = QResolveColors.Secondary
                        )
                    }

                    Surface(
                        color = QResolveColors.SuccessBg,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "AUDIT STATUS: CLOSED",
                                style = QResolveType.CodeBadge,
                                color = QResolveColors.Tertiary
                            )
                            Text(
                                text = "IATF 16949 §10.2.3",
                                style = QResolveType.CodeBadge.copy(fontSize = 9.sp),
                                color = QResolveColors.Secondary
                            )
                        }
                    }
                }

                HorizontalDivider(color = QResolveColors.OutlineVariant)

                // D1 - D8 Complete Structured Ledger Table
                val sections = listOf(
                    DossierSection(
                        discipline = "D1 • TEAM ESTABLISHMENT",
                        owner = "M. Reyes (QA Lead) • Dr. A. Chen (Metallurgy) • T. Kowalski (Mfg Eng) • D. Scott (SQE)",
                        summary = "Cross-functional 8D response team chartered within 45 minutes of Stellantis Toledo Plant line-stop notification (#CUST-STL-992)."
                    ),
                    DossierSection(
                        discipline = "D2 • 5W2H PROBLEM DESCRIPTION",
                        owner = "IS / IS-NOT Boundary Validated • 14 Confirmed NG Units (5,714 PPM)",
                        summary = "Solder bridge between Pin 14 (CAN_H) and Pin 15 (GND) on J3 Header during selective wave soldering on Line 4 Station 30 (Carrier #8 only)."
                    ),
                    DossierSection(
                        discipline = "D3 • INTERIM CONTAINMENT ACTIONS (ICA)",
                        owner = "100% Yard & WIP Quarantine • 6,970 Total Units Certified",
                        summary = "LOT-2025-05-9921 (2,450 pcs) quarantined; Carrier #8 removed from circulation; 200% X-Ray + Pin-to-Pin continuity firewall instituted."
                    ),
                    DossierSection(
                        discipline = "D4 • ROOT CAUSE ANALYSIS (OCCURRENCE & ESCAPE)",
                        owner = "SEM-EDS #409-B • Dual-Track 5-Why Verified via Recreate-01",
                        summary = "Occurrence: 304-SS clamp spring thermal relaxation allowed 0.28mm PCB lift. Escape: 2D AOI lacked Z-height laser triangulation and threshold was throttled to 65%."
                    ),
                    DossierSection(
                        discipline = "D5 • PERMANENT CORRECTIVE ACTIONS (PCA)",
                        owner = "ECO-2025-0892 Approved • CAPEX $42,500",
                        summary = "PCA-01: Upgraded all 24 wave carriers to Inconel X-750 springs with Kistler piezo force interlocks (<18.5N halt). PCA-02: Installed 3D Dual-Laser Profilometry at Station 45."
                    ),
                    DossierSection(
                        discipline = "D6 • PCA VALIDATION & CONTAINMENT EXIT",
                        owner = "Cpk Improved 0.55 → 1.84 • Gage R&R 4.8%",
                        summary = "Validated across 3 consecutive production lots (#2505-B, #2505-C, #2505-D) totaling 4,220 units with 0 solder bridges."
                    ),
                    DossierSection(
                        discipline = "D7 • PREVENT RECURRENCE & SYSTEMIC STANDARDIZATION",
                        owner = "PFMEA-SMT-04 Rev 14.2 (RPN 336 → 32) • CP-8842-B Rev 09",
                        summary = "Updated SOP-WSC-014 & WI-MAINT-088 with RFID cycle lock at 10,000 passes. Yokoten deployed across Detroit, Stuttgart, Nagoya, and Kokomo plants."
                    ),
                    DossierSection(
                        discipline = "D8 • FORMAL CLOSURE & CRYPTOGRAPHIC SIGN-OFF",
                        owner = if (oemCounterSigned) "Counter-Signed by Dr. Aris Thorne ($oemSignTimestamp)" else "Internal QA & Plant Ops Signed • OEM Counter-Sign Ready",
                        summary = "Zero customer warranty returns post-containment. Financial abatement verified at $418,500 net savings."
                    )
                )

                sections.forEach { sec ->
                    Surface(
                        color = QResolveColors.SurfaceContainerLow,
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = sec.discipline,
                                    style = QResolveType.CodeBadge,
                                    color = QResolveColors.Primary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = sec.owner,
                                    style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                                    color = QResolveColors.Secondary
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = sec.summary,
                                style = QResolveType.BodySm,
                                color = QResolveColors.OnSurface
                            )
                        }
                    }
                }

                // Cryptographic Seal Footer
                Surface(
                    color = QResolveColors.InverseSurface,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "CRYPTOGRAPHIC DOSSIER HASH (SHA-256)",
                                style = QResolveType.CodeBadge.copy(fontSize = 9.sp),
                                color = Color(0xFF93C5FD)
                            )
                            Text(
                                text = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
                                style = QResolveType.CodeBadge.copy(fontSize = 10.sp),
                                color = Color.White
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = "Verified Seal",
                            tint = Color(0xFF6EE7B7),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

private data class DossierSection(
    val discipline: String,
    val owner: String,
    val summary: String
)
