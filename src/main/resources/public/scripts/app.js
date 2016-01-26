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
        templateUrl: 'views/stocks.html',
        controller: 'StocksCtrl'
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
    }).when('/history/', {
        templateUrl: 'views/history.html',
        controller: 'HistoryCtrl'
    }).when('/stock-quotes/:markedUuid/:stockUuid', {
        templateUrl: 'views/stock-quotes.html',
        controller: 'StockQuotesCtrl'
    }).when('/portfolio', {
        templateUrl: 'views/portfolio.html',
        controller: 'PortfolioCtrl'
    }).when('/portfolio/:ownerUuid/:name', {
        templateUrl: 'views/portfolio-spectator.html',
        controller: 'PortfolioSpectatorCtrl'
    }).when('/moderator', {
        templateUrl: 'views/moderator.html',
        controller: 'ModeratorCtrl'
    }).when('/ranking', {
        templateUrl: 'views/ranking.html',
        controller: 'RankingCtrl'
    }).otherwise({
        redirectTo: '/'
    })
});

app.run(function ($rootScope, UserService, GamePlayService, NavigationService) {
    $rootScope.UserService = UserService;
    $rootScope.GamePlayService = GamePlayService;
    $rootScope.navigationService = NavigationService;
    GamePlayService.startService();
    UserService.startService();
});


app.controller('StocksCtrl', function ($scope, $routeParams, ApiService, NavigationService) {
    NavigationService.makeCurrent('Stocks');
    ApiService.getAllInstrumentsForMarket('d93e338a-6d0c-4ae7-a730-f84a22eac0cc', function (data) { // TODO remove default
        $scope.instruments = data;
    });
});

app.controller('RankingCtrl', function ($scope, GamePlayService, ApiService, NavigationService) {
    NavigationService.makeCurrent('Ranking');
    if (!GamePlayService.isPlaying()) {
        GamePlayService.redirectToGames();
        return;
    }
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
            return -element.val;
        })
    };

    ApiService.getRankingForGame(GamePlayService.currentUuid, function (data) {
        $scope.ranking = prepareData(data);
    });
});

app.controller('HistoryCtrl', function ($scope, GamePlayService, ApiService, NavigationService) {
    NavigationService.makeCurrent('History');
    if (!GamePlayService.isPlaying()) {
        GamePlayService.redirectToGames();
        return;
    }
    ApiService.getHistory(GamePlayService.playerUuid, function (data) {
        $scope.history = data;
        _.forEach($scope.history, function (element, index) {
            ApiService.getInstrument(element.instrumentUuid, function (data) {
                _.extend($scope.history[index], data);
            });
        });
    });
});

