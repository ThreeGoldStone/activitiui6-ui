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

angular.module('activitiModeler').controller('BpmServiceidCtrl',
    ['$scope', '$modal', '$timeout', '$translate', function ($scope, $modal, $timeout, $translate) {

        // Config for the modal window
        var opts = {
            template: 'editor-app/configuration/extjs/properties/serviceid-popup.html?version=' + Date.now(),
            scope: $scope,
            // 可以让弹窗不关闭，除非调用hide
            backdrop: false
        };

        // Open the dialog
        _internalCreateModal(opts, $modal, $scope);
    }]);

angular.module('activitiModeler').controller('BpmServiceidPopupCtrl',
    ['$scope', '$q', '$translate', '$timeout', '$http', function ($scope, $q, $translate, $timeout, $http) {
// javaservices的数据定义
        $scope.serviceList = [
            {
                id: "1",
                appname: "系统应用",
                serviceList: [
                    {
                        serviceid: "test-service-1",
                        servicename: "测试服务1",
                        requestParams: [
                            {
                                id: 2,
                                name: "p13",
                                des: "des-p1",
                                type: 'object',
                                bindParam: null,
                                children: [
                                    {
                                        id: 3,
                                        name: "p131",
                                        des: "des-p131",
                                        type: 'string',
                                        bindParam: null

                                    },
                                    {
                                        id: 4,
                                        name: "p132",
                                        des: "des-p132",
                                        type: 'string',
                                        bindParam: null

                                    }
                                ]
                            }
                        ],
                        resultSet: [
                            {
                                id: 2,
                                name: "p13",
                                des: "des-p1",
                                type: 'object',
                                bindParam: null,
                                children: [
                                    {
                                        id: 3,
                                        name: "p131",
                                        des: "des-p131",
                                        type: 'string',
                                        bindParam: null

                                    },
                                    {
                                        id: 4,
                                        name: "p132",
                                        des: "des-p132",
                                        type: 'string',
                                        bindParam: null

                                    }
                                ]
                            }
                        ],
                        des: "测试服务1 des"
                    },
                    {
                        serviceid: "test-service-2",
                        servicename: "测试服务1",
                        des: "测试服务2 des"
                    },
                    {
                        serviceid: "test-service-3",
                        servicename: "测试服务1",
                        des: "测试服务3 des"
                    }
                ]
            },
            {
                id: "",
                appname: "djl应用",
                serviceList: [
                    {
                        serviceid: "test-djl-service-1",
                        servicename: "djl服务1",
                        des: "djl服务1 des"
                    },
                    {
                        serviceid: "test-djl-service-2",
                        servicename: "djl服务2",
                        des: "djl服务2 des"
                    }
                ]
            },

        ];
        $scope.service = {
            serviceid: null,
            servicename: null,
            des: null
        };
        // 初始化已选择的数据
        if ($scope.property.value != null) {
            djlLoop:
                for (var j = 0, len = $scope.serviceList.length; j < len; j++) {
                    var app = $scope.serviceList[j];
                    for (var i = 0, len = app.serviceList.length; i < len; i++) {
                        var service = app.serviceList[i];
                        if (service.serviceid == $scope.property.value.serviceid) {
                            $scope.service = service;
                            $scope.service.$$isChecked = true;
                            app.$$isExpend = true;
                            break djlLoop;
                        }
                    }
                }
        }


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
            if ($item.$$isChecked && $scope.service != null && ($scope.service != $item)) {
                $scope.service.$$isChecked = false;
            }
            $scope.service = $item;
            // $http.post('/url', $item);
            console.log($item, 'item checked');
        };
        // Click handler for save button
        $scope.save = function () {

            if ($scope.service.serviceid !== undefined && $scope.service.serviceid !== null) {
                $scope.property.value = {};
                $scope.property.value = $scope.service;
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
