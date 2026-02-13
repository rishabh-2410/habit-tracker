package com.niko.apps.controllers.habits;

import java.util.List;

import com.niko.apps.models.habits.AddHabitRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.niko.apps.models.habits.HabitsResponse;
import com.niko.apps.service.HabitService;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class HabitController {
	private final HabitService habitService;
	
	public HabitController(HabitService habitService) {
		super();
		this.habitService = habitService;
	}
	
	
	
	@GetMapping("/habits")
	public List<HabitsResponse> getAllHabits(Authentication authentication) {
		log.info("Inside habits controller");
		return habitService.getHabitsForCurrentUser(authentication.getName());
	}

	@PostMapping("/habits")
	public void addHabit(@Valid @RequestBody AddHabitRequest req, Authentication authentication) {
		log.debug("req body: {}", req );
		habitService.addHabit(req.name(),req.description(), authentication.getName());
	}
}
