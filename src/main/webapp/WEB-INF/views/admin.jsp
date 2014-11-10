<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<tiles:insertDefinition name="defaultTemplate">
	<tiles:putAttribute name="body">
		<form action="admin-crawl-moveek">
			<input type="submit" class="btn btn-primary btn-lg btn-block"
				id="btnSubmit" value="Crawl schedule from moveek.com" />
		</form>
		<form action="admin-crawl-phim-chieu-rap">
			<input type="submit" class="btn btn-primary btn-lg btn-block"
				id="btnSubmit" value="Crawl schedule from phimchieurap.com" />
		</form>
		<form action="admin-crawl-mytv">
			<input type="submit" class="btn btn-primary btn-lg btn-block"
				id="btnSubmit" value="Crawl schedule from mytv.com.vn" />
		</form>
		
		<form action="admin-crawl-manual">
			<input type="submit" class="btn btn-primary btn-lg btn-block"
				id="btnSubmit" value="Crawl Manually" />
		</form>
		<form action="admin-show-tickets">
			<input type="submit" class="btn btn-primary btn-lg btn-block"
				id="btnSubmit" value="Show Tickets Today" />
		</form>
		<form action="admin-show-tvprograms">
			<input type="submit" class="btn btn-primary btn-lg btn-block"
				id="btnSubmit" value="Show TVPrograms Today" />
		</form>
		
		</form>
		<form action="admin-show-name-mapper">
			<input type="submit" class="btn btn-primary btn-lg btn-block"
				id="btnSubmit" value="Show name mapper" />
		</form>
		
		<form action="admin-add-cinema">
			<input type="submit" class="btn btn-primary btn-lg btn-block"
				id="btnSubmit" value="Add Cinema Information" />
		</form>
	
		
	</tiles:putAttribute>
</tiles:insertDefinition>

