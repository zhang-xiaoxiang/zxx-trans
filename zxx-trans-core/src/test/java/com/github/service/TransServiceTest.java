package com.github.service;


import com.github.dto.Result;
import com.github.dto.UserDto;
import com.github.dto.UserDto2;
import com.github.repository.SubjectTransRepository;
import com.github.repository.TeacherTransRepository;
import com.github.repository.TransRepositoryFactory;
import com.github.repository.dict.DictLoader;
import com.github.repository.dict.DictTransRepository;
import com.github.resolver.ResultResolver;
import com.github.resolver.TransObjResolverFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TransServiceTest {

    TransService transService;

    @BeforeAll
    public static void before() {
        TransRepositoryFactory.register(new TeacherTransRepository());
        TransRepositoryFactory.register(new SubjectTransRepository());
        TransRepositoryFactory.register(new DictTransRepository(new DictLoader() {
            @Override
            public Map<String, String> loadDict(String dictGroup) {
                return dictMap().getOrDefault(dictGroup, new HashMap<>());
            }

            private Map<String, Map<String, String>> dictMap() {
                Map<String, Map<String, String>> map = new HashMap<>();
                map.put("sexDict", new HashMap<>());
                map.put("jobDict", new HashMap<>());
                map.get("sexDict").put("1", "男");
                map.get("sexDict").put("2", "女");
                map.get("jobDict").put("1", "学习委员");
                map.get("jobDict").put("2", "生活委员");
                map.get("jobDict").put("3", "宣传委员");
                map.get("jobDict").put("4", "班长");
                map.get("jobDict").put("5", "团支书");
                map.get("jobDict").put("6", "团长");
                return map;
            }

        }));
        TransObjResolverFactory.register(new ResultResolver());
    }

    @BeforeEach
    public void init() {
        transService = new TransService();
        transService.init();
    }

    @Test
    void trans() {
        UserDto userDto = new UserDto(1L, "张三", 2L, "1", "2");
        System.out.println("翻译前：" + userDto);
        transService.trans(userDto);
        System.out.println("翻译后：" + userDto);
        List<UserDto> userDtoList = new ArrayList<>();
        UserDto userDto2 = new UserDto(2L, "李四", 1L, "2", "1");
        UserDto userDto3 = new UserDto(3L, "王五", 2L, "1", "3");
        UserDto userDto4 = new UserDto(4L, "赵六", 3L, "2", "4");
        userDtoList.add(userDto4);
        userDtoList.add(userDto3);
        userDtoList.add(userDto2);
        System.out.println("翻译前：" + userDtoList);
        transService.trans(userDtoList);
        System.out.println("翻译后：" + userDtoList);

    }

    @Test
    void trans2() {
        List<Long> teacherIds = new ArrayList<>();
        teacherIds.add(1L);
        teacherIds.add(2L);
        List<String> jobIds = new ArrayList<>();
        jobIds.add("1");
        jobIds.add("2");
        UserDto2 userDto = new UserDto2(1L, "张三", teacherIds, jobIds);
        System.out.println("翻译前：" + userDto);
        transService.trans(userDto);
        System.out.println("翻译后：" + userDto);
        List<UserDto2> userDtoList = new ArrayList<>();
        UserDto2 userDto2 = new UserDto2(2L, "李四", teacherIds, jobIds);
        List<Long> teacherIds2 = new ArrayList<>();
        teacherIds2.add(3L);
        teacherIds2.add(4L);
        List<String> jobIds2 = new ArrayList<>();
        jobIds2.add("3");
        jobIds2.add("4");
        UserDto2 userDto3 = new UserDto2(3L, "王五", teacherIds2, jobIds2);
        UserDto2 userDto4 = new UserDto2(4L, "赵六", teacherIds2, jobIds2);
        userDtoList.add(userDto4);
        userDtoList.add(userDto3);
        userDtoList.add(userDto2);
        System.out.println("翻译前：" + userDtoList);
        transService.trans(userDtoList);
        System.out.println("翻译后：" + userDtoList);
    }

    @Test
    void trans3() {
        List<Long> teacherIds = new ArrayList<>();
        teacherIds.add(1L);
        teacherIds.add(2L);
        List<String> jobIds = new ArrayList<>();
        jobIds.add("1");
        jobIds.add("2");
        jobIds.add("3");
        UserDto2 userDto = new UserDto2(1L, "张三", teacherIds, jobIds);
        Result<UserDto2> result = new Result<>(userDto, "success");
        System.out.println("翻译前：" + result);
        transService.trans(result);
        System.out.println("翻译后：" + result);
        UserDto2 userDto2 = new UserDto2(2L, "李四", teacherIds, jobIds);
        Result<UserDto2> result2 = new Result<>(userDto2, "success");
        Result<Result<UserDto2>> result3 = new Result<>(result2, "success");
        System.out.println("翻译前：" + result3);
        transService.trans(result3);
        System.out.println("翻译后：" + result3);
    }

}
