/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Task listeners
 */

angular.module('activitiModeler').controller('GlobalVariablesPopupCtrl',
    ['$scope', '$q', '$translate', '$timeout', '$http', function ($scope, $q, $translate, $timeout, $http) {
        // $scope.globalVariables
        $scope.selectField = {};
        // 初始化已选择的数据
        let bindParam = $scope.selectedItem.bindParam;
        let split = bindParam.split('.');
        let taskId = split[0];
        let variableName = split[1];
        if (bindParam != null) {
            myForEach($scope.globalVariables, function (task) {
                if (taskId == task.id) {
                    myForEach(task.fields, function (field) {
                        if (field.name == variableName) {
                            field.$$isChecked = true;
                            task.$$isExpend = true;
                            $scope.selectField = field;
                        } else {
                            field.$$isChecked = false;
                        }
                    });
                    task.$$break =true;
                }

            });
        }
        // if ($scope.property.value != null) {
        //     djlLoop:
        //         for (var j = 0, len = $scope.serviceList.length; j < len; j++) {
        //             var app = $scope.serviceList[j];
        //             for (var i = 0, len = app.serviceList.length; i < len; i++) {
        //                 var service = app.serviceList[i];
        //                 if (service.serviceid == $scope.property.value.serviceid) {
        //                     $scope.service = service;
        //                     $scope.service.$$isChecked = true;
        //                     app.$$isExpend = true;
        //                     break djlLoop;
        //                 }
        //             }
        //         }
        // }


        var namePromise = $translate('PROPERTY.FIELDS.NAME');
        var implementationPromise = $translate('PROPERTY.FIELDS.IMPLEMENTATION');

        $q.all([namePromise, implementationPromise]).then(function (results) {
            // $scope.labels.nameLabel = results[0];
            // $scope.labels.implementationLabel = results[1];
            // $scope.translationsRetrieved = true;

        });
        $scope.getItemIcon = function (item) {
            return item.$$isExpend ? 'fa fa-minus' : 'fa fa-plus';
        };
        $scope.itemExpended = function (item, $event) {
            item.$$isExpend = !item.$$isExpend;
            $event.stopPropagation();
        };
        $scope.itemCheckedChanged = function ($item) {
            // 实现单选
            if ($item.$$isChecked && $scope.selectField != null && ($scope.selectField != $item)) {
                $scope.selectField.$$isChecked = false;
            }
            $scope.selectField = $item;
            // $http.post('/url', $item);
            console.log($item, 'item checked');
        };
        // Click handler for save button
        $scope.save = function () {
            if ($scope.selectField != null && $scope.selectField.name != null) {
                djlloop:
                    myForEach($scope.globalVariables, function (task) {
                        myForEach(task.fields, function (field) {
                            if (field == $scope.selectField) {
                                $scope.selectedItem.bindParam = task.id + '.' + $scope.selectField.name;
                                field.$$break = true;
                                task.$$break = true;
                            }
                        });
                    });
            }
            // if ($scope.service.serviceid !== undefined && $scope.service.serviceid !== null) {
            //     $scope.property.value = {};
            //     $scope.property.value = $scope.service;
            // } else {
            //     $scope.property.value = null;
            // }
            //
            // $scope.updatePropertyInModel($scope.property);
            $scope.close();
        };

        $scope.cancel = function () {
            $scope.close();
        };

        // Close button handler
        $scope.close = function () {
            $scope.$hide();
        };
    }]);
