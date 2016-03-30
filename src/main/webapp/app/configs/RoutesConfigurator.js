/*
 * ******************************************************************************
 *  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
 *  *****************************************************************************
 */
angular.module('app', ['ui.router']).run(function($rootScope, $state){
    $rootScope.$state = $state;
});
angular
    .module('app', [
        'ui.router'
    ])
    .config(['$urlRouterProvider', '$stateProvider', function($urlRouterProvider, $stateProvider) {
        $urlRouterProvider.otherwise('/');
        $stateProvider
            .state('base', {
                url: '/',
                templateUrl: 'app/browse/browse.html',
                controller: 'BrowseCtrl'
            })
            .state('base.browse', {
                url: 'browse/:assetId',
                templateUrl: 'app/browse/browse.html',
                controller: 'BrowseCtrl'
            })

    }]);