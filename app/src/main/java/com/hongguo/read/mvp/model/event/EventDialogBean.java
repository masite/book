package com.hongguo.read.mvp.model.event;

import java.util.List;

/**
 * Created by losg on 17-12-14.
 */

public class EventDialogBean {


    public List<EventsBean> events;

    public static class EventsBean {
        /**
         * eventVersion :
         * appVersion :
         * eventFloat :
         * eventBigImage :
         * eventUrl :
         */

        public int    eventVersion;
        public String appVersion;
        public String eventFloat;
        public String eventBigImage;
        public String eventUrl;
    }
}
