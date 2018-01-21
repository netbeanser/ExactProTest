<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>ExactProTest</title>   
    <link rel="stylesheet" src='<c:url value="/pages/css/style.css" />' >      
  </head>
  <style>
table, th, td {
 border: 1px solid black;  
}      
.Login .loginForm {
    width: 300px;
    background-color: #eeeeee;
    border-collapse: collapse;
}     
.v-scroll {
  display: block;
  overflow-y: auto;
  height: 300px;
  width: 215px;
}

.InstrumentsTable {
  width: 200px;
  border-collapse: collapse;
}

.PriceTable-f {
  width: 300px;
  border-collapse: collapse; 
  background-color: lightblue; 
}

.msg {
  color: red;
}

.selected {
  background-color: #22ee22;
}

.no-left-border {  
  border-left: none;
}

.no-right-border {  
  border-right: none;
}

.ordinal {
  background-color: #f2f2f2 
}
    
  </style>
  <body>      
      <h2>Simultaneous WebSocketServer and Fetch API joint work</h2>
    <div id="app">
    </div>  
        <script  src='<c:url value="/pages/js/bundle.js" />' ></script>
  </body>
</html>
