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
  $scope.gameUserParameters = null;
  
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
	   * {"name":null,"login":null,"role":null,"email":null,"turnEmail":null,"gameEmail":null,
	   * "adminEmail":null,"password":null,"repeatedPassword":null,"rulesOk":false}
	   */
	  $scope.getUserContract = function(){$http({
		  method: 'GET',
		  url: 'http://localhost:8080/Server/api/user/contract'
		}).then(function successCallback(response) {
			$scope.gameUserParameters = response.data;
			$scope.message = response.data;
		  }, function errorCallback(response) {
			  $scope.error = "något gick fel " + "data " + response.data + "status " + response.status + "headers " + response.headers + "config " + response.config + "statusText " + response.statusText;
		  })};
		  
	
	  $scope.callCreateUser = function(){$http({
		  method: 'PUT',
		  url: 'http://localhost:8080/Server/api/user/',
		  headers: {
			   'Content-Type': 'application/json'
			 },
			 data : {"name":"test testare1","login":"test1","role":"player","email": "tbaumbach@hotmail.com" ,"turnEmail":"checked","gameEmail":null, "adminEmail":null,"password":null,"repeatedPassword":null,"rulesOk":true}
			 //data: $scope.gameUserParameters
			 
		}).then(function successCallback(response) {
			$scope.message = response.data;
		  }, function errorCallback(response) {
			  $scope.error = "något gick fel " + "data " + response.data + "status " + response.status + "headers " + response.headers + "config " + response.config + "statusText " + response.statusText;
		  })};
	  
	  $scope.activate = function(){$http({
		  method: 'POST',
		  url: 'http://localhost:8080/Server/api/user/activate/',
		  headers: {
			   'Content-Type': 'application/json'
			 },
			 data : {"login":"test1", "password":"test1","repeatedPassword":"test1"}
			 //data: $scope.gameUserParameters
		}).then(function successCallback(response) {
			$scope.message = response.data;
		  }, function errorCallback(response) {
			  $scope.error = "något gick fel " + "data " + response.data + "status " + response.status + "headers " + response.headers + "config " + response.config + "statusText " + response.statusText;
		  })};
		  
	  $scope.login = function(){$http({
		  method: 'POST',
		  url: 'http://localhost:8080/Server/api/user/login/',
		  headers: {
			   'Content-Type': 'application/json'
			 },
			 data : {"login":"test1", "password":"test1"}
			 //data: $scope.gameUserParameters
		}).then(function successCallback(response) {
			$scope.message = response.data;
		  }, function errorCallback(response) {
			  $scope.error = "något gick fel " + "data " + response.data + "status " + response.status + "headers " + response.headers + "config " + response.config + "statusText " + response.statusText;
		  })};
		  
	  $scope.logout = function(){$http({
		  method: 'GET',
		  url: 'http://localhost:8080/Server/api/user/logout'
		}).then(function successCallback(response) {
			$scope.messages = response.data;
		  }, function errorCallback(response) {
			  $scope.error = "något gick fel " + "data " + response.data + "status " + response.status + "headers " + response.headers + "config " + response.config + "statusText " + response.statusText;
		  })};
		  
	  $scope.callJoinGame = function(){$http({
		  method: 'PUT',
		  url: 'http://localhost:8080/Server/api/games/2/users/USA/tobbe/kungen/',
		  headers: {
			   'Content-Type': 'application/json'
			 },
			 data : ''
			 //data: $scope.gameUserParameters
			 
		}).then(function successCallback(response) {
			$scope.message = response.data;
		  }, function errorCallback(response) {
			  $scope.error = "något gick fel " + "data " + response.data + "status " + response.status + "headers " + response.headers + "config " + response.config + "statusText " + response.statusText;
		  })};  
		  
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
		  url: 'http://localhost:8080/Server/api/games/game/contract'
		}).then(function successCallback(response) {
			$scope.gameParameters = response.data;
		  }, function errorCallback(response) {
			  $scope.error = "något gick fel " + "data " + response.data + "status " + response.status + "headers " + response.headers + "config " + response.config + "statusText " + response.statusText;
		  })};
	

		  $scope.getGameWorlds = function(){$http({
			  method: 'GET',
			  url: 'http://localhost:8080/Server/api/gameworlds'
			}).then(function successCallback(response) {
				$scope.gameWorlds = response.data;
				$scope.message = response.data;
			  }, function errorCallback(response) {
				  $scope.error = "något gick fel " + "data " + response.data + "status " + response.status + "headers " + response.headers + "config " + response.config + "statusText " + response.statusText;
			  })};
			  
		  $scope.getMaps = function(){$http({
			  method: 'GET',
			  url: 'http://localhost:8080/Server/api/maps/'
			}).then(function successCallback(response) {
				$scope.maps = response.data;
				$scope.message = response.data;
			  }, function errorCallback(response) {
				  $scope.error = "något gick fel " + "data " + response.data + "status " + response.status + "headers " + response.headers + "config " + response.config + "statusText " + response.statusText;
			  })};
			  
		  $scope.getGame = function(){$http({
			  method: 'GET',
			  url: 'http://localhost:8080/Server/api/games/game/2/tobbe'
			}).then(function successCallback(response) {
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
		  method: 'PUT',
		  url: 'http://localhost:8080/Server/api/games/game',
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
		  
	
	  $scope.callNewOrders = function(){$http({
		  method: 'PUT',
		  url: 'http://localhost:8080/Server/api/games/game/orders/1/Tobbe',
		  headers: {
			   'Content-Type': 'application/json'
			 },
			 data: {expenses:[
			                  {planetName: 'Tyrell', spaceshipTypeName: 'C Bomber', type: 'buildship', currentBuildingId: 1, playerName: 'tobbe'},
			                  {planetName: 'Tyrell', troopTypeName: 'China Light Infantry', type: 'buildtroop', currentBuildingId: 4, playerName: 'tobbe'},
			                  {planetName: 'Tyrell', buildingTypeName: 'City', type: 'building', currentBuildingId: 2, playerName: 'tobbe'},
			                  {playerName: 'a', type: 'transaction', playerName: 'a'}
			                  ],
				 shipMoves:[{spaceShipID : 73,destinationName: 'Riven', owner: 'tobbe'} ],
				 planetVisibilities:['Eroticon'],
				 abandonPlanets:[ ],
				 shipSelfDestructs:[ ],
				 screenedShips : [ ],
				  shipToCarrierMoves : [ ],
				  troopToCarrierMoves : [ ],
				  troopToPlanetMoves : [ ],
				  troopSelfDestructs : [ ],
				  abandonGame : false,
				  buildingSelfDestructs : [ ],
				  diplomacyOffers : [ ],
				  diplomacyChanges : [ ],
				  taxChanges : [ ],
				  planetNotesChanges : [ ],
				  vipmoves : [ ],
				  researchOrders : [ ],
				  vipselfDestructs : [ ],
				  blackMarketBids : [ ]}
			
		}).then(function successCallback(response) {
			$scope.message = response.data;
		  }, function errorCallback(response) {
			  $scope.error = "något gick fel " + "data " + response.data + "status " + response.status + "headers " + response.headers + "config " + response.config + "statusText " + response.statusText;
		  })};
		  
		  
		  		  
		  // Testning mot API för uppgift "boka taxi"
		  
		  $scope.callOrderTaxi = function(){$http({
			  method: 'PUT',
			  url: 'http://localhost:8080/taxi/api/taxi',
			  headers: {
				   'Content-Type': 'application/json'
				 },
				 data: {"userName": "Thobias Baumbach", "addressFrom" : "sveavägen 49", "addressTo" : "Schlytersvägen 36", "time" : "19:30"}
				 
			}).then(function successCallback(response) {
				$scope.message = response.data;
			  }, function errorCallback(response) {
				  $scope.error = "något gick fel " + "data " + response.data + "status " + response.status + "headers " + response.headers + "config " + response.config + "statusText " + response.statusText;
			  })};
			  
			  $scope.listTaxiOrders = function(){$http({
				  method: 'GET',
				  url: 'http://localhost:8080/taxi/api/taxi/orders'
				}).then(function successCallback(response) {
					$scope.hej = response.data;
				  }, function errorCallback(response) {
					  $scope.hej = "något gick fel " + "data " + response.data + "status " + response.status + "headers " + response.headers + "config " + response.config + "statusText " + response.statusText;
				  })};
				  
			
				  $scope.cancelTaxi = function(){$http({
					  method: 'DELETE',
					  url: 'http://localhost:8080/taxi/api/taxi',
					  headers: {
						   'Content-Type': 'application/json'
						 },
						 data: "1"
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