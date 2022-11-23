app.controller("voucher-ctrl", function ($scope, $http, $filter) {
    $scope.list_vouchers = [];
    $scope.detail_vouchers = [];
    $scope.form = {};
    $scope.form_new = {};
    $scope.user = {};

    $scope.initialize = function () {
        $http.get(`/rest/voucher`).then(resp => {
            $scope.list_vouchers = resp.data;
        }).catch(error => {
            console.log("Error", error);
        });
    };

    $scope.initialize();

    $scope.edit = function(voucher){

        $scope.form = angular.copy(voucher);
        $(".nav-tabs a:eq(1)").tab('show');
    }

    $scope.reset = function () {
        $scope.form = {};
    };

    $scope.create = function (){
        var voucher = angular.copy($scope.form_new);
        $http.post(`/rest/voucher`,voucher).then(resp =>{
            $scope.list_vouchers.push(resp.data);
            $scope.reset();
            $scope.initialize();
            alert('Tạo voucher mới thành công');
        }).catch(error =>{
            alert('Lỗi khi tạo voucher : ' + error.message);
            console.log("Error", error);
        })
    };

    $scope.delete = function(voucher){
        let check = confirm(`Bạn có chắn chắc muốn xóa voucher này không ${voucher.code}`);
        if(check){
            $http.delete(`/rest/voucher/${voucher.id}`).then(resp =>{
                let index = $scope.list_vouchers.findIndex(p => p.code == voucher.code);
                $scope.list_vouchers.splice(index,1);
                $scope.reset();
                $scope.initialize();
                alert('Xóa voucher thành công');
            }).catch(error =>{
                alert('Lỗi khi xóa voucher : ' + error.message);
                console.log("Error",error);
            })
        }
    };

    $scope.update = function() {
        let item = angular.copy($scope.form);
        console.log(item)
        let check = confirm(`Bạn có chắc chắn cập nhật voucher này không ?`);
        if (check) {
            $http.put(`/rest/voucher/${item.id}`, item).then(resp => {
                let index = $scope.list_vouchers.findIndex(item => item.id == item.id);
                $scope.list_vouchers[index] = item;
                $scope.initialize();
                $scope.reset();
                alert("Cập nhật thành công ");
            }).catch(error => {
                alert("Lỗi khi cập nhật voucher: " + error.message);
                console.log("Error", error);
            });
        }

    };

    $scope.pager = {
        page: 0,
        size: 10,
        get items() {
            let start = this.page * this.size;
            return $scope.list_vouchers.slice(start, start + this.size);
        },
        get count() {
            return Math.ceil(1.0 * $scope.list_vouchers.length / this.size);
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