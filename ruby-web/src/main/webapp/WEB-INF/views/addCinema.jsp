<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<tiles:insertDefinition name="defaultTemplate">
	<tiles:putAttribute name="body">

<c:if test="${status=='failed'}">
	<div class="alert alert-danger" role="alert">Oh snap! Something
		went wrong!</div>
</c:if>

<c:if test="${status=='success'}">
	<div class="alert alert-success" role="alert">Cinema info added!</div>
</c:if>
<form action="/rubyweb/addCinema" method="post" role="form"
	data-toggle="validator" class="form-horizontal" accept-charset="UTF-8">
	<fieldset>
      <legend>Add Cinema Information</legend>
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
		<label class="col-sm-2 control-label"> Địa chỉ</label>
		<div class="col-sm-10">
			<input type="text" class="form-control " id="cin_address"
				name="cin_address" placeholder="Địa chỉ" required>
		</div>
	</div>
	<div class="form-group ">
		<label for="inputPassword" class="col-sm-2 control-label">Điện thoại</label>
		<div class="col-sm-10">
			<input type="text" class="form-control " id="mobile"
				name="mobile" placeholder="Điện thoại liên lạc" required>
		</div>
	</div>
	
	

	<button type="submit" class="btn btn-primary btn-lg btn-block "
		id="btnSubmit" style="margin-top: 10px">Thêm thông tin rạp</button>
	</fieldset>
</form>

	</tiles:putAttribute>
</tiles:insertDefinition>
