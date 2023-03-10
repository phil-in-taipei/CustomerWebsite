package customer.website.CustomerWebsite.controllers;

import customer.website.CustomerWebsite.models.Customer;
import customer.website.CustomerWebsite.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable(name = "id") Long id, Model model) {
        if (customerService.getCustomer(id) == null) {
            model.addAttribute("message",
                    "Cannot delete, customer with id " + id + " does not exist.");
            return "error";
        }
        customerService.deleteCustomer(id);
        return "redirect:/";
    }

    @PostMapping(value = "/save")
    // As the Model is received back from the view, @ModelAttribute
    // creates a Customer based on the object you collected from
    // the HTML page above
    public String saveCustomer(
            @ModelAttribute("customer") Customer customer, Model model) {
        try {
            customerService.saveCustomer(customer);
        } catch (IllegalArgumentException e) {
            model.addAttribute(
                    "message",
                    "Could not save customer, "
                    + e.getMessage()
            );
            return "error";
        }
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    // The path variable "id" is used to pull a customer from the database
    public ModelAndView showEditCustomerPage(@PathVariable(name = "id") Long id) {
        // Since the previous methods use Model, this one uses ModelAndView
        // to get some experience using both. Model is more common these days,
        // but ModelAndView accomplishes the same thing and can be useful in
        // certain circumstances. The view name is passed to the constructor.
        ModelAndView mav = new ModelAndView("edit-customer");
        Customer customer = customerService.getCustomer(id);
        if (customer == null) {
            mav.setViewName("error");
            mav.addObject("message",
                    "Customer with id "
                    + id + " does not exist."
            );
        } else {
            mav.addObject("customer", customer);
        }
        return mav;
    }

    @GetMapping("/new")
    public String showNewCustomerPage(Model model) {
        // Here a new (empty) Customer is created and then sent to the view
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "new-customer";
    }


    @PostMapping("/update/{id}")
    public String updateCustomer(
            @PathVariable(name = "id") Long id,
            @ModelAttribute("customer") Customer customer, Model model) {
        if (!id.equals(customer.getId())) {
            model.addAttribute("message",
                    "Cannot update, customer id " + customer.getId()
                            + " doesn't match id to be updated: " + id + ".");
            return "error";
        }
        customerService.saveCustomer(customer);
        return "redirect:/";
    }
    @GetMapping("/")
    public String viewHomePage(Model model) {
        // Here you call the service to retrieve all the customers
        final List<Customer> customerList = customerService.getAllCustomers();
        // Once the customers are retrieved, you can store them in model and return it to the view
        model.addAttribute("customerList", customerList);
        return "index";
    }

}