app.controller('RegisterCtrl', function ($scope, ApiService, UserService, NavigationService) {
    NavigationService.makeCurrent('Register');
    $scope.attemptRegister = function () {
        $scope.dataLoading = true;
        ApiService.register($scope.login, $scope.password, function (uuid) {
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

app.controller('LoginCtrl', function ($scope, ApiService, UserService, NavigationService) {
    NavigationService.makeCurrent('Login');
    $scope.attemptLogin = function () {
        ApiService.login($scope.login, $scope.password, function (data) {
            if(data == 'moderator'){
                UserService.moderator = true;
            }
            UserService.rememberUser($scope.login, data, UserService.moderator);
            UserService.redirectToLanding()
        }, function (data) {
            $scope.result = data;
        })

    };
});

app.controller('GamesWidgetCtrl', function ($scope, ApiService, UserService, GamePlayService) {
    $scope.reload = function () {
        if (UserService.userUuid) {
            ApiService.getAllGamesForUser(UserService.userUuid, function (data) {
                _.forEach(data, checkIfUserCanJoin);
                $scope.gamesInWidget = data;
            });
        }else{
            $scope.gamesInWidget = []
        }
    };

    $scope.playAsPlayer = function (game) {
        GamePlayService.startPlaying(game.gameUuid, game.name, true);
    };

    $scope.playAsSpectator = function (game) {
        GamePlayService.startPlaying(game.gameUuid, game.name, false);
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
});

app.controller('StockQuotesCtrl', function ($scope,
                                            $routeParams,
                                            ApiService,
                                            GamePlayService,
                                            myDateTimeFormat,
                                            NavigationService) {
    NavigationService.makeCurrent('Stocks');
    ApiService.getCurrentRecord($routeParams.stockUuid,
        function (data) {
            $scope.stockRecord = data;
        });

    ApiService.getInstrument($routeParams.stockUuid,
        function (data) {
            $scope.stock = data;
        });

    $scope.buy = function (stock) {
        if ($scope.amount) {
            ApiService.buy(GamePlayService.playerUuid, stock.uuid, $scope.amount,
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

    ApiService.getRecordsForPeriod($routeParams.stockUuid, start, end,
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

    ApiService.getRecordsFromLastSession($routeParams.stockUuid, today, moment().format(myDateTimeFormat),
        function (data) {
            prepareCurrentSessionChart(data);
        });

    $scope.onClick = function (points, evt) {
        console.log(points, evt);
    };
});


app.controller('MarketsCtrl', function ($scope,
                                        ApiService,
                                        NavigationService) {
    NavigationService.makeCurrent('Stocks');
    ApiService.getAllMarkets(function (data) {
        $scope.markets = data;
    })
});

app.controller('ModeratorCtrl', function ($scope,
                                        ApiService,
                                        NavigationService) {
    NavigationService.makeCurrent('Moderator');

    var getNameByUuid = function (marketUuid) {
        return _.find($scope.markets, function (market) {
            return market.uuid === marketUuid;
        }).name;
    };


    var onComplete = function () {
        if ($scope.markets && $scope.games) {
            _.forEach($scope.games, function (game) {
                game.marketName = getNameByUuid(game.marketUuid);
                game.numberOfPlayers = _.keys(game.players).length;
            });
        }
    };

    var loadAllGames = function () {
        ApiService.getAllGames(function (data) {
            $scope.games = data;
            onComplete();
        });
    };

    loadAllGames();

    ApiService.getAllMarkets(function (data) {
        $scope.markets = data;
        onComplete();
    });

    $scope.createGame = function(){
        ApiService.createGame($scope.gameName, $scope.marketUuid, $scope.initialAmount*100, function(data){
            loadAllGames();
        }, function(data){
            $scope.transactionStatus = data;
        });
    }
});

app.controller('PortfolioCtrl', function ($scope,
                                          ApiService,
                                          GamePlayService,
                                          NavigationService) {
    NavigationService.makeCurrent('Portfolio');
    if (!GamePlayService.isPlaying()) {
        GamePlayService.redirectToGames();
        return;
    }
    if (!GamePlayService.player) {
        NavigationService.redirectToRanking();
        return;
    }
    var reloadData = function () {
        ApiService.getPortfolioForOwner(GamePlayService.playerUuid, function (portfolio) {
            $scope.portfolio = portfolio;
            $scope.portfolio.assets = _.map($scope.portfolio.assets, function (value, key) {
                return {
                    amount: value,
                    uuid: key
                }
            });
            _.forEach($scope.portfolio.assets, function (element, index) {
                ApiService.getInstrument(element.uuid, function (data) {
                    _.extend($scope.portfolio.assets[index], data);
                });
                ApiService.getCurrentRecord(element.uuid, function (data) {
                    _.extend($scope.portfolio.assets[index], data);
                })
            });
        });
    };

    reloadData();

    $scope.sell = function (asset) {
        if (asset.amount >= asset.transactionAmount) {
            ApiService.sell(GamePlayService.playerUuid, asset.uuid, asset.transactionAmount,
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
            ApiService.buy(GamePlayService.playerUuid, asset.uuid, asset.transactionAmount,
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

app.controller('PortfolioSpectatorCtrl', function ($scope,
                                                   ApiService,
                                                   GamePlayService,
                                                   NavigationService,
                                                   $routeParams) {
    NavigationService.makeCurrent('Portfolio');
    if (!GamePlayService.isPlaying()) {
        GamePlayService.redirectToGames();
        return;
    }
    if (GamePlayService.player) {
        NavigationService.redirectToPlayerPortfolio();
        return;
    }
    var reloadData = function () {
        ApiService.getPortfolioForOwner($routeParams.ownerUuid, function (portfolio) {
            $scope.portfolio = portfolio;
            $scope.name = $routeParams.name;
            $scope.portfolio.assets = _.map($scope.portfolio.assets, function (value, key) {
                return {
                    amount: value,
                    uuid: key
                }
            });
            _.forEach($scope.portfolio.assets, function (element, index) {
                ApiService.getInstrument(element.uuid, function (data) {
                    _.extend($scope.portfolio.assets[index], data);
                });
                ApiService.getCurrentRecord(element.uuid, function (data) {
                    _.extend($scope.portfolio.assets[index], data);
                })
            });
        });
    };

    reloadData();
});


app.controller('GamesCtrl', function ($scope,
                                      NavigationService,
                                      ApiService,
                                      UserService,
                                      GamePlayService) {
    NavigationService.makeCurrent('Games');

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
        ApiService.getAllGames(function (data) {
            $scope.games = data;
            onComplete();
        });
    };

    loadAllGames();

    ApiService.getAllMarkets(function (data) {
        $scope.markets = data;
        onComplete();
    });


    $scope.joinAsSpectator = function (game) {
        ApiService.joinAsSpectator(game.gameUuid, UserService.userUuid, function (data) {
            game.canJoin = false;
            game.isSpectator = true;
        });
    };

    $scope.joinAsPlayer = function (game) {
        ApiService.joinAsPlayer(game.gameUuid, UserService.userUuid, function (data) {
            loadAllGames();
        });
    };

    $scope.playAsPlayer = function (game) {
        GamePlayService.startPlaying(game.gameUuid, game.name, true);
    };

    $scope.playAsSpectator = function (game) {
        GamePlayService.startPlaying(game.gameUuid, game.name, false);
    };
});
