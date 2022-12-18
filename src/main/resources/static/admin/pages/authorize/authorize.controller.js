app.controller("authorize-ctrl", function ($scope, $http, $location) {
	$scope.roles = [];
	$scope.admins = [];
	$scope.authorities = [];
	$scope.loading = true;
	$scope.userPrincipal = {};
	$scope.myroles = [];

	$scope.initialize = function () {
		$scope.userPrincipal = JSON.parse(localStorage.getItem('userPrincipal'));
		//Load data roles
		$http.get("/rest/roles").then(resp => {
			$scope.roles = resp.data;
		}).catch(err => {
			console.error(err);
		}).finally(function () {
			// $scope.loading = false;
		});

		//Load adm and nv
		$http.get("/rest/users?admin=true").then(resp => {
			$scope.admins = resp.data;
		}).catch(err => {
			console.error(err);
		}).finally(function () {
			$scope.loading = false;
		});

		//Load authorites of staffs and directors
		$http.get("/rest/authorities?admin=true").then(resp => {
			$scope.authorities = resp.data;
		}).catch(err => {
			console.error(err);
			$location.path("/unauthorized");
		}).finally(function () {
			// $scope.loading = false;
		});
	};
	$scope.initialize();
	$scope.authority_of = function (acc, role) {
		if ($scope.authorities) {
			return $scope.authorities.find(ur => ur.user.username == acc.username && ur.role.id == role.id);
		}
	};

	$scope.authority_changed = function (_user, _role) {
		let authority = $scope.authority_of(_user, _role);
		if (authority) { //Tuoc quyen adm
			$scope.revoke_authority(authority);
		} else { //Cap quyen adm
			authority = { user: _user, role: _role };
			$scope.grant_authority(authority);
		}
	};
	// Xoa authority(adm)
	$scope.revoke_authority = function (authority) {
		$http.delete(`/rest/authorities/${authority.id}`).then(resp => {
			let index = $scope.authorities.findIndex(a => a.id == authority.id);
			$scope.authorities.splice(index, 1);
			alert("Tước quyền thành công");
		}).catch(error => {
			alert("Chưa đủ quyền để tước quyền, xin thử lại lần sau!");
			console.log("Error", error);
		});
	};
	// Them authority(adm)
	$scope.grant_authority = function (authority) {
		$http.post(`/rest/authorities`, authority).then(resp => {
			$scope.authorities.push(resp.data);
			alert("Cấp quyền thành công");
		}).catch(error => {
			alert("Chưa đủ quyền để cấp quyền, xin thử lại lần sau!");
			console.log("Error", error);
		});
	};
});