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
  
  $scope.hej = "tryck!!!";
  
  $scope.message = null;
  $scope.gameParameters = null;
  $scope.error = null;
  $scope.gameWorlds = null;
  $scope.maps = null;
  $scope.selectedMap = null;
  $scope.selectedMap = null;
  
  /*
  $scope.createGame = function(){
	  
	  
	  <input type='text' ng-model='gameName' />
	        <input type='text' ng-model='gameWorldName' />
	        <input type='text' ng-model='steps' />
	        <input type='text' ng-model='time' />
	        <input type='text' ng-model='maxNrPlayers' />
	        <input type='text' ng-model='gamePassword' />
	        <input type='text' ng-model='groupFaction' />
	        <input type='text' ng-model='selectableFactionNames' />
	        <input type='text' ng-model='randomFaction' />
	        <input type='text' ng-model='numberOfStartPlanet' />
  }
  */
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
	  
	  
	  /*
	   * Gets a contract for creating a new game
	   * 
	   * {"gameWorldName":"thelastgreatwar","gameName":"","mapName":"wigge9","steps":"10","autoBalance":"yes",
	   * "time":"0","emailPlayers":"no","maxNrPlayers":"9","gamePassword":"","groupFaction":"yes",
	   * "selectableFactionNames":["China","USA"],"randomFaction":"no","diplomacy":"faction","ranked":"no",
	   * "singleVictory":60,"factionVictory":60,"endTurn":0,"numberOfStartPlanet":1,"statisticGameType":"ALL"}
	   */
	  $scope.getContract = function(){$http({
		  method: 'GET',
		  url: 'http://localhost:8080/Server/api/creategame/create/contract'
		}).then(function successCallback(response) {
			$scope.gameParameters = response.data;
		  }, function errorCallback(response) {
			  $scope.error = "något gick fel " + "data " + response.data + "status " + response.status + "headers " + response.headers + "config " + response.config + "statusText " + response.statusText;
		  })};
	

		  $scope.getGameWorlds = function(){$http({
			  method: 'GET',
			  url: 'http://localhost:8080/Server/api/gameworld/list'
			}).then(function successCallback(response) {
				$scope.gameWorlds = response.data;
				$scope.message = response.data;
			  }, function errorCallback(response) {
				  $scope.error = "något gick fel " + "data " + response.data + "status " + response.status + "headers " + response.headers + "config " + response.config + "statusText " + response.statusText;
			  })};
			  
		  $scope.getMaps = function(){$http({
			  method: 'GET',
			  url: 'http://localhost:8080/Server/api/map/list'
			}).then(function successCallback(response) {
				$scope.maps = response.data;
				$scope.message = response.data;
			  }, function errorCallback(response) {
				  $scope.error = "något gick fel " + "data " + response.data + "status " + response.status + "headers " + response.headers + "config " + response.config + "statusText " + response.statusText;
			  })};
			  
		$scope.selectMap = function (){
			console.log('$scope.selectedMap ', $scope.selectedMap);
			
			$scope.gameParameters.mapName = $scope.selectedMap.fileName;
			$scope.gameParameters.maxNrPlayers = $scope.selectedMap.maxNrStartPlanets;
			console.log('$scope.gameParameters.mapName ', $scope.gameParameters.mapName);
						
		};

		$scope.selectGameWorld = function (){
			
			console.log('$scope.selectedGameWorld ', $scope.selectedGameWorld);
			
			$scope.gameParameters.gameWorldName = $scope.selectedGameWorld.id;
			console.log('$scope.gameParameters.gameWorldName ', $scope.gameParameters.gameWorldName);			
		};
		  
 
  $scope.callJson = function(){$http({
	  method: 'GET',
	  url: 'http://localhost:8080/Server/api/PlanetInfo2/gameworld/thelastgreatwar/2/'
	}).then(function successCallback(response) {
		$scope.hej = response.data;
	  }, function errorCallback(response) {
		  $scope.hej = "något gick fel " + "data " + response.data + "status " + response.status + "headers " + response.headers + "config " + response.config + "statusText " + response.statusText;
	  })};
	  
	  
	  /*
	   * {gameWorldName:"thelastgreatwar",gameName:"",mapName:"wigge9",steps:"10",autoBalance:"yes",time:"0",emailPlayers:"no",maxNrPlayers:"9",,gamePassword:"",groupFaction:"yes",selectableFactionNames:[],randomFaction:"no",diplomacy:"",singlePlayer:false,ranked:"no",singleVictory:60,factionVictory:60,endTurn:0,numberOfStartPlanet:1,statisticGameType:"ALL"}
	   * 
	   */
	  $scope.callCreateNewgame = function(){$http({
		  method: 'POST',
		  url: 'http://localhost:8080/Server/api/creategame/create/',
		  headers: {
			   'Content-Type': 'application/json'
			 },
			 data: $scope.gameParameters
			//data: {gameWorldName:"thelastgreatwar",gameName:"Tobbe",mapName:"wigge9",steps:"10",autoBalance:"yes",time:"0",emailPlayers:"no",maxNrPlayers:"9",gamePassword:"",groupFaction:"yes",selectableFactionNames:["China","USA"],randomFaction:"no",diplomacy:"faction",ranked:"no",singleVictory:60,factionVictory:60,endTurn:0,numberOfStartPlanet:1,statisticGameType:"ALL"}
		}).then(function successCallback(response) {
			$scope.message = response.data;
		  }, function errorCallback(response) {
			  $scope.error = "något gick fel " + "data " + response.data + "status " + response.status + "headers " + response.headers + "config " + response.config + "statusText " + response.statusText;
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