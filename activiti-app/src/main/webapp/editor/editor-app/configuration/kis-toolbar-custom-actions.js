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

// Create custom functions for the KIS-editor
KISBPM.TOOLBAR.ACTIONS.closeEditor = function (services) {
    services.$location.path("/processes");
};
KISBPM.TOOLBAR.ACTIONS.publish = function (services) {
    _internalCreateModal({
        backdrop: true,
        keyboard: true,
        template: 'editor-app/popups/my-publish.html?version=' + Date.now(),
        scope: services.$scope
    }, services.$modal, services.$scope);
};

KISBPM.TOOLBAR.ACTIONS.navigateToProcess = function (processId) {
    var navigateEvent = {
        type: KISBPM.eventBus.EVENT_TYPE_NAVIGATE_TO_PROCESS,
        processId: processId
    };
    KISBPM.eventBus.dispatch(KISBPM.eventBus.EVENT_TYPE_NAVIGATE_TO_PROCESS, navigateEvent);
},

// Add custom buttons 
//     KISBPM.TOOLBAR_CONFIG.secondaryItems.push(
//         {
//             "type": "button",
//             "title": "Close",
//             "cssClass": "glyphicon glyphicon-remove",
//             "action": "KISBPM.TOOLBAR.ACTIONS.closeEditor"
//         }
//     );
    KISBPM.TOOLBAR_CONFIG.secondaryItems.push(
        {
            "type": "button",
            "title": "发布",
            "cssClass": "glyphicon glyphicon-play-circle",
            "action": "KISBPM.TOOLBAR.ACTIONS.publish"
        }
    );

/** Custom controller for the save dialog */
angular.module('activitiModeler').controller('myPublish', ['$rootScope', '$scope', '$http', '$route', '$location',
    function ($rootScope, $scope, $http, $route, $location) {

        var modelMetaData = $scope.editor.getModelMetaData();

        var description = '';
        if (modelMetaData.description) {
            description = modelMetaData.description;
        }

        var saveDialog = {
            'name': modelMetaData.name,
            'key': modelMetaData.key,
            'description': description,
            'newVersion': false,
            'comment': ''
        };

        $scope.saveDialog = saveDialog;

        var json = $scope.editor.getJSON();
        json = JSON.stringify(json);

        var params = {
            modeltype: modelMetaData.model.modelType,
            json_xml: json,
            name: 'model'
        };

        $scope.status = {
            loading: false
        };

        $scope.close = function () {
            $scope.$hide();
        };


        $scope.myPublish = function (successCallback) {

            // if (!$scope.saveDialog.name || $scope.saveDialog.name.length == 0 ||
            //     !$scope.saveDialog.key || $scope.saveDialog.key.length == 0) {
            //
            //     return;
            // }

            // Indicator spinner image
            $scope.status = {
                loading: true
            };

            // modelMetaData.name = $scope.saveDialog.name;
            // modelMetaData.key = $scope.saveDialog.key;
            // modelMetaData.description = $scope.saveDialog.description;
            //
            // var json = $scope.editor.getJSON();
            // json = JSON.stringify(json);
            //
            // var params = {
            //     modeltype: modelMetaData.model.modelType,
            //     json_xml: json,
            //     name: $scope.saveDialog.name,
            //     key: $scope.saveDialog.key,
            //     description: $scope.saveDialog.description,
            //     newversion: $scope.saveDialog.newVersion,
            //     comment: $scope.saveDialog.comment,
            //     lastUpdated: modelMetaData.lastUpdated
            // };
            //
            // if ($scope.error && $scope.error.isConflict) {
            //     params.conflictResolveAction = $scope.error.conflictResolveAction;
            //     if ($scope.error.conflictResolveAction === 'saveAs') {
            //         params.saveAs = $scope.error.saveAs;
            //     }
            // }

            // Update
            $http({
                method: 'GET',
                url: KISBPM.URL.myPublish(modelMetaData.modelId)
            }).success(function (data, status, headers, config) {
                $scope.status.loading = false;
                $scope.$hide();
            }).error(function (data, status, headers, config) {
                if (status == 409) {
                    $scope.error = {};
                    $scope.error.isConflict = true;
                    $scope.error.userFullName = data.customData.userFullName;
                    $scope.error.isNewVersionAllowed = data.customData.newVersionAllowed;
                    $scope.error.saveAs = modelMetaData.name + "_2";
                } else {
                    $scope.error = undefined;
                    $scope.saveDialog.errorMessage = data.message;
                }
                $scope.status.loading = false;
            });
            // $http({
            //     method: 'POST',
            //     data: params,
            //     ignoreErrors: true,
            //     headers: {
            //         'Accept': 'application/json',
            //         'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
            //     },
            //     transformRequest: function (obj) {
            //         var str = [];
            //         for (var p in obj) {
            //             str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
            //         }
            //         return str.join("&");
            //     },
            //     url: KISBPM.URL.putModel(modelMetaData.modelId)
            // })
            //
            //     .success(function (data, status, headers, config) {
            //         $scope.editor.handleEvents({
            //             type: ORYX.CONFIG.EVENT_SAVED
            //         });
            //         $scope.modelData.name = $scope.saveDialog.name;
            //         $scope.modelData.key = $scope.saveDialog.key;
            //         $scope.modelData.lastUpdated = data.lastUpdated;
            //
            //         $scope.status.loading = false;
            //         $scope.$hide();
            //
            //         // Fire event to all who is listening
            //         var saveEvent = {
            //             type: KISBPM.eventBus.EVENT_TYPE_MODEL_SAVED,
            //             model: params,
            //             modelId: modelMetaData.modelId,
            //             eventType: 'update-model'
            //         };
            //         KISBPM.eventBus.dispatch(KISBPM.eventBus.EVENT_TYPE_MODEL_SAVED, saveEvent);
            //
            //         // Reset state
            //         $scope.error = undefined;
            //         $scope.status.loading = false;
            //
            //         // Execute any callback
            //         if (successCallback) {
            //             successCallback();
            //         }
            //
            //     })
            //     .error(function (data, status, headers, config) {
            //         if (status == 409) {
            //             $scope.error = {};
            //             $scope.error.isConflict = true;
            //             $scope.error.userFullName = data.customData.userFullName;
            //             $scope.error.isNewVersionAllowed = data.customData.newVersionAllowed;
            //             $scope.error.saveAs = modelMetaData.name + "_2";
            //         } else {
            //             $scope.error = undefined;
            //             $scope.saveDialog.errorMessage = data.message;
            //         }
            //         $scope.status.loading = false;
            //     });
        };


    }]);


