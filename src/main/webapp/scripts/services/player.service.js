'use strict';

angular.module('kahootCloneApp')

    .factory('PlayerService', function ($http) {

        return {

            join: function(playerId, playerName) {
                return $http.post('/api/join/' + playerId + '?playerName=' + playerName);
            },
        };

    });


