package com.isep.testjpa.controller;

import com.isep.testjpa.repository.EmpRepository;
import com.isep.testjpa.model.Emp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees") // Base path for all employee endpoints
public class SimpleController {

    @Autowired
    private EmpRepository empRepository;

    @GetMapping
    public List<Emp> getAllEmployees() {
        return empRepository.findAll();
    }

    @GetMapping("/{id}")
    public Emp getEmployeeById(@PathVariable Long id) {
        Optional<Emp> employee = empRepository.findById(id);
        return employee.orElse(null); // Returns null if not found (consider ResponseEntity for better practice)
    }

    @PostMapping
    public Emp createEmployee(@RequestBody Emp emp) {
        return empRepository.save(emp);
    }

    @PutMapping("/{id}")
    public Emp updateEmployee(@PathVariable Long id, @RequestBody Emp empDetails) {
        return empRepository.findById(id)
                .map(emp -> {
                    emp.setEname(empDetails.getEname());
                    emp.setEfirst(empDetails.getEfirst());
                    emp.setJob(empDetails.getJob());
                    emp.setMgr(empDetails.getMgr());
                    emp.setSal(empDetails.getSal());
                    return empRepository.save(emp);
                })
                .orElseGet(() -> {
                    empDetails.setEmpno(id);
                    return empRepository.save(empDetails);
                });
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        empRepository.deleteById(id);
    }

    @RequestMapping(value="/", method= RequestMethod.GET)
    public String hello(@Param("name") String name) {
        return "Hello " + name;
    }
}