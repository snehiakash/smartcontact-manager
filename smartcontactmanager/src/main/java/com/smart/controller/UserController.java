package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/user")
public class UserController {
    
	@Autowired
	private UserRepository userRepository;
	
	//method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String userName=principal.getName();
		System.out.println("USERNAME" + userName);
		
		//get the user using username(Email)
		
		
		User user=userRepository.getUserByUserName(userName);
		System.out.println("USER" + user);
		 model.addAttribute("user", user);
	}
	
	//Dashboard Home
	
	@RequestMapping("/index")
	public String dashboard(Model model , Principal principal) {
		String userName=principal.getName();
		System.out.println("USERNAME" + userName);
		model.addAttribute("title","User Dashboard");
		
		//get the user using username(Email)
		
		User user=userRepository.getUserByUserName(userName);
		System.out.println("USER" + user);
		 if (user != null) {
		        model.addAttribute("user", user);
		    } else {
		        // Handle the case when the user object is null
		        return "error_page";  // Return an error page or show an error message
		    }
		
		
		return "normal/user_dashboard";
	}
	
	//open add form Handler
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {
	    model.addAttribute("title", "Add Contact");
	    model.addAttribute("contact", new Contact());

	   

	    return "normal/add_contact_form";
	}

	//Processing Add Contact Form
	
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,
			@RequestParam("profileImage") MultipartFile file,
			Principal principal,Model model,HttpSession session ) {
		try{
		String name=principal.getName();
		User user=this.userRepository.getUserByUserName(name);
		
		//Processing and Uploading file..
		if(file.isEmpty())
		{
			//if the file is empty then try our message.
			System.out.println("File is Empty");
			
		}
		else {
			//file the file to folder and update the name to contact
			contact.setImage(file.getOriginalFilename());
			File saveFile=new ClassPathResource("static/img").getFile();
			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
			Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Image is Uploaded");
			
		}
		contact.setUser(user);
		
		user.getContacts().add(contact);
		
		this.userRepository.save(user);
		
		System.out.println("DATA"+ contact);
		
		System.out.println("Added to DataBase");
		
		//Success Message.... 
		
		
		  
    session.setAttribute("message",new Message("Your Contact is added !!  Add More...","success"));
		 
		
		
		}catch(Exception e){
			System.out.println("ERROR"+e.getMessage());
			e.printStackTrace();
			//Error Message...
			
	session.setAttribute("message",new Message("Something Went Wrong !! Try Again...","danger"));
			 
		}
		return "normal/add_contact_form";
	}
	
}
