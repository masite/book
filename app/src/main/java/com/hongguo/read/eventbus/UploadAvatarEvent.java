package com.hongguo.read.eventbus;

/**
 * Created by losg on 2018/1/8.
 */

public class UploadAvatarEvent {

    public String filePath;

    public UploadAvatarEvent(String filePath) {
        this.filePath = filePath;
    }
}

