package com.github.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TransRepositoryFactory: 翻译仓库工厂类
 *
 * @author zhangxiaoxiang
 * @since 2025/1/31
 */
public class TransRepositoryFactory {


    /**
     * 获取翻译仓库的缓存容器  key: 翻译仓库的class  value: 翻译仓库实例
     */
    private final static Map<Class<? extends TransRepository>, TransRepository> TRANS_REPOSITORY_MAP = new ConcurrentHashMap<>();

    public static TransRepository getTransRepository(Class<? extends TransRepository> repository) {
        return TRANS_REPOSITORY_MAP.get(repository);
    }

    public static void register(TransRepository transRepository) {
        if (transRepository == null) {
            return;
        }
        TRANS_REPOSITORY_MAP.put(transRepository.getClass(), transRepository);
    }

}
