var r2aApp = angular.module('r2aApp', ['ngRoute', 'mgcrea.ngStrap']);

// Configure the routes
r2aApp.config(function($routeProvider) {
	$routeProvider
	
	// route for home page
	.when('/', {
		templateUrl : 'page-home.html',
		controller  : 'homeController'
	})
	.when('/queue', {
		templateUrl : 'page-queue.html',
		controller  : 'queueController'
	})
	.when('/message', {
		templateUrl : 'page-message.html',
		controller  : 'messagecontroller'
	});
});

r2aApp.controller('homeController', function($scope) {
	
});

r2aApp.controller('queueController', function($scope) {
	
});

r2aApp.controller('messageController', function($scope) {
	
});
r2aApp.controller('addQueueController', function($scope, $http, $alert) {
	
	// create a blank object to hold our form information
    // $scope will allow this to pass between controller and view
    $scope.formData = {};
    
    $scope.action = '';
    
    $scope.processForm = function() {
    	
    	if($scope.formData.queueName == undefined) {
    		$scope.formData.queueName = "";
    	}
    	
    	console.log("Action: " + $scope.action);
    	var queue = $scope.formData.queueName;
    	var url = "/rabbitmq/queue/" + queue;
    	
    	var ajaxFunc = null;
    	var actionPastTense = null;	
    	if($scope.action == 'add' ) {
    		console.log("Setting ajax function to put");
    		ajaxFunc = $http.put;
    		actionPastTense = "added";
    	} else if ($scope.action == 'remove' ) {
    		console.log("Setting ajax function to delete");
    		ajaxFunc = $http.delete;
    		actionPastTense = "removed";
    	}
    	ajaxFunc(url,"",{ headers	: {'Authorization': 'Basic '+ btoa('guest:guest') } })
    	.then(
    		function(successResponse) {
    			console.log("SUCCESS: " + JSON.stringify(successResponse))
    			var queueAlert = $alert(
    				{
    					title: 'SUCCESS!', 
    					content: 'Queue ' + queue + ' has been ' + actionPastTense, 
    					container: '#queueResult', 
    					type: 'success',
    					duration: 5,
    					show: true
    				});
    		},
    		function(errorResponse) {
    			console.log("ERROR  : " + JSON.stringify(errorResponse))
    			
    			var responseData = errorResponse["data"];
    			var message = "Error status code: " + responseData["status"] + "\n" +
    							"Error: " + responseData["error"] + ": " + responseData["message"];
    			var queueAlert = $alert(
        				{
        					title: 'ERROR!', 
        					content: message, 
        					container: '#queueResult', 
        					type: 'danger',
        					show: true
        				});
    		}
    	);
    }
});