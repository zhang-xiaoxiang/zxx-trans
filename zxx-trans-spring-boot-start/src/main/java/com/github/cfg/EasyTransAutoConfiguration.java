package com.github.cfg;


import com.github.aop.AutoTransAspect;
import com.github.register.EasyTransRegister;
import com.github.repository.dict.DictLoader;
import com.github.repository.dict.DictTransRepository;
import com.github.service.TransService;
import com.github.uitl.TransUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class EasyTransAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TransService transService() {
        TransService transService = new TransService();
        transService.init();
        log.warn("==================zxx-trans 翻译插件已注入 ======================");
        return transService;
    }

    @Bean
    @ConditionalOnBean(DictLoader.class)
    public DictTransRepository dictTransRepository(DictLoader dictLoader) {
        return new DictTransRepository(dictLoader);
    }

    @Bean
    public EasyTransRegister easyTransRegister() {
        return new EasyTransRegister();
    }

    @Bean
    public AutoTransAspect autoTransAspect() {
        return new AutoTransAspect();
    }

    @Bean
    public TransUtil transUtil() {
        return new TransUtil();
    }

}
