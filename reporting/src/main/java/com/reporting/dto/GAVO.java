package com.reporting.dto;

import java.util.List;

import lombok.Data;

@Data
public class GAVO {

	private String dimensionHeader;
	private String metricHeader;
	private List<String> dimensions;
	private List<String> metrics;
}
