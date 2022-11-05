package org.rhasspy.mobile.android.settings.content

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.rhasspy.mobile.android.TestTag
import org.rhasspy.mobile.android.main.LocalMainNavController
import org.rhasspy.mobile.android.onNodeWithTag
import org.rhasspy.mobile.data.LanguageOptions
import org.rhasspy.mobile.viewModels.settings.LanguageSettingsViewModel
import kotlin.test.assertEquals

class LanguageSettingsContentTest {

    @get: Rule
    val composeTestRule = createComposeRule()

    private val viewModel = LanguageSettingsViewModel()

    @Before
    fun setUp() {

        composeTestRule.setContent {
            val navController = rememberNavController()

            CompositionLocalProvider(
                LocalMainNavController provides navController
            ) {
                LanguageSettingsScreenItemContent(viewModel)
            }
        }

    }

    /**
     * language is english
     * english is selected
     * title is "Language"
     * StringDesc is English
     * AppViewModel languageOption is English
     *
     * User clicks german
     * language is german
     * german is selected
     * title is "Sprache"
     * StringDesc is German
     * AppViewModel languageOption is German
     * language german is saved
     *
     * User clicks english
     * language is english
     * english is selected
     * title is "Sprache"
     * StringDesc is English
     * AppViewModel languageOption is English
     * language english is saved
     */
    @Test
    fun testLanguage() = runBlocking {
        viewModel.selectLanguageOption(LanguageOptions.English)

        //language is english
        assertEquals(LanguageOptions.English, viewModel.languageOption.value)
        //english is selected
        composeTestRule.onNodeWithTag(LanguageOptions.English, true).onChildAt(0).assertIsSelected()
        //title is "Language"
        composeTestRule.onNodeWithTag(TestTag.AppBarTitle).assertTextEquals("Language")
        //StringDesc is English
        assertEquals(LanguageOptions.English.code, StringDesc.localeType.systemLocale!!.language)

        //User clicks german
        composeTestRule.onNodeWithTag(LanguageOptions.German).performClick()
        //language is german
        assertEquals(LanguageOptions.German, viewModel.languageOption.value)
        //german is selected
        composeTestRule.onNodeWithTag(LanguageOptions.German, true).onChildAt(0).assertIsSelected()
        //title is "Sprache"
        composeTestRule.onNodeWithTag(TestTag.AppBarTitle).assertTextEquals("Sprache")
        //StringDesc is German
        assertEquals(LanguageOptions.German.code, StringDesc.localeType.systemLocale!!.language)
        //AppViewModel languageOption is German
        assertEquals(LanguageOptions.German, viewModel.languageOption.value)
        //language german is saved
        var newViewModel = LanguageSettingsViewModel()
        assertEquals(LanguageOptions.German, newViewModel.languageOption.value)

        //User clicks english
        composeTestRule.onNodeWithTag(LanguageOptions.English).performClick()
        //language is english
        assertEquals(LanguageOptions.English, viewModel.languageOption.value)
        //english is selected
        composeTestRule.onNodeWithTag(LanguageOptions.English, true).onChildAt(0).assertIsSelected()
        //title is "Language"
        composeTestRule.onNodeWithTag(TestTag.AppBarTitle).assertTextEquals("Language")
        //StringDesc is English
        assertEquals(LanguageOptions.English.code, StringDesc.localeType.systemLocale!!.language)
        //AppViewModel languageOption is English
        assertEquals(LanguageOptions.English, viewModel.languageOption.value)
        //language english is saved
        newViewModel = LanguageSettingsViewModel()
        assertEquals(LanguageOptions.English, newViewModel.languageOption.value)
    }

}