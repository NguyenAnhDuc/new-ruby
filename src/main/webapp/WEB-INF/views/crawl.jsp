<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="defaultTemplate">
	<tiles:putAttribute name="body">
<script type="text/javascript">
	function crawlManual() {
		var cin_name = $("#cin_name").val();
		var mov_title = $("#mov_title").val();
		var time = $("#schedule").val();

		$.ajax({
			type : "POST",
			url : "/rubyweb/crawlManual",
			contentType : "application/x-www-form-urlencoded;charset=UTF-8",
			data : "cin_name=" + encodeURIComponent(cin_name) + "&mov_title="
					+ encodeURIComponent(mov_title) + "&time="
					+ encodeURIComponent(time),
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
<c:if test="${status=='failed'}">
	<div class="alert alert-danger" role="alert">Oh snap! Something
		went wrong!</div>
</c:if>

<c:if test="${status=='success'}">
	<div class="alert alert-success" role="alert">Schedule added!</div>
</c:if>
<form action="/rubyweb/crawlManual" method="post" role="form"
	data-toggle="validator" class="form-horizontal" accept-charset="UTF-8">
	<fieldset>
      <legend>Add Movie</legend>
	<!-- <div class="row" style="margin-bottom: 10px">
		<select class="form-control " id="cin_name" name="cin_name">
			<option>CGV Vincom City Towers</option>
			<option>CGV MIPEC Tower</option>
			<option>Lotte Cinema Landmark</option>
			<option>Lotte Cinema Hà Đông</option>
			<option>Platinum Vincom Royal City</option>
			<option>Platinum Cineplex Times City</option>
			<option>Platinum Vincom Long Biên</option>
			<option>Platinum Garden Mall</option>
			<option>TT Chiếu Phim Quốc Gia</option>
			<option>Tháng 8 Cinema</option>
			<option>Kim Đồng Cinema</option>
		</select>
	</div> -->
	<div class="form-group" style="margin-bottom: 10px">
	<label class="col-sm-2 control-label" for="expiry-month">Rạp</label>
		<div class="row">
			<div class="col-sm-9">
			<select class="form-control " id="cin_name" name="cin_name">
			<option>CGV Vincom City Towers</option>
			<option>CGV MIPEC Tower</option>
			<option>Lotte Cinema Landmark</option>
			<option>Lotte Cinema Hà Đông</option>
			<option>Platinum Vincom Royal City</option>
			<option>Platinum Cineplex Times City</option>
			<option>Platinum Vincom Long Biên</option>
			<option>Platinum Garden Mall</option>
			<option>TT Chiếu Phim Quốc Gia</option>
			<option>Tháng 8 Cinema</option>
			<option>Kim Đồng Cinema</option>
			</select>
			</div>
		</div>
		
	</div>
	
	<div class="form-group ">
		<label class="col-sm-2 control-label">Tên phim</label>
		<div class="col-sm-10">
			<input type="text" class="form-control " id="mov_title"
				name="mov_title" placeholder="Tên phim" required>
		</div>
	</div>
	<div class="form-group ">
		<label for="inputPassword" class="col-sm-2 control-label">Giờ
			chiếu</label>
		<div class="col-sm-10">
			<textarea rows="3" class="form-control" id="schedule" name="time"
				placeholder="Giờ chiếu" required>
					</textarea>
		</div>
	</div>
	 <div class="form-group">
        <label class="col-sm-2 control-label">Loại</label>
		<div class="col-sm-10">
				<label class="radio-inline"> <input type="radio"
				name="inlineRadioOptions" id="inlineRadio1" value="2D" checked="checked">
				2D
			</label> <label class="radio-inline"> <input type="radio"
				name="inlineRadioOptions" id="inlineRadio2" value="3D">
				3D
			</label>
		</div>	
	</div>
	 <div class="form-group">
        <label class="col-sm-2 control-label">Ngày</label>
        <div class="col-sm-10">
          <input type="text" class="form-control"  id="datepicker" name="date" placeholder="Date">
        </div>
      </div>
	

	<button type="submit" class="btn btn-primary btn-lg btn-block "
		id="btnSubmit" style="margin-top: 10px">Thêm lịch chiếu</button>
	</fieldset>
</form>
<link rel="stylesheet" type="text/css" href="resources/css/jquery.datetimepicker.css"/ >
<script src="resources/js/jquery.datetimepicker.js"></script>
<script type="text/javascript">
	$('#datepicker').datetimepicker(
			{
				lang : 'de',
				i18n : {
					de : {
						months : [ 'Januar', 'Februar', 'März', 'April', 'Mai',
								'Juni', 'Juli', 'August', 'September',
								'Oktober', 'November', 'Dezember', ],
						dayOfWeek : [ "So.", "Mo", "Di", "Mi", "Do", "Fr",
								"Sa.", ]
					}
				},
				timepicker : false,
				format : 'd.m.Y'
			});
</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
