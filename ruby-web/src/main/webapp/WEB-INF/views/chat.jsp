<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<tiles:insertDefinition name="defaultTemplate">
	<tiles:putAttribute name="body">


		<link rel="stylesheet" type="text/css"
			  href="resources/css/chat-widget.css" />
		<link rel="stylesheet" type="text/css" href="resources/css/debug.css" />
		<link rel="stylesheet" type="text/css" href="resources/css/main.css" />

		<link rel="stylesheet" type="text/css"
			  href="resources/css/combo_css/style.css" />
		<script type="text/javascript"	src="resources/js/jquery.confirm.min.js"></script>
		<script src="resources/js/jquery.scrollablecombo.js"></script><script>
		var currentQuestion = "";
		$(function() {
			$('#ui_element').scrollablecombo();
		});

		function buildHtmlQuestion(question){
			var htmlResult = "";
			//question
			htmlResult = htmlResult
					.concat("<li class=\"left clearfix\"><span class=\"chat-img pull-left\">");
			htmlResult = htmlResult
					.concat("<img src=\"http://placehold.it/50/55C1E7/fff&text=U\" alt=\"User Avatar\" class=\"img-circle\" /></span>");
			htmlResult = htmlResult
					.concat("<div class=\"chat-body clearfix\">");
			htmlResult = htmlResult.concat("<p>");
			htmlResult = htmlResult.concat(question);
			htmlResult = htmlResult.concat("</p></div></li>");
			return htmlResult;
		}

		function buildHtmlAnswer(answer, text){
			var htmlResult = "";
			//answer
			htmlResult = htmlResult
					.concat("<li class=\"right clearfix\"><span class=\"chat-img pull-right\">");
			htmlResult = htmlResult
					.concat("<img src=\"http://placehold.it/50/FA6F57/fff&text=" + text + "\" alt=\"User Avatar\" class=\"img-circle\" /></span>");
			htmlResult = htmlResult
					.concat("<div class=\"chat-body clearfix\">");
			htmlResult = htmlResult.concat("<p>");
			htmlResult = htmlResult.concat(answer);
			htmlResult = htmlResult.concat("</p></div></li>");
			return htmlResult;
		}

		function searchWeb(question){
			$('#loading').removeClass('hidden');
			$('.report').addClass('hidden');
			$('.panel-body').scrollTop(1E10);
			var oldHeight = $('.panel-body').scrollTop();
			$
					.ajax({
						type : "POST",
						url : "searchWeb",
						contentType : "application/x-www-form-urlencoded;charset=UTF-8",
						data : "question=" + encodeURIComponent(question) ,
						success : function(result) {
							$('#loading').addClass('hidden');
							var htmlResult =  buildHtmlQuestion(result.question).concat(buildHtmlAnswer(result.answer,"WEB"));
							$('#btn-input').val('');
							$('.chat').append(htmlResult);
							$('.panel-body').scrollTop(oldHeight+400);
							//show debug
							$('#panel-debug').removeClass('hidden');
							$('.report').removeClass('hidden');

							// Query Paramaters

							$('#result-final').html(result.answer);
						},
						error : function(result) {
							alert("Error");
						}
					});
		};

		function onChat() {
			//alert("searchtext");
			var question = $('#btn-input').val();
			$('#currentqs').val(question);
			$('#result-intent').html("");
			$('#result-question-type').html("");
			$('#result-paramaters').html("");
			$('#result-time').html("");
			$('#result-final').html("");
			$('#loading').removeClass('hidden');
			$('.report').addClass('hidden');
			$('.panel-body').scrollTop(1E10);
			var oldHeight = $('.panel-body').scrollTop();
			$
					.ajax({
						type : "POST",
						url : "getAnswer",
						contentType : "application/x-www-form-urlencoded;charset=UTF-8",
						data : "question=" + encodeURIComponent(question) + "&confirmWebSearch=" + encodeURIComponent("yes"),
						success : function(result) {
							$('#loading').addClass('hidden');
							var htmlResult = buildHtmlQuestion(result.question).concat(buildHtmlAnswer(result.answer,"ME"));
							$('#btn-input').val('');
							$('.chat').append(htmlResult);
							$('.panel-body').scrollTop(oldHeight+400);
							//show debug
							$('#panel-debug').removeClass('hidden');
							$('.report').removeClass('hidden');

							$('#result-domain').html(result.domain);
							$('#result-intent').html(result.intent);

							// Query Paramaters
							var rubyModifiers = "";
							if (result.rubyModifiers != null){
								if (result.rubyModifiers.movieTitle != null) rubyModifiers +=  "Movie: " + result.rubyModifiers.movieTitle + "</br>";
								if (result.rubyModifiers.cinName != null) rubyModifiers += "Cinema: " +  result.rubyModifiers.cinName + "</br>";
								if (result.rubyModifiers.tvChannel != null) rubyModifiers += "Channel: " + result.rubyModifiers.tvChannel + "</br>";
								if (result.rubyModifiers.tvProTitle != null) rubyModifiers += "TV Program: " + result.rubyModifiers.tvProTitle + "</br>";
								if (result.rubyModifiers.types != null ) {
									rubyModifiers += "Question Type: " + result.rubyModifiers.types + "</br>";
								}
								$('#result-paramaters').html(rubyModifiers);
							}
							var htmlParamater = '', dateExtract = '';
							if (result.timeExtract.beforeDate != null)
								dateExtract += 'Begin Time: '
								+ new Date(result.timeExtract.beforeDate) + "</br>";
							if (result.timeExtract.afterDate != null)
								dateExtract += 'End Time: '
								+ new Date(result.timeExtract.afterDate);
							+"</br>";
							$('#result-time').html(dateExtract);
							$('#result-final').html(result.answer);
						},
						error : function(result) {
							alert("Error");
						}
					});
		};
	</script>
		<style type="text/css">
			body {
				margin-top: 20px;
				background-color: #f0f0f0;
				font-family: "Helvetica Neue", Arial, Helvetica, Geneva, sans-serif;
			}

			a{
				color: blue;
				text-decoration: underline;
			}

			#report{
				text-decoration: none;
			}
			.box {
				border: 15px solid #fff;
				height: 300px;
				width: 500px;
				position: relative;
				padding: 10px 10px 10px 10px;
				-moz-box-shadow: 0px 0px 2px #ccc inset;
				-webkit-box-shadow: 0px 0px 2px #ccc inset;
				box-shadow: 0px 0px 2px #ccc inset;
			}

			.box h3 {
				text-transform: uppercase;
				color: #ccc;
				text-shadow: 0 1px 0 #fff;
			}

			.spinner {
				margin: 100px auto;
				width: 50px;
				height: 30px;
				text-align: center;
				font-size: 10px;
			}

			.spinner > div {
				background-color: #333;
				height: 100%;
				width: 6px;
				display: inline-block;

				-webkit-animation: stretchdelay 1.2s infinite ease-in-out;
				animation: stretchdelay 1.2s infinite ease-in-out;
			}

			.spinner .rect2 {
				-webkit-animation-delay: -1.1s;
				animation-delay: -1.1s;
			}

			.spinner .rect3 {
				-webkit-animation-delay: -1.0s;
				animation-delay: -1.0s;
			}

			.spinner .rect4 {
				-webkit-animation-delay: -0.9s;
				animation-delay: -0.9s;
			}

			.spinner .rect5 {
				-webkit-animation-delay: -0.8s;
				animation-delay: -0.8s;
			}

			@-webkit-keyframes stretchdelay {
				0%, 40%, 100% { -webkit-transform: scaleY(0.4) }
				20% { -webkit-transform: scaleY(1.0) }
			}

			@keyframes stretchdelay {
				0%, 40%, 100% {
					transform: scaleY(0.4);
					-webkit-transform: scaleY(0.4);
				}  20% {
					   transform: scaleY(1.0);
					   -webkit-transform: scaleY(1.0);
				   }
			}
		</style>
		<input type="text" class="hidden" id="currentqs"/>
		<div class="row">
			<div class="col-md-5 col-lg-5">
				<div class="panel panel-primary">
					<div class="panel-heading"></div>
					<div class="panel-body">
						<ul class="chat">
							<li class="left clearfix"><span class="chat-img pull-left">
										<img src="http://placehold.it/50/55C1E7/fff&text=U"
											 alt="User Avatar" class="img-circle" />
								</span>
								<div class="chat-body clearfix">
									<!-- <div class="header"> -->
									<!--  <strong class="primary-font"></strong> <small class="pull-right text-muted">
                                 <span class="glyphicon glyphicon-time"></span>12 mins ago</small>  -->
									<!-- </div> -->
									<p>Xin chào, bạn là ai?</p>
								</div></li>
							<li class="right clearfix"><span
									class="chat-img pull-right"> <img
									src="http://placehold.it/50/FA6F57/fff&text=ME"
									alt="User Avatar" class="img-circle" />
								</span>
								<div class="chat-body clearfix">
									<div class="header">
										<!-- <small class=" text-muted"><span class="glyphicon glyphicon-time"></span>13 mins ago</small>
                                <strong class="pull-right primary-font">Bhaumik Patel</strong> -->
									</div>
									<p>Mọi người gọi tôi là ruby.</p>
								</div></li>
							<li class="left clearfix"><span class="chat-img pull-left">
										<img src="http://placehold.it/50/55C1E7/fff&text=U"
											 alt="User Avatar" class="img-circle" />
								</span>
								<div class="chat-body clearfix">
									<div class="header">
										<!-- <strong class="primary-font">Jack Sparrow</strong> <small class="pull-right text-muted">
                                    <span class="glyphicon glyphicon-time"></span>14 mins ago</small> -->
									</div>
									<p>Bạn có thể giúp gì được cho tôi?</p>
								</div></li>
							<li class="right clearfix"><span
									class="chat-img pull-right"> <img
									src="http://placehold.it/50/FA6F57/fff&text=ME"
									alt="User Avatar" class="img-circle" />
								</span>
								<div class="chat-body clearfix">
									<div class="header">
										<!-- <small class=" text-muted"><span class="glyphicon glyphicon-time"></span>15 mins ago</small>
                                <strong class="pull-right primary-font">Bhaumik Patel</strong> -->
									</div>
									<p>Tôi có thể tra cứu phim, lịch chiếu tại rạp, lịch chiếu các chương trình tivi.</p>
								</div></li>
						</ul>
					</div>
					<div class="panel-footer">
						<div class="input-group">
							<input id="btn-input" type="text" class="form-control input-sm"
								   placeholder="Hỏi tôi..."
								   onkeyup="if (event.keyCode == 13) document.getElementById('btn-chat').click();" />
								<span class="input-group-btn">
									<button class="btn btn-warning btn-sm" id="btn-chat"
											onclick="onChat()">Gửi</button>
								</span>
						</div>
					</div>

				</div>
				<div class="report  hidden" style="float: right;">
					<!-- <button id = "report" " class="btn btn-danger simpleConfirm" onclick="report()">Report Wrong Answer</button> -->
					<a id = "report" href="#" class="btn btn-danger simpleConfirm" onclick="report()">Report Wrong Answer</a>
				</div>
			</div>
			<div class="col-md-6 col-md-offset-1 col-lg-6 col-lg-offset-1 "
				 id="panel-debug">
				<div class="timeline-centered">


					<article class="timeline-entry">
						<div class="timeline-entry-inner">
							<div class="timeline-icon bg-violet">
								<i class="entypo-feather"></i>
							</div>
							<div class="timeline-label">
								<h2>Domain Classify</h2>
								<p id="result-domain">Domain of the question</p>
							</div>
						</div>
					</article>

					<article class="timeline-entry">
						<div class="timeline-entry-inner">
							<div class="timeline-icon bg-coral">
								<i class="entypo-suitcase"></i>
							</div>
							<div class="timeline-label">
								<h2>Intent</h2>
								<p id="result-intent">Intent of the question</p>
							</div>
						</div>
					</article>

					<article class="timeline-entry">
						<div id="panel-process-question" class="timeline-entry-inner">
							<div class="timeline-icon bg-info">
								<i class="entypo-suitcase"></i>
							</div>

							<div class="timeline-label">
								<h2>Query Paramater</h2>
								<p id="result-paramaters">Paramaters to query DB</p>
							</div>
						</div>
					</article>

					<article class="timeline-entry">
						<div id="panel-result-final" class="timeline-entry-inner">
							<div class="timeline-icon  bg-secondary">
								<i class="entypo-suitcase"></i>
							</div>
							<div class="timeline-label">
								<h2>Time Extractor</h2>
								<p id="result-time">Time Conditions</p>
							</div>
						</div>
					</article>

					<article class="timeline-entry">
						<div id="panel-result-final" class="timeline-entry-inner">
							<div class="timeline-icon bg-success">
								<i class="entypo-suitcase"></i>
							</div>
							<div class="timeline-label">
								<h2>Result</h2>
								<p id="result-final">Result</p>
							</div>
						</div>
					</article>

					<article class="timeline-entry begin">
						<div class="timeline-entry-inner">
							<div class="timeline-icon"
								 style="-webkit-transform: rotate(-90deg); -moz-transform: rotate(-90deg);">
								<i class="entypo-flight"></i> +
							</div>
						</div>
					</article>

				</div>

			</div>
			<div class="spinner hidden" id="loading">
				<div class="rect1"></div>
				<div class="rect2"></div>
				<div class="rect3"></div>
				<div class="rect4"></div>
				<div class="rect5"></div>
			</div>
		</div>
		<script type="text/javascript">
			$(".simpleConfirm").confirm();
		</script>

		<!-- <div class="row">
		<div id="disqus_thread"></div>
		<script type="text/javascript">
		/* * * CONFIGURATION VARIABLES: EDIT BEFORE PASTING INTO YOUR WEBPAGE * * */
		var disqus_shortname = 'ftiqa'; // required: replace example with your forum shortname

		/* * * DON'T EDIT BELOW THIS LINE * * */
		(function() {
		var dsq = document.createElement('script');
		dsq.type = 'text/javascript';
		dsq.async = true;
		dsq.src = '//' + disqus_shortname + '.disqus.com/embed.js';
		(document.getElementsByTagName('head')[0] || document
		.getElementsByTagName('body')[0]).appendChild(dsq);
		})();
		</script>
		<noscript>
		Please enable JavaScript to view the <a
		href="http://disqus.com/?ref_noscript">comments powered by
		Disqus.</a>
		</noscript>
		<a href="http://disqus.com" class="dsq-brlink">comments powered
		by <span class="logo-disqus">Disqus</span>
		</a>

		</div> -->
	</tiles:putAttribute>
</tiles:insertDefinition>