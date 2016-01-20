var app = angular.module('bubbleApp', [
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ngRoute',
    'apiServices',
    'applicationServices',
    'chart.js'
]);

app.constant("moment", moment);
app.constant("myDateTimeFormat", 'YYYY/MM/DD-HH:mm:ss');

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
    }).when('/ranking', {
        templateUrl: 'views/ranking.html',
        controller: 'RankingCtrl'
    }).otherwise({
        redirectTo: '/'
    })
});

app.run(function ($rootScope, UserService, GamePlayService) {
    $rootScope.UserService = UserService;
    $rootScope.GamePlayService = GamePlayService;
    GamePlayService.startService();
    UserService.startService();
});


app.controller('StocksCtrl', function ($scope, $http, $routeParams, MarketService) {
    MarketService.getAllInstrumentsForMarket($routeParams.marketId, function (data) {
        $scope.instruments = data;
    });
});

app.controller('RankingCtrl', function ($scope, $http, GamePlayService, GamesService) {
    function prepareData(data) {
        var result = [];
        _.forEach(data['Ranking'], function (value, key) {
            result.push({
                val: value,
                name: data['UserNames'][key],
                uuid: key
            });
        });
        return _.sortBy(result, function (element) {
            return -element.value;
        })
    }

    GamesService.getRankingForGame(GamePlayService.currentUuid, function (data) {
        $scope.ranking = prepareData(data);
    });
});

app.controller('RegisterCtrl', function ($scope, $http, $location, ApplicationService, UserService) {
    $scope.attemptRegister = function () {
        $scope.dataLoading = true;
        ApplicationService.register($scope.login, $scope.password, function (uuid) {
            UserService.rememberUser($scope.login, uuid);
            UserService.redirectToLanding();
            $scope.dataLoading = false;
        }, function (data) {
            $scope.result = data;
            $scope.dataLoading = false;
        });
    };

    $scope.confirmDoesNotMatch = function () {
        return $scope.confirm !== "" && $scope.confirm !== $scope.password;
    }
});

app.controller('LoginCtrl', function ($scope, $http, $location, $rootScope, ApplicationService, UserService) {
    $scope.attemptLogin = function () {
        ApplicationService.login($scope.login, $scope.password, function (data) {
            UserService.rememberUser($scope.login, data);
            UserService.redirectToLanding()
        }, function (data) {
            $scope.result = data;
        })

    };
});

app.controller('StockQuotesCtrl', function ($scope,
                                            $routeParams,
                                            MarketService,
                                            TransactionService,
                                            GamePlayService,
                                            myDateTimeFormat) {
    MarketService.getCurrentRecord($routeParams.stockUuid,
        function (data) {
            $scope.stockRecord = data;
        });

    MarketService.getInstrument($routeParams.stockUuid,
        function (data) {
            $scope.stock = data;
        });

    $scope.buy = function (stock) {
        if ($scope.amount) {
            TransactionService.buy(GamePlayService.playerUuid, stock.uuid, $scope.amount,
                function (data) {
                    $scope.transactionStatus = 'OK'
                },
                function (data) {
                    $scope.transactionStatus = data;
                }
            )
        }
    };
    var start = moment().subtract(30, 'days').format(myDateTimeFormat);
    var end = moment().format(myDateTimeFormat);

    function prepareHistoricalChart(data) {
        $scope.labels = [];

        $scope.valueData = [[], []];
        $scope.valueSeries = ['closingValue', 'minimum value'];
        $scope.valueColors = ['Yellow', 'Red'];

        $scope.changeData = [[]];
        $scope.changeSeries = ['change'];

        $scope.volumeData = [[], []];
        $scope.volumeSeries = ['volume', 'transactions'];
        $scope.volumeColors = ['Yellow', 'Green'];


        _.forEach(data, function (element) {
            $scope.labels.push(element.date.substring(0, 12));

            $scope.volumeData[0].push(element.volumeInShares / 100);
            $scope.volumeData[1].push(element.numberOfTransactions);

            $scope.valueData[0].push(element.closingValue / 100);
            $scope.valueData[1].push(element.minimumValue / 100);

            $scope.changeData[0].push(element.valueChangePercentage);
        });
    }

    MarketService.getRecordsForPeriod($routeParams.stockUuid, start, end,
        function (data) {
            prepareHistoricalChart(data);
        });

    var today = moment().subtract(1, 'days').format(myDateTimeFormat);

    function limitLabels(labels, number) {
        var interval = Math.floor(labels.length / number);
        for (var i = 0; i < labels.length; i++) {
            if (i % interval !== 0) {
                labels[i] = '';
            }
        }
    }

    function prepareCurrentSessionChart(data) {
        $scope.currentLabels = [];
        $scope.currentData = [[]];
        $scope.currentSeries = ['current value'];
        $scope.currentOptions = {
            scaleShowHorizontalLines: true,
            scaleShowVerticalLines: false,
            bezierCurve: false,
            pointDot: false
        };

        _.forEach(data, function (element, index) {
            $scope.currentData[0].push(element.value / 100);
            $scope.currentLabels.push(element.date.substring(13, 24));
        });

        limitLabels($scope.currentLabels, 10);
    }

    MarketService.getRecordsFromLastSession($routeParams.stockUuid, today, moment().format(myDateTimeFormat),
        function (data) {
            prepareCurrentSessionChart(data);
        });

    $scope.onClick = function (points, evt) {
        console.log(points, evt);
    };
});


