/*
 * ******************************************************************************
 *  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
 *  *****************************************************************************
 */

var app = angular.module('app', []);


app.controller("BrowseCtrl", function(BrowseService, $scope, $window)
{

    $scope.folder = [];
    $window.onload = browseFunc('');
    $scope.browse = function (id) {
        browseFunc(id);
    };
    $scope.navigate = function (asset) {
        if (asset.type == "DIR") {
            browseFunc(asset.assetId);
        }
        else {
            // read API
          console.log("file");
        }
    };
    function browseFunc(id) {
        BrowseService.browse(id).then(function (response) {
            $scope.folder = response.data.folder;
        });
    };
});


app.service('BrowseService', function($q, $http){
   this.browse = function (id) {
       var deferred = $q.defer();
       var promise;
       var url = 'api/v1/browse/';
       if (id != '') {
           url += id;
       }
       promise = $http.get(url,{});
       deferred.resolve(promise);
       return deferred.promise;
   };
});

