app.controller("favorite-ctrl", favoriteCtrl);

function favoriteCtrl($scope, $http, $window) {
    $scope.favoriteListLoading = true;
    $scope.favoriteProductList = [];
    $scope.userPrincipal = {};

    $scope.initialize = function () {
        $scope.getFavoriteProductList();
    };

    $scope.getFavoriteProductList = function () {
        $http.get("/rest/users/principal").then(resp => {
            $scope.userPrincipal = resp.data;
            localStorage.setItem("userPrincipal", JSON.stringify(resp.data));
            $http.get(`/rest/favorites/userId/${$scope.userPrincipal.id}`).then(res => {
                $scope.favoriteProductList = res.data;
            }).catch(error => {
                console.error(error);
            }).finally(function () {
                $scope.favoriteListLoading = false;
            });
        }).catch(error => {
            console.log("Error", error);
        });
    };

    $scope.addToFavorite = function (productId) {
        let userId = $scope.userPrincipal.id || JSON.parse(localStorage.getItem('userPrincipal')).id;
        $scope.favoriteObj = {
            user: {
                id: userId
            },
            product: {
                id: productId
            }
        };
        if ($scope.userPrincipal.id === undefined) {
            alert("Bạn chưa đăng nhập!");
        } else {
            // nếu sp yêu thích có tồn tại api trả về true, ko tồn tại trả false
            $http.get(`/rest/favorites/checkExist?userId=${$scope.userPrincipal.id}&productId=${productId}`).then(resp => {
                if (resp.data === true) {
                    alert('Bạn đã thêm sản phẩm này vào danh sách yêu thích rồi!');
                } else {
                    $http.post(`/rest/favorites`, $scope.favoriteObj).then(resp => {
                        $scope.initialize();
                        alert("Thêm sản phẩm vào danh sách yêu thích thành công!");
                    }).catch(error => {
                        alert("Lỗi thêm sản phẩm yêu thích");
                        console.log("Error", error);
                    });
                }
            }).catch(error => {
                alert("Lỗi check sản phẩm có tồn tại hay không!");
                console.log("Error", error);
            });
        }
    };

    $scope.removeFromFavorite = function (favoriteId) {
        $http.delete(`/rest/favorites/${favoriteId}`).then(res => {
            let index = $scope.favoriteProductList.findIndex(fav => fav.id == favoriteId);
            $scope.favoriteProductList.splice(index, 1);
            alert("Xóa sản phẩm khỏi danh sách yêu thích thành công!");
        }).catch(error => {
            alert("Có lỗi khi Xóa sản phẩm khỏi danh sách yêu thích!");
            console.error(error);
        });
    };

    $scope.favoritePager = {
        page: 0,
        size: 10,
        get favoriteProductList() {
            // thêm property "favoriteId" vào list favorite để ng-repeat ở product/favorite.html tiện, ví dụ:
            // [
            //     {
            //         id: 232323, -------------------
            //         product:{                     - 
            //             ...                       -
            //             favoriteId: 232323,    <--- 
            //             ...
            //         }
            //     },
            //     ...
            // ]
            let start = this.page * this.size;
            let tmp = $scope.favoriteProductList.map(fav => {
                fav.product.favoriteId = fav.id;
                return fav.product;
            });
            return tmp.slice(start, start + this.size);
        },
        get count() {
            return Math.ceil(1.0 * $scope.favoriteProductList.length / this.size);
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

    $scope.initialize();


}