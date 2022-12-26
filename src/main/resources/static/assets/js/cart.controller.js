app.controller("shopping-cart-ctrl", shoppingCartCtrl);

function shoppingCartCtrl($scope, $http, $window) {

    $scope.userPrincipal = {};
    $scope.cartOrderType = '';
    $scope.checkoutSortUI = [
        { code: 'price_asc', name: 'Giá (Tăng dần)' },
        { code: 'price_desc', name: 'Giá (Giảm dần)' },
    ];
    $scope.voucherInput = '';
    $scope.voucherResponse = {};

    $scope.get_user_pricipal = function () {
        $http.get("/rest/users/principal").then(resp => {
            $scope.userPrincipal = resp.data;
            if ($window.location.pathname === '/order/checkout') {
                if (resp.data.address === null || resp.data.phone === null || resp.data.fullname === null) {
                    alert('Cần cung cấp thông tin địa chỉ, họ tên và SDT để tiến hành thanh toán!');
                    $window.location.href = '/account/editprofile';
                }
            }
            // localStorage.setItem("user", JSON.stringify(resp.data.id));
        }).catch(error => {
            console.log("Error", error);
        });
    };



    $scope.initialize = function () {
        if ($window.location.pathname === '/order/checkout' || $window.location.pathname === '/cart/view') {
            if ($scope.cart.items.length === 0) {
                alert('Giỏ hàng bạn đang trống!');
                $window.location.href = '/';
            }
        }

        $scope.get_user_pricipal();
    };

    $scope.cart = {
        items: [],
        //Thêm
        add(id) {
            let item = this.items.find(item => item.id == id);
            if (item) {
                item.qty++;
                this.saveToLocalStorage();
            } else {
                $http.get(`/rest/products/${id}`).then(resp => {
                    resp.data.qty = 1;
                    this.items.push(resp.data);
                    this.saveToLocalStorage();
                });
            }
        },
        //Xóa sản phẩm
        remove(id) {
            let index = this.items.findIndex(item => item.id == id);
            this.items.splice(index, 1);
            this.saveToLocalStorage();
            if (this.items.length === 0) {
                alert('Bạn không có sản phẩm trong giỏ!');
                location.href = '/';
            }
        },
        //Dọn giỏ hàng
        clear() {
            this.items = [];
            this.saveToLocalStorage();
        },
        ammount_of(item) {
        },
        //Tính Tổng SP trong giỏ hàng
        get count() {
            return this.items
                .map(item => item.qty)
                .reduce((total, qty) => total += qty, 0);
        },
        //Tính tổng giá tiền trong giỏ hàng
        get amount() {
            return this.items
                .map(item => item.qty * (item.price - (item.price * item.sale_off / 100)))
                .reduce((total, qty) => total += qty, 0);
        },
        //Lưu giỏ hàng đổi sang Json
        saveToLocalStorage() {
            let json = JSON.stringify(angular.copy(this.items));
            localStorage.setItem('cart', json);

        },
        //Đọc giỏ hàng từ Local Storage
        loadFromLocalStorage() {
            let json = localStorage.getItem('cart');
            this.items = json ? JSON.parse(json) : [];
        }
    };
    $scope.cart.loadFromLocalStorage();

    $scope.sortCheckoutItems = function (orderType) {
        if (orderType === 'price_asc') {
            $scope.cartOrderType = 'price';
        } else if (orderType === 'price_desc') {
            $scope.cartOrderType = '-price';
        }
    };

    $scope.checkVoucherValid = function (voucherCode) {
        $scope.voucherResponse = {};
        $http.get(`/rest/vouchers/code/${voucherCode}`).then(resp => {
            if (resp.data.isDeleted) {
                $scope.voucherResponse = {};
            } else {
                console.log('voucher exist and not deleted');
                let todayDate = new Date();
                let voucherEndDate = new Date(resp.data.endDate);
                let voucherStartDate = new Date(resp.data.startDate);
                if (todayDate > voucherEndDate || todayDate < voucherStartDate) {
                    $scope.voucherResponse = {};
                    console.log('voucher hết hạn!');
                } else if (todayDate < voucherEndDate && todayDate > voucherStartDate) {
                    $scope.voucherResponse = resp.data;
                    console.log('voucher còn hạn!');
                }
            }

        }).catch(err => {
            $scope.voucherResponse = {};
            console.log("voucher didn't exist!!!",err);
        });
    };

    $scope.isObjEmpty = function (obj) {
        return Object.keys(obj).length === 0;
    };

    $scope.semdEmailReceipt = function (orderObj) {
        $http.post('/rest/orders/send-email-receipt', orderObj).then(res => {
            if (res.status === 200) {
                alert('Đã gửi email hóa đơn!');
            }
        }).catch(err => {
            console.error("Lỗi gửi hóa đơn email!", err);
        });
    };

    $scope.order = {
        // createdAt: new Date(),
        total: 0.0,
        address: '',
        get order_details() {
            return $scope.cart.items.map(item => {
                return {
                    product: { id: item.id },
                    price: parseFloat(item.price),
                    quantity: parseInt(item.qty),
                };
            });
        },
        total_cost() {
            return $scope.cart.amount + (this.payment_method == 'cod' ? 20000 : 0) +
                (!$scope.isObjEmpty($scope.voucherResponse) ? -($scope.cart.amount * $scope.voucherResponse.value / 100) : 0);
        },
        purchase() {
            let order = angular.copy(this);
            if (!$scope.isObjEmpty($scope.voucherResponse)) {
                // thêm property voucher vào object order
                order.voucher = { id: null, code: null };
                order.voucher.id = $scope.voucherResponse.id;
                order.voucher.code = $scope.voucherResponse.code;
            }
            order.total = $scope.order.total_cost();
            // console.log(order);
            if ($scope.cart.count === 0) {
                alert("Có lỗi khi tạo hóa đơn hoặc giỏ hàng của bạn đang trống, xin hãy thử lại!");
            } else {
                if (order.payment_method === "paypal") {
                    $http.post('/paypal', order).then(res => {
                        $scope.cart.clear();
                        location.href = res.data.returned_url;
                    }).catch(err => {
                        alert("Có lỗi khi tạo hóa đơn hoặc giỏ hàng của bạn đang trống, xin hãy thử lại!");
                        console.log(err);
                    });
                } else if (order.payment_method === "cod") {
                    $http.post('/rest/orders', order).then(res => {
                        alert("Đặt hàng thành công, chúng tôi sẽ gửi mail hóa đơn vào địa chỉ email của bạn!");
                        // $scope.semdEmailReceipt(res.data);
                        $scope.cart.clear();
                        location.href = "/order/list";
                    }).catch(err => {
                        alert("Có lỗi khi tạo hóa đơn hoặc giỏ hàng của bạn đang trống, xin hãy thử lại!");
                        console.log(err);
                    });
                }
            }
        }
    };

    $scope.initialize();


}