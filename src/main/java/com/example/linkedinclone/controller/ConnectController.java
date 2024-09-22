package com.example.linkedinclone.controller;

import com.example.linkedinclone.entity.Connect;
import com.example.linkedinclone.entity.User;
import com.example.linkedinclone.service.ConnectService;
import com.example.linkedinclone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/connections")
public class ConnectController {

    @Autowired
    private ConnectService connectService;

    @Autowired
    private UserService userService;

    // View connections sorted by user-selected field
    @GetMapping
    public String viewConnections(Model model,
                                  @AuthenticationPrincipal UserDetails currentUser,
                                  @RequestParam (defaultValue = "receiver.username") String sortBy) {
        // find the current user
        User user = userService.findByUsername(currentUser.getUsername());

        // Get sorted connections by users selected
        List<Connect> connections = connectService.getConnectionsSorted(user, sortBy);
        List<Connect> pendingRequests = connectService.getPendingRequests(user);

        model.addAttribute("connections", connections);        // Sorted accepted connections
        model.addAttribute("pendingRequests", pendingRequests); // Pending requests (unsorted)

        return "network";
    }

    // Accept a connection request
    @PostMapping("/accept")
    public String acceptConnectionRequest(@RequestParam Long requestId,
                                          @AuthenticationPrincipal UserDetails currentUser) {
        User receiver = userService.findByUsername(currentUser.getUsername());
        connectService.acceptConnectionRequest(requestId, receiver.getId());
        return "redirect:/connections";
    }

    // Reject a connection request
    @PostMapping("/reject")
    public String rejectConnectionRequest(@RequestParam Long requestId,
                                          @AuthenticationPrincipal UserDetails currentUser) {
        User receiver = userService.findByUsername(currentUser.getUsername());
        connectService.rejectConnectionRequest(requestId, receiver.getId());
        return "redirect:/connections";
    }


}
