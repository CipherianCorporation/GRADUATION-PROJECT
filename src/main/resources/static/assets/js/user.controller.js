app.controller("user-ctrl", userController);

function userController($scope, $http, $interval) {
    $scope.form = {};
    $scope.items = [];
    $scope.message = '';

    $scope.initialize = function () {
        $http.get("/rest/users/principal").then(resp => {
            $scope.form = resp.data;
        }).catch(error => {
            console.log("Error", error);
        });

    };

    $scope.initialize();

    // upload image
    $scope.imageChanged = function (files) {
        var data = new FormData();
        data.append('file', files[0]);
        $http.post('/rest/upload/images', data, {
            transformRequest: angular.identity,
            headers: { 'Content-Type': undefined }
        }).then(resp => {
            $scope.form.image_url = resp.data.name;
        }).catch(error => {
            alert("Loi upload hinh anh");
            console.log("Error", error);
        });
    };

    $scope.reset = function () {
        $scope.form = {
            image_url: 'default-user.jpg',
        };
    };

    $scope.update = function () {
        let item = angular.copy($scope.form);
        $http.put(`/rest/users/${item.id}`, item).then(resp => {
            let index = $scope.items.findIndex(p => p.id == item.id);
            $scope.items[index] = item;
            alert("Cập nhật thành công, xin hãy đăng nhập lại !!!");
            location.href = '/security/logoff';
        }).catch(error => {
            alert("Cập nhật thất bại !!!");
            console.log("Error", error);
        });
    };
}