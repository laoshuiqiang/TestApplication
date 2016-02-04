package poco.uiautomatortest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by Administrator on 2016/2/2.
 */
//@RunWith(AndroidJUnit4.class)
//@SdkSuppress(minSdkVersion = 18)
public class JackTest extends InstrumentationTestCase {
    private UiDevice mDevice;
    private static final int LAUNCH_TIMEOUT = 5000;
    private final String BASIC_SAMPLE_PACKAGE = "com.android.calculator2";

    @Override
    public void setUp() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Start from the home screen
        mDevice.pressHome();

        // Wait for the Apps icon to show up on the screen
        mDevice.wait(Until.hasObject(By.text("简客")), 3000);
        UiObject2 appsButton = mDevice.findObject(By.text("简客"));
        appsButton.click();
        // Wait for the Calculator icon to show up on the screen
        mDevice.wait(Until.hasObject(By.text("简客")), 3000);
        // Wait for launcher
//        final String launcherPackage = "com.lenovo.launcher";
//        assertThat(launcherPackage, notNullValue());
//
//        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);
    }

//    @Test
//    public void checkPreconditions() {
//        assertThat(mDevice, notNullValue());
//    }
//
    @Test
    public void calculatorTest() {
        mDevice.findObject(By.desc("应用")).click();
        mDevice.wait(Until.hasObject(By.desc("简客")), LAUNCH_TIMEOUT);
        mDevice.findObject(By.desc("简客")).click();

        UiObject2 button7 = mDevice.wait(Until.findObject(By.res("com.android.calculator2", "digit_7")), 500);
        UiObject2 buttonX = mDevice.wait(Until.findObject(By.res("com.android.calculator2", "op_mul")), 500);
        UiObject2 button6 = mDevice.wait(Until.findObject(By.res("com.android.calculator2", "digit_6")), 500);
        UiObject2 buttonEqual = mDevice.wait(Until.findObject(By.res("com.android.calculator2", "eq")), 500);
        UiObject2 output = mDevice.wait(Until.findObject(By.res("com.android.calculator2", "result")), 500);

        button7.click();
        buttonX.click();
        button6.click();
        buttonEqual.click();
        assertEquals(output.getText(), "42");

    }
//
//    private String getLauncherPackageName() {
//        // Create launcher Intent
//        final Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//
//        // Use PackageManager to get the launcher package name
//        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
//        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
//        return resolveInfo.activityInfo.packageName;
//    }

}
