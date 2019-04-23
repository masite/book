package com.hongguo.read.repertory.data;

import com.hongguo.read.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by losg on 2018/1/12.
 */

public class ClassifyRepertory {

    private static List<Classify> sClassifies = new ArrayList<>();
    private static List<Classify> sSimpleClassifies = new ArrayList<>();

    static{
        sClassifies.add(new Classify("347", "浪漫言情", R.mipmap.ic_classify_lmyq));
        sClassifies.add(new Classify("344", "玄幻奇幻",R.mipmap.ic_classify_xhqx));
        sClassifies.add(new Classify("341", "恐怖悬疑",R.mipmap.ic_classify_kbxy));
        sClassifies.add(new Classify("342", "婚恋职场",R.mipmap.ic_classify_lazc));
        sClassifies.add(new Classify("343", "科幻传奇",R.mipmap.ic_classify_khcq));
        sClassifies.add(new Classify("346", "恋爱同人",R.mipmap.ic_classify_latr));
        sClassifies.add(new Classify("340", "青春校园",R.mipmap.ic_classify_dsqc));
    }

    static{
        sSimpleClassifies.add(new Classify("347", "言情", 0));
        sSimpleClassifies.add(new Classify("344", "玄幻",0));
        sSimpleClassifies.add(new Classify("343", "科幻",0));
        sSimpleClassifies.add(new Classify("341", "悬疑",0));
        sSimpleClassifies.add(new Classify("342", "职业",0));
        sSimpleClassifies.add(new Classify("346", "同人",0));
        sSimpleClassifies.add(new Classify("340", "校园",0));
    }

    public static List<Classify> getClassifies() {
        return sClassifies;
    }

    public static List<Classify> getSimpleClassifies() {
        return sSimpleClassifies;
    }

    public static class Classify {

        public String id;
        public String name;
        public int    resource;

        public Classify(String id, String name, int resource) {
            this.id = id;
            this.name = name;
            this.resource = resource;
        }
    }
}
