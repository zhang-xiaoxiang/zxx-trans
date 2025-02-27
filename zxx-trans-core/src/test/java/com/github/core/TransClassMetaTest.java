package com.github.core;

import com.github.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class TransClassMetaTest {

    @Test
    void getTransField() {
        TransClassMeta transClassMeta = new TransClassMeta(UserDto.class);
        Assertions.assertTrue(transClassMeta.needTrans());
        List<TransFieldMeta> transFieldMeta = transClassMeta.getTransFieldList();
        Assertions.assertEquals(4, transFieldMeta.size());
    }


}
