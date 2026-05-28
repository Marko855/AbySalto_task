package hr.abysalto.hiring.api.junior.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hr.abysalto.hiring.api.junior.model.Buyer;
import hr.abysalto.hiring.api.junior.repository.BuyerRepository;

@Controller
@RequestMapping("/ui/buyer")
public class BuyerViewController {

    private final BuyerRepository buyerRepository;

    public BuyerViewController(BuyerRepository buyerRepository) {
        this.buyerRepository = buyerRepository;
    }

    @GetMapping({"", "/"})
    public String viewHomePage(Model model) {
        model.addAttribute("buyerList", buyerRepository.findAll());
        return "buyer/index";
    }

    @GetMapping("/addnew")
    public String showNewBuyerForm(Model model) {
        Buyer buyer = new Buyer();
        model.addAttribute("buyer", buyer);
        return "buyer/new_buyer";
    }

    @PostMapping("/save")
    public String saveBuyer(@ModelAttribute("buyer") Buyer buyer) {
        buyerRepository.save(buyer);
        return "redirect:/ui/buyer/";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") Long id, Model model) {
        Buyer buyer = buyerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid buyer Id:" + id));

        model.addAttribute("buyer", buyer);
        return "buyer/update_buyer";
    }

    @GetMapping("/deleteBuyer/{id}")
    public String deleteBuyer(@PathVariable(value = "id") Long id) {
        buyerRepository.deleteById(id);
        return "redirect:/ui/buyer/";
    }
}
