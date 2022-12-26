
app.controller("order-ctrl", orderCtrl);

function orderCtrl($scope, $http, $window) {
    $scope.list_items = [];
    $scope.items = [];
    $scope.detail_items = [];
    $scope.form = {};
    $scope.user = {};
    $scope.searchTextOrder = '';

    $scope.swich_order_list = function () {
        var url = "http://" + $window.location.host + "/order/list";
        $window.location.href = url;
    };

    $scope.get_user_id = function () {
        $http.get("/rest/users/principal").then(resp => {
            $scope.user = resp.data;
            localStorage.setItem("user", JSON.stringify(resp.data.id));
        }).catch(error => {
            console.log("Error", error);
        });
    }


    $scope.initializez = function () {
        $scope.loading = true;
        let list;
        if (localStorage.getItem('list') !== null) {
            list = JSON.parse(localStorage.getItem('list'));
        }

        let id;
        if (localStorage.getItem('user') !== null) {
            id = JSON.parse(localStorage.getItem('user'));
        }

        $http.get(`/rest/order/list/${id}`).then(resp => {
            $scope.list_items = resp.data;
        }).catch(error => {
            console.log("Error", error);
        }).finally(function () {
            $scope.loading = false;
        });

        $scope.detail(list);
    };

    $scope.switchPage = function (list) {
        localStorage.setItem("list", JSON.stringify(list.id));
        var url = "http://" + $window.location.host + "/order/detail";
        $window.location.href = url;
    };

    $scope.detail = function (list) {
        $http.get(`/rest/order/detail/${list}`).then(resp => {
            $scope.detail_items = resp.data;
            $scope.form = angular.copy($scope.detail_items[0]);
        }).catch(error => {
            console.log("Error", error);
        });
    };

    $scope.check = function () {
        $scope.get_user_id();
        if (localStorage.getItem('user') == null || localStorage.getItem('user') == "undefined") {
            return ;
        }
        $scope.initializez();
    };

    $scope.check();

    $scope.update_status = function (item) {
        item.orderStatus.name = "cancel";
        let check = confirm(`Bạn có muốn hủy đơn ${item.id}?`);
        if (check) {
            $http.put(`/rest/order/orderstatus/${item.id}`, item).then(resp => {
                $scope.initialize();
                alert("Cập nhập trạng thái đơn hàng thành công");
            }).catch(error => {
                alert("Lỗi cập nhập trạng thái");
                $scope.initialize();
                console.log("Error", error);
            });
        }
    };

    $scope.find_order = function (id){
        console.log(id)
        $http.get(`/rest/order/${id}`).then(resp => {
            $scope.form =  resp.data[0];
            console.log($scope.form)
            if ($scope.form === undefined){
                alert("Đơn hàng không tồn tại")
            }
        }).catch(error => {
            console.log("Error", error);
        }).finally(function () {
            $scope.loading = false;
        });
    }

    $scope.update_status_for_shipper = function (item) {
        item.orderStatus.name = "success";
        let check = confirm(`Bạn đã hoàn thành đơn hàng ${item.id}?`);
        if (check) {
            $http.put(`/rest/order/orderstatus/${item.id}`, item).then(resp => {
                $scope.initialize();
                alert("Cập nhập trạng thái đơn hàng thành công");
            }).catch(error => {
                alert("Lỗi cập nhập trạng thái");
                $scope.initialize();
                console.log("Error", error);
            });
        }
    };
}