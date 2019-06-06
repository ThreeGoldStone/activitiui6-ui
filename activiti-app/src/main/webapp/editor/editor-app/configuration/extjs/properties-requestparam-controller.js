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
        if ($scope.property.value != null && (typeof $scope.property.value) == 'object') {
            $scope.requestParamTree = $scope.property.value;
        }
        let editor = $scope.editor;
        let resourceId = editor.selection[0].resourceId;
        let allJson = editor.getJSON();
        let childShapes = allJson.childShapes;
        myForEach(childShapes, function (childShape) {
            let stencil = childShape.stencil;
            if (stencil.id == 'EXTJSServiceTask') {
                let globalVariable = {
                    id: childShape.properties.overrideid,
                    name: childShape.properties.name,
                    fields: [],
                    stencilid: stencil.id
                };
                let fields = childShape.properties.extjsserviceresultset.fields;
                myForEach(fields, function (field) {
                    globalVariable.fields.push({
                        name: field.name,
                        type: field.type // TODO resultset 得添加数据类型
                    });
                });
                $scope.globalVariables.push(globalVariable);
                if ($scope.requestParamTree == null && resourceId == childShape.resourceId) {
                    console.log('当前选中的节点：' + childShape);
                    // 从选取的服务处，初始化请求参数
                    $scope.requestParamTree = childShape.properties.extjsserviceid.requestParams;
                }
            } else if (stencil.id == 'StartNoneEvent') {
                // TODO 把startevent和人工节点等地方进啦的参数也添加到全局可选参数中
            }

        });

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
            treeDiGui($scope.requestParamTree, function (element) {
                element.$$isExpend = false;
                element.$$isChecked = false;
            })
            $scope.property.mode = 'read';
            $scope.$hide();
        };
    }]);
