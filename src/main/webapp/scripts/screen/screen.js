(function () {

    'use strict';

    angular
        .module('kahootCloneApp')
        .controller('ScreenCtrl', ScreenCtrl);

    ScreenCtrl.$inject = ['$scope', '$rootScope', 'WebSock', 'HostService', 'toaster', '$interval','$window', 'GameService'];

    function ScreenCtrl($scope, $rootScope, WebSock, HostService, toaster, $interval, $window, GameService) {

        $scope.sayHello = sayHello;
        $scope.getAnswerCount = getAnswerCount;

        $scope.players = [];
        $scope.playerVisible = 0;
        $scope.allplayers = [];
        $scope.numberOfPlayer;
        $scope.gameStatus = 'show-connection';
        $scope.gameContent;
        $scope.question = null;
        $scope.answerShape = ["bg-success", "bg-info", "bg-danger", "bg-warning"];
        $scope.animate = [];
        $scope.currentAnswer = null;
        $scope.count = 0;
        $scope.ranking = [];
        $scope.ranking_answer = [];


        function sayHello() {
            HostService.hello();
        }

        HostService.refreshPlayers();

        WebSock.receive().then(null, null, function (message) {
            if (message.type == 'player') {
                toaster.pop({type: 'success', body: "Un nouveau joueur arrive : " + message.content.name});
            }
            else if (message.type == 'players') {
                $scope.allplayers = [];
                $scope.allplayers = _.map(message.content, function (player) {
                    return player
                });

                // Generate
                /*
                for (var i = 0; i < 30; i++) {
                    $scope.allplayers.push({name:_.sample('ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789', 15).join('')});
                }
                */

                $scope.players = $scope.allplayers;
                $scope.numberOfPlayer = $scope.allplayers.length;
            }
            else if (message.type == 'show-connection') {
                $scope.gameStatus = message.type;

            }
            else if (message.type == 'ranking') {
                $scope.ranking = message.content;
                // Generate
                /*
                for (var i = 0; i < 30; i++) {
                    $scope.ranking.push({answer:$scope.question.answers[_.random(0,4)],playerName:_.sample('ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789', 15).join('')});
                }
                */
                $scope.ranking_answer = _.countBy($scope.ranking, function(rank) {return rank.answer});

            }
            else if (message.type == 'scoring') {
                $scope.gameStatus = message.type;
                // Generate
                /*
                for (var i = 0; i < 130; i++) {
                    message.content.push({score: _.random(0,100), playerName:_.sample('ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789', 15).join('')});
                }*/
                message.content = _.first(message.content, 33);
                $scope.scoring = [[], [], []];

                var modulo = message.content.length / 3;
                for (var i=0; i < message.content.length; i++) {
                    $scope.scoring[Math.floor(i / modulo)].push(message.content[i]);

                }


                $scope.type = message.scoring;

            }
            else {
                $scope.gameStatus = message.type;
                $scope.gameContent = message.content;


                GameService.manageGameStatus($scope);
            }
        });



        $scope.conf = {
            players_pos: 200, // the starting position from the right in the news container
            players_margin: 20,
            line: 3
        };

        $interval(players_move ,50);

        function getAnswerCount(answer) {
            var value = $scope.ranking_answer[answer.text];
            if (_.isNaN(value) || _.isUndefined(value)) {
                return 0;
            }
            return value;
        };

        $scope.get_players_right = function(idx) {
            var $right = $scope.conf.players_pos;
            for (var ri=0; ri < idx; ri += $scope.conf.line) {
                var element = angular.element(document.getElementById('player_'+ri));
                if (element) {
                    $right += $scope.conf.players_margin + element[0].offsetWidth;
                }
            }
            return $right+'px';
        };

        $scope.get_players_top = function(idx) {
            return ((idx % $scope.conf.line) * 40) +'px';
        };

        $scope.get_answer_height = function(text) {
            var height = (150 * ($scope.ranking_answer[text] / $scope.ranking.length)) + "px";
            return height;
        }

        function players_move() {
            if ($scope.players.length > 12 && $scope.gameStatus==null) {
                $scope.conf.players_pos--;

                var elem0 = angular.element(document.getElementById('player_0'));
                var strip = angular.element(document.getElementById('players_strip'));

                if (!elem0 || !strip || _.isUndefined(elem0)) {
                    return;
                }

                var limit = strip[0].offsetWidth + $scope.conf.players_margin;
                if (( elem0[0].offsetLeft > limit)
                && ( angular.element(document.getElementById('player_1'))[0].offsetLeft > limit)
                && ( angular.element(document.getElementById('player_2'))[0].offsetLeft > limit)) {
                    $scope.players.push($scope.players[0]);
                    $scope.players.push($scope.players[1]);
                    $scope.players.push($scope.players[2]);
                    $scope.players.shift();
                    $scope.players.shift();
                    $scope.players.shift();
                    $scope.conf.players_pos += angular.element(document.getElementById('player_0'))[0].offsetWidth + $scope.conf.players_margin;
                }
            }
        };

        /*
        $scope.gameStatus='game-end';
        $scope.question = {
            "title": "Quel est votre couleur préférée ?",
            "waitTime": 10,
            "answers": [
                {"text": "Bleu", "correct": true},
                {"text": "Blanc", "correct": false},
                {"text": "Rouge", "correct": false},
                {"text": "Transparent longue reponse", "correct": false}
            ]
        };

        $scope.ranking=[];
        for (var i = 0; i < 30; i++) {
            $scope.ranking.push({answer:$scope.question.answers[_.random(0,3)].text,playerName:_.sample('ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789', 15).join('')});
        }
        console.log($scope.ranking);
        $scope.ranking_answer = _.countBy($scope.ranking, function(rank) {return rank.answer});
        console.log($scope.ranking_answer);

        */

    }


})();
