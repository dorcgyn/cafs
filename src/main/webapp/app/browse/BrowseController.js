/*
 * ******************************************************************************
 *  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
 *  *****************************************************************************
 */

var app = angular.module('app', []);


app.controller("BrowseCtrl", function(BrowseService, $scope)
{

    $scope.folder = [];
    $scope.browse = function (id) {
        BrowseService.browse(id).then(function (response) {
           $scope.folder = response.data.folder;
        });
    };
    $scope.navigate = function (asset) {
        if (asset.type == "DIR") {
            BrowseService.browse(asset.assetId).then(function (response) {
                $scope.folder = response.data.folder;
            });
        }
        else {
          console.log("file");
        }

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

