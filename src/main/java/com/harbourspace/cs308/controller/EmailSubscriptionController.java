package com.harbourspace.cs308.controller;

import com.harbourspace.cs308.model.EmailSubscriber;
import com.harbourspace.cs308.repository.EmailSubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class EmailSubscriptionController {

    @Autowired
    private EmailSubscriberRepository repository;

    @GetMapping("/email-subscribe")
    public String showLandingPage(Model model) {
        model.addAttribute("subscriber", new EmailSubscriber());
        return "index";
    }

    @PostMapping("/email-subscribe")
    public String subscribe(@Valid EmailSubscriber subscriber, BindingResult bindingResult,
                            HttpServletRequest request, Model model) {
        if (repository.existsByEmail(subscriber.getEmail())) {
            bindingResult.rejectValue("email", "error.subscriber", "This email has already been registered.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("subscriber", subscriber);
            return "index";
        }
        
        subscriber.setIpAddress(request.getRemoteAddr());        
        repository.save(subscriber);
        
        model.addAttribute("successMessage", "Thank you for subscribing!");
        model.addAttribute("subscriber", new EmailSubscriber());
        return "index";
    }
}
