'use strict';

/**
 * @ngdoc overview
 * @name kahootCloneApp
 * @description
 * # kahootCloneApp
 *
 * Main module of the application.
 */

var underscore = angular.module('underscore', []);
underscore.factory('_', ['$window', function($window) {
  return $window._; // assumes underscore has already been loaded on the page
}]);

angular.module("websock.services", []);

angular.module('kahootCloneApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ui.router',
    'ngSanitize',
    'ngTouch',
    'underscore',
    'ui.router',
    'toaster',
    '720kb.fx'
  ])

    .run(function ($rootScope, $state, $location, WebSock) {

        $rootScope.isConnected = false;

        $rootScope.absUrl =  "http://" + location.host;
        $rootScope.requiredUrl = $location.url();
        $rootScope.isPlayer = (($rootScope.requiredUrl == "") || ($rootScope.requiredUrl == "/") || ($rootScope.requiredUrl.indexOf("/player") !== -1));
        $rootScope.isAdmin = ($rootScope.requiredUrl.indexOf("/game-master") !== -1);


        if ($rootScope.isPlayer) {
            WebSock.initialize(WebSock.PLAYER_TOPIC);
        }
        else if ($rootScope.isAdmin)  {
            $state.go("admin");
        }
        else {
            WebSock.initialize(WebSock.HOST_TOPIC);
        }

        $location.url("/");

        $rootScope.$on('websocket.connected', function(event) {
            $rootScope.isConnected = true;
            if ($rootScope.isPlayer) {
                $state.go('pseudo');
            }
            else {
                var state = _.find($state.get(), function (state) {
                    return state.url == $rootScope.requiredUrl});
                if (_.isUndefined(state)) {
                    $state.go('pseudo');
                }
                else {
                    $state.go(state);
                }

            }
        });



    })
    .config(function ($stateProvider) {

        $stateProvider.state('site', {
            abstract: true,
        });

    })

    .directive('animationend', function() {
        return {
            restrict: 'A',
            scope: {
                animationend: '&'
            },
            link: function(scope, element) {
                var callback = scope.animationend();
                var events = 'animationend webkitAnimationEnd MSAnimationEnd' +
                        'transitionend webkitTransitionEnd';

                element.on(events, function(event) {
                    callback.call(element[0], event);
                });
            }
        };
    })
    .directive('animateOnChange', function($animate,$timeout) {
        return function(scope, elem, attr) {
            scope.$watch(attr.animateOnChange, function(nv,ov) {
                if (nv!=ov) {
                    var c = attr.animateOnChangeClass;
                    $animate.addClass(elem,c).then(function() {
                        $timeout(function() {$animate.removeClass(elem,c)});
                    });
                }
            })
        }
    })
    .directive('animateOnChangeAndStay', function($animate,$timeout) {
        return function(scope, elem, attr) {
            scope.$watch(attr.animateOnChangeAndStay, function(nv,ov) {
                if (nv!=ov) {
                    var c = attr.animateOnChangeClass;
                    $animate.addClass(elem,c);
                }
            })
        }
    });

;



