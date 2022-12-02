app.controller("contact-ctrl", contactCtrl);

function contactCtrl($scope, $http, $window) {
    $scope.form = {};

    $scope.reset = function () {
        $scope.form = {};
    };

    $scope.contact = function () {
        var contact = angular.copy($scope.form);
        console.log(contact)
        $http.post("/rest/send_contact",contact).then(resp => {
            alert("Gửi liện hệ thành công")
        }).catch(error => {
            console.log("Error", error);
        });
    }
}