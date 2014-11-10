<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Ruby Project</title>
	
	<!-- Bootstrap -->
	<!-- Latest compiled and minified CSS -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
	
	<!-- Optional theme -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
	
	
	<!-- Jquery js -->
	<script src="resources/js/jquery-1.10.1.min.js"></script>   
	<!-- <script src="http://code.jquery.com/jquery-1.9.1.js"></script> -->
	
	<!-- Menue canvas -->
	<link rel="stylesheet" type="text/css" href="resources/css/normalize.css" />
		<link rel="stylesheet" type="text/css" href="resources/css/demo.css" />
		<link rel="stylesheet" type="text/css" href="resources/fonts/font-awesome-4.2.0/css/font-awesome.min.css" />
		<link rel="stylesheet" type="text/css" href="resources/css/menu_bubble.css" /> 
		<script src="resources/js/snap.svg-min.js"></script>
		<!--[if IE]>
  		<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
		<![endif]-->
	
	
</head>
<body>
	<div class="page">
		<tiles:insertAttribute name="header" />
		
		<div class="main">
					
		
			<div class="menu-wrap">
				<nav class="menu">
					<div class="icon-list">
						<a href="/rubyweb"><i class="fa fa-fw fa-home"></i><span>Home</span></a>
						<a href="/rubyweb/admin"><i class="fa fa-fw fa-star-o"></i><span>Admin</span></a>
						<a href="/rubyweb/admin-show-cinemas"><i class="fa fa-fw fa-bank"></i><span>Cinemas</span></a>
						<a href="/rubyweb/admin-show-tickets?day=0"><i class="fa fa-fw fa-list"></i><span>Tickets Today</span></a>
						<a href="/rubyweb/admin-show-tvprograms?day=0"><i class="fa fa-fw fa-list-alt"></i><span>TV Today</span></a>
						<a href="/rubyweb/admin-show-logs?num=0"><i class="fa fa-fw fa-list-ul"></i><span>Logs</span></a>
						<a href="/rubyweb/comments"><i class="fa fa-fw fa-comment-o"></i><span>Comments</span></a>
						<a href="/rubyweb/admin-analytic"><i class="fa fa-fw fa-bar-chart-o"></i><span>Analytics</span></a>
						<!-- <a href="#"><i class="fa fa-fw fa-newspaper-o"></i><span>Reading List</span></a> -->
						
					</div>
				</nav>
				<button class="close-button" id="close-button">Close Menu</button>
				<div class="morph-shape" id="morph-shape" data-morph-open="M-7.312,0H15c0,0,66,113.339,66,399.5C81,664.006,15,800,15,800H-7.312V0z;M-7.312,0H100c0,0,0,113.839,0,400c0,264.506,0,400,0,400H-7.312V0z">
					<svg xmlns="http://www.w3.org/2000/svg" width="100%" height="100%" viewBox="0 0 100 800" preserveAspectRatio="none">
						<path d="M-7.312,0H0c0,0,0,113.839,0,400c0,264.506,0,400,0,400h-7.312V0z"/>
					</svg>
				</div>
			</div>
			<button class="menu-button" id="open-button">Open Menu</button>
			<div class="content-wrap">
				<div class="content">
					<header class="codrops-header">
						<img src="resources/images/fpt_logo.png" class="logo" height="80" /> 	
						
					</header>
					<!-- Related demos -->
					<div class="container">
						<tiles:insertAttribute name="body" />
					</div>
				</div>
			</div>
		<script src="resources/js/classie.js"></script>
		<script src="resources/js/main4.js"></script>
		<script src="resources/js/bootstrap.min.js"></script>
		<!-- Latest compiled and minified JavaScript -->
		<!-- <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script> -->
			<%-- <tiles:insertAttribute name="menu" />
			<tiles:insertAttribute name="body" /> --%>
		</div>
		<%-- <tiles:insertAttribute name="footer" /> --%> 
	</div>
</body>
</html>