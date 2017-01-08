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

import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;

import com.alexzh.simplecoffeeorder.R;
import com.alexzh.simplecoffeeorder.view.activity.CoffeeOrderListActivity;

import org.hamcrest.Description;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@CucumberOptions(features = "features")
//public class LoginActivitySteps {
//
//    @Rule
//    public ActivityTestRule<CoffeeOrderListActivity> testRule = new ActivityTestRule<>(CoffeeOrderListActivity.class);

//TODO: replace with : @RunWith

//Not needed for current implementation but may be needed for tagging
@RunWith(AndroidJUnit4.class)
public class LoginActivitySteps extends ActivityInstrumentationTestCase2<CoffeeOrderListActivity> {

//    @Rule
//    public ActivityTestRule<CoffeeOrderListActivity> mActivityRule =
//            new ActivityTestRule<>(CoffeeOrderListActivity.class);
//
//    @Before
//    public void setup() {
//        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
//
//        mServiceIdlingResource =
//                new ServiceIdlingResource(mActivityRule.getActivity().getApplicationContext());
//        registerIdlingResources(mServiceIdlingResource);
//    }

    public LoginActivitySteps(CoffeeOrderListActivity activityClass) {
        super(CoffeeOrderListActivity.class);
    }

    @When("^I input email (\\S+)$")
    public void I_input_email(final String email) {
        onView(withId(R.id.coffee_price)).perform(typeText(email), closeSoftKeyboard());
    }

    @Given("^I have a LoginActivity$")
    public void i_have_a_LoginActivity() throws Throwable {
        assertNotNull(getActivity());
        //onView(withId(R.id.pay)).perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.pay)).perform(click());
        onView(withId(R.id.pay)).perform(click());
    }


    /**
     * Custom matcher to assert equal EditText.setError();
     */
    private static class ErrorTextMatcher extends TypeSafeMatcher<View> {

        private final String mExpectedError;

        //@Rule
        //public ActivityTestRule<CoffeeOrderListActivity> testRule = new ActivityTestRule<>(CoffeeOrderListActivity.class);

        @Given("^I have a LoginActivity")
        public void I_have_a_LoginActivity() {
            System.out.println("dsddf");
            //  Assert.assertThat(testRule.getActivity(), not(Matchers.<LoginActivity>nullValue()));
//        assertNotNull(getActivity());
        }

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
