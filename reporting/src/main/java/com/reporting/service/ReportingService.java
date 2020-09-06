package com.reporting.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.AnalyticsReportingScopes;
import com.google.api.services.analyticsreporting.v4.model.ColumnHeader;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.DateRangeValues;
import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.api.services.analyticsreporting.v4.model.GetReportsRequest;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import com.google.api.services.analyticsreporting.v4.model.MetricHeaderEntry;
import com.google.api.services.analyticsreporting.v4.model.Report;
import com.google.api.services.analyticsreporting.v4.model.ReportData;
import com.google.api.services.analyticsreporting.v4.model.ReportRequest;
import com.google.api.services.analyticsreporting.v4.model.ReportRow;
import com.google.api.services.analyticsreporting.v4.model.Segment;
import com.reporting.dto.GAVO;

@Service
public class ReportingService {

	private static final String APPLICATION_NAME = "analytics Reporting";
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final String KEY_FILE_LOCATION = "/Users/kim/dev/keys/reportingapi-288102-5f27caa11edd.json";
	private static final String VIEW_ID = "ga:227779435";
	private AnalyticsReporting analyticsReporting = null;

	@PostConstruct
	private void initializeAnalyticsReporting() throws GeneralSecurityException, IOException {

		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(KEY_FILE_LOCATION))
				.createScoped(AnalyticsReportingScopes.all());

		// Construct the Analytics Reporting service object.
		this.analyticsReporting = new AnalyticsReporting.Builder(httpTransport, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build();
	}
	

	public GetReportsResponse getReport() throws IOException {
	    // Create the DateRange object.
	    DateRange dateRange = new DateRange();
	    dateRange.setStartDate("30DaysAgo");
	    dateRange.setEndDate("today");

	    // Create the Metrics object.
	    Metric metric = new Metric()
	        .setExpression("ga:users");

	    Dimension dimension = new Dimension().setName("ga:date");
	    Dimension dimension2 = new Dimension().setName("ga:segment");
	    
	    // Create the ReportRequest object.
	    ReportRequest request = new ReportRequest()
	        .setViewId(VIEW_ID)
	        .setDateRanges(Arrays.asList(dateRange))
	        .setMetrics(Arrays.asList(metric))
	        .setDimensions(Arrays.asList(dimension, dimension2))
	        .setSegments(Arrays.asList(new Segment().setSegmentId("gaid::-1")));

	    ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
	    requests.add(request);

	    // Create the GetReportsRequest object.
	    GetReportsRequest getReport = new GetReportsRequest()
	        .setReportRequests(requests);

	    // Call the batchGet method.
	    GetReportsResponse response = this.analyticsReporting.reports().batchGet(getReport).execute();

	    // Return the response.
	    return response;
	  }

	  /**
	   * Parses and prints the Analytics Reporting API V4 response.
	   *
	   * @param response An Analytics Reporting API V4 response.
	   */
	/*
	  public void printResponse(GetReportsResponse response) {

	    for (Report report: response.getReports()) {
	      ColumnHeader header = report.getColumnHeader();
	      List<String> dimensionHeaders = header.getDimensions();
	      List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
	      List<ReportRow> rows = report.getData().getRows();

	      if (rows == null) {
	         System.out.println("No data found for " + VIEW_ID);
	         return;
	      }

	      for (ReportRow row: rows) {
	        List<String> dimensions = row.getDimensions();
	        List<DateRangeValues> metrics = row.getMetrics();

	        for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
	          System.out.println(dimensionHeaders.get(i) + ": " + dimensions.get(i));
	        }

	        for (int j = 0; j < metrics.size(); j++) {
	          System.out.print("Date Range (" + j + "): ");
	          DateRangeValues values = metrics.get(j);
	          for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
	            System.out.println(metricHeaders.get(k).getName() + ": " + values.getValues().get(k));
	          }
	        }
	      }
	    }
	  }
	  */
	
	
	public GAVO getVO(GetReportsResponse response) {
		GAVO gavo = new GAVO();
		List<String> dimensions = new ArrayList<>();
		List<String> metrics = new ArrayList<>();
		
		
		Report report = response.getReports().get(0);
		String metricHeader = report.getColumnHeader().getMetricHeader().getMetricHeaderEntries().get(0).getName();
		String dimensionHeader = report.getColumnHeader().getDimensions().get(0);
		ReportData data = report.getData();
		
		for (ReportRow row : data.getRows()) {
			dimensions.add(row.getDimensions().get(0));
			metrics.add(row.getMetrics().get(0).getValues().get(0));
		}
		
		gavo.setDimensionHeader(dimensionHeader);
		gavo.setMetricHeader(metricHeader);
		gavo.setDimensions(dimensions);
		gavo.setMetrics(metrics);
		
		return gavo;
	}
}
