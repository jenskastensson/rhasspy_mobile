package org.rhasspy.mobile.android.settings.content

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.rhasspy.mobile.android.TestTag
import org.rhasspy.mobile.android.main.LocalMainNavController
import org.rhasspy.mobile.android.onNodeWithTag
import org.rhasspy.mobile.android.settings.content.sound.IndicationSettingsScreens
import org.rhasspy.mobile.android.testTag
import org.rhasspy.mobile.data.AudioOutputOptions
import org.rhasspy.mobile.viewModels.settings.IndicationSettingsViewModel
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class IndicationSettingsContentTest {

    @get: Rule
    val composeTestRule = createComposeRule()

    private val viewModel = IndicationSettingsViewModel()

    @Before
    fun setUp() {

        composeTestRule.setContent {
            Surface(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .testTag(TestTag.Background)
            ) {
                val navController = rememberNavController()

                CompositionLocalProvider(
                    LocalMainNavController provides navController
                ) {
                    WakeWordIndicationSettingsContent(viewModel)
                }
            }
        }

    }

    /**
     * wake up display disabled
     * visual disabled
     * sound disabled
     *
     * user clicks wake up display
     * wake up display is enabled
     * wake up display is saved
     *
     * user clicks visual
     * visual is enabled
     * visual is saved
     *
     * user clicks sound
     * sound is enabled
     * sound is saved
     */
    @Test
    fun testIndicationSettings() {
        viewModel.toggleWakeWordDetectionTurnOnDisplay(false)
        viewModel.toggleWakeWordLightIndicationEnabled(false)
        viewModel.toggleWakeWordSoundIndicationEnabled(false)

        //wake up display disabled
        composeTestRule.onNodeWithTag(TestTag.WakeWordDetectionTurnOnDisplay).assertIsOff()
        //visual disabled
        composeTestRule.onNodeWithTag(TestTag.WakeWordLightIndicationEnabled).assertIsOff()
        //sound disabled
        composeTestRule.onNodeWithTag(TestTag.SoundIndicationEnabled).assertIsOff()

        //user clicks wake up display
        composeTestRule.onNodeWithTag(TestTag.WakeWordDetectionTurnOnDisplay).performClick()
        //wake up display is enabled
        composeTestRule.onNodeWithTag(TestTag.WakeWordDetectionTurnOnDisplay).assertIsOn()
        //wake up display is saved
        assertTrue { IndicationSettingsViewModel().isWakeWordDetectionTurnOnDisplayEnabled.value }

        //user clicks visual
        composeTestRule.onNodeWithTag(TestTag.WakeWordLightIndicationEnabled).performClick()
        //visual is enabled
        composeTestRule.onNodeWithTag(TestTag.WakeWordLightIndicationEnabled).assertIsOn()
        //visual is saved
        assertTrue { IndicationSettingsViewModel().isWakeWordLightIndicationEnabled.value }

        //user clicks sound
        composeTestRule.onNodeWithTag(TestTag.SoundIndicationEnabled).performClick()
        //sound is enabled
        composeTestRule.onNodeWithTag(TestTag.SoundIndicationEnabled).assertIsOn()
        //sound is saved
        assertTrue { IndicationSettingsViewModel().isSoundIndicationEnabled.value }
    }

    /**
     * Sound is disabled
     * sound settings invisible
     *
     * user clicks sound
     * sound is enabled
     * sound settings visible
     *
     * sound output is notification
     * sound output notification is selected
     *
     * user clicks sound output sound
     * sound output sound is selected
     * sound output sound is saved
     *
     * user clicks wake word
     * wake word page is opened
     * user clicks back
     *
     * user clicks recorded
     * recorded page is opened
     * user clicks back
     *
     * user click error
     * error page is opened
     * user clicks back
     */
    @Test
    fun testSoundIndicationOptions() {
        //Sound is disabled
        viewModel.toggleWakeWordSoundIndicationEnabled(false)
        //sound settings invisible
        composeTestRule.onNodeWithTag(TestTag.AudioOutputOptions).assertDoesNotExist()
        composeTestRule.onNodeWithTag(IndicationSettingsScreens.WakeIndicationSound)
            .assertDoesNotExist()
        composeTestRule.onNodeWithTag(IndicationSettingsScreens.RecordedIndicationSound)
            .assertDoesNotExist()
        composeTestRule.onNodeWithTag(IndicationSettingsScreens.ErrorIndicationSound)
            .assertDoesNotExist()

        //user clicks sound
        composeTestRule.onNodeWithTag(TestTag.SoundIndicationEnabled).performClick()
        //sound is enabled
        composeTestRule.onNodeWithTag(TestTag.SoundIndicationEnabled).assertIsOn()
        //sound settings visible
        composeTestRule.onNodeWithTag(TestTag.AudioOutputOptions).assertIsDisplayed()
        composeTestRule.onNodeWithTag(IndicationSettingsScreens.WakeIndicationSound)
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag(IndicationSettingsScreens.RecordedIndicationSound)
            .assertIsDisplayed()
        composeTestRule.onNodeWithTag(IndicationSettingsScreens.ErrorIndicationSound)
            .assertIsDisplayed()

        //sound output is notification
        assertEquals(AudioOutputOptions.Notification, viewModel.soundIndicationOutputOption.value)
        //sound output notification is selected
        composeTestRule.onNodeWithTag(AudioOutputOptions.Notification, true).onChildAt(0)
            .assertIsSelected()

        //user clicks sound output sound
        composeTestRule.onNodeWithTag(AudioOutputOptions.Sound).performClick()
        //sound output sound is selected
        composeTestRule.onNodeWithTag(AudioOutputOptions.Sound, true).onChildAt(0)
            .assertIsSelected()
        //sound output sound is saved
        assertEquals(
            AudioOutputOptions.Sound,
            IndicationSettingsViewModel().soundIndicationOutputOption.value
        )

        //user clicks wake word
        composeTestRule.onNodeWithTag(IndicationSettingsScreens.WakeIndicationSound).performClick()
        //wake word page is opened
        composeTestRule.onNodeWithTag(IndicationSettingsScreens.WakeIndicationSound)
            .assertIsDisplayed()
        //user clicks back
        composeTestRule.onNodeWithTag(TestTag.AppBarBackButton).performClick()

        //user clicks recorded
        composeTestRule.onNodeWithTag(IndicationSettingsScreens.RecordedIndicationSound)
            .performClick()
        //recorded page is opened
        composeTestRule.onNodeWithTag(IndicationSettingsScreens.RecordedIndicationSound)
            .assertIsDisplayed()
        //user clicks back
        composeTestRule.onNodeWithTag(TestTag.AppBarBackButton).performClick()

        //user click error
        composeTestRule.onNodeWithTag(IndicationSettingsScreens.ErrorIndicationSound).performClick()
        //error page is opened
        composeTestRule.onNodeWithTag(IndicationSettingsScreens.ErrorIndicationSound)
            .assertIsDisplayed()
        //user clicks back
        composeTestRule.onNodeWithTag(TestTag.AppBarBackButton).performClick()
    }

}