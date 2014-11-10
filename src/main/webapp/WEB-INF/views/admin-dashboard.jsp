<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<tiles:insertDefinition name="defaultTemplate">
	<tiles:putAttribute name="body">
		<title>Analytic</title>

		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>

		<script type="text/javascript">
$(function () {
    $('#line-chart').highcharts({
        title: {
            text: 'Requests Per Day',
            x: -20 //center
        },
        subtitle: {
            text: 'Source: FPT',
            x: -20
        },
        xAxis: {
            categories: JSON.parse('${data}')
           //  categories: '${data}'
           /*  categories:["Jan","Fe"] */
        },
        yAxis: {
            title: {
                text: 'Number Of Request'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            valueSuffix: ' request(s)'
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 0
        },
        /* series: [{
            name: 'Tokyo',
            data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
        }] */
        series: JSON.parse('${jsonS}')
    });
    
    // Pie Chart
    var dataPie =  JSON.parse('${jsonPie}');
    $('#pie-chart').highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        title: {
            text: 'Intent Of Question'
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: false
                },
                showInLegend: true
            }
        },
        series: [{
            type: 'pie',
            name: 'Percent',
            data : JSON.parse('${jsonPie}')
        }]
    });
    
    
});
		</script>
<script src="resources/js/highcharts.js"></script>
<div id="line-chart" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
<div id="pie-chart" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
	</tiles:putAttribute>
</tiles:insertDefinition>