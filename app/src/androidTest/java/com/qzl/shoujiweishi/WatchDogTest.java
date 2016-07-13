package com.qzl.shoujiweishi;

import android.test.AndroidTestCase;
import com.qzl.shoujiweishi.db.WatchDogOpenHelper;

/**
 * Created by Qzl on 2016-07-13.
 */
public class WatchDogTest extends AndroidTestCase {
    public void testWatchDogOpenhelper(){
        WatchDogOpenHelper watchDogOpenHelper = new WatchDogOpenHelper(getContext());
        watchDogOpenHelper.getReadableDatabase();
    }
}
