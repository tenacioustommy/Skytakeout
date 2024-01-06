package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "employee api")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        // 登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("logout")
    public Result<String> logout() {
        return Result.success();
    }

    @PostMapping
    @ApiOperation("add employee")
    public Result<String> save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("add employee: {}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("page query")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("employee page {}", employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("status/{status}")
    @ApiOperation("account enable or disable")
    public Result startorstop(@PathVariable Integer status, Long id) {
        log.info("enable or disable account: {},{}", status, id);
        employeeService.startorstop(status, id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("get employee by id")
    public Result<Employee> getbyid(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    @PutMapping
    @ApiOperation("修改员工信息")
    public Result update(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.update(employeeDTO);
        return Result.success();
    }
}
