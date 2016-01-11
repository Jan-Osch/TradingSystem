var app = angular.module('bubbleApp', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute',
    'apiServices',
    'applicationServices'
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
    }).when('/portfolio', {
        templateUrl: 'views/portfolio.html',
        controller: 'PortfolioCtrl'
    }).otherwise({
        redirectTo: '/'
    })
});

app.run(function ($rootScope, UserService, GamePlayService, StocksService) {
    $rootScope.UserService = UserService;
    $rootScope.GamePlayService = GamePlayService;
    $rootScope.StocksService = StocksService;
    StocksService.startService();
    GamePlayService.startService();
});


app.controller('StocksCtrl', function ($scope, $http, $routeParams) {
    $http.get('/api/markets/' + $routeParams.marketId + '/instrument').success(function (data) {
        $scope.instruments = data;
    }).error(function (data, status) {
        console.log('Error ' + data)
    });
});

app.controller('RegisterCtrl', function ($scope, $http, $location, $rootScpoe) {
    $scope.attemptRegister = function () {
        $scope.dataLoading = true;
        $http.post('/api/register', {login: $scope.login, password: $scope.password}).success(function (data) {
            if (data == '\"OK\"') {
                $rootScope.UserService.rememberUser($scope.login);
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
            $rootScope.UserService.rememberUser($scope.login, data);
            $location.path('/');
        }).error(function (data, status) {
            console.log('Error ' + data)
        });
    };
});

app.controller('StockQuotesCtrl', function ($scope, $routeParams, StocksService, MarketService) {


    MarketService.getCurrentRecord($routeParams.markedUuid, $routeParams.stockUuid, function (data) {
        $scope.stocRecord = data;
        console.warn(data);
        $scope.stock = StocksService.getInstrument($routeParams.stockUuid);
    });
    $scope.buy = function (stock) {
    }
});


app.controller('MarketsCtrl', function ($scope, MarketService) {
    MarketService.getAllMarkets(function (data) {
        $scope.markets = data;
    })
});

app.controller('PortfolioCtrl', function ($scope, AccountsService, GamePlayService, GamesService, UserService, StocksService) {
    AccountsService.getPortfolioForOwner(GamePlayService.playerUuid, function (portfolio) {
        _.forEach(portfolio.assets, function (asset) {
            asset.name = StocksService.getName(asset.uuid);
        });
        $scope.portfolio = portfolio;
    });
});

app.controller('GamesCtrl', function ($scope, $http, MarketService, GamesService, UserService, GamePlayService) {

    var getNameByUuid = function (marketUuid) {
        return _.find($scope.markets, function (market) {
            return market.uuid === marketUuid;
        }).name;
    };

    function checkIfUserCanJoin(game) {
        if (UserService.isLogged()) {
            if (_.indexOf(game.spectators, UserService.getUserUuid()) !== -1) {
                game.isSpectator = true;
            }
            if (game.players.hasOwnProperty(UserService.getUserUuid())) {
                game.isPlayer = true;
            }
            game.canJoin = !(game.isPlayer || game.isSpectator);
        }
    }

    var onComplete = function () {
        if ($scope.markets && $scope.games) {
            _.forEach($scope.games, function (game) {
                game.marketName = getNameByUuid(game.marketUuid);
                game.numberOfPlayers = _.keys(game.players).length;
                checkIfUserCanJoin(game);
            });
        }
    };

    var loadAllGames = function () {
        GamesService.getAllGames(function (data) {
            $scope.games = data;
            onComplete();
        });
    };

    loadAllGames();

    MarketService.getAllMarkets(function (data) {
        $scope.markets = data;
        onComplete();
    });


    $scope.joinAsSpectator = function (game) {
        GamesService.joinAsSpectator(game.gameUuid, UserService.getUserUuid(), function (data) {
            game.canJoin = false;
            game.isSpectator = true;
        });
    };

    $scope.joinAsPlayer = function (game) {
        GamesService.joinAsPlayer(game.gameUuid, UserService.getUserUuid(), function (data) {
            loadAllGames();
        });
    };

    $scope.playAsPlayer = function (game) {
        GamePlayService.startPlaying(game.gameUuid, game.name, true);
    };

    $scope.playAsSpectator = function (game) {
        GamePlayService.startPlaying(game.gameUuid, game.name, false);
        console.warn(game);
    };
});
