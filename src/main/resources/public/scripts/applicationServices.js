angular.module('applicationServices', [])
    .service('UserService', function ($cookieStore, $location) {
        return {
            user: undefined,
            userUuid: undefined,
            logout: function () {
                $cookieStore.remove("user_uuid");
                $cookieStore.remove("remembered_user");
                this.user = undefined;
                this.userUuid = undefined;
            },
            isLogged: function () {
                if (this.user) {
                    return true;
                }
                if (this.getRememberedUser() !== undefined) {
                    this.user = this.getRememberedUser();
                    return true;
                }
                return false;
            },
            rememberUser: function (userName, userUuid) {
                $cookieStore.put("remembered_user", userName);
                $cookieStore.put("user_uuid", userUuid);
                this.user = userName;
                this.userUuid = userUuid;
            },
            redirectToLogin: function () {
                $location.path('/login');
            },
            getUserUuid: function () {
                return $cookieStore.get("user_uuid");
            },
            getRememberedUser: function () {
                return $cookieStore.get("remembered_user");
            }
        };
    })
    .service('GamePlayService', function ($cookieStore, GamesService, UserService) {
        return {
            currentGame: undefined,
            currentUuid: undefined,
            player: undefined,
            playerUuid: undefined,

            loadCookies: function () {
                this.currentGame = $cookieStore.get('game_name');
                this.currentUuid = $cookieStore.get('game_uuid');
                this.player = $cookieStore.get('game_player');
            },

            loadPlayerUuid: function () {
                var that = this;
                GamesService.getPlayerUuid(this.currentUuid, UserService.getUserUuid(), function (data) {
                    that.playerUuid = data;
                });
            },

            isPlaying: function () {
                return this.currentGame;
            },

            setCookies: function (gameName, gameUuid, player) {
                $cookieStore.put('game_name', gameName);
                $cookieStore.put('game_uuid', gameUuid);
                $cookieStore.put('game_player', player);
            },

            startPlaying: function (gameUuid, gameName, player) {
                this.currentGame = gameName;
                this.currentUuid = gameUuid;
                this.player = player;
                this.setCookies(gameName, gameUuid, player);
                this.loadPlayerUuid();
            },

            startService: function () {
                this.loadCookies();
                this.loadPlayerUuid();
            }

        };
    }).service('StocksService', function ($cookieStore, MarketService) {
    return {
        instruments: [],
        getInstrument: function (instrumentUuid) {
            return _.find(this.instruments, function (element) {
                return element.uuid === instrumentUuid;
            });
        },
        getCode: function (instrumentUuid) {
            return this.getInstrument(instrumentUuid).codeName;

        },
        getName: function (instrumentUuid) {
            return this.getInstrument(instrumentUuid).fullName;
        },
        loadAll: function () {
            console.info('loadAll called');
            var that = this;
            MarketService.getAllMarkets(function (allMarkets) {
                _.forEach(allMarkets, function (market) {
                    MarketService.getAllInstrumentsForMarket(market.uuid, function (instruments) {
                        that.instruments.concat(instruments)
                    })
                })
            })
        }
    };
});
