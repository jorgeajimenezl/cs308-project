package com.harbourspace.cs308.controller;

import com.harbourspace.cs308.model.Subscriber;
import com.harbourspace.cs308.repository.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.LocalDateTime;

@Controller
public class EmailSubscriptionController {

    @Autowired
    private SubscriberRepository repository;

    @GetMapping("/")
    public String showLandingPage(Model model) {
        model.addAttribute("subscriber", new Subscriber());
        return "index";
    }

    @PostMapping("/subscribe")
    public String subscribe(@Valid Subscriber subscriber, BindingResult bindingResult,
                            HttpServletRequest request, Model model) {
        if (repository.existsByEmail(subscriber.getEmail())) {
            bindingResult.rejectValue("email", "error.subscriber", "This email has already been registered.");
        }

        if (bindingResult.hasErrors()) {
            return "index";
        }
        
        subscriber.setSubscriptionTime(LocalDateTime.now());
        subscriber.setIpAddress(request.getRemoteAddr());
        
        repository.addSubscriber(subscriber);
        
        model.addAttribute("successMessage", "Thank you for subscribing!");
        model.addAttribute("subscriber", new Subscriber());
        return "index";
    }
}
