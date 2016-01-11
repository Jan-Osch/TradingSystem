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
                this.playerUuid = $cookieStore.get('player_uuid');
            },

            loadPlayerUuid: function (callback) {
                var that = this;
                GamesService.getPlayerUuid(that.currentUuid, UserService.getUserUuid(), callback);
            },

            isPlaying: function () {
                return (this.currentGame !== undefined &&
                this.currentUuid !== undefined &&
                this.player !== undefined &&
                this.playerUuid !== undefined);
            },

            setCookies: function (gameName, gameUuid, player, playerUuid) {
                $cookieStore.put('game_name', gameName);
                $cookieStore.put('game_uuid', gameUuid);
                $cookieStore.put('game_player', player);
                $cookieStore.put('player_uuid', playerUuid);
            },

            startPlaying: function (gameUuid, gameName, player) {
                var that = this;
                that.currentGame = gameName;
                that.currentUuid = gameUuid;
                that.player = player;
                that.loadPlayerUuid(function (playerUuid) {
                    that.playerUuid = playerUuid;
                    console.warn(that.playerUuid);
                    that.setCookies(gameName, gameUuid, player, playerUuid);
                });
            },

            startService: function () {
                this.loadCookies();
            }

        };
    }).service('StocksService', function ($cookieStore, MarketService) {
    return {
        instruments: {},
        markets: [],
        getInstrument: function (instrumentUuid) {
            var that = this;
            return that.instruments[instrumentUuid];
        },
        getCode: function (instrumentUuid) {
            var that = this;
            return that.getInstrument(instrumentUuid).codeName;
        },
        getName: function (instrumentUuid) {
            var that = this;
            return that.getInstrument(instrumentUuid).fullName;
        },
        addInstrument: function (instrument) {
            var that = this;
            that.instruments[instrument.uuid] = _.pick(instrument, 'codeName', 'fullName', 'marketUuid');
        },
        loadAllInstruments: function () {
            var that = this;
            MarketService.getAllMarkets(function (allMarkets) {
                that.markets = allMarkets;
                _.forEach(allMarkets, function (market) {
                    MarketService.getAllInstrumentsForMarket(market.uuid, function (instruments) {
                        _.forEach(instruments, that.addInstrument, that);
                    })
                })
            })
        },
        startService: function () {
            this.loadAllInstruments();
        }
    };
});
