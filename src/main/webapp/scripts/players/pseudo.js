(function() {

    'use strict';

    angular
        .module('kahootCloneApp')
        .controller('PseudoCtrl', PseudoCtrl);

    PseudoCtrl.$inject = ['$scope', '$rootScope','$state', '$cookies', 'PlayerService', 'toaster'];

    function PseudoCtrl($scope, $rootScope, $state, $cookies, PlayerService, toaster) {

        $scope.connect = connect;
        $scope.isPreviousExists = isPreviousExists;
        $scope.getPreviousPlayerName = getPreviousPlayerName;
        $scope.newPlayerName;

        function connect() {
            if (_.isUndefined($rootScope.playerId) || $rootScope.playerId=='undefined') {
                $rootScope.playerId = ""+ _.random(0, 999999999);
            }
            $rootScope.playerName = $scope.newPlayerName;

            $cookies.playerName = $rootScope.playerName;
            $cookies.playerId = $rootScope.playerId;

            PlayerService.join($rootScope.playerId, $rootScope.playerName)
                .success(function(data, status, headers, config){
                    $state.go("game");

                }).
                error(function(data, status, headers, config) {
                    toaster.pop({ type: 'error', body: "Ce nom est déjà pris !"});
                });

        }

        function loadLastInformation() {
            // generate a unique idenftifier for the player and save it in a cookie to allow refreshes
            if (isPreviousExists()) {
                $rootScope.playerId = $cookies.playerId;
                $rootScope.playerName = $cookies.playerName;
                $scope.newPlayerName = $rootScope.playerName;
            }
        }

        function isPreviousExists() {
            return (!_.isUndefined($cookies.playerName)) && (!_.isUndefined($cookies.playerId));
        }

        function getPreviousPlayerName() {
            return $rootScope.playerName;
        }

        loadLastInformation();



    }
})();;
