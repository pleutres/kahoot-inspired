'use strict';

angular.module('kahootCloneApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('pseudo', {
                parent: 'site',
                url: '/player/pseudo',
                views: {
                    'content@': {
                        templateUrl: '/scripts/players/pseudo.html',
                        controller: 'PseudoCtrl'
                    }
                },
                resolve: {
                }
            })
            .state('game', {
                parent: 'site',
                url: '/player/game',
                views: {
                    'content@': {
                        templateUrl: '/scripts/players/player.html',
                        controller: 'PlayerCtrl'
                    }
                },
                resolve: {
                }
            })
            .state('screen', {
                parent: 'site',
                url: '/screen',
                views: {
                    'content@': {
                        templateUrl: '/scripts/screen/screen.html',
                        controller: 'ScreenCtrl'
                    }
                },
                resolve: {
                }
            })
            .state('admin', {
                parent: 'site',
                url: '/game-master',
                views: {
                    'content@': {
                        templateUrl: '/scripts/admin/admin.html',
                        controller: 'AdminCtrl'
                    }
                },
                resolve: {
                }
            })
        ;
    });
