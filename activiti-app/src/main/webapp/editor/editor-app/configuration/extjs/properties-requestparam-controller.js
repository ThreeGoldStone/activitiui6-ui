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
    ['$scope', '$modal', '$q', '$translate', '$timeout', '$http', function ($scope, $modal, $q, $translate, $timeout, $http) {


        $q.all([]).then(function (results) {
            $scope.translationsRetrieved = true;
        });
        $scope.globalVariables = [];
        $scope.requestParamTree = null;
        /**
         * 根据父级是否有选取，刷新item是否可选取
         */
        $scope.refreshDisabled = function () {
            treeDiGuiWithParent(null, $scope.requestParamTree, function (parents, element) {
                if (parents != null && parents.size() > 0) {
                    let isParentHasValue = false;
                    myForEach(parents, function (parent) {
                        if (parent.valueConfig && parent.valueConfig.path) {
                            isParentHasValue = true;
                            parent.$$break;

                        }
                    });
                    element.$$isDisabled = isParentHasValue;
                }
            });
        };

        if ($scope.property.value != null && (typeof $scope.property.value) == 'object') {
            $scope.requestParamTree = angular.copy($scope.property.value);
            $scope.refreshDisabled();
        }
        // Close button handler
        $scope.close = function () {
            treeDiGui($scope.requestParamTree, function (element) {
                element.$$isExpend = false;
                element.$$isChecked = false;
            })
            $scope.property.mode = 'read';
            $scope.$hide();
        };
        $scope.forceRefresh = function () {
            // 获取返回参数报文模板
            let mShapeData = getSelectionShapesData($scope);
            if (!mShapeData.properties.extjsserviceid) {
                // 如果没选取extjs的服务，就报错
                $scope.addAlert('请先选择extjs服务！', 'error');
                $scope.close();
                return;
            }
            let reqObj = JSON.parse(mShapeData.properties.extjsserviceid.requestTemplate);
            // 生成返回参数报文模板树
            $scope.requestParamTree = parseJsonToTree(reqObj);
        };
        // 获取默认参数模板
        if ($scope.requestParamTree == null) {
            $scope.forceRefresh();
        }

        // 获取全局参数 json
        $scope.globalVariables = getAllGlobalVariablesJson($scope);
        // myForEach($scope.globalVariables, function (vj) {
        //     let vt = parseJsonToTree(vj.variables);
        //     vj.jsonTree = vt;
        // });
        $http({
            method: 'GET',
            url: KISBPM.URL.getExtraParams($scope.editor.id),
            // data: instanceQueryData
        }).success(function (response, status, headers, config) {
            console.log('data: ' + response);
            if (response) {

                myForEach($scope.globalVariables, function (item) {
                    if (item.stencilid.indexOf('Start') > -1) {
                        // try {
                        //     response = JSON.parse(response);
                        // } catch (e) {
                        //     console.log(response + " 不是json，按照string处理")
                        // }
                        item.variables.inputData = response.data;
                    }
                });
            }
            myForEach($scope.globalVariables, function (vj) {
                let vt = parseJsonToTree(vj.variables);
                vj.jsonTree = vt;
            });
        }).error(function (response, status, headers, config) {
            console.log('Something went wrong: ' + response);
        });
        // 把各个节点的参数转化为树状结构

        $scope.selectFromAllParam = function () {
            // $scope.selectedItem = $item;
            console.log('selectFromAllParam');
            // $scope.itemCheckedChanged($item)
            _internalCreateModal({
                backdrop: true,
                keyboard: true,
                template: 'editor-app/configuration/extjs/properties/global-variables-popup.html?version=' + Date.now(),
                scope: $scope
            }, $modal, $scope);


        };
        $scope.clearSelectedParam = function () {
            console.log('selectFromAllParam');
            $scope.selectedItem.valueConfig = null;
            $scope.refreshDisabled();

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
            singleCheckById($scope.requestParamTree, $scope.selectedItem.path);
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


    }]);
