package io.tracker.covid.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import io.tracker.covid.models.Location;

@Service
public class CovidDataService {

	private static final String url = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

	private List<Location> locations = new ArrayList<Location>();
	private int totalCases;
	private int totalNewCases;

	@PostConstruct
	@Scheduled(cron = "* * 1 * * *")
	public void fetchData() throws Exception {
		List<Location> newLocations = new ArrayList<Location>();
		int totalCases = 0;
		int totalNewCases = 0;
		URL yahoo = new URL(url);
		URLConnection yc = yahoo.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
		for (CSVRecord record : csvParser.getRecords()) {
			Location location = new Location();
			location.setCountry(record.get("Country/Region"));
			location.setState(record.get("Province/State"));
			
			Integer todayCases = Integer.parseInt(record.get(record.size() - 1));
			location.setTotalCases(todayCases);

			Integer previousDayCases = Integer.parseInt(record.get(record.size() - 2));
			int diff = todayCases - previousDayCases;
			location.setDiffFromPreviousDay(diff);
			totalNewCases += diff;
			totalCases += todayCases;
			
			newLocations.add(location);
		}
		this.locations = newLocations;
		this.totalCases += totalCases;
		this.totalNewCases += totalNewCases;
		in.close();
	}

	public List<Location> getLocations() {
		return locations;
	}

	public int getTotalCases() {
		return totalCases;
	}

	public int getTotalNewCases() {
		return totalNewCases;
	}

}
