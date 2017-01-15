/*
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 * THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.alexzh.simplecoffeeorder.test;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.view.View;
import android.widget.EditText;

import com.alexzh.simplecoffeeorder.R;
import com.alexzh.simplecoffeeorder.ServiceIdlingResource;
import com.alexzh.simplecoffeeorder.view.activity.CoffeeOrderListActivity;

import org.hamcrest.Description;
import org.junit.Rule;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.alexzh.simplecoffeeorder.actions.RecyclerChildViewActions.checkTextViewCountForCoffee;
import static com.alexzh.simplecoffeeorder.actions.RecyclerChildViewActions.clickToViewChildItem;
import static com.alexzh.simplecoffeeorder.utils.StringUtils.getString;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.assertEquals;

@CucumberOptions(features = "features")

@RunWith(AndroidJUnit4.class)
public class LoginActivitySteps  {
    private UiDevice mDevice;
    private ServiceIdlingResource mServiceIdlingResource;

    //Test statics
    private final String espresso = "Espresso";
    private final String espressoCount = "3";
    private final float totalCoffeePrice = 15.0f;
    private final float coffeePrice = 5.0f;
    private final String notificationTitle = "Coffee order app";
    private final String notificationText = "Thank you for your payment.";

    @Rule
    public ActivityTestRule<CoffeeOrderListActivity> mActivityRule = new ActivityTestRule<>(CoffeeOrderListActivity.class,false,false);

    @cucumber.api.java.Before
    public void setup() {
        Intent testIntent = new Intent();
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mActivityRule.launchActivity(testIntent);
        mServiceIdlingResource =  new ServiceIdlingResource(mActivityRule.getActivity().getApplicationContext());
        registerIdlingResources(mServiceIdlingResource);
    }

    private void checkCoffeeListViewItem(String name, String count, float coffeePrice, float totalCoffeePrice) {
        onData(anything()).atPosition(0).onChildView(withId(com.alexzh.simplecoffeeorder.R.id.coffee_name))
                .check(matches(withText(name)));
        onData(anything()).atPosition(0).onChildView(withId(com.alexzh.simplecoffeeorder.R.id.coffee_count))
                .check(matches(withText(count)));
        onData(anything()).atPosition(0).onChildView(withId(com.alexzh.simplecoffeeorder.R.id.coffee_price))
                .check(matches(withText(getString(mActivityRule, com.alexzh.simplecoffeeorder.R.string.price, coffeePrice))));
        onData(anything()).atPosition(0).onChildView(withId(com.alexzh.simplecoffeeorder.R.id.total_price))
                .check(matches(withText(getString(mActivityRule, R.string.price, totalCoffeePrice))));
    }

    @Given("^I have a CoffeeOrderActivity$")
    public void iHaveACoffeeOrderActivity() throws Throwable {

    }

    @When("^I add '(\\d+)' Espressos$")
    public void iAddEspressos(int arg0) throws Throwable {
        clickToViewChildItem(com.alexzh.simplecoffeeorder.R.id.recyclerView, espresso, com.alexzh.simplecoffeeorder.R.id.coffee_increment);
        clickToViewChildItem(com.alexzh.simplecoffeeorder.R.id.recyclerView, espresso, com.alexzh.simplecoffeeorder.R.id.coffee_increment);
        clickToViewChildItem(com.alexzh.simplecoffeeorder.R.id.recyclerView, espresso, com.alexzh.simplecoffeeorder.R.id.coffee_increment);

        checkTextViewCountForCoffee(com.alexzh.simplecoffeeorder.R.id.recyclerView,
                com.alexzh.simplecoffeeorder.R.id.coffee_count,
                espresso,
                String.valueOf(espressoCount));

        onView(withId(com.alexzh.simplecoffeeorder.R.id.total_price_toolbar))
                .check(matches(withText(getString(mActivityRule,
                        com.alexzh.simplecoffeeorder.R.string.price,
                        totalCoffeePrice))));

    }

    @Then("^I pay for the order$")
    public void iPayForTheOrder() throws Throwable {
        onView(withId(com.alexzh.simplecoffeeorder.R.id.pay)).perform(click());

        onView(withId(com.alexzh.simplecoffeeorder.R.id.delivery_info))
                .perform(typeText("User"), closeSoftKeyboard());

        onView(withId(com.alexzh.simplecoffeeorder.R.id.delivery_info))
                .check(matches(withText("User")));

        //Verify order
        checkCoffeeListViewItem(espresso, espressoCount, coffeePrice, totalCoffeePrice);

        //Pay for order
        onView(withId(com.alexzh.simplecoffeeorder.R.id.pay)).perform(click());

    }

    @Then("^I should receive a notification that an order has been placed$")
    public void iShouldReceiveANotificationThatAnOrderHasBeenPlaced() throws Throwable {
        mDevice.openNotification();
        mDevice.wait(Until.hasObject(By.text(notificationTitle)), 3000);
        UiObject2 title = mDevice.findObject(By.text(notificationTitle));
        UiObject2 text = mDevice.findObject(By.text(notificationText));
        assertEquals(notificationTitle, title.getText());
        assertEquals(notificationText, text.getText());
        title.click();

        checkCoffeeListViewItem(espresso, espressoCount, coffeePrice, totalCoffeePrice);
    }


    /**
     * Custom matcher to assert equal EditText.setError();
     */
    private static class ErrorTextMatcher extends TypeSafeMatcher<View> {

        private final String mExpectedError;

        private ErrorTextMatcher(String expectedError) {
            mExpectedError = expectedError;
        }

        @Override
        public boolean matchesSafely(View view) {
            if(!(view instanceof EditText)) {
                return false;
            }

            EditText editText = (EditText) view;

            return mExpectedError.equals(editText.getError());
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("with error: " + mExpectedError);
        }
    }
}
