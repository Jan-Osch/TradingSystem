var app = angular.module('todoapp', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute'
]);

app.config(function ($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'views/markets.html',
        controller: 'MarketsCtrl'
    }).when('/games', {
        templateUrl: 'views/games.html',
        controller: 'GamesCtrl'
    }).when('/markets', {
        templateUrl: 'views/markets.html',
        controller: 'MarketsCtrl'
    }).when('/stocks/:marketId', {
        templateUrl: 'views/stocks.html',
        controller: 'StocksCtrl'
    }).when('/stock-quotes/:markedUuid/:stockUuid', {
        templateUrl: 'views/stock-quotes.html',
        controller: 'StockQuotesCtrl'
    }).otherwise({
        redirectTo: '/'
    })
});

app.controller('StocksCtrl', function ($scope, $http, $routeParams) {
    $http.get('/api/markets/'+$routeParams.marketId+'/instrument').success(function (data) {
            $scope.instruments= data;
        }).error(function (data, status) {
            console.log('Error ' + data)
        });
});

app.controller('StockQuotesCtrl', function ($scope, $http, $routeParams) {
    $http.get('/api/markets/'+$routeParams.markedUuid+'/instrument/'+$routeParams.stockUuid).success(function (data) {
            $scope.stock= data;
        }).error(function (data, status) {
            console.log('Error ' + data)
        });
     $http.get('/api/markets/'+$routeParams.markedUuid+'/instrument/'+$routeParams.stockUuid+'/record/current').success(function (data) {
            $scope.stockRecord= data;
        }).error(function (data, status) {
            console.log('Error ' + data)
        });
});


app.controller('MarketsCtrl', function ($scope, $http) {
    console.log('markets ctrl');
    $http.get('/api/markets').success(function (data) {
        $scope.markets = data;
    }).error(function (data, status) {
        console.log('Error ' + data)
    });
});
