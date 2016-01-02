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
    }).when('/login', {
        templateUrl: 'views/login.html',
        controller: 'LoginCtrl'
    }).when('/register', {
        templateUrl: 'views/register.html',
        controller: 'RegisterCtrl'
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


app.factory('userService', function ($cookieStore) {
    return {
        user: undefined,
        logout: function () {
            $cookieStore.remove("user_uuid");
            $cookieStore.remove("remembered_user");
        },
        isLogged: function () {
            var userName = $cookieStore.get("remembered_user");
            if (userName !== undefined) {
                this.user = userName;
                return true;
            }
            return false;
        },
        rememberUser: function (userName) {
            $cookieStore.put("remembered_user", userName);
        }
    };
});

app.run(function ($rootScope, userService) {
    $rootScope.userService = userService;
});


app.controller('StocksCtrl', function ($scope, $http, $routeParams) {
    $http.get('/api/markets/' + $routeParams.marketId + '/instrument').success(function (data) {
        $scope.instruments = data;
    }).error(function (data, status) {
        console.log('Error ' + data)
    });
});

app.controller('RegisterCtrl', function ($scope, $http, $location) {
    $scope.attemptRegister = function () {
        $scope.dataLoading = true;
        $http.post('/api/register', {login: $scope.login, password: $scope.password}).success(function (data) {
            if (data == '\"OK\"') {
                $rootScope.userService.rememberUser($scope.login);
                $location.path('/');
            } else {
                $scope.result = data;
            }
        }).error(function (data, status) {
            console.log('Error ' + data)
        });
        $scope.dataLoading = false;
    };

    $scope.confirmDoesNotMatch = function () {
        return $scope.confirm !== "" && $scope.confirm !== $scope.password;
    }
});

app.controller('LoginCtrl', function ($scope, $http, $location, $rootScope) {
    $scope.attemptLogin = function () {

        $http.post('/api/login', {login: $scope.login, password: $scope.password}).success(function (data) {
            if (data === '\"OK\"') {
                $rootScope.userService.rememberUser($scope.login);
                $location.path('/');
            } else {
                $scope.result = data;
            }

        }).error(function (data, status) {
            console.log('Error ' + data)
        });
    };
});

app.controller('StockQuotesCtrl', function ($scope, $http, $routeParams) {
    $http.get('/api/markets/' + $routeParams.markedUuid + '/instrument/' + $routeParams.stockUuid).success(function (data) {
        $scope.stock = data;
    }).error(function (data, status) {
        console.log('Error ' + data)
    });
    $http.get('/api/markets/' + $routeParams.markedUuid + '/instrument/' + $routeParams.stockUuid + '/record/current').success(function (data) {
        $scope.stockRecord = data;
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
