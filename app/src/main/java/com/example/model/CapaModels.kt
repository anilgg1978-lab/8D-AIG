package com.example.model

enum class AppScreen(val title: String, val shortLabel: String, val badge: String) {
    DASHBOARD("8D Dashboard & Incident Control", "Dashboard", "LIVE"),
    D4_ROOT_CAUSE("Active Investigations / Root Cause Engine (D4)", "D4 Root Cause", "D4"),
    D5_D6_PCA("PCA & Validation (D5/D6)", "D5/D6 PCA", "D5/D6"),
    D7_STANDARDIZATION("Standardization & FMEA (D7)", "D7 PFMEA", "D7"),
    D8_CLOSURE("D8: Closure & Team Recognition", "D8 Closure", "D8"),
    LESSONS_LEARNED("Executive Audits & Lessons Learned", "Yokoten KB", "LL"),
    EXECUTIVE_DOSSIER("AIAG / VDA Harmonized Executive Dossier", "8D Dossier", "PDF")
}

object HotlinkedImages {
    const val LOGO_URL =
        "https://lh3.googleusercontent.com/aida/AEtjO1VXDitshzPoYAX--FNn24yuRtbD99ESzHEkv3FRNp4LInRKti881IwXFH14CaOYY2tTreBmQ0HUw9W6FruJmAppC6OUhPx5Ty8hRg_7Khfmo3XhCOxaV50OjvFDnWiWDfWoCRjPwC_51eD_SzVYRpDANHqu2SKnKNxBHVnT7etPvd-s424pwyCPLhTjRVOokbxO1QBQJLZDh1O2xnxSwhCze22opea1Eelksex6Vj6JCBgw38I3qX2G0xjF"

    const val ELENA_VANCE_AVATAR =
        "https://lh3.googleusercontent.com/aida-public/AB6AXuClhDkVL3JXR27Sd4ZRJCH63uUjabwaHYxKcP04ZJmxnTHbWubdDTZROEUYx4UGG-0gjvpooysDni42b2Y420axIKTATkmQ4l6BB5p682ywB4-M8OdO7q3lSz4XzHE8HjgtNYJ2Jw47v91E4-6EBN66e2QslekHwIkw-F_gWIjdpIb-xMUp2KTzsKzP3z8zGZIYpUllOBU7vSXNgLS_nf9kq1jGLhqcc5q42s0dBDQezHHARwciuayt_A"

    const val SEM_MICROGRAPH =
        "https://lh3.googleusercontent.com/aida-public/AB6AXuCIqvA2kwW5q26QDhxQ1OvA7_UEFhQm17mwDXLa7G429P1oBwlwDMn6AMSJkxYJohNXHvfM8FBhpHG3gkA0bmsxZRyZVTepobc-Fgiuz6bpOfx-Z-pqoHi-OuokTZsyCwUqsp5Vau8sFy17uKBePb9NBqcxkAwPyrFeax_KHD4NUlqTEU1EKoJuMRsye-QbOt4CgUgXyuLHPLYLzO9FksUeir1QAVBKWqUVGo24UR-IiDD4Rf_hEnqlTg"

    const val SOP_WSC_014_PHOTO =
        "https://lh3.googleusercontent.com/aida-public/AB6AXuDFEwHYLjZI_ZhccWlF6oF63esXtI8n0c2tjtnlw2JxpLdixyZcs2WYdgqY9XtPYt8rZK8iPOWAzWFmFJl6gGcE_Ym3mxSrx44FpR9VQyAEKoRZyTA27yDYZTdYJtPhlNenNbEZCT_qRnrM-kWx65hgxQ_axo7HWRd_rHwgdE0Wj-rNlbhaCGtcDMfGOp1lVrwWmaMqolkyhnv2_vgH6ljwXpey9Ip-3meUqyrQ1PCuURHDCzolmJ8QOQ"

    const val WI_MAINT_088_PHOTO =
        "https://lh3.googleusercontent.com/aida-public/AB6AXuC38OeXSaKabEypcezZ0yg2WdiwTqDOG8Ly7VVsvfO-ms0sRWLbMREn65iWdVjRlGlyOU1O1vUhj1qFEtarkUuvC3cTNnJ7eDj8HzKJ5bKPE2LfqQGGQvXD2iazvf3htGd2yc5DpXYvqXshbOGMayEasgZ0XCvDSo3UMdyx-prNyJNLRULlEVt_mT3jAAtIUmu16A87xhScg0HE_mq8QVGbOFloi_KnGR_9g2mrSCZxZNmc1w8FMH34pA"

    const val ELENA_ROSTOVA_AVATAR =
        "https://lh3.googleusercontent.com/aida/AEtjO1UgKzUCzpBmBBxPjpXSW9z1WLnF-ZBRyDLQb4UlZQnRv0PY6F2FJJ4oigZyBiFD_i0reJm2g1WkaycGkibEC6oXv2F-l3IuUl6AHMe_po9fK0t3Lp868nesivrhp3rbikoPh-V99JyQ9U0s1sKpt5EZvfbZLetjyktMXWNJqsOgEntdSadcqnCINb8R_EQzxKtewgdxu4HxHEvKBSiM3S5LWWB30gTAw2_dr1qjfjG-hd8fl4kjl6xbBksn"
}

enum class CaseSeverity(val label: String) {
    CRITICAL("CRITICAL"),
    MAJOR("MAJOR"),
    MINOR("MINOR")
}

data class LedgerCase(
    val ticketId: String,
    val loggedTime: String,
    val partNumber: String,
    val partDescription: String,
    val customer: String,
    val workcell: String,
    val lineFilterKey: String,
    val severity: CaseSeverity,
    val completedStages: Int, // 1..8
    val activeStage: Int,     // 1..8
    val activeStageError: Boolean = false,
    val phaseStatusNote: String,
    val slaStatusText: String,
    val slaIsUrgent: Boolean,
    val slaIsGood: Boolean = false,
    val containmentTitle: String,
    val containmentSub: String,
    val containmentIsAlert: Boolean = false,
    val leadName: String,
    val leadRole: String,
    val leadInitials: String,
    val agingDays: Int,
    val targetScreen: AppScreen
)

data class IshikawaCategory(
    val name: String,
    val probabilityBadge: String,
    val isHighConfidence: Boolean = false,
    val hypotheses: List<IshikawaHypothesis>
)

data class IshikawaHypothesis(
    val id: String,
    val statusBadge: String,
    val isProbable: Boolean,
    val title: String,
    val description: String,
    val footerLeft: String,
    val footerRight: String
)

data class WhyStep(
    val stepCode: String,
    val statement: String,
    val evidence: String,
    val isRootCause: Boolean = false,
    val rootBadge: String = ""
)

data class QuarantineLot(
    val lotId: String,
    val locationAndCustomer: String,
    val pieces: String,
    val tagStatus: String,
    val isRedTag: Boolean,
    val isGreenTag: Boolean
)
