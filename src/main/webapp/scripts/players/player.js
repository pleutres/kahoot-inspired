(function() {

    'use strict';

    angular
        .module('kahootCloneApp')
        .controller('PlayerCtrl', PlayerCtrl);

    PlayerCtrl.$inject = ['$scope', '$rootScope','WebSock', 'GameService'];

    function PlayerCtrl($scope, $rootScope, WebSock, GameService) {

        $scope.sendAnswer = sendAnswer;
        $scope.removeAnimation = removeAnimation;

        $scope.gameStatus='game-reset';
        $scope.gameContent;
        $scope.question = null;
        $scope.answerShape = ["bg-success", "bg-info", "bg-danger", "bg-warning"];
        $scope.animate = [];
        $scope.currentAnswer = null;
        $scope.count = 0;
        $scope.myscore;

        function removeAnimation(event) {
            if (event.type=="animationend") {
                for (var i=0; i < $scope.animate.length; i++) {
                    $scope.animate[i]=false;
                }
                $scope.$apply();
            }
        }

        function sendAnswer(index, answer) {
            $scope.currentAnswer = answer;
            $scope.animate[index]=true;
            WebSock.send('playerAnswer', {'playerId':$rootScope.playerId, 'answer':answer.text});
            //$scope.animate[1]=false;

        }


        WebSock.receive().then(null, null, function(message) {
            $scope.gameStatus = message.type;
            $scope.gameContent = message.content;

            if ($scope.gameStatus=='scoring') {
                $scope.myscore = _.filter($scope.gameContent, function(score) {return score.playerId == $rootScope.playerId; })
                if ($scope.myscore.length > 0) {
                    $scope.myscore = $scope.myscore[0];
                }

            }
            else {
                GameService.manageGameStatus($scope);
            }
        });

    }

})();
