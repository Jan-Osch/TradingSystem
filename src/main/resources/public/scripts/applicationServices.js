angular.module('applicationServices', [])
    .service('UserService', function ($cookieStore, $location) {
        return {
            user: undefined,
            userUuid: undefined,

            logout: function () {
                var that = this;
                $cookieStore.remove("user_uuid");
                $cookieStore.remove("remembered_user");
                that.user = undefined;
                that.userUuid = undefined;
            },

            isLogged: function () {
                return (this.user !== undefined &&
                this.userUuid !== undefined)
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

            redirectToLanding: function () {
                $location.path('/');
            },

            loadCookies: function () {
                var that = this;
                that.userUuid = $cookieStore.get("user_uuid");
                that.user = $cookieStore.get("remembered_user");
            },

            startService: function () {
                this.loadCookies();
            }
        };
    })
    .service('NavigationService', function ($location) {
        return {
            current: undefined,

            makeCurrent: function (tab) {
                this.current = tab;
            },

            isCurrent: function (tab) {
                return this.current == tab;
            },

            redirectToRanking: function(){
                $location.path('/ranking');
            },

            redirectToPlayerPortfolio: function(){
                $location.path('/portfolio');
            }

        };
    })
    .service('GamePlayService', function ($cookieStore, ApiService, UserService, $location) {
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

            clearCookies: function () {
                var that = this;
                $cookieStore.remove("game_name");
                $cookieStore.remove('game_uuid');
                $cookieStore.remove('game_player');
                $cookieStore.remove('player_uuid');
                that.currentGame = that.currentUuid = that.currentGame = that.player = undefined;
            },

            loadPlayerUuid: function (callback) {
                var that = this;
                ApiService.getPlayerUuid(that.currentUuid, UserService.userUuid, callback);
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
                    that.setCookies(gameName, gameUuid, player, playerUuid);
                });
            },

            redirectToGames: function () {
                $location.path('/games');
            },

            startService: function () {
                this.loadCookies();
            }

        };
    });
