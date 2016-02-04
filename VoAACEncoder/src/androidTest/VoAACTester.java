package androidTest;

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

/**
 * Created by Administrator on 2016/2/2.
 */
//@RunWith(AndroidJUnit4.class)
//@SdkSuppress(minSdkVersion = 18)
public class VoAACTester extends InstrumentationTestCase {
    private UiDevice mDevice;
    @Override
    public void setUp() throws Exception {
//        super.setUp();
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.pressHome();

        // Wait for launcher
//        final String launcherPackage = getLauncherPackageName();
//        assertThat(launcherPackage, notNullValue());
//        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);
        // Wait for the Apps icon to show up on the screen
        mDevice.wait(Until.hasObject(By.text("简客")), 8000);
        UiObject2 appButton=mDevice.findObject(By.text("简客"));
        appButton.click();
    }

//    @Test
//    public void checkPreconditions() {
//        ass(mDevice, notNullValue());
//    }

    public void testOpenAcc() throws Exception {

    }

    private String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }
}
