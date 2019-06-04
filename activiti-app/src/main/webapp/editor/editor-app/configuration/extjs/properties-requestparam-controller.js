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

angular.module('activitiModeler').controller('BpmRequestparamCtrl',
    ['$scope', '$modal', '$timeout', '$translate', function ($scope, $modal, $timeout, $translate) {

        // Config for the modal window
        var opts = {
            template: 'editor-app/configuration/extjs/properties/requestparam-popup.html?version=' + Date.now(),
            scope: $scope,
            // 可以让弹窗不关闭，除非调用hide
            backdrop: false
        };

        // Open the dialog
        _internalCreateModal(opts, $modal, $scope);
    }]);

angular.module('activitiModeler').controller('BpmRequestparamPopupCtrl',
    ['$scope', '$q', '$translate', '$timeout', '$http', function ($scope, $q, $translate, $timeout, $http) {
// javaservices的数据定义
        $scope.javaServices = [];
        $scope.isJavaServicesLoaded = false;
        $scope.currentJavaServices = {};
        $scope.currentJavaServicesFields = [];
        // grid 绑定的数组只能插入删除，指向新的数组，会导致变更无效
        $scope.pushAllJavaServices = function () {
            // 清空
            $scope.currentJavaServicesFields.splice(0,$scope.currentJavaServicesFields.length)
            // 塞入
            $scope.currentJavaServices.fields.forEach(function (field) {
                $scope.currentJavaServicesFields.push(field);
            });
        }

        $scope.selectJavaServices = function (currentJavaServices) {
            $scope.currentJavaServices = currentJavaServices;
            // $scope.currentJavaServicesFields.concat($scope.currentJavaServices.fields);
            $scope.pushAllJavaServices();
            // for (var i = 0, arrayLength = $scope.currentJavaServices.fields.length; j < arrayLength; j++)
            //     array.push(arguments[i][j]);
            console.log(currentJavaServices);
        }
        // javaservices的数据加载
        $http({
            method: 'GET',
            url: KISBPM.URL.getJavaServices()
        }).success(function (data, status, headers, config) {

            $scope.javaServices = data;
            $scope.currentJavaServices = data[0];
            // $scope.currentJavaServicesFields = angular.copy($scope.currentJavaServices.fields);
            // $scope.fields = angular.copy($scope.property.value.fields);
            $scope.pushAllJavaServices();
            $scope.isJavaServicesLoaded = true;
            console.log(data)

        }).error(function (data, status, headers, config) {
            console.log('Something went wrong when fetching java services:' + JSON.stringify(data));
        });
        // Put json representing form properties on scope
        if ($scope.property.value !== undefined && $scope.property.value !== null
            && $scope.property.value.fields !== undefined
            && $scope.property.value.fields !== null) {

            // Note that we clone the json object rather then setting it directly,
            // this to cope with the fact that the user can click the cancel button and no changes should have happened
            $scope.fields = angular.copy($scope.property.value.fields);

            for (var i = 0; i < $scope.fields.length; i++) {
                var field = $scope.fields[i];
                if (field.stringValue !== undefined && field.stringValue !== '') {
                    field.implementation = field.stringValue;
                } else if (field.expression !== undefined && field.expression !== '') {
                    field.implementation = field.expression;
                } else if (field.string !== undefined && field.string !== '') {
                    field.implementation = field.string;
                }
            }

        } else {
            $scope.fields = [];
        }

        $scope.translationsRetrieved = false;
        $scope.labels = {};

        var namePromise = $translate('PROPERTY.FIELDS.NAME');
        var implementationPromise = $translate('PROPERTY.FIELDS.IMPLEMENTATION');

        $q.all([namePromise, implementationPromise]).then(function (results) {
            $scope.labels.nameLabel = results[0];
            $scope.labels.implementationLabel = results[1];
            $scope.translationsRetrieved = true;

            // Config for grid
            $scope.gridOptions = {
                data: $scope.currentJavaServicesFields,
                // data: $scope.fields,
                headerRowHeight: 28,
                enableRowSelection: true,
                enableRowHeaderSelection: false,
                multiSelect: false,
                modifierKeysToMultiSelect: false,
                enableHorizontalScrollbar: 0,
                enableColumnMenus: false,
                enableSorting: false,
                columnDefs: [{field: 'name', displayName: $scope.labels.nameLabel},
                    {field: 'implementation', displayName: $scope.labels.implementationLabel}]
            };

            $scope.gridOptions.onRegisterApi = function (gridApi) {
                //set gridApi on scope
                $scope.gridApi = gridApi;
                gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                    $scope.selectedField = row.entity;
                });
            };
        });

        $scope.fieldDetailsChanged = function () {
            if ($scope.selectedField.stringValue != '') {
                $scope.selectedField.implementation = $scope.selectedField.stringValue;
            } else if ($scope.selectedField.expression != '') {
                $scope.selectedField.implementation = $scope.selectedField.expression;
            } else if ($scope.selectedField.string != '') {
                $scope.selectedField.implementation = $scope.selectedField.string;
            } else {
                $scope.selectedField.implementation = '';
            }
        };

        // Click handler for add button
        // $scope.addNewField = function () {
        //     var newField = {
        //         name: 'fieldName',
        //         implementation: '',
        //         stringValue: '',
        //         expression: '',
        //         string: ''
        //     };
        //
        //     $scope.fields.push(newField);
        //     $timeout(function () {
        //         $scope.gridApi.selection.toggleRowSelection(newField);
        //     });
        // };
        //
        // // Click handler for remove button
        // $scope.removeField = function () {
        //
        //     var selectedItems = $scope.gridApi.selection.getSelectedRows();
        //     if (selectedItems && selectedItems.length > 0) {
        //         var index = $scope.fields.indexOf(selectedItems[0]);
        //         $scope.gridApi.selection.toggleRowSelection(selectedItems[0]);
        //         $scope.fields.splice(index, 1);
        //
        //         if ($scope.fields.length == 0) {
        //             $scope.selectedField = undefined;
        //         }
        //
        //         $timeout(function () {
        //             if ($scope.fields.length > 0) {
        //                 $scope.gridApi.selection.toggleRowSelection($scope.fields[0]);
        //             }
        //         });
        //     }
        // };
        //
        // // Click handler for up button
        // $scope.moveFieldUp = function () {
        //     var selectedItems = $scope.gridApi.selection.getSelectedRows();
        //     if (selectedItems && selectedItems.length > 0) {
        //         var index = $scope.fields.indexOf(selectedItems[0]);
        //         if (index != 0) { // If it's the first, no moving up of course
        //             var temp = $scope.fields[index];
        //             $scope.fields.splice(index, 1);
        //             $timeout(function () {
        //                 $scope.fields.splice(index + -1, 0, temp);
        //                 $timeout(function () {
        //                     $scope.gridApi.selection.toggleRowSelection(temp);
        //                 });
        //             });
        //         }
        //     }
        // };
        //
        // // Click handler for down button
        // $scope.moveFieldDown = function () {
        //     var selectedItems = $scope.gridApi.selection.getSelectedRows();
        //     if (selectedItems && selectedItems.length > 0) {
        //         var index = $scope.fields.indexOf(selectedItems[0]);
        //         if (index != $scope.fields.length - 1) { // If it's the last element, no moving down of course
        //             var temp = $scope.fields[index];
        //             $scope.fields.splice(index, 1);
        //             $timeout(function () {
        //                 $scope.fields.splice(index + 1, 0, temp);
        //                 $timeout(function () {
        //                     $scope.gridApi.selection.toggleRowSelection(temp);
        //                 });
        //             });
        //         }
        //     }
        // };

        // Click handler for save button
        $scope.save = function () {

            if ($scope.fields.length > 0) {
                $scope.property.value = {};
                $scope.property.value.fields = $scope.fields;
            } else {
                $scope.property.value = null;
            }

            $scope.updatePropertyInModel($scope.property);
            $scope.close();
        };

        $scope.cancel = function () {
            $scope.close();
        };

        // Close button handler
        $scope.close = function () {
            $scope.property.mode = 'read';
            $scope.$hide();
        };
    }]);
