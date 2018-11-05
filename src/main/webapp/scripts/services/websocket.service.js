'use strict';

angular.module('kahootCloneApp')
    .factory('WebSock', function ($rootScope, $q, $timeout) {
        var service = {};
        var listener = $q.defer();
        var socket = {
            client: null,
            stomp: null
        };
        var messageIds = [];

        service.RECONNECT_TIMEOUT = 5000;
        service.SOCKET_URL = "/questionsWebSocket";
        service.PLAYER_TOPIC = "/topic/player";
        service.HOST_TOPIC = "/topic/host";
        service.SEND_TO_HOST = "/sendTo/host";

        service.receive = function () {
            return listener.promise;
        };

        service.send = function (type, message) {
            console.log(type);
            var id = Math.floor(Math.random() * 1000000);
            socket.stomp.send(service.SEND_TO_HOST, {
                priority: 9
            }, JSON.stringify({
                message: message,
                "type": type,
                id: id
            }));
            console.log(JSON.stringify({
                message: message,
                "type": type,
                id: id
            }));
            messageIds.push(id);
        };

        var reconnect = function () {
            $timeout(function () {
                service.initialize(service.topic);
            }, service.RECONNECT_TIMEOUT);
        };

        var getMessage = function (data) {
            var message = JSON.parse(data), out = {};
            out.content = message.message;
            out.type = message.type;
            out.time = new Date(message.time);
            if (_.contains(messageIds, message.id)) {
                out.self = true;
                messageIds = _.reject(messageIds, function(mess) {mess.id == message.id;});

            }
            return out;
        };

        var startListener = function () {
            $rootScope.$broadcast("websocket.connected");
            socket.stomp.subscribe(service.topic, function (data) {
                listener.notify(getMessage(data.body));
            });
        };

        service.initialize = function (selectedTopic) {
            service.topic = selectedTopic;
            socket.client = new SockJS(service.SOCKET_URL);
            socket.stomp = Stomp.over(socket.client);
            socket.stomp.connect({}, startListener, reconnect);
        };

        return service;
    });
