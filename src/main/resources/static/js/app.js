var app = angular.module('myApp', [])
  .directive('tabs', function() {
    return {
      restrict: 'E',
      transclude: true,
      template: '<ul id="myTab" class="nav nav-tabs">' +
                '<li ng-class="{active: activeTab == tab}" ng-repeat="tab in tabs"><a ng-click="setActive(tab)">{{tab}}</a></li>' +
                '</ul>' + 
                '<div class="tab-content" ng-transclude></div>',
      controller: function($scope) {
        $scope.tabs = [];
        $scope.activeTab = '';
        
        $scope.setActive = function(tab) {
          $scope.activeTab = tab;
        };
        
        this.register = function(tabName) {
          $scope.tabs.push(tabName);
          if (!$scope.activeTab) {
            $scope.setActive(tabName);
          }
        };
        
        this.isActive = function(tabName) {
          return tabName == $scope.activeTab;
        };
      }
    }  
  })
  .directive('pane', function() {
    return {
      restrict: 'E',
      require: '^tabs',
      template: '<div class="tab-pane" ng-show="isActive(title)" ng-transclude></div>',
      transclude: true,
      scope: {
        title: '@'
      },
      link: function(scope, element, attrs, tabsController) {
        tabsController.register(scope.title);
        scope.isActive = function(tabName) {
          return tabsController.isActive(tabName);
        };
      }
    };
  })
  .directive('fileModel', ['$parse', function ($parse) {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        var model = $parse(attrs.fileModel);
        var modelSetter = model.assign;
        element.bind('change', function() {
          scope.$apply(function() {
            modelSetter(scope, element[0].files[0]);
          });
        });
      }
    };
  }])
  .directive('product', [
    '$http',
    function($http) {
      return {
        templateUrl: '../views/product.html',
        restrict: 'E',
        link: function(scope, element, attrs) {
          var getData = function(){
            $http.get('http://localhost:8080/api/products').then(function(data,status) {
              scope.products = data.data
            })
          }
          getData()
          scope.uploadCSV = function() {
            const formData = new FormData();
            formData.append('file', scope.myFile);
            try{
              var headers =  {"Content-type": "application/csv"}
              axios.post( 'http://localhost:8080/api/products/upload_csv', formData, headers );
            }catch(err){
              console.log(err)
            }finally{
              alert("uploaded, please refresh")
            }
          }
          scope.addProduct = function() {
            scope.products.push({type: "new", isEditing: true})
          }
          scope.editProduct =function(product){
            product.isEditing = true
          }
          scope.saveProduct = function(product) {
            if(product.type === 'new'){
              var productData =angular.copy(product)
              $http.post('http://localhost:8080/api/products/', productData).then(function(data,status) {
                alert('Created');
                getData();
              })
            }else{
            var productData = angular.copy(product)
             $http.put('http://localhost:8080/api/products/' + product.code, productData).then(function(data,status) {
                alert('Updated');
                getData();
              })
            }
            product.isEditing = false
          }
          scope.deleteProduct = function(product){
            $http.delete('http://localhost:8080/api/products/'+ product.code).then(function(data,status) {
              alert(product.code+ 'deleted');
              getData()
            })
          }
          scope.downloadProductCSV=function() {
           axios({
             url: 'http://localhost:8080/api/products/download_csv',
             method: 'GET',
             responseType: 'blob',
           }).then((response) => {
             const url = window.URL.createObjectURL(new Blob([response.data]));
             const link = document.createElement('a');
             link.href = url;
             link.setAttribute('download', 'products.csv');
             document.body.appendChild(link);
             link.click();
           });
          }
        }
      };
    }
  ])
  .directive('stock', [
    '$http',
    function($http) {
      return {
        templateUrl: '../views/stock.html',
        restrict: 'E',
        link: function(scope, element, attrs) {
          var getData = function(){
            $http.get('http://localhost:8080/api/stocks').then(function(data,status) {
              scope.stocks = data.data
            })
          }
          getData()
          scope.addStock = function() {
            scope.stocks.push({type: "new", isEditing: true})
          }
          scope.editStock = function(stock){
            stock.isEditing = true
          }
          scope.uploadCSV = function() {
            const formData = new FormData();
            formData.append('file', scope.myFile);
            try{
              var headers =  {"Content-type": "application/csv"}
              axios.post( 'http://localhost:8080/api/stocks/upload_csv', formData, headers );
            }catch(err){
              console.log(err)
            }finally{
              alert("uploaded, please refresh")
            }
          }
          scope.saveStock = function(stock) {
            if(stock.type === 'new'){
              var stockData =angular.copy(stock)
              $http.post('http://localhost:8080/api/stocks/', stockData).then(function(data,status) {
                alert('Created');
                getData();
              })
            }else{
            var stockData = angular.copy(stock)
              $http.put('http://localhost:8080/api/stocks/updateQt/' + stock.code + '/' + stock.location, stockData).then(function(data,status) {
                alert('Updated');
                getData();
              })
            }
            stock.isEditing = false
          }
          scope.transferStock = function(stock){
            stock.isTransfering = true
          }
          scope.submitTransfer = function(stock){
            $http.put('http://localhost:8080/api/stocks/'+ stock.code + '/' + stock.location + '/' + stock.targetLocation + '/' + stock.transferQuantity).then(function(data,status) {
              alert('Done');
              getData();
            })
          }
          scope.deleteStock = function(stock){
            $http.delete('http://localhost:8080/api/stocks/'+ stock.code+ '/' + stock.location).then(function(data,status) {
              alert(stock.code+ 'deleted');
              getData()
            })
          }
          scope.downloadStockCSV = function() {
           axios({
             url: 'http://localhost:8080/api/stocks/download_csv',
             method: 'GET',
             responseType: 'blob',
           }).then((response) => {
             const url = window.URL.createObjectURL(new Blob([response.data]));
             const link = document.createElement('a');
             link.href = url;
             link.setAttribute('download', 'products.csv');
             document.body.appendChild(link);
             link.click();
           });
          }
        }
      };
    }
  ]);
