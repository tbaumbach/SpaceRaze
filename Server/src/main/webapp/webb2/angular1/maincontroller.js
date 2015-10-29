app.controller('MainController', ['$scope', '$http', function($scope, $http){
  $scope.people = [
        {
            id: 0,
            name: 'Leon',
            music: [
                'Rock',
                'Metal',
                'Dubstep',
                'Electro'
            ],
            live: true
        },
        {
            id: 1,
            name: 'Chris',
            music: [
                'Indie',
                'Drumstep',
                'Dubstep',
                'Electro'
            ],
            live: true
        },
        {
            id: 2,
            name: 'Harry',
            music: [
                'Rock',
                'Metal',
                'Thrash Metal',
                'Heavy Metal'
            ],
            live: false
        },
        {
            id: 3,
            name: 'Allyce',
            music: [
                'Pop',
                'RnB',
                'Hip Hop'
            ],
            live: true
        }
    ];
  
  $scope.newPerson = null;
  $scope.addNew = function() {
      if ($scope.newPerson != null && $scope.newPerson != "") {
          $scope.people.push({
              id: $scope.people.length,
              name: $scope.newPerson,
              live: true,
              music: []
          });
      }
  }
  
  $scope.hej = "tryck!!!"
  
  
	  delete $http.defaults.headers.common['X-Requested-With'];  
  /*
  $scope.callJson = function(){
	  $http.get("http://localhost:8080/Server/api/PlanetInfo/get?gamename=test&planet=1")
	  	.success(function(response) {$scope.hej = response;})
	  	.error(function(response) {$scope.hej = "något gick fel " + response;});
  };
  */
  
  // url: 'http://localhost:8080/Server/api/PlanetInfo/get?gamename=test&planet=1'
  //url: 'http://localhost:8080/SpaceRaze/servletjson/JSONAndHTMLServlet?gamename=test&planet=2'
 
  $scope.callJson = function(){$http({
	  method: 'GET',
	  url: 'http://localhost:8080/Server/api/PlanetInfo2/gameworld/thelastgreatwar/2/'
	}).then(function successCallback(response) {
		$scope.hej = response.data;
	  }, function errorCallback(response) {
		  $scope.hej = "något gick fel " + "data " + response.data + "status " + response.status + "headers " + response.headers + "config " + response.config + "statusText " + response.statusText;
	  })};
  
	  
/*	  
  $scope.callJson = function(){$http.get("http://www.w3schools.com/angular/customers.php")
  .success(function(response) {$scope.hej = response;});};
*/
  
  
  
  //$scope.planetInfo = "";
  /*
  $scope.getPlanetInfo = function customersController($scope,$http) {
	    $http.get("http://localhost:8080/Server/api/PlanetInfo/get?gamename=test&planet=1")
	    .success(function(response) {$scope.planetInfo = response;});
	}*/
}]);