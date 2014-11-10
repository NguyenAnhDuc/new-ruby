<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<tiles:insertDefinition name="defaultTemplate">
	<tiles:putAttribute name="body">
		<script type="text/javascript"	src="resources/js/jquery.confirm.min.js">
			
		</script>
		<style>
/* body {
	padding-top: 50px;
	padding-bottom: 20px;
}
 */
.row {
	/* margin-top: 40px; */
	padding: 0 10px;
}

.clickable {
	cursor: pointer;
}

.panel-heading div {
	margin-top: -18px;
	font-size: 15px;
}

.panel-heading div span {
	margin-left: 5px;
}

.panel-body {
	display: none;
}
</style>


		<script type="text/javascript">
			/**
			 *   I don't recommend using this plugin on large tables, I just wrote it to make the demo useable. It will work fine for smaller tables 
			 *   but will likely encounter performance issues on larger tables.
			 *
			 *		<input type="text" class="form-control" id="dev-table-filter" data-action="filter" data-filters="#dev-table" placeholder="Filter Developers" />
			 *		$(input-element).filterTable()
			 *		
			 *	The important attributes are 'data-action="filter"' and 'data-filters="#table-selector"'
			 */
			(function() {
				'use strict';
				var $ = jQuery;
				$.fn
						.extend({
							filterTable : function() {
								return this
										.each(function() {
											$(this)
													.on(
															'keyup',
															function(e) {
																$(
																		'.filterTable_no_results')
																		.remove();
																var $this = $(this), search = $this
																		.val()
																		.toLowerCase(), target = $this
																		.attr('data-filters'), $target = $(target), $rows = $target
																		.find('tbody tr');
																if (search == '') {
																	$rows
																			.show();
																} else {
																	$rows
																			.each(function() {
																				var $this = $(this);
																				$this
																						.text()
																						.toLowerCase()
																						.indexOf(
																								search) === -1 ? $this
																						.hide()
																						: $this
																								.show();
																			})
																	if ($target
																			.find(
																					'tbody tr:visible')
																			.size() === 0) {
																		var col_count = $target
																				.find(
																						'tr')
																				.first()
																				.find(
																						'td')
																				.size();
																		var no_results = $('<tr class="filterTable_no_results"><td colspan="'+col_count+'">No results found</td></tr>')
																		$target
																				.find(
																						'tbody')
																				.append(
																						no_results);
																	}
																}
															});
										});
							}
						});
				$('[data-action="filter"]').filterTable();
			})(jQuery);

			$(function() {
				// attach table filter plugin to inputs
				$('[data-action="filter"]').filterTable();

				$('.container').on(
						'click',
						'.panel-heading span.filter',
						function(e) {
							var $this = $(this), $panel = $this
									.parents('.panel');

							$panel.find('.panel-body').slideToggle();
							if ($this.css('display') != 'none') {
								$panel.find('.panel-body input').focus();
							}
						});
				$('[data-toggle="tooltip"]').tooltip();
			})
		</script>

		</head>


		<body>


			<div class="row">
				<div class="col-md-2 col-md-offset-5"></div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<h4>
						Click the filter icon <small>(<i
							class="glyphicon glyphicon-filter"></i>)
						</small> to filter
					</h4>
				</div>
			</div>
			<div class="row">
				<div class="col-md-10">
					<div class="panel panel-primary">
						<div class="panel-heading">
							<h3 class="panel-title">Today TV</h3>
							<div class="pull-right">
								<span class="clickable filter" data-toggle="tooltip"
									title="Toggle table filter" data-container="body"> <i
									class="glyphicon glyphicon-filter"></i>
								</span>
							</div>
						</div>
						<div class="panel-body">
							<input type="text" class="form-control" id="dev-table-filter"
								data-action="filter" data-filters="#dev-table"
								placeholder="Filter Bots" />
						</div>
						<table class="table table-hover" id="dev-table">
							<thead>
								<tr>
									<!-- <th>#</th> -->
									<th>Cinema</th>
									<th>Address</th>
									<th>Phone</th>
								</tr>
							</thead>
							<tbody>
							   <%--  <%
									int i = 0;
								%> --%>
								<c:forEach var="cinema" items="${cinemas}">
									<%-- <%
										i++;
									%> --%>
									<tr>
									<td>${cinema.name}</td>
									<td>${cinema.address}</td>
									<td>${cinema.mobile}</td>
										<td>
											<div class="pull-right action-buttons">

												<a href="deleteTicket?ticketId=${ticket.id}"
													class="simpleConfirm trash"><span
													class="glyphicon glyphicon-trash"></span></a>
											</div>
										</td>
									</tr>
								</c:forEach> 
							</tbody>
						</table>
					</div>
				</div>

			</div>
			<div class="row">
				<div class="col-md-6">
					<!-- <a class="btn btn-info" href="crawl">Add Data</a> -->
				</div>
			</div>
			<script src="resources/js/run_prettify.js"></script>
			<script type="text/javascript">
				$(".simpleConfirm").confirm();
			</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
