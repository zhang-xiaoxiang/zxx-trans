package com.github.register;


import com.github.repository.TransRepository;
import com.github.repository.TransRepositoryFactory;
import com.github.resolver.TransObjResolver;
import com.github.resolver.TransObjResolverFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class EasyTransRegister implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof TransRepository) {
            TransRepositoryFactory.register((TransRepository) bean);
        } else if (bean instanceof TransObjResolver) {
            TransObjResolverFactory.register((TransObjResolver) bean);
        }
        return bean;
    }

}
