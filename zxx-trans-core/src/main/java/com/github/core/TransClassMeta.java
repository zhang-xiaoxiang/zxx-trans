package com.github.core;


import com.github.annotation.Trans;
import com.github.repository.TransRepository;
import com.github.util.ReflectUtils;
import com.github.util.StringUtils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * TransClassMeta: 需要翻译的类源信息
 *
 * @author zhangxiaoxiang
 * @since 2025/1/31
 */
public class TransClassMeta implements Serializable {

    private static final long serialVersionUID = -8211850528694193388L;

    /**
     * 需要翻译的类
     */
    private final Class<?> clazz;
    /**
     * 需要翻译的字段集合
     */
    private List<TransFieldMeta> transFieldMetaList = new ArrayList<>();


    /**
     * TransClassMeta的构造函数
     *
     * @param clazz 要解析的类
     */
    public TransClassMeta(Class<?> clazz) {
        // 保存传入的类对象
        this.clazz = clazz;
        // 解析带有Trans注解的字段
        parseTransField();
    }


    /**
     * 获取需要翻译的字段
     *
     * @return 字段集合
     */
    public List<TransFieldMeta> getTransFieldList() {
        return this.transFieldMetaList;
    }



    /**
     * 解析带有Trans注解的字段
     */
    private void parseTransField() {
        // 获取类的所有字段
        List<Field> declaredFields = ReflectUtils.getAllField(this.clazz);
        // 创建一个字段名称到字段对象的映射(如果出现重复键，默认保留旧值。)
        Map<String, Field> fieldNameMap = declaredFields.stream().collect(Collectors.toMap(Field::getName, x -> x, (o, n) -> o));
        int mod;
        List<TransFieldMeta> transFieldMetas = new ArrayList<>();
        // 循环遍历所有的属性进行判断
        for (Field field : declaredFields) {
            mod = field.getModifiers();
            // 如果是 static, final, volatile, transient 的字段，则直接跳过
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod) || Modifier.isVolatile(mod) || Modifier.isTransient(mod)) {
                continue;
            }

            Trans transAnno = field.getAnnotation(Trans.class);
            String trans = null;
            String key = null;
            Class<? extends TransRepository> repository = null;
            Annotation transAnnotation = transAnno;
            // 解析字段上的 Trans 注解，如果字段没有直接标注 Trans 注解，则检查其所有注解是否包含 Trans 注解
            if (transAnno == null) {
                Annotation[] annotations = field.getDeclaredAnnotations();
                for (Annotation annotation : annotations) {
                    Class<? extends Annotation> annotationType = annotation.annotationType();
                    transAnno = annotationType.getAnnotation(Trans.class);
                    if (transAnno != null) {
                        repository = transAnno.using();
                        trans = StringUtils.isNotEmpty(transAnno.trans()) ? transAnno.trans() : (String) ReflectUtils.invokeAnnotation(annotationType, annotation, "trans");
                        key = StringUtils.isNotEmpty(transAnno.key()) ? transAnno.key() : (String) ReflectUtils.invokeAnnotation(annotationType, annotation, "key");
                        transAnnotation = annotation;
                        break;
                    }
                }
            // 如果字段有 Trans 注解，直接获取 trans、key 和 repository 属性
            } else {
                trans = transAnno.trans();
                key = transAnno.key();
                repository = transAnno.using();
            }
            if (StringUtils.isEmpty(trans)) {
                continue;
            }
            if (!fieldNameMap.containsKey(trans)) {
                continue;
            }
            if (StringUtils.isEmpty(key)) {
                key = field.getName();
            }
            // 将解析到的Trans字段信息添加到列表中
            transFieldMetas.add(new TransFieldMeta(field, fieldNameMap.get(trans), key, repository, transAnnotation));
        }
        // 构建Trans字段的解析树
        this.transFieldMetaList = buildTransTree(transFieldMetas);
    }



    /**
     * 构建Trans字段的解析树
     *
     * @param transFieldMetas Trans字段信息列表
     * @return 构建好的Trans字段解析树
     */
    private List<TransFieldMeta> buildTransTree(List<TransFieldMeta> transFieldMetas) {
        Map<String, List<TransFieldMeta>> tempMap = transFieldMetas.stream().collect(Collectors.groupingBy(TransFieldMeta::getTrans));
        Map<String, List<TransFieldMeta>> nameMap = transFieldMetas.stream().collect(Collectors.groupingBy(x -> x.getField().getName()));

        return transFieldMetas.stream()
                .filter(m -> !nameMap.containsKey(m.getTrans()))
                .peek(m -> findChildren(Collections.singletonList(m), tempMap))
                .collect(toList());
    }

    /**
     * 递归查找子节点
     *
     * @param root     根节点列表
     * @param tempMap  临时映射表，用于存储字段名到TransFieldMeta列表的映射关系
     */
    public static void findChildren(List<TransFieldMeta> root, Map<String, List<TransFieldMeta>> tempMap) {
        root.stream()
                .filter(x -> tempMap.containsKey(x.getField().getName()))
                .forEach(x -> {
                    List<TransFieldMeta> children = tempMap.get(x.getField().getName());
                    x.setChildren(children);
                    findChildren(children, tempMap);
                });
    }

    /**
     * @return 判断是否需要翻译
     */
    public boolean needTrans() {
        return !transFieldMetaList.isEmpty();
    }

}
