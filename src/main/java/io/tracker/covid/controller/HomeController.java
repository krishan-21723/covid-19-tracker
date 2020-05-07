package io.tracker.covid.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.tracker.covid.service.CovidDataService;

@Controller
public class HomeController {

	@Autowired
	private CovidDataService covidDataService;

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("locations", covidDataService.getLocations());
		model.addAttribute("totalCases", covidDataService.getTotalCases());
		model.addAttribute("totalNewCases", covidDataService.getTotalNewCases());
		return "home";
	}
}
