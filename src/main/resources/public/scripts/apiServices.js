angular.module('apiServices', [])
    .service('MarketService', ['$http', function ($http) {
        return {
            getAllMarkets: function (callback) {
                $http.get('/api/markets')
                    .success(function (data) {
                        callback(data);
                    })
                    .error(function (data, status) {
                        console.error('status: %s Error: %s' % (status, data));
                    });
            },
            getAllInstrumentsForMarket: function (marketUuid, callback) {
                $http.get('/api/markets/' + marketUuid + '/instrument')
                    .success(function (data) {
                        callback(data);
                    })
                    .error(function (data, status) {
                        console.error('status: %s Error: %s' % (status, data));
                    });
            },
            getInstrument: function (instrumentUuid, callback) {
                $http.get('/api/markets/instrument/' + instrumentUuid)
                    .success(function (data) {
                        callback(data);
                    })
                    .error(function (data, status) {
                        console.error('status: %s Error: %s' % (status, data));
                    });
            },
            getCurrentRecord: function (instrumentUuid, callback) {
                $http.get('/api/markets/instrument/' + instrumentUuid + '/record/current')
                    .success(function (data) {
                        callback(data);
                    })
                    .error(function (data, status) {
                        console.error('status: %s Error: %s' % (status, data));
                    });
            },
            getRecordsForPeriod: function (instrumentUuid, startDate, endDate, callback) {
                $http.get('/api/markets/instrument/' + instrumentUuid + '/record/historical', {
                        params: {
                            start: startDate,
                            end: endDate
                        }
                    })
                    .success(function (data) {
                        callback(data);
                    })
                    .error(function (data, status) {
                        console.error('status: %s Error: %s' % (status, data));
                    });
            }
        }
    }])
    .service('GamesService', ['$http', function ($http) {
        return {
            getAllGames: function (callback) {
                $http.get('/api/games')
                    .success(function (data) {
                        callback(data);
                    })
                    .error(function (data, status) {
                        console.error(data);
                    });
            },
            joinAsSpectator: function (gameUuid, userUuid, callback) {
                $http.post('/api/games/' + gameUuid + '/join-as-spectator',
                    {'userUuid': userUuid})
                    .success(function (data) {
                        callback(data);
                    })
                    .error(function (data, status) {
                        console.error(data);
                    });
            },
            joinAsPlayer: function (gameUuid, userUuid, callback) {
                $http.post('/api/games/' + gameUuid + '/join-as-player', {'userUuid': userUuid})
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
                $http.get('/api/account/' + ownerUuid)
                    .success(function (data) {
                        callback(data);
                    })
                    .error(function (data, status) {
                        console.error(data);
                    });
            }
        }
    }])
    .service('TransactionService', ['$http', function ($http) {
        return {
            buy: function (ownerUuid, instrumentUuid, amount, callback, errback) {
                $http.post('/api/transactions/buyByMarketPrice',
                    {
                        accountUuid: ownerUuid,
                        instrumentUuid: instrumentUuid,
                        amount: amount
                    })
                    .success(function (data) {
                        callback(data);
                    })
                    .error(function (data, status) {
                        console.error('status: %s Error: %s' % (status, data));
                        if (errback) {
                            errback(data);
                        }
                    });
            },
            sell: function (ownerUuid, instrumentUuid, amount, callback, errback) {
                $http.post('/api/transactions/sellByMarketPrice',
                    {
                        accountUuid: ownerUuid,
                        instrumentUuid: instrumentUuid,
                        amount: amount
                    })
                    .success(function (data) {
                        callback(data);
                    })
                    .error(function (data, status) {
                        console.error('status: %s Error: %s' % (status, data));
                        if (errback) {
                            errback(data);
                        }
                    });
            }
        }
    }])
    .service('ApplicationService', ['$http', function ($http) {
        return {
            register: function (userLogin, userPassword, callback, errback) {
                $http.post('/api/register', {login: userLogin, password: userPassword})
                    .success(function (data) {
                        callback(data);
                    })
                    .error(function (data, status) {
                        console.error('status: %s Error: %s' % (status, data));
                        if (errback) {
                            errback(data);
                        }
                    });
            },
            login: function (login, password, callback, errback) {
                $http.post('/api/login', {login: login, password: password})
                    .success(function (data) {
                        callback(data);
                    })
                    .error(function (data, status) {
                        console.error('status: %s Error: %s' % (status, data));
                        if (errback) {
                            errback(data);
                        }
                    });
            }
        }
    }]);

