angular.module('apiServices', [])
    .service('MarketService', ['$http', function ($http) {
        return {
            getAllMarkets: function (callback) {
                $http.get('/api/markets').success(function (data) {
                    callback(data);
                }).error(function (data, status) {
                    console.error(data);
                });
            },
            getAllInstrumentsForMarket: function (marketUuid, callback) {
                $http.get('/api/markets/' + marketUuid + '/instrument')
                    .success(function (data) {
                        callback(data);
                    }).error(function (data, status) {
                    console.error(data);
                });
            },
            getCurrentRecord: function (marketUuid, instrumentUuid, callback) {
                $http.get('/api/markets/' + marketUuid + '/instrument/' + instrumentUuid + '/record/current')
                    .success(function (data) {
                        callback(data);
                    })
                    .error(function (data, status) {
                        console.error(data);
                    });
            }
        }
    }])
    .service('GamesService', ['$http', function ($http) {
        return {
            getAllGames: function (callback) {
                $http.get('/api/games').success(function (data) {
                    callback(data);
                }).error(function (data, status) {
                    console.error(data);
                });
            },
            joinAsSpectator: function (gameUuid, userUuid, callback) {
                console.warn(gameUuid, userUuid);
                $http.post('/api/games/' + gameUuid + '/join-as-spectator',
                    {'userUuid': userUuid})
                    .success(function (data) {
                        callback(data);
                    }).error(function (data, status) {
                    console.error(data);
                });
            },
            joinAsPlayer: function (gameUuid, userUuid, callback) {
                $http.post('/api/games/' + gameUuid + '/join-as-player',
                    {'userUuid': userUuid})
                    .success(function (data) {
                        callback(data);
                    }).error(function (data, status) {
                    console.error(data);
                });
            },
            getPlayerUuid: function (gameUuid, userUuid, callback) {
                $http.get('/api/games/' + gameUuid + '/player/' + userUuid)
                    .success(function (data) {
                        callback(data);
                    })
                    .error(function (data, status) {
                        console.error(data);
                    });
            }
        }
    }])
    .service('AccountsService', ['$http', function ($http) {
        return {
            getPortfolioForOwner: function (ownerUuid, callback) {
                $http.get('/api/account/' + ownerUuid).success(function (data) {
                    callback(data);
                }).error(function (data, status) {
                    console.error(data);
                });
            }
        }
    }])
    .service('TransactionService', ['$http', function ($http) {
        return {
            buy: function (ownerUuid, instrumentUuid, amount, callback) {
                $http.get('/api/account/' + ownerUuid).success(function (data) {
                    callback(data);
                }).error(function (data, status) {
                    console.error(data);
                });
            }
        }
    }]);

