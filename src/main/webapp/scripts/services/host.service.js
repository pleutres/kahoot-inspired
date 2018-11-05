'use strict';


angular.module('kahootCloneApp')

    .factory('HostService', function ($http) {

        return {

            hello: function() {
                return $http.get('/api/sayHello');
            },

            refreshPlayers: function() {
                return $http.get('/api/refreshPlayers');
            },
            startQuestion: function(question) {
                return $http.post('/api/question/start', question);
            },
            stopQuestion: function() {
                return $http.post('/api/question/stop');
            },
            getQuestions: function() {
                return $http.get('/api/questions');
            },
            scoring: function() {
                return $http.get('/api/scoring');
            },
            showConnection: function() {
                return $http.get('/api/showconnection');
            },
        };

    });
