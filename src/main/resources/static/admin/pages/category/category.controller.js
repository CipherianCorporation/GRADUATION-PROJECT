app.controller("category-ctrl", function ($scope, $http) {
    $scope.items = [];
    $scope.form = {};
    $scope.category = [];
    $scope.subcate = [];
    $scope.formsub = {};

    // spinner loading
    $scope.catesLoading = true;
    $scope.subCatesLoading = true;

    $scope.initialize = function () {
        $http.get("/rest/categories").then(resp => {
            $scope.category = resp.data.filter(c => c.isDeleted === false);
        }).catch(error => {
            console.log("Error", error);
        }).finally(function () {
            $scope.catesLoading = false;
        });

        $http.get("/rest/sub-categories").then(resp => {
            $scope.subcate = resp.data.filter(s => s.isDeleted === false);
        }).catch(error => {
            console.log("Error", error);
        }).finally(function () {
            $scope.subCatesLoading = false;
        });
    };

    $scope.initialize();

    $scope.create = function () {
        let item = angular.copy($scope.form);
        let isDuplicate = false;
        if (item.image_url == null) {
            item.image_url = 'default-user.jpg';
        }
        $scope.category.forEach(cates => {
            if (cates.name === item.name) {
                isDuplicate = true;
            }
        });
        if (isDuplicate) {
            alert('Tên danh mục bị trùng!');
        } else {
            $http.post(`/rest/categories`, item).then(resp => {
                $scope.category.push(resp.data);
                $scope.reset();
                alert("Thêm mới thể loại thành công !!");
            }).catch(error => {
                alert("Thêm mới thể loại thất bại !!");
                console.log("Error", error);
            });
        }
    };

    $scope.reset = function () {
        $scope.form = {
            image_url: 'default-user.jpg',
        };
    };

    $scope.resetSubCate = function () {
        $scope.formsub = {};
    };

    $scope.edit = function (item) {
        $scope.form = angular.copy(item);
        $(".nav-tabs a:eq(1)").tab('show');
    };

    $scope.update = function () {
        var item = angular.copy($scope.form);
        item.updatedAt = new Date();
        let check = confirm(`Bạn có chắc chắn sửa thể loại này không ?`);
        if (check) {
            $http.put(`/rest/categories/${item.id}`, item).then(resp => {
                var index = $scope.category.findIndex(p => p.id == item.id);
                $scope.category[index] = item;
                $scope.initialize();
                $scope.reset();
                alert("Cập nhập thể loại thành công");
            }).catch(error => {
                alert("Lỗi cập nhập thể loại");
                console.log("Error", error);
            });
        }
    };

    $scope.delete = function (item) {
        let check = confirm(`Bạn có chắc chắn xóa thể loại ${item.name} này không?`);
        if (check) {
            $http.delete(`/rest/categories/${item.id}`).then(resp => {
                let index = $scope.category.findIndex(p => p.id == item.id);
                $scope.category.splice(index, 1);
                $scope.initialize();
                alert("Xóa thể loại thành công");
            }).catch(error => {
                alert("Xóa thể loại thất bại");
                console.log("Error", error);
            });
        }
    };

    $scope.imageChanged = function (files) {
        let data = new FormData();
        data.append('file', files[0]);
        $http.post('/rest/upload/images', data, {
            transformRequest: angular.identity,
            headers: { 'Content-Type': undefined }
        }).then(resp => {
            $scope.form.image_url = resp.data.name;
        }).catch(error => {
            alert("Lối upload hình ảnh");
            console.log("Error", error);
        });
    };

    $scope.createSubCate = function () {
        let item = angular.copy($scope.formsub);
        let isDuplicate = false;
        $scope.category.forEach(cates => {
            if (cates.name === item.name) {
                isDuplicate = true;
            }
        });
        if (isDuplicate) {
            alert('Tên danh mục phụ bị trùng!');
        } else {
            $http.post(`/rest/sub-categories`, item).then(resp => {
                $scope.subcate.push(resp.data);
                $scope.resetSubCate();
                $scope.initialize();
                alert("Thêm mới danh mục thành công !!");
            }).catch(error => {
                alert("Thêm mới danh mục thất bại !!");
                console.log("Error", error);
            });
        }
    };

    $scope.updateSubCate = function () {
        var item = angular.copy($scope.formsub);
        item.updatedAt = new Date();
        let check = confirm(`Bạn có chắc chắn sửa danh mục này không ?`);
        if (check) {
            $http.put(`/rest/sub-categories/${item.id}`, item).then(resp => {
                var index = $scope.subcate.findIndex(p => p.id == item.id);
                $scope.subcate[index] = item;
                $scope.initialize();
                $scope.resetSubCate();
                alert("Cập nhập danh mục thành công");
            }).catch(error => {
                alert("Lỗi cập nhập danh mục");
                console.log("Error", error);
            });
        }
    };

    $scope.deleteSubCate = function (item) {
        let check = confirm(`Bạn có chắc chắn xóa danh mục ${item.name} này không?`);
        if (check) {
            $http.delete(`/rest/sub-categories/${item.id}`).then(resp => {
                let index = $scope.subcate.findIndex(p => p.id == item.id);
                $scope.subcate.splice(index, 1);
                $scope.initialize();
                alert("Xóa danh mục thành công");
            }).catch(error => {
                alert("Xóa danh mục thất bại");
                console.log("Error", error);
            });
        }
    };

    $scope.editSubCate = function (item) {
        $scope.formsub = angular.copy(item);
        $(".nav-tabs a:eq(1)").tab('show');
    };

    // page sub
    $scope.pager = {
        page: 0,
        size: 5,
        get subcate() {
            let start = this.page * this.size;
            return $scope.subcate.slice(start, start + this.size);
        },
        get count() {
            return Math.ceil(1.0 * $scope.subcate.length / this.size);
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

    // page category
    $scope.pagers = {
        page: 0,
        size: 3,
        get category() {
            let start = this.page * this.size;
            return $scope.category.slice(start, start + this.size);
        },
        get count() {
            return Math.ceil(1.0 * $scope.category.length / this.size);
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