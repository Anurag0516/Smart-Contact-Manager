package com.smart.controllers;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.entities.User;
import com.smart.repositories.UserRepository;
import com.smart.service.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotController {
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	// email id form open Handler
	@RequestMapping("/forgot")
	public String openEmailForm() {
		return "forgot_email_form";
	}

	// sending otp Handler
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email, HttpSession session) {
		//generating otp of 4 digit
		Random random = new Random(10000);
		int otp = random.nextInt(999999);
		
		//write code for send OTP to email...
		String message = "<div style='border: 1px solid #e2e2e2; padding: 20px'>"
				+"<h1>"+otp+"</h1></div>";
		String to = email;
		boolean flag = this.emailService.sendEmail("Verification", message, to);
		if(flag) {
			session.setAttribute("myotp", otp);
			session.setAttribute("email", email);
			return "verify_otp";
		}
		else {
			return "forgot_email_form";
		}
	}
	
	//verifying otp Handler
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int otp, HttpSession session) {
		int myotp = (int)session.getAttribute("myotp");
		String email = (String)session.getAttribute("email");
		if(myotp==otp) {
			//password change form
			User user = this.userRepository.getUserByUserName(email);
			if(user == null) {
				//send error message
				//System.out.println("User does not exist");
				return "forgot_email_form";
			}
			else {
				//send change password form
			}
			
			return "password_change_form";
		}
		else {
			return "verify_otp";
		}
	}
	//change password
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newpassword") String newpassword, HttpSession session) {
		String email = (String)session.getAttribute("email");
		User user = this.userRepository.getUserByUserName(email);
		user.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
		this.userRepository.save(user);
		return "login";
	}
}
