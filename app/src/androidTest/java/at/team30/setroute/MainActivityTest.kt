package at.team30.setroute

import android.Manifest
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import at.team30.setroute.infrastructure.DependencyInjection
import at.team30.setroute.infrastructure.IRoutesRepository
import at.team30.setroute.ui.MainActivity
import at.team30.setroute.ui.settings.SettingsFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@UninstallModules(DependencyInjection::class)
@HiltAndroidTest
class MainActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule(order = 2)
    val runtimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION)

    @Inject
    lateinit var routesRepository: IRoutesRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun given_many_routes_in_repository_expect_all_routes_displayed_in_list() {
        // Assert
        for(route in routesRepository.getRoutes()) {
            onView(withText(route.name)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun given_many_routes_enter_detail_of_first_route() {
        // Arrange
        val selectedRoute = routesRepository.getRoutes()[0]

        // Act
        onView(withText(selectedRoute.name)).perform(click())

        // Assert
        onView(withText(selectedRoute.name)).check(matches(isDisplayed()))
        onView(withText(selectedRoute.description)).check(matches(isDisplayed()))
    }

    @Test
    fun given_many_routes_enter_detail_press_back_displays_list_view() {
        // Arrange
        val selectedRoute = routesRepository.getRoutes()[0]

        // Act
        onView(withText(selectedRoute.name)).perform(click())

        // Assert
        onView(withText(selectedRoute.name)).check(matches(isDisplayed()))
        onView(withText(selectedRoute.description)).check(matches(isDisplayed()))

        // Act
        onView(isRoot()).perform(pressBack())

        // Assert
        for(route in routesRepository.getRoutes()) {
            onView(withText(route.name)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun given_many_routes_enter_detail_press_routes_button_displays_list_view() {
        // Arrange
        val selectedRoute = routesRepository.getRoutes()[0]

        // Act
        onView(withText(selectedRoute.name)).perform(click())

        // Assert
        onView(withText(selectedRoute.name)).check(matches(isDisplayed()))
        onView(withText(selectedRoute.description)).check(matches(isDisplayed()))

        // Act
        onView(withId(R.id.routesFragment)).perform(click())

        // Assert
        for(route in routesRepository.getRoutes()) {
            onView(withText(route.name)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun starting_activity_navigating_to_setting_view() {
        //Arrange
        val context = InstrumentationRegistry.getInstrumentation().targetContext;

        //Act
        onView(withId(R.id.settingsFragment)).perform(click())

        //Assert
        onView(withText(context.resources.getString(R.string.select_a_language))).check(matches((isDisplayed())))
        onView(withText(context.resources.getString(R.string.switch_to_miles))).check(matches((isDisplayed())))
        onView(withText(context.resources.getString(R.string.switch_to_dark_mode))).check(matches((isDisplayed())))
    }

    @Test
    fun option_menu_opens() {
        //Arrange
        val context = InstrumentationRegistry.getInstrumentation().targetContext;

        //Act
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)

        //Assert
        onView(withText(context.resources.getString(R.string.sort))).check(matches((isDisplayed())))
        onView(withText(context.resources.getString(R.string.filter))).check(matches((isDisplayed())))
    }

    @Test
    fun sort_dialog_is_shown() {
        //Arrange
        val context = InstrumentationRegistry.getInstrumentation().targetContext;

        //Act
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(context.resources.getString(R.string.sort))).perform(click())

        //Assert
        onView(withText(context.resources.getString(R.string.sort_order))).check(matches((isDisplayed())))
    }

    @Test
    fun filter_dialog_is_shown() {
        //Arrange
        val context = InstrumentationRegistry.getInstrumentation().targetContext;

        //Act
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(context.resources.getString(R.string.filter))).perform(click())

        //Assert
        onView(withText(context.resources.getString(R.string.filter_options))).check(matches((isDisplayed())))
    }
}
