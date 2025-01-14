package org.rhasspy.mobile.android

import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.permission.PermissionRequester
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import org.rhasspy.mobile.viewmodel.configuration.IConfigurationViewModel


fun SemanticsNodeInteraction.onSwitch(): SemanticsNodeInteraction {
    return this.onChildren().filter(isToggleable()).onFirst()
}

fun hasTestTag(testTag: Enum<*>): SemanticsMatcher =
    SemanticsMatcher.expectValue(SemanticsProperties.TestTag, testTag.name)

fun hasCombinedTestTag(tag1: Enum<*>, tag2: Enum<*>): SemanticsMatcher =
    SemanticsMatcher.expectValue(SemanticsProperties.TestTag, "${tag1.name}${tag2.name}")

fun SemanticsNodeInteractionsProvider.onNodeWithTag(
    testTag: Enum<*>,
    useUnmergedTree: Boolean = false
): SemanticsNodeInteraction = onNode(hasTestTag(testTag.name), useUnmergedTree)

fun SemanticsNodeInteractionsProvider.onNodeWithTag(
    name: String,
    useUnmergedTree: Boolean = false
): SemanticsNodeInteraction = onNode(hasTestTag(name), useUnmergedTree)

fun SemanticsNodeInteractionsProvider.onNodeWithCombinedTag(
    tag1: TestTag, tag2: TestTag,
    useUnmergedTree: Boolean = false
): SemanticsNodeInteraction = onNode(hasTestTag("${tag1.name}${tag2.name}"), useUnmergedTree)


fun SemanticsNodeInteractionsProvider.onNodeWithCombinedTag(
    testTag: Enum<*>, tag: TestTag,
    useUnmergedTree: Boolean = false
): SemanticsNodeInteraction = onNode(hasTestTag("${testTag.name}${tag.name}"), useUnmergedTree)

fun SemanticsNodeInteractionsProvider.onNodeWithCombinedTag(
    name: String, tag: TestTag,
    useUnmergedTree: Boolean = false
): SemanticsNodeInteraction = onNode(hasTestTag("$name${tag.name}"), useUnmergedTree)

fun UiSelector.text(text: StringResource): UiSelector {
    return this.textMatches(
        StringDesc.Resource(text).toString(
            getInstrumentation()
                .targetContext.applicationContext
        )
    )
}


//https://github.com/SergKhram/allure-kotlin/blob/081c7d39ee440b82ca490ce91d34ce1a1421670c/allure-kotlin-android/src/main/kotlin/io/qameta/allure/android/internal/TestUtils.kt
fun requestExternalStoragePermissions(device: UiDevice) {
    when {
        Build.VERSION.SDK_INT > Build.VERSION_CODES.M && Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q -> {
            with(PermissionRequester()) {
                addPermissions("android.permission.WRITE_EXTERNAL_STORAGE")
                addPermissions("android.permission.READ_EXTERNAL_STORAGE")
                requestPermissions()
            }
        }

        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
            device.executeShellCommand("appops set --uid ${getInstrumentation().targetContext.packageName} MANAGE_EXTERNAL_STORAGE allow")
        }

        else -> return
    }
    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).listFiles()?.forEach {
        it.deleteRecursively()
    }
}

fun requestMicrophonePermissions() {
    with(PermissionRequester()) {
        addPermissions("android.permission.RECORD_AUDIO")
        requestPermissions()
    }
}

fun UiDevice.requestOverlayPermissions(context: Context) {
    try {
        with(PermissionRequester()) {
            try {
                addPermissions("android.permission.SYSTEM_ALERT_WINDOW")
                requestPermissions()
            } catch (e: Exception) {
                requestOverlayPermissionLegacy(context)
            }
        }
    } catch (e: Exception) {
        requestOverlayPermissionLegacy(context)
    }
    if (!Settings.canDrawOverlays(context)) {
        //will be called on android M (23)
        requestOverlayPermissionLegacy(context)
    }
}


fun SemanticsNodeInteraction.assertTextEquals(
    text: StringResource,
    includeEditableText: Boolean = true
): SemanticsNodeInteraction =
    this.assertTextEquals(
        values = arrayOf(
            StringDesc.Resource(text).toString(
                getInstrumentation()
                    .targetContext.applicationContext
            )
        ),
        includeEditableText = includeEditableText
    )

fun SemanticsNodeInteractionsProvider.onNodeWithText(
    text: StringResource,
    substring: Boolean = false,
    ignoreCase: Boolean = false,
    useUnmergedTree: Boolean = false
): SemanticsNodeInteraction = onNode(
    hasText(
        StringDesc.Resource(text).toString(
            getInstrumentation()
                .targetContext.applicationContext
        ), substring, ignoreCase
    ), useUnmergedTree
)

fun ComposeContentTestRule.awaitSaved(viewModel: IConfigurationViewModel) {
    this.waitUntil(
        condition = { !viewModel.isLoading.value },
        timeoutMillis = 5000
    )
}


fun ComposeContentTestRule.waitUntilExists(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = 1_000L
) {
    return this.waitUntilNodeCount(matcher, 1, timeoutMillis)
}

fun ComposeContentTestRule.waitUntilDoesNotExist(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = 1_000L
) {
    return this.waitUntilNodeCount(matcher, 0, timeoutMillis)
}

fun ComposeContentTestRule.waitUntilNodeCount(
    matcher: SemanticsMatcher,
    count: Int,
    timeoutMillis: Long = 1_000L
) {
    this.waitUntil(timeoutMillis) {
        this.onAllNodes(matcher).fetchSemanticsNodes().size == count
    }
}