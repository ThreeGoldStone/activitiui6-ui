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
    ['$rootScope', '$scope', '$q', '$translate', '$timeout', '$http', '$location', function ($rootScope, $scope, $q, $translate, $timeout, $http, $location) {
// javaservices的数据定义
        $scope.getServiceDetail = function (service) {
            $scope.detailStatus = {
                loading: true,
                error: false,
                errorMessage: ''
            };
            $http({
                method: 'GET',
                url: ACTIVITI.CONFIG.contextRoot + '/app/rest/java/service/details/' + service.serviceid,
                // data: instanceQueryData
            }).success(function (response, status, headers, config) {
                $scope.detailStatus.loading = false;
                console.log('data: ' + response);
                service.requestTemplate = response.requestbody;
                service.responceTemplate = response.responsebody;
            }).error(function (response, status, headers, config) {
                $scope.detailStatus.loading = false;
                $scope.detailStatus.error = true;
                $scope.detailStatus.errorMessage = '获取'+service.serviceid+'的模板失败！';
                console.log('Something went wrong: ' + response);
            });

        };
        $scope.listStatus = {
            loading: true,
            error: false,
            errorMessage: ''
        };
        $scope.detailStatus = {
            loading: false,
            error: false,
            errorMessage: ''
        };
        $scope.close = function () {
            $scope.property.mode = 'read';
            $scope.$hide();
        };
        $scope.retryDetail = function () {
            $scope.getServiceDetail($scope.service);
        };
        // TODO 当代数据接口
        $scope.serviceList = [
            // {
            //     id: "1",
            //     appname: "系统应用",
            //     serviceList: [
            //         {
            //             serviceid: "test-service-1",
            //             servicename: "测试服务1",
            //             requestParams: [
            //                 {
            //                     id: 2,
            //                     name: "p13",
            //                     des: "des-p1",
            //                     type: 'object',
            //                     bindParam: null,
            //                     children: [
            //                         {
            //                             id: 3,
            //                             name: "p131",
            //                             des: "des-p131",
            //                             type: 'string',
            //                             bindParam: null
            //
            //                         },
            //                         {
            //                             id: 4,
            //                             name: "p132",
            //                             des: "des-p132",
            //                             type: 'string',
            //                             bindParam: null
            //
            //                         }
            //                     ]
            //                 }
            //             ],
            //             resultSet: [
            //                 {
            //                     id: 2,
            //                     name: "p13",
            //                     des: "des-p1",
            //                     type: 'object',
            //                     bindParam: null,
            //                     children: [
            //                         {
            //                             id: 3,
            //                             name: "p131",
            //                             des: "des-p131",
            //                             type: 'string',
            //                             bindParam: null
            //
            //                         },
            //                         {
            //                             id: 4,
            //                             name: "p132",
            //                             des: "des-p132",
            //                             type: 'string',
            //                             bindParam: null
            //
            //                         }
            //                     ]
            //                 }
            //             ],
            //             des: "测试服务1 des"
            //         },
            //         {
            //             serviceid: "test-service-2",
            //             servicename: "测试服务1",
            //             des: "测试服务2 des"
            //         },
            //         {
            //             serviceid: "test-service-3",
            //             servicename: "测试服务1",
            //             des: "测试服务3 des"
            //         }
            //     ]
            // },
            // {
            //     id: "",
            //     appname: "djl应用",
            //     serviceList: [
            //         {
            //             serviceid: "test-djl-service-1",
            //             servicename: "djl服务1",
            //             des: "djl服务1 des"
            //         },
            //         {
            //             serviceid: "test-djl-service-2",
            //             servicename: "djl服务2",
            //             des: "djl服务2 des"
            //         }
            //     ]
            // },

        ];
        let tenantid = $location.search().tenantid;
        $http({
            method: 'GET',
            url: ACTIVITI.CONFIG.contextRoot + '/app/rest/java/ExtServices/' + tenantid,
            // data: instanceQueryData
        }).success(function (response, status, headers, config) {
            console.log('data: ' + response);
            $scope.serviceList = response;
            // 初始化已选择的数据
            if ($scope.property.value != null) {
                djlLoop:
                    for (var j = 0, len = $scope.serviceList.length; j < len; j++) {
                        var app = $scope.serviceList[j];
                        for (var i = 0, len2 = app.serviceList.length; i < len2; i++) {
                            var service = app.serviceList[i];
                            if (service.serviceid == $scope.property.value.serviceid) {
                                $scope.service = service;
                                $scope.getServiceDetail($scope.service);
                                $scope.service.$$isChecked = true;
                                app.$$isExpend = true;
                                break djlLoop;
                            }
                        }
                    }
            }
            $scope.listStatus.loading = false;
        }).error(function (response, status, headers, config) {
            console.log('Something went wrong: ' + response);
            $scope.close();
            $rootScope.addAlert('获取EXTJS服务列表失败：' + status, 'error');
            $scope.listStatus.loading = false;
        });
        $scope.service = {
            serviceid: null,
            servicename: null,
            des: null
        };


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
            $scope.detailStatus = {
                loading: false,
                error: false,
                errorMessage: ''
            };
            // 实现单选
            if ($item.$$isChecked && $scope.service != null && ($scope.service != $item)) {
                $scope.service.$$isChecked = false;
            }
            $scope.service = $item;
            if (!($scope.service.requestTemplate || $scope.service.responceTemplate)) {
                $scope.getServiceDetail($scope.service);
            }

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

    }]);
