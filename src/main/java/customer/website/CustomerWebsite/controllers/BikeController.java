package customer.website.CustomerWebsite.controllers;
import customer.website.CustomerWebsite.models.Customer;
import customer.website.CustomerWebsite.models.RentalBike;
import customer.website.CustomerWebsite.services.BikeService;
import customer.website.CustomerWebsite.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;
@Controller
public class BikeController {

    @Autowired
    BikeService bikeService;

    @Autowired
    CustomerService customerService;

    @GetMapping("/bikes/assign/{customerId}")
    public String assignBikeToCustomer(@PathVariable(name = "customerId") Long customerId, Model model) {
        Customer customer = customerService.getCustomer(customerId);
        List<RentalBike> bikeList = bikeService.getAvailableBikes();
        model.addAttribute("customer", customer);
        model.addAttribute("bikeList", bikeList);
        return "assign-bike";
    }

    @PostMapping("/bikes/assign")
    public String saveBikeToCustomerAssignment(
            @RequestParam("customerId") Long customerId,
            @RequestParam("bikeId") Long bikeId) {
        RentalBike bike = bikeService.getBike(bikeId);
        bike.setCustomer(customerService.getCustomer(customerId));
        bikeService.saveBike(bike);
        return "redirect:/";
    }

    @RequestMapping("/unassign/{bikeId}")
    public String unassignBikeFromCustomer(@PathVariable(name = "bikeId") Long bikeId) {
        RentalBike bike = bikeService.getBike(bikeId);
        bike.setCustomer(null);
        bikeService.saveBike(bike);
        return "redirect:/";
    }



    @GetMapping("/bikes")
    public String viewAllBikes(Model model) {
        final List<RentalBike> bikeList = bikeService.getAllBikes();
        model.addAttribute("bikeList", bikeList);
        return "bikes";
    }

    @GetMapping("/new-bike")
    public String showSubmitBikeFormPage(Model model) {
        RentalBike bike = new RentalBike();
        model.addAttribute("bike", bike);
        return "new-bike";
    }

    @PostMapping("/bikes")
    public String saveNewBike(@ModelAttribute("bike") RentalBike bike, Model model) {
        try {
            bikeService.saveBike(bike);
        } catch (IllegalArgumentException e) {
            model.addAttribute(
                    "message",
                    "Could not save bike, "
                    + e.getMessage());
            return "error";
        }
        return "redirect:/bikes";
    }
}
