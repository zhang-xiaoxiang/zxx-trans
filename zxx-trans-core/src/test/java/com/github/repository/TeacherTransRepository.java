package com.github.repository;


import com.github.dto.TeacherDto;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TeacherTransRepository implements TransRepository {

    @Override
    public Map<Object, Object> getTransValueMap(List<Object> transValues, Annotation transAnno) {
        return getTeachers().stream().filter(x -> transValues.contains(x.getId())).collect(Collectors.toMap(TeacherDto::getId, x -> x));
    }

    public List<TeacherDto> getTeachers() {
        List<TeacherDto> teachers = new ArrayList<>();
        teachers.add(new TeacherDto(1L, "老师1", 1L));
        teachers.add(new TeacherDto(2L, "老师2", 2L));
        teachers.add(new TeacherDto(3L, "老师3", 3L));
        teachers.add(new TeacherDto(4L, "老师4", 4L));
        return teachers;
    }

}
