<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<!--
  ~ /*******************************************************************************
  ~  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
  ~  ******************************************************************************/
  -->

<html ng-app="app">
<head lang="en">
    <meta charset="UTF-8">
    <title></title>
    <script src="./resources/lib/angular-1.5.3/angular.min.js"></script>
    <link href="./resources/css/ui.css" rel="stylesheet" />
    <script src="./app/browse/BrowseController.js" type="text/javascript"></script>
</head>
<body>

<div ng-controller="BrowseCtrl">
    <button ng-click="browse('')">BROWSE ROOT</button>
    <div class="row">
        <div class="col-3 menu">
            <ul>
                <li ng-repeat="asset in folder">
                    <div ng-click="navigate(asset)">
                        {{asset.name}}
                    </div>
                </li>
            </ul>
        </div>
    </div>

</div>
</body>
</html>