package com.hongguo.read.utils.convert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过注解为对应的类赋值
 */
public class BeanConvert{


    public static <G,T> T beanConvert(G g, Class<T> tClass) throws Exception {
        T t = tClass.newInstance();
        BeanConvert.convertBean(g, t);
        return t;
    }

    /**
     * 注解转换
     *
     * @param from      源bean
     * @param armObject 目标bean
     */
    public static void convertBean(Object from, Object armObject) {
        try {
            Field[] fields = from.getClass().getDeclaredFields();  //获取from类下变量数组
            Class clazz = armObject.getClass();  //获取目标bean实例 的 类型的类
            for (Field field : fields) {    //遍历源bean 类 的所有变量
                Annotation[] annotations = field.getAnnotations();   //获取变量的 注解
                if (annotations == null || annotations.length == 0) {
                    continue;   //如果注解为null，进行下一次循环 。目的是处理加了注解的变量
                }
                if (annotations[0] instanceof ListParma) {   //如果是list
                    //是list类型
                    if (field.getType() == List.class) {  //如果是List类型
                        String paramName = ((ListParma) annotations[0]).paramListName();   //取到此注解的名称
                        Field name = null;
                        try {
                            name = clazz.getDeclaredField(paramName);  //通过反射，取到目标类中，  名称为paramName  的变量
                        } catch (Exception e) {
                        }
                        if (name == null) {
                            continue;  //如果目标中没有此变量，继续循环。
                        }
                        name.setAccessible(true);  //设置此变量可以访问
                        name.set(armObject, new ArrayList<>());  //设置此变量的值
                        List<?> list = (List<?>) field.get(from);  //取到源bean中，这个变量的list集合
                        Type[] actualTypeArguments = ((ParameterizedType) name.getGenericType()).getActualTypeArguments();  //获取name 所属的值  类型
                        boolean isBaseType = false;   //判断是否属于基础类型
                        if (actualTypeArguments[0] == String.class || actualTypeArguments[0] == int.class
                                || actualTypeArguments[0] == double.class || actualTypeArguments[0] == float.class
                                || actualTypeArguments[0] == boolean.class) {
                            isBaseType = true;
                        }
                        //自定义类型
                        for (Object o1 : list) {  //遍历源bean list集合
                            List<Object> lists = (List<Object>) name.get(armObject);   //获取目标bean中 name 变量的集合。 已知它是集合
                            if (isBaseType) {  //如果属于上面的基础类型
                                try {
                                    lists.add(o1);  //  目标bean 的list集合加入 此元素（源bean的元素）
                                } catch (Exception e) {
                                    //类型不一致
                                }
                            } else {  //如果不属于基础类型
                                Object o2 = ((Class) actualTypeArguments[0]).newInstance();  //获取这个元素的实例
                                convertBean(o1, o2);  //继续走循环
                                lists.add(o2); //  目标元素中集合加入此元素
                            }
                        }
                    }
                    continue;
                } else if (annotations[0] instanceof Parma) {  //如果是list的元素
                    String paramName = ((Parma) annotations[0]).paramName();  //取到这个注解名称
                    Field name = null;
                    name = getDeclaredField(clazz, paramName);
                    if (name == null) {
                        continue;  //如果变量为null，继续循环
                    }
                    name.setAccessible(true);  //设置为可访问
                    //基本数据类型
                    if (field.getType() == String.class || field.getType() == int.class
                            || field.getType() == double.class || field.getType() == float.class
                            || field.getType() == boolean.class) {
                        try {
                            name.set(armObject, field.get(from));  //给该变量赋值
                        } catch (Exception e) {
                            //类型不一致
                        }
                    } else {
                        if (field.get(from) == null) {
                            continue;  //
                        } else {
                            name.set(armObject, name.getType().newInstance());  // 初始值
                            convertBean(field.get(from), name.get(armObject));  //得到此非基础类型，继续循环
                        }
                    }
                    continue;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Field getDeclaredField(Class clazz, String fieldName) {
        Field field = null;

        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                return field;
            } catch (Exception e) {

            }
        }

        return null;
    }
}
