package com.ssf.day17workshop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ssf.day17workshop.model.CountryCode;
import com.ssf.day17workshop.service.CurrencyService;

@SpringBootApplication
public class Day17WorkshopApplication implements CommandLineRunner {

	@Autowired
	CurrencyService currencyService;

	public static void main(String[] args) {
		SpringApplication.run(Day17WorkshopApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<CountryCode> countryCodes = currencyService.getCountryCode();
		System.out.println("Application started. Retrieved " + countryCodes.size() + " country codes.");
	}
}