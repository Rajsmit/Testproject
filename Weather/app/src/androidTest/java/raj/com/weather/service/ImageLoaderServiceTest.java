package raj.com.weather.service;

import org.junit.Test;

import android.test.AndroidTestCase;

public class ImageLoaderServiceTest extends AndroidTestCase{
    @Test
    public void getInstance() throws Exception {
        ImageLoaderService ins = ImageLoaderService.getInstance(mContext);
        assertTrue(ins != null);
    }

    @Test
    public void getRequestQueue() throws Exception {
        ImageLoaderService ins = ImageLoaderService.getInstance(mContext);
        assertTrue(ins != null);
        assertTrue(null != ins.getRequestQueue());
    }

    @Test
    public void getImageLoader() throws Exception {
        ImageLoaderService ins = ImageLoaderService.getInstance(mContext);
        assertTrue(ins != null);
        assertTrue(null != ins.getImageLoader());

    }

}