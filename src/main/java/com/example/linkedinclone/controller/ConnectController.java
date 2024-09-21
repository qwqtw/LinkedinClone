package com.example.linkedinclone.controller;

import com.example.linkedinclone.entity.Connect;
import com.example.linkedinclone.entity.User;
import com.example.linkedinclone.service.ConnectService;
import com.example.linkedinclone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/connections")
public class ConnectController {

    @Autowired
    private ConnectService connectService;

    @Autowired
    private UserService userService;

    // View sorted list of connections
    @GetMapping
    public String viewConnections(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        User user = userService.findByUsername(currentUser.getUsername());

        // Get sorted connections from the service layer
        List<Connect> connections = connectService.getConnections(user);
        List<Connect> pendingRequests = connectService.getPendingRequests(user);

        model.addAttribute("connections", connections);        // Sorted accepted connections
        model.addAttribute("pendingRequests", pendingRequests); // Pending requests (unsorted)

        return "connections"; // Thymeleaf template to display connections
    }

    // Send a connection request
    @PostMapping("/request")
    public String sendConnectionRequest(@RequestParam Long receiverId, @AuthenticationPrincipal UserDetails currentUser) {
        User sender = userService.findByUsername(currentUser.getUsername());
        connectService.sendConnectionRequest(sender.getId(), receiverId);
        return "redirect:/connections";
    }

    // Accept a connection request
    @PostMapping("/accept")
    public String acceptConnectionRequest(@RequestParam Long requestId, @AuthenticationPrincipal UserDetails currentUser) {
        User receiver = userService.findByUsername(currentUser.getUsername());
        connectService.acceptConnectionRequest(requestId, receiver.getId());
        return "redirect:/connections";
    }

    // Reject a connection request
    @PostMapping("/reject")
    public String rejectConnectionRequest(@RequestParam Long requestId, @AuthenticationPrincipal UserDetails currentUser) {
        User receiver = userService.findByUsername(currentUser.getUsername());
        connectService.rejectConnectionRequest(requestId, receiver.getId());
        return "redirect:/connections";
    }
}
