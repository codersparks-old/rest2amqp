// Create the module and name it
var r2aApp = angular.module('r2aApp', ['ngRoute']);

// Configure our routes
r2aApp.config(function($routeProvider, $locationProvider) {
	$routeProvider
	
		// route for the home page
		.when('/', {
			templateUrl : 'pages/home.html',
			controller  : 'mainController'
		})
		
		// route for the about page
		.when('/about', {
			templateUrl : 'pages/about.html',
			controller  : 'aboutController'
		})
		
		// route for the contact page
		.when('/contact', {
			templateUrl : 'pages/contact.html',
			controller  : 'contactController'
		});
	
		$locationProvider.html5Mode(true);
});

// Create the controller and inject Angular's scope 
r2aApp.controller('addQueueController', function($scope, $http) {
	
	// create a blank object to hold our form information
    // $scope will allow this to pass between controller and view
    $scope.formData = {};
    
    $scope.processForm = function() {
    	var url = "/rabbitmq/queue/" + $scope.FormData.queueName
    	$http.put(url,"",{ headers	: {'Authorization': 'Basic '+ base64.encode('guest:guest') } })
    	.then(
    		function(successResponse) {
    			console.log("SUCCESS: " + JSON.stringify(successResponse))
    		},
    		function(errorResponse) {
    			console.log("ERROR  : " + JSON.stringify(errorResponse))
    		}
    	);
    }
});

r2aApp.controller('aboutController', function($scope){
	$scope.message='Look! I am an about page.';
});

r2aApp.controller('contactController', function($scope) {
	$scope.message = 'Contact us! JK. This is just a demo.';
})