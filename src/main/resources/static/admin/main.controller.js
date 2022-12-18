app.controller("main-ctrl", function ($scope, $http) {
    $scope.userPrincipal = {};

    $scope.initialize = function () {
        $http.get("/rest/users/principal").then(resp => {
            $scope.userPrincipal = resp.data;
            if ($scope.userPrincipal.username === undefined) {
                alert('Xin hãy đăng nhập lại!');
                location.href = '/security/login/form';
            } else {
                const userPrincipal = resp.data;
                userPrincipal.roles = userPrincipal.roles.map(r => r.authority.replace("ROLE_", ""));
                localStorage.setItem("userPrincipal", JSON.stringify(userPrincipal));
            }
        }).catch(error => {
            console.log("Error", error);
        });

    };

    $scope.initialize();
});