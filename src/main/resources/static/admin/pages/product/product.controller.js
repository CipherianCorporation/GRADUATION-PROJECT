app.controller("product-ctrl", function ($scope, $http) {
    $scope.items = [];
    $scope.searchTextProduct = '';
    $scope.categories = [];
    $scope.sub_categories = [];
    $scope.colors = [];
    $scope.sizes = [];
    $scope.saleRange = 0;
    $scope.saleRangeUI = {
        min: 0,
        max: 100,
        step: 1,
        value: 0,
    };
    $scope.newForm = {};
    $scope.editForm = {};

    $scope.initialize = function () {
        $scope.loading = true;
        $scope.items = [];
        $http.get("/rest/products").then(resp => {
            $scope.items = resp.data;
            $scope.items.forEach(item => {
                item.createdAt = new Date(item.createdAt);
            });
            localStorage.removeItem('prodList');
            localStorage.setItem('prodList', JSON.stringify($scope.items));
        }).finally(function () {
            $scope.loading = false;
        });

        $http.get("/rest/categories").then(resp => {
            $scope.categories = resp.data;
        });

        $http.get("/rest/sub-categories").then(resp => {
            $scope.sub_categories = resp.data;
        });

        $http.get("/rest/colors").then(resp => {
            $scope.colors = resp.data;
        });

        $http.get("/rest/sizes").then(resp => {
            $scope.sizes = resp.data;
        });
    };

    $scope.reset = function () {
        let tmp = {
            createdAt: new Date(),
            image: 'default-product.jpg',
            available: true,
        };
        $scope.newForm = tmp;
        $scope.editForm = tmp;
    };

    $scope.edit = function (item) {
        let tmp = $scope.items.filter((prod) => prod.id === item.id);
        let prod = tmp[0];
        $scope.editForm = prod;
        $(".nav-tabs a:eq(1)").tab('show');
    };

    $scope.create = function () {
        var item = angular.copy($scope.newForm);
        if (item.image == null) {
            item.image = 'default-product.jpg';
        }
        if (item.available == null) {
            item.available = true;
        }
        if (item.size.value == null) {
            item.size.value = null;
        }
        if (item.color.name == null) {
            item.color.name = colors[0];
        }
        if (item.category.id == null) {
            item.category.id = categories[0].id;
        }
        if (item.subCategory.id == null) {
            item.subCategory.id = sub_categories[0].id;
        }
        item.user = {
            // lấy user id từ localStorage khi dashboard.controller.js vừa chạy lên
            id: JSON.parse(localStorage.getItem('userPrincipal')).id
        };
        item.sold = 0;
        console.log(item);

        $http.post(`/rest/products`, item).then(resp => {
            resp.data.createdAt = new Date(resp.data.createdAt);
            $scope.items.push(resp.date);
            $scope.reset();
            $scope.initialize();
            alert("Thêm mới sản phẩm thành công");
        }).catch(error => {
            alert("Lỗi thêm mới sản phẩm");
            console.log("Error", error);
        });
    };

    $scope.update = function () {
        var item = angular.copy($scope.editForm);
        item.updatedAt = new Date();
        let check = confirm(`Are you sure to update this product?`);
        console.log(item);
        if (check) {
            $http.put(`/rest/products/${item.id}`, item).then(resp => {
                var index = $scope.items.findIndex(p => p.id == item.id);
                $scope.items[index] = item;
                $scope.initialize();
                alert("Cập nhập sản phẩm thành công");
            }).catch(error => {
                alert("Lỗi cập nhập sản phẩm");
                console.log("Error", error);
            });
        }
    };

    $scope.delete = function (item) {
        let check = confirm(`Are you sure to delete this product ${item.name}?`);
        if (check) {
            $http.delete(`/rest/products/${item.id}`).then(resp => {
                let index = $scope.items.findIndex(p => p.id == item.id);
                $scope.items.splice(index, 1);
                $scope.initialize();
                alert("Xóa sản phẩm thành công");
            }).catch(error => {
                alert("Xóa sản phẩm thất bại");
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
            $scope.newForm.image = resp.data.name;
            $scope.editForm.image = resp.data.name;
        }).catch(error => {
            alert("Lối upload hình ảnh");
            console.log("Error", error);
        });
    };
    $scope.initialize();

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
    console.log($scope.pager.items);
});


