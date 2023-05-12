package server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class SomeController {


    @GetMapping("/")
    @ResponseBody
    public String hello(){
        return "Hello World!";
    }

    @PostMapping("/name/{user}")
    public void storeUser(@PathVariable("user") String user){

    }
}
