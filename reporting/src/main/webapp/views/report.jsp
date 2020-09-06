<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Report</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>
<script src="https://code.highcharts.com/modules/export-data.js"></script>
<script src="https://code.highcharts.com/modules/accessibility.js"></script>
<style type="text/css">
 #container {
 	width: 800px;
 }

</style>
</head>
<body>
    <div id="container"></div>
    <script type="text/javascript">
    Highcharts.chart('container', {
        chart: {
            type: 'line'
        },
        title: {
            text: '사용자'
        },
        xAxis: {
            //categories: ${response.dimensions},
            type: 'datetime',
            /* labels: {
                format: '{value:%m-%d}',
            } */
            dateTimeLabelFormats: {
                day: '%m.%e'
            }
        },
        yAxis: {
            title: {
                text: '사용자 수(명)'
            }
        },
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true
                },
                enableMouseTracking: true
            }
        },
        series: [{
            name: '사용자',
            data: ${response.metrics},
            //data: [1,2,3,4,5,4,3,2,1,2,3,2,3,4,5,4,3,2,6,2,1,2,3,4,5,4,3,2,1,2,1,2,3,4,5,4,3,2,1,2,6,2,3,4,5,4,3,2,1,2,1,2,3,4,5,4,3,2,1,2,1,2,3,4,5,4,3,2,1,2,1,2,3,4,5,4,3,2,1,2,1,2,3,4,5,4,3,2,1,2,1,2,3,4,5,4,3,2,1,2,],
            pointStart: Date.UTC(2019, 11, 25),
            pointInterval: 24 * 36e5 // one day
        }]
    });
    </script>
</body>
</html>