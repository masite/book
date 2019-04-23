package com.hongguo.read.repertory.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by losg on 2018/1/12.
 */

public class HotsSearchRepertory {
    private static String[] sHotSearch = {"重生", "前妻", "妃", "老公", "萌妻", "鬼", "美女", "总裁", "都市",
            "嫡女", "穿越", "王妃", "宠婚", "王爷"};

    public static String[] getRandomBooks() {
        Random random = new Random();
        List<String> suggest = new ArrayList<>();
        List<String> hots = new ArrayList<>();
        for (String hotSearch : sHotSearch) {
            hots.add(hotSearch);
        }
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(hots.size());
            suggest.add(hots.remove(index));
        }
        return suggest.toArray(new String[suggest.size()]);
    }
}
