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

angular.module('activitiModeler').controller('BpmResultsetCtrl',
    ['$scope', '$modal', '$timeout', '$translate', function ($scope, $modal, $timeout, $translate) {

        // Config for the modal window
        var opts = {
            template: 'editor-app/configuration/extjs/properties/resultset-popup.html?version=' + Date.now(),
            scope: $scope,
            // 可以让弹窗不关闭，除非调用hide
            backdrop: false
        };

        // Open the dialog
        _internalCreateModal(opts, $modal, $scope);
    }]);


angular.module('activitiModeler').controller('BpmResultsetPopupCtrl',
    ['$scope', '$q', '$translate', '$timeout', '$http', function ($scope, $q, $translate, $timeout, $http) {
        // Put json representing form properties on scope
        $scope.requestParamTree = [
            {
                id: 1,
                name: "p1",
                des: "des-pp1",
                bindParam: "bindParam",
                children: [
                    {
                        id: 2,
                        name: "p11",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },

                    {
                        id: 3,
                        name: "p13",
                        des: "des-p1",
                        bindParam: "bindParam",
                        children: [
                            {
                                id: 4,
                                name: "p131",
                                des: "des-p1",
                                bindParam: "bindParam"

                            },
                            {
                                id: 5,
                                name: "p132",
                                bindParam: "bindParam"

                            },
                            {
                                id: 6,
                                name: "p133",
                                des: "des-p1",

                            }
                        ]
                    }
                ]
            },
            {
                id: 7,
                name: "p2",
                des: "des-pp1",
                bindParam: "bindParam",
                children: [

                    {
                        id: 8,
                        name: "p21",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 9,
                        name: "p22",
                        des: "des-p1",
                        bindParam: "bindParam"

                    }
                ]
            },

        ];
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


        $q.all([]).then(function (results) {
            $scope.translationsRetrieved = true;

            // Config for grid
            $scope.gridOptions = {
                data: $scope.fields,
                headerRowHeight: 28,
                enableRowSelection: true,
                enableRowHeaderSelection: false,
                multiSelect: false,
                modifierKeysToMultiSelect: false,
                enableHorizontalScrollbar: 0,
                enableColumnMenus: false,
                enableSorting: false,
                columnDefs: [{field: 'name', displayName: '全局变量名称'},
                    {field: 'implementation', displayName: '全局变量取值'}]
            };

            $scope.gridOptions.onRegisterApi = function (gridApi) {
                //set gridApi on scope
                $scope.gridApi = gridApi;
                gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                    $scope.selectedField = row.entity;
                    // treeDiGui($scope.requestParamTree, function (element) {
                    //     element.$$isChecked = element.id == $scope.selectedField.implementationid;
                    // });
                    singleCheckByIdWhithParentExpend($scope.requestParamTree, $scope.selectedField.implementationid);

                });
            };
        });

        // Click handler for add button
        $scope.addNewField = function () {
            var newField = {
                name: 'fieldName',
                implementation: '',
                stringValue: '',
                expression: '',
                string: ''
            };

            $scope.fields.push(newField);
            $timeout(function () {
                $scope.gridApi.selection.toggleRowSelection(newField);
            });

        };

        // Click handler for remove button
        $scope.removeField = function () {

            var selectedItems = $scope.gridApi.selection.getSelectedRows();
            if (selectedItems && selectedItems.length > 0) {
                var index = $scope.fields.indexOf(selectedItems[0]);
                $scope.gridApi.selection.toggleRowSelection(selectedItems[0]);
                $scope.fields.splice(index, 1);

                if ($scope.fields.length == 0) {
                    $scope.selectedField = undefined;
                }

                $timeout(function () {
                    if ($scope.fields.length > 0) {
                        $scope.gridApi.selection.toggleRowSelection($scope.fields[0]);
                    }
                });
            }
        };

        // Click handler for up button
        $scope.moveFieldUp = function () {
            var selectedItems = $scope.gridApi.selection.getSelectedRows();
            if (selectedItems && selectedItems.length > 0) {
                var index = $scope.fields.indexOf(selectedItems[0]);
                if (index != 0) { // If it's the first, no moving up of course
                    var temp = $scope.fields[index];
                    $scope.fields.splice(index, 1);
                    $timeout(function () {
                        $scope.fields.splice(index + -1, 0, temp);
                        $timeout(function () {
                            $scope.gridApi.selection.toggleRowSelection(temp);
                        });
                    });
                }
            }
        };

        // Click handler for down button
        $scope.moveFieldDown = function () {
            var selectedItems = $scope.gridApi.selection.getSelectedRows();
            if (selectedItems && selectedItems.length > 0) {
                var index = $scope.fields.indexOf(selectedItems[0]);
                if (index != $scope.fields.length - 1) { // If it's the last element, no moving down of course
                    var temp = $scope.fields[index];
                    $scope.fields.splice(index, 1);
                    $timeout(function () {
                        $scope.fields.splice(index + 1, 0, temp);
                        $timeout(function () {
                            $scope.gridApi.selection.toggleRowSelection(temp);
                        });
                    });
                }
            }
        };
        $scope.itemClicked = function ($item) {
            // $scope.selectedItem = $item;
            // console.log($item, 'item clicked');
            // $scope.itemCheckedChanged($item)

        };

        // function changeAll(list, status) {
        //     for (var j = 0, len = list.length; j < len; j++) {
        //         var item = list[j];
        //         if (item.children != null && item.children.length > 0) {
        //             item.$$isExpend = status;
        //             changeAll(item.children, status);
        //         }
        //     }
        // }

        $scope.changeAllOpenStatus = function (status) {
            // changeAll($scope.requestParamTree, status);
            treeDiGui($scope.requestParamTree, function (element) {
                element.$$isExpend = status;
            })

        };

        $scope.itemCheckedChanged = function ($item) {
            // 实现单选
            $scope.selectedItem = $item;
            singleCheckById($scope.requestParamTree, $scope.selectedItem.id);
            if ($item.$$isChecked) {
                $scope.selectedField.implementation = $item.name;
                $scope.selectedField.implementationid = $item.id;
            } else {
                $scope.selectedField.implementation = null;
                $scope.selectedField.implementationid = null;
            }
            // $http.post('/url', $item);
            console.log($item, 'item checked');
        };
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
