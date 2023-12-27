package com.rhymthwave.API_LR;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VerificationController {

	@GetMapping("/verification-success")
    public String showVerificationSuccessPage() {
        return "user/notifi";
    }
}
