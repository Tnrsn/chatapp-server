package com.chatapp.server.search;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatapp.server.user.User;
import com.chatapp.server.user.UserService;

@RestController
@RequestMapping("/search")
public class SearchManager {

	private final UserService userService;
	
    public SearchManager(UserService userService) {
        this.userService = userService;
    }
    
	@GetMapping("/people")
	public List<User> searchPeople(@RequestParam String search)
	{
		List<User> users = userService.searchUsers(search);

		return users;
	}
	
}
