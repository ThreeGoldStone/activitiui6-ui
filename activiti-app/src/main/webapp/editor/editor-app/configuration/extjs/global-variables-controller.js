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
        $scope.myGlobalVarables = angular.copy($scope.globalVariables);
        let valueConfig = $scope.selectedItem.valueConfig;
        myForEach($scope.myGlobalVarables, function (element) {
            treeDiGui(element.jsonTree, function (e) {
                e.$$isDisabled = e.type != $scope.selectedItem.type;
            });
        });
        if (valueConfig != null) {
            myForEach($scope.myGlobalVarables, function (element) {
                if (element.resourceId == valueConfig.resourceId) {
                    element.$$isExpend = true;
                    singleCheckByIdWhithParentExpend(element.jsonTree, valueConfig.path);
                    element.$$break = true;
                }

            });
            //
            // let split = bindParam.split('.');
            // let taskId = split[0];
            // let variableName = split[1];
            // if (bindParam != null) {
            //     myForEach($scope.globalVariables, function (task) {
            //         if (taskId == task.id) {
            //             myForEach(task.fields, function (field) {
            //                 if (field.name == variableName) {
            //                     field.$$isChecked = true;
            //                     task.$$isExpend = true;
            //                     $scope.selectField = field;
            //                 } else {
            //                     field.$$isChecked = false;
            //                 }
            //             });
            //             task.$$break = true;
            //         }
            //
            //     });
            // }
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
            $scope.selectField = {};
            myForEach($scope.myGlobalVarables, function (element) {
                let jsonTree = element.jsonTree;
                let resourceId = element.resourceId;
                treeDiGuiWithParent(null, jsonTree, function (parents, item) {
                    if ($item == item) {
                        item.$$isChecked = true;
                        $scope.selectField.name = element.name || element.overrideid || element.resourceId;
                        $scope.selectField.resourceId = resourceId;
                        $scope.selectField.stencilid = element.stencilid;
                        $scope.selectField.path = item.path;
                    } else {
                        item.$$isChecked = false;
                    }
                });
            });
            if ($item.$$isChecked && $scope.selectField != null && ($scope.selectField != $item)) {
                $scope.selectField.$$isChecked = false;
            }
            // $scope.selectField = $item;
            // $http.post('/url', $item);
            console.log($item, 'item checked');
        };
        // Click handler for save button
        $scope.save = function () {
            if ($scope.selectField != null && $scope.selectField.name != null) {
                // myForEach($scope.globalVariables, function (task) {
                //     myForEach(task.fields, function (field) {
                //         if (field == $scope.selectField) {
                //             $scope.selectedItem.bindParam = task.id + '.' + $scope.selectField.name;
                //             field.$$break = true;
                //             task.$$break = true;
                //         }
                //     });
                // });
                $scope.selectedItem.valueConfig = $scope.selectField;
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
        $scope.changeAllOpenStatus = function (status) {
            // changeAll($scope.requestParamTree, status);
            myForEach($scope.myGlobalVarables, function (element) {

                treeDiGui(element.jsonTree, function (e) {
                    e.$$isExpend = status;
                });
                element.$$isExpend = status;
            });


        };
        $scope.cancel = function () {
            $scope.close();
        };

        // Close button handler
        $scope.close = function () {
            $scope.$hide();
        };
    }]);
