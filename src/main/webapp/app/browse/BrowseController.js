/*
 * ******************************************************************************
 *  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
 *  *****************************************************************************
 */



angular.module('app').controller("BrowseCtrl", ['$scope', 'BrowseService', '$state', '$stateParams', function($scope, BrowseService, $state, $stateParams) {

    $scope.folder = [];
    $scope.currentFolder = '';
    $scope.fileContent = '';
    $scope.createFolderName = undefined;
    $scope.browse = function (id) {
        $scope.fileContent = '';
        browseFunc(id);
    };
    BrowseService.browse('').then(function(response) {

        $scope.folder = response.data.folder;
    });

    $scope.navigate = function (asset) {
        $scope.fileContent = '';
        if (asset.type == "DIR") {
            $scope.currentFolder = asset.assetId;
            browseFunc(asset.assetId);
        }
        else {
            BrowseService.read(asset.assetId).then( function(response) {
                $scope.fileContent = response.data.content;
            });
        }
    };
    $scope.createFolder = function() {
        $('#modalCreateFolder').modal('hide')
        BrowseService.createFolder($scope.currentFolder, $scope.createFolderName).then(
            function() {
                $scope.createFolderName = undefined;
                browseFunc($scope.currentFolder);
            }
        );
    }

    function browseFunc(id) {
        BrowseService.browse(id).then(function (response) {
            $scope.folder = response.data.folder;
        });
    };
}]);


angular.module('app').service('BrowseService', ['$q','$http', function($q, $http){
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

    this.createFolder = function (parentId, dirName) {
        var deferred = $q.defer();
        var promise;
        var url = 'api/v1/folder/';
        if (parentId == '') {
            parentId = undefined;
        }

        promise = $http.post(url,{parentId:parentId, name:dirName});
        deferred.resolve(promise);
        return deferred.promise;
    }

    this.read = function (id) {
        var deferred = $q.defer();
        var promise;
        var url = 'api/v1/read/' + id;
        promise = $http.get(url,{});
        deferred.resolve(promise);
        return deferred.promise;
    }


}]);

