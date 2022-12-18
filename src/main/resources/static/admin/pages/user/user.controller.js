app.controller("user-ctrl", function ($scope, $http) {
    $scope.items = [];
    $scope.providers = [];
    $scope.newForm = {};
    $scope.editForm = {};
    $scope.roles = [];
    $scope.searchTextUser = '';
    $scope.userPrincipal = {};

    $scope.initialize = function () {
        $scope.loading = true;
        $scope.userPrincipal = JSON.parse(localStorage.getItem('userPrincipal'));
        $http.get("/rest/roles").then(resp => {
            new Set(resp.data).forEach(role => {
                if (!$scope.roles.includes({ id: role.id })) {
                    $scope.roles.push(role);
                }
            });
            if (!$scope.userPrincipal.roles.includes("ADMIN")) {
                $scope.roles = $scope.roles.filter((role) => role.id !== "ADMIN");
                console.log($scope.roles);
            }
        }).catch(err => {
            console.error(err);
        });

        $http.get(`/rest/users`).then(resp => {
            // lọc tất cả user isDeleted là false
            $scope.items = resp.data.filter((prod) => prod.isDeleted === false);
            new Set($scope.items).forEach(user => {
                if (!$scope.providers.includes(user.provider)) {
                    $scope.providers.push(user.provider);
                }
            });
        }).finally(function () {
            $scope.loading = false;
        });
    };

    $scope.reset = function () {
        let tmp = {
            image_url: 'default-user.jpg',
        };
        $scope.newForm = tmp;
        $scope.editForm = tmp;
    };

    $scope.create = function () {
        let item = angular.copy($scope.newForm);
        if (item.image_url == null) {
            item.image_url = 'default-user.jpg';
        }
        if (item.role == null) {
            let findRole = $scope.roles.find(({ id }) => id === 'STAFF');
            item.role = findRole.id;
        }
        if (item.provider == null) {
            item.provider = $scope.providers.find(p => p === 'DATABASE');
        }
        if (item.enabled == null) {
            item.enabled = true;
        }
        if (item.isDeleted == null) {
            item.isDeleted = false;
        }

        let isExist = false;
        $scope.items.forEach(user => {
            if (user.username === item.username) {
                isExist = true;
                alert('Tên người dùng đã được sử dụng. Hãy chọn một tên khác');
            } else if (user.email === item.email) {
                isExist = true;
                alert('Email đã được sử dụng. Hãy chọn một email khác');
            }
        });

        if (!isExist) {
            let userRole = {};
            $http.post(`/rest/users`, item).then(resp => {
                let message = '';
                $scope.items.push(resp.data);
                // save user 
                userRole = { user: resp.data, role: { id: item.role } };
                console.log(`Vai trò người dùng đã lưu mới là: ${JSON.stringify(userRole)}`);
                // set quyen cho user
                $scope.grant_authority(userRole);
                $scope.reset();
                // $scope.initialize();
                alert(message + 'Tạo người dùng mới thành công');
            }).catch(error => {
                alert('Lỗi khi tạo người dùng : ' + error.message);
                console.log("Error", error);
            });
        }
    };

    $scope.update = function () {
        let item = angular.copy($scope.editForm);
        item.isDeleted = false;
        let check = confirm(`Bạn có chắc chắn cập nhật người dùng này không ?`);
        if (check) {
            $http.put(`/rest/admin/${item.id}`, item).then(resp => {
                let index = $scope.items.findIndex(user => user.id == item.id);
                $scope.items[index] = item;
                $scope.reset();
                alert("Cập nhật thành công ");
            }).catch(error => {
                alert("Lỗi khi cập nhật người dùng: " + error.message);
                console.log("Error", error);
            });
        }

    };

    $scope.edit = function (item) {
        $scope.editForm = angular.copy(item);
        $(".nav-tabs a:eq(1)").tab('show');
    };

    $scope.imageChanged = function (files) {
        let data = new FormData();
        data.append('file', files[0]);
        $http.post('/rest/upload/images', data, {
            transformRequest: angular.identity,
            headers: { 'Content-Type': undefined }
        }).then(resp => {
            $scope.newForm.image_url = resp.data.name;
            $scope.editForm.image_url = resp.data.name;
        }).catch(error => {
            alert("Lối upload hình ảnh");
            console.log("Error", error);
        });
    };

    $scope.delete = function (item) {
        let check = confirm(`Bạn có chắn chắc muốn xóa người dùng này không ${item.username}`);
        if (check) {
            if (item.id === $scope.userPrincipal.id || item.username === $scope.userPrincipal.username) {
                alert('Không thể xóa người dùng đang đăng nhập');
                return;
            }
            $http.delete(`/rest/users/${item.username}`).then(resp => {
                let index = $scope.items.findIndex(p => p.username == item.username);
                $scope.items.splice(index, 1);
                $scope.reset();
                alert('Xóa người dùng thành công');
            }).catch(error => {
                alert('Lỗi khi tạo người dùng : ' + error.message);
                console.log("Ërror", error);
            });
        }
    };

    $scope.initialize();

    $scope.grant_authority = function (authority) {
        $http.post(`/rest/authorities`, authority).then(resp => {
            alert("Cập nhập quyền sử dùng thành công");
        }).catch(error => {
            alert("Cập nhập quyền sử dụng thất bại");
            console.log("Error", error);
        });
    };

    $scope.pager = {
        page: 0,
        size: 10,
        get items() {
            let start = this.page * this.size;
            return $scope.items.slice(start, start + this.size);
        },
        get count() {
            return Math.ceil(1.0 * $scope.items.length / this.size);
        },
        first() {
            this.page = 0;
        },
        prev() {
            this.page--;
            if (this.page < 0) {
                this.last();
            }
        },
        next() {
            this.page++;
            if (this.page >= this.count) {
                this.first();
            }
        },
        last() {
            this.page = this.count - 1;
        }
    };


});