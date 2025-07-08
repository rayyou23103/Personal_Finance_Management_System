package com.rayyou.personal_finance_management_system.controller;

import com.rayyou.personal_finance_management_system.dto.UserLoginDTO;
import com.rayyou.personal_finance_management_system.dto.UserRegisterDTO;
import com.rayyou.personal_finance_management_system.entity.User;
import com.rayyou.personal_finance_management_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/userapi")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserRegisterDTO dto) {
        Map<String, Object> response = new HashMap<>();
        try{
            Integer userId = userService.register(dto.getEmail(),dto.getPassword());
            response.put("success",true);
            response.put("userId",userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("success",false);
            response.put("error",e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e){
            response.put("success",false);
            response.put("error","Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserLoginDTO dto) {
        boolean success = userService.login(dto.getEmail(),dto.getPassword());
        Map<String, Object> response = new HashMap<>();
        response.put("success",success);
        return ResponseEntity.ok(response);
    }



//    ================================test================================
    @GetMapping("/class/{id}")
    public ResponseEntity<TestClass> getClass(@PathVariable Integer id) {
        TestClass testClass = new TestClass("Hello, My id is 1",1);
        if (testClass.getId() != id){
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(testClass);
    }

    @PostMapping("/testcreate")
    public ResponseEntity<TestClass> createTestClass(@RequestBody TestClass testClass){
        TestClass testClass1 = new TestClass();
        testClass1.setId(testClass.getId());
        testClass1.setMessage(testClass.getMessage());

        if (testClass1 == null){
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location","/users/"+ testClass.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(testClass1);
    }

   private static class TestClass{
        String message;
        Integer id;

        public TestClass(){}

        TestClass(String message, Integer num){
            this.message = message;
            this.id = num;
        }

       public String getMessage() {
           return message;
       }

       public void setMessage(String message) {
           this.message = message;
       }

       public Integer getId() {
           return id;
       }

       public void setId(Integer id) {
           this.id = id;
       }
   }


}
