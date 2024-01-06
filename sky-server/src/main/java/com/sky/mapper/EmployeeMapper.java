package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.Employee;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
