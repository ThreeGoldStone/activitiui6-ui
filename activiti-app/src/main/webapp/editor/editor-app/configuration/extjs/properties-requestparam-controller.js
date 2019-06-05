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




        $q.all([]).then(function (results) {
            $scope.translationsRetrieved = true;
        });
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
                        id: 2,
                        name: "p12",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p11",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p12",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p11",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p12",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p11",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p12",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p11",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p12",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p11",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p12",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p11",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p12",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p11",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p12",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p11",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p12",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p11",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p12",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p11",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p12",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p11",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p12",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p11",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p12",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p11",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p12",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p13",
                        des: "des-p1",
                        bindParam: "bindParam",
                        children: [
                            {
                                id: 2,
                                name: "p131",
                                des: "des-p1",
                                bindParam: "bindParam"

                            },
                            {
                                id: 2,
                                name: "p132",
                                bindParam: "bindParam"

                            },
                            {
                                id: 2,
                                name: "p133",
                                des: "des-p1",

                            }
                        ]
                    }
                ]
            },
            {
                id: 1,
                name: "p2",
                des: "des-pp1",
                bindParam: "bindParam",
                children: [

                    {
                        id: 2,
                        name: "p21",
                        des: "des-p1",
                        bindParam: "bindParam"

                    },
                    {
                        id: 2,
                        name: "p22",
                        des: "des-p1",
                        bindParam: "bindParam"

                    }
                ]
            },

        ];
        let allJson = $scope.editor.getJSON();
        console.log(allJson);
        let childShapes = allJson.childShapes;
        console.log(childShapes);
        for (var i=0,len=childShapes.length;i < len;i++) {
            let childShape = childShapes[i];
            let stencil = childShape.stencil;
            if (stencil.id=='EXTJSServiceTask') {
                console.log(childShape)
                // TODO 生成全局参数总树
            }
        }
        if ($scope.property.value != null && (typeof $scope.property.value)=='object') {
            $scope.requestParamTree = $scope.property.value;
        }
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
            treeDiGui($scope.requestParamTree,function (element) {
                element.$$isExpend = status;
            })

        };


        $scope.itemCheckedChanged = function ($item) {
            // 实现单选
            if ($item.$$isChecked && $scope.selectedItem != null && ($scope.selectedItem != $item)) {
                $scope.selectedItem.$$isChecked = false;
            }
            $scope.selectedItem = $item;
            // $http.post('/url', $item);
            console.log($item, 'item checked');
        };


        // Click handler for save button
        $scope.save = function () {

            $scope.property.value = $scope.requestParamTree;
            $scope.updatePropertyInModel($scope.property);
            $scope.close();
        };

        $scope.cancel = function () {
            $scope.close();
        };

        // Close button handler
        $scope.close = function () {
            treeDiGui($scope.requestParamTree,function (element) {
                element.$$isExpend = false;
                element.$$isChecked = false;
            })
            $scope.property.mode = 'read';
            $scope.$hide();
        };
    }]);
