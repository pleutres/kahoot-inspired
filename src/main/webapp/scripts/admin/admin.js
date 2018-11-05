(function () {

    'use strict';

    angular
        .module('kahootCloneApp')
        .controller('AdminCtrl', AdminCtrl);

    AdminCtrl.$inject = ['$scope', '$rootScope', 'HostService'];

    function AdminCtrl($scope, $rootScope, HostService) {

        $scope.startQuestion = startQuestion;
        $scope.stopQuestion = stopQuestion;
        $scope.scoring = scoring;
        $scope.getQuestions = getQuestions;
        $scope.showConnection = showConnection;


        function startQuestion(question) {
            HostService.startQuestion(question);
            question.run  = true;
        }

        function showConnection() {
            HostService.showConnection();

        }

        function stopQuestion() {
            HostService.stopQuestion();

        }

        function scoring() {
            HostService.scoring();

        }

        function getQuestions() {
            HostService.getQuestions().then(function successCallback(response) {
                console.log(response);
                $scope.questions = response.data.questions;
            }, function errorCallback(response) {
            });
        }

        getQuestions();
    }


})();
;
