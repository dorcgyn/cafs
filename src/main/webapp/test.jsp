<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<!--
  ~ /*******************************************************************************
  ~  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
  ~  ******************************************************************************/
  -->

<html>
<head lang="en">
    <meta charset="UTF-8">
    <title></title>
</head>
<body>
<% ArrayList<String> paths = (ArrayList)request.getAttribute("paths");
   for (String path : paths) {
       %><p><%= path %><p><%
   }
%>
</body>
</html>