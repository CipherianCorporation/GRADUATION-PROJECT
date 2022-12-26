app.controller("order-ctrl", function ($scope, $http) {
    $scope.list_items = [];
    $scope.detail_items = [];
    $scope.form = {};
    $scope.user = {};
    $scope.searchTextOrder = '';

    $scope.initialize = function () {
        $scope.loading = true;
        $http.get(`/rest/order/sortstatus`).then(resp => {
            $scope.list_items = resp.data;
        }).catch(error => {
            console.log("Error", error);
        }).finally(function () {
            $scope.loading = false;
        });
    };

    $scope.initialize();
    
    $scope.update_status = function (item) {
        item.orderStatus.name = "success";
        $http.put(`/rest/order/orderstatus/${item.id}`, item).then(resp => {
            $scope.initialize();
            alert("Cập nhập trạng thái đơn hàng thành công")
        }).catch(error=>{
            alert("Lỗi cập nhập trạng thái");
            $scope.initialize();
            console.log("Error", error);
        });
    };

    $scope.order_detail = function (item) {
        $scope.detailLoading = true;
        $scope.detail_items = [];
        $(".nav-tabs a:eq(1)").tab('show');
        $http.get(`/rest/order/detail/${item.id}`).then(resp => {
            $scope.detail_items = resp.data;
            console.log($scope.detail_items)
        }).catch(error => {
            console.log("Error", error);
        }).finally(function () {
            $scope.detailLoading = false;
        });

    };


    $scope.pager = {
        page: 0,
        size: 10,
        get items() {
            let start = this.page * this.size;
            return $scope.list_items.slice(start, start + this.size);
        },
        get count() {
            return Math.ceil(1.0 * $scope.list_items.length / this.size);
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