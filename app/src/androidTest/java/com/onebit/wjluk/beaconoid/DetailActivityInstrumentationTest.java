package com.onebit.wjluk.beaconoid;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.onebit.wjluk.beaconoid.util.AdManager;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DetailActivityInstrumentationTest {
//    @BeforeClass
//    public void setup() {
//
//    }
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.onebit.wjluk.beaconoid", appContext.getPackageName());

    }

    @Test
    public void adManager() throws Exception {
        AdManager manager = AdManager.getInstance();
        manager.setEmail("czlukuan@gmail.com");
        manager.setbId("1");
        manager.setPhone("1234");
        manager.setDistance(0.1);

        assertEquals("czlukuan@gmail.com",manager.getEmail());
        assertEquals("1",manager.getbId());
        assertEquals("1234",manager.getPhone());
        assertEquals(0.1, manager.getDistance(),0.01);

    }

    @Test
    public void fetchTask() {
        Dash2Activity activity = new Dash2Activity();

    }
}
