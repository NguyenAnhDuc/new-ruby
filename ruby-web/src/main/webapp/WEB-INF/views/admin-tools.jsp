<%--
  Created by IntelliJ IDEA.
  User: ducna
  Date: 12/1/2014
  Time: 11:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<script type="text/javascript">
    function restartTVCache(){
      $
              .ajax({
                type : "POST",
                url : "admin-restart-cached-tv",
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
<tiles:insertDefinition name="defaultTemplate">
  <tiles:putAttribute name="body">
      <input id="btn-restartTVCache" type="submit" class="btn btn-primary btn-lg btn-block"
             value="Restart TV Schedule Cached" onclick="restartTVCache()" onkeyup="if (event.keyCode == 13) document.getElementById('btn-restartTVCache').click();" />
  </tiles:putAttribute>
</tiles:insertDefinition>