app.controller('MarketsCtrl', function ($scope, MarketService) {
    MarketService.getAllMarkets(function (data) {
        $scope.markets = data;
    })
});

app.controller('PortfolioCtrl', function ($scope, AccountsService, GamePlayService, MarketService, TransactionService) {
    var reloadData = function () {
        AccountsService.getPortfolioForOwner(GamePlayService.playerUuid, function (portfolio) {
            $scope.portfolio = portfolio;
            $scope.portfolio.assets = _.map($scope.portfolio.assets, function (value, key) {
                return {
                    amount: value,
                    uuid: key
                }
            });
            _.forEach($scope.portfolio.assets, function (element, index) {
                MarketService.getInstrument(element.uuid, function (data) {
                    _.extend($scope.portfolio.assets[index], data);
                });
                MarketService.getCurrentRecord(element.uuid, function (data) {
                    _.extend($scope.portfolio.assets[index], data);
                })
            });
        });
    };

    reloadData();

    $scope.sell = function (asset) {
        if (asset.amount >= asset.transactionAmount) {
            TransactionService.sell(GamePlayService.playerUuid, asset.uuid, asset.transactionAmount,
                function (data) {
                    $scope.transactionStatus = 'Successfully selled ' + asset.transactionAmount + ' of ' + asset.fullName;
                    reloadData();
                },
                function (data) {
                    $scope.transactionStatus = data;
                }
            )
        }
    };

    $scope.buy = function (asset) {
        if (asset.transactionAmount) {
            TransactionService.buy(GamePlayService.playerUuid, asset.uuid, asset.transactionAmount,
                function (data) {
                    $scope.transactionStatus = 'Successfully buyed ' + asset.transactionAmount + ' of ' + asset.fullName;
                    reloadData();
                },
                function (data) {
                    $scope.transactionStatus = data;
                }
            )
        }
    };
});

app.controller('GamesCtrl', function ($scope, $http, MarketService, GamesService, UserService, GamePlayService) {

    var getNameByUuid = function (marketUuid) {
        return _.find($scope.markets, function (market) {
            return market.uuid === marketUuid;
        }).name;
    };

    function checkIfUserCanJoin(game) {
        if (UserService.isLogged()) {
            if (_.indexOf(game.spectators, UserService.userUuid) !== -1) {
                game.isSpectator = true;
            }
            if (game.players.hasOwnProperty(UserService.userUuid)) {
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
        GamesService.joinAsSpectator(game.gameUuid, UserService.userUuid, function (data) {
            game.canJoin = false;
            game.isSpectator = true;
        });
    };

    $scope.joinAsPlayer = function (game) {
        GamesService.joinAsPlayer(game.gameUuid, UserService.userUuid, function (data) {
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
