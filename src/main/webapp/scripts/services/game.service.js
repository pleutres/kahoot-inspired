'use strict';

angular.module('kahootCloneApp')

    .factory('GameService', function () {


        function checkSecond($scope) {
            if (($scope.gameContent == "1") || ($scope.gameContent == "0")) {
                $scope.gameContent = $scope.gameContent + " seconde";
            }
            else {
                $scope.gameContent = $scope.gameContent + " secondes";
            }
        }


        function initQuestion($scope, question) {
            if ($scope.question == null) {
                $scope.question = question;

                for (var i = 0; i < question.answers.length; i++) {
                    $scope.animate[i] = false;
                }
            }
        }

        return {
            manageGameStatus: function ($scope) {
                if ($scope.gameStatus == 'game-start') {
                    $scope.question = null;
                    $scope.currentAnswer = null;
                    checkSecond($scope);
                }
                if ($scope.gameStatus == 'game-question') {
                    initQuestion($scope, $scope.gameContent);
                }
                if ($scope.gameStatus == 'game-wait') {
                    initQuestion($scope, $scope.gameContent.question);
                    $scope.count = $scope.gameContent.count;
                    checkSecond($scope);
                }

                if ($scope.gameStatus == 'game-end') {

                }
                if ($scope.gameStatus == 'game-reset') {
                    $scope.gameStatus = 'game-reset';
                }
            }

        };

    });


