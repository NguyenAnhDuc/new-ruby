<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<tiles:insertDefinition name="defaultTemplate">
	<tiles:putAttribute name="body">
		<script type="text/javascript">
			$(document).ready(function() {
				var activeSystemClass = $('.list-group-item.active');

				//something is entered in search form
				$('#btn-search').click( function() {
					var that = $('#system-search');
					// affect all table rows on in systems table
					var tableBody = $('.table-list-search tbody');
					var tableRowsClass = $('.table-list-search tbody tr');
					$('.search-sf').remove();
					tableRowsClass.each( function(i, val) {

						//Lower text for case insensitive
						var rowText = $(val).text().toLowerCase();
						var inputText = $(that).val().toLowerCase();
						if(inputText != '')
						{
							$('.search-query-sf').remove();
							tableBody.prepend('<tr class="search-query-sf"><td colspan="6"><strong>Searching for: "'
							+ $(that).val()
							+ '"</strong></td></tr>');
						}
						else
						{
							$('.search-query-sf').remove();
						}

						if( rowText.indexOf( inputText ) == -1 )
						{
							//hide rows
							tableRowsClass.eq(i).hide();

						}
						else
						{
							$('.search-sf').remove();
							tableRowsClass.eq(i).show();
						}
					});
					//all tr elements are hidden
					if(tableRowsClass.children(':visible').length == 0)
					{
						tableBody.append('<tr class="search-sf"><td class="text-muted" colspan="6">No entries found.</td></tr>');
					}
				});
			});
		</script>

		<div class="container">

			<div class="row">
				<div class="col-md-3">
					<div class="input-group">
							<!-- USE TWITTER TYPEAHEAD JSON WITH API TO SEARCH -->
							<input  onkeyup="if (event.keyCode == 13) document.getElementById('btn-search').click();"  class="form-control" id="system-search" type="text" placeholder="Search for" required>
                    		<span class="input-group-btn">
                        		<button id="btn-search" type="submit" class="btn btn-default">Search</button>
                    		</span>
					</div>
				</div>
			</div>
			<div>
				<div class="col-md-12">
					<table class="table table-list-search">
						<thead>
						<tr>
							<!-- <th>#</th> -->
							<th>Movie</th>
							<th>Cinema</th>
							<th>Type</th>
							<th>Date</th>
						</tr>
						</thead>
						<tbody>
							<%--  <%
                                    int i = 0;
                                %> --%>
						<c:forEach var="ticket" items="${tickets}">
							<%-- <%
                                i++;
                            %> --%>
							<tr>
								<td>${ticket.movie}</td>
								<td>${ticket.cinema}</td>
								<td>${ticket.type}</td>
								<td>${ticket.date.toLocaleString()}</td>
								<td>
									<div class="pull-right action-buttons">

										<a href="deleteTicket?ticketId=${ticket.id}"
										   class="trash"><span
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
	</tiles:putAttribute>
</tiles:insertDefinition>
