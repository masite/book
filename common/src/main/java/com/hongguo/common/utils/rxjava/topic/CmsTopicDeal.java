package com.hongguo.common.utils.rxjava.topic;

import com.hongguo.common.model.CmsTopicBean;
import com.losg.library.utils.JsonUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

/**
 * Created by losg on 2018/1/4.
 */

public class CmsTopicDeal {

    public static <T extends CmsTopicBean, G> ObservableTransformer<T, G> cmsTopDeal(Class<G> gClass) {
        return o -> o.flatMap((Function<T, ObservableSource<G>>) t -> {
            String content = t.data.content;
            G g = JsonUtils.fromJson(content, gClass);
            return Observable.just(g);
        });
    }

    public static <T extends CmsTopicBean> ObservableTransformer<T, String> cmsTopString() {
        return o -> o.flatMap((Function<T, ObservableSource<String>>) t -> {
            String content = t.data.content;
            return Observable.just(content);
        });
    }
}
