package com.niko.apps.controllers.tracking;

import com.niko.apps.models.habits.HabitStats;
import com.niko.apps.models.habits.MarkHabitReq;
import com.niko.apps.service.HabitLogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user/habit")
public class HabitLogController {

    private final HabitLogService habitLogService;

    public HabitLogController(HabitLogService habitLogService) {
        this.habitLogService = habitLogService;
    }

    @PostMapping("/{id}/mark-done")
    public ResponseEntity<Object> markHabit(@Valid @RequestBody MarkHabitReq req, @PathVariable Long id, Authentication authentication) {
    habitLogService.markHabit(req.status(), id, authentication.getName());
    return ResponseEntity.ok().build();
    }


    @GetMapping("/{id}/history")
    public ResponseEntity<List<LocalDate>> getHistory(@PathVariable Long id, Authentication authentication) {
        List<LocalDate> history = habitLogService.getHabitHistory( id, authentication.getName());
        return new ResponseEntity<>(history, HttpStatus.OK);
    }


    @GetMapping("/{id}/stats")
    public ResponseEntity<HabitStats> getStats(@PathVariable Long id, Authentication authentication) {
        HabitStats stats = habitLogService.getHabitStats( id, authentication.getName());
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }


}
