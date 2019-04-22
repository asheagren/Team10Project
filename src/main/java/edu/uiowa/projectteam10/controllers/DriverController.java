package edu.uiowa.projectteam10.controllers;

import edu.uiowa.projectteam10.model.Ride;
import edu.uiowa.projectteam10.services.RidesService;
import edu.uiowa.projectteam10.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class DriverController {
    @Autowired
    private RidesService rideService;
    @Autowired
    private UserService userService;

    @GetMapping("/driver/driverRides")
    public String driverPage(Model model){
        if(!checkAccess()){
            return "redirect:/login";
        }
        List<Ride> rides = this.rideService.getEmptyRides();
        model.addAttribute("unselectRides", rides);
        /*UserDetails userLogged = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();*/
        return "driverRides";
    }
    @PostMapping("/driver/driverRides")
    public String update(@RequestParam("selection") Integer selection, Model model, HttpServletRequest request){
        rideService.assignDriver(selection, userService.getCurrentUser().getUserName());
        return "driver";
    }
    @GetMapping("/driver")
    public String getDriver(){
        if(!checkAccess()){
            return "redirect:/login";
        }
        return "driver";
    }
    private boolean checkAccess(){
        try {
            String currentUser = userService.getCurrentUser().getUserName();
            if (userService.getRole(currentUser).equals("Driver")) {
                return true;
            }
            return false;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
