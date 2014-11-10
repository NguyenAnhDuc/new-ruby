<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<tiles:insertDefinition name="defaultTemplate">
	<tiles:putAttribute name="body">
		<script type="text/javascript">
			function crawl() {
				//alert("searchtext");
				var numday = $('input:radio[name=inlineRadioOptions]:checked')
						.val();
				$
						.ajax({
							type : "POST",
							url : "/rubyweb/crawl-mytv",
							contentType : "application/x-www-form-urlencoded;charset=UTF-8",
							success : function(result) {
								if (result.status === "success")
									alert("DONE!");
								else
									alert("Something went wrong");
							},
							error : function(result) {
								alert("Error");
							}
						});
			};
		</script>
		<div class="container">
			
			<button type="button" class="btn btn-primary btn-lg btn-block"
				id="btnSubmit" onclick="crawl()">Crawl schedule from
				mytv.com.vn</button>
		</div>
	</tiles:putAttribute>
</tiles:insertDefinition>

