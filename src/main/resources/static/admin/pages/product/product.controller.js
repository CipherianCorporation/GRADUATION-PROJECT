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
            // lọc tất cả product isDeleted là false
            $scope.items = resp.data.filter((p) => p.isDeleted === false);
            $scope.items.forEach(item => {
                item.createdAt = new Date(item.createdAt);
            });
        }).finally(function () {
            $scope.loading = false;
        });

        $http.get("/rest/categories").then(resp => {
            $scope.categories = resp.data.filter((c) => c.isDeleted === false);
        });

        $http.get("/rest/sub-categories").then(resp => {
            $scope.sub_categories = resp.data.filter((s) => s.isDeleted === false);
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
        let tmp = $scope.items.filter((p) => p.id === item.id);
        let prod = tmp[0];
        $scope.editForm = prod;
        $(".nav-tabs a:eq(1)").tab('show');
    };

    $scope.create = function () {
        let item = angular.copy($scope.newForm);
        let isDuplicate = false;
        if (item.image == null) {
            item.image = 'default-product.jpg';
        }
        if (item.stock == null) {
            item.stock = 0;
        }
        if (item.isDeleted == null) {
            item.isDeleted = false;
        }
        if (item.available == null) {
            item.available = true;
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
            // lấy user id từ localStorage khi main.controller.js vừa chạy lên
            id: JSON.parse(localStorage.getItem('userPrincipal')).id
        };
        item.sold = 0;
        $scope.items.forEach((p) => {
            if (p.name === item.name) {
                isDuplicate = true;
            }
        });
        if (isDuplicate) {
            alert("Tên sản phẩm bị trùng!");
        } else {
            $http.post(`/rest/products`, item).then(resp => {
                resp.data.createdAt = new Date(resp.data.createdAt);
                $scope.items.push(resp.data);
                $scope.reset();
                $scope.initialize();
                alert("Thêm mới sản phẩm thành công");
            }).catch(error => {
                alert("Lỗi thêm mới sản phẩm");
                console.log("Error", error);
            });
        }

        // cách kiểm tra trùng name bằng api => ko đc vì nó kiểm tra luôn mấy prod có isDeleted = true

        // thay khoảng trắng bằng %20 và trim() trong product name mới gửi request đc
        // nếu array trả về length là 0 => ko tìm thấy name => name ko bị trùng nên tạo prod mới đc
        // let prodName = item.name.replace(/\s/g, '%20').trim();
        // $http.get(`/rest/products/name/${prodName}`, item).then(resp => {
        //     if (resp.data.length === 0) {
        //         $http.post(`/rest/products`, item).then(resp => {
        //             resp.data.createdAt = new Date(resp.data.createdAt);
        //             $scope.items.push(resp.data);
        //             $scope.reset();
        //             $scope.initialize();
        //             alert("Thêm mới sản phẩm thành công");
        //         }).catch(error => {
        //             alert("Lỗi thêm mới sản phẩm");
        //             console.log("Error", error);
        //         });
        //     } else {
        //         alert("Tên sản phẩm bị trùng!");
        //     }
        // }).catch(error => {
        //     console.log("Lỗi tìm sp bằng name", error);
        // });
    };

    $scope.update = function () {
        let item = angular.copy($scope.editForm);
        item.updatedAt = new Date();
        item.isDeleted = false;
        let check = confirm(`Bạn có muốn cập nhật sản phẩm này không?`);
        if (check) {
            $http.put(`/rest/products/${item.id}`, item).then(resp => {
                let index = $scope.items.findIndex(p => p.id == item.id);
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
        let check = confirm(`Bạn có muốn xóa sản phẩm ${item.name}?`);
        if (check) {
            $http.delete(`/rest/products/${item.id}`).then(resp => {
                let index = $scope.items.findIndex(p => p.id == item.id);
                $scope.items.splice(index, 1);
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
});


