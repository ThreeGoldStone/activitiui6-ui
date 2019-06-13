// angular.module("com.ngbook.demo", [])
// .controller("DemoController", ['$http', function ($http) {
//     var vm = this;
//     vm.tree = [
//         {
//             "id": "1",
//             "pid": "0",
//             "name": "家用电器",
//             "children": [
//                 {
//                     "id": "4",
//                     "pid": "1",
//                     "name": "大家电",
//                     "children": [
//                         {
//                             "id": "7",
//                             "pid": "4",
//                             "name": "空调",
//                             "children": [
//                                 {
//                                     "id": "15",
//                                     "pid": "7",
//                                     "name": "海尔空调"
//                                 },
//                                 {
//                                     "id": "16",
//                                     "pid": "7",
//                                     "name": "美的空调"
//                                 }
//                             ]
//                         },
//                         {
//                             "id": "8",
//                             "pid": "4",
//                             "name": "冰箱"
//                         },
//                         {
//                             "id": "9",
//                             "pid": "4",
//                             "name": "洗衣机"
//                         },
//                         {
//                             "id": "10",
//                             "pid": "4",
//                             "name": "热水器"
//                         }
//                     ]
//                 },
//                 {
//                     "id": "5",
//                     "pid": "1",
//                     "name": "生活电器",
//                     "children": [
//                         {
//                             "id": "19",
//                             "pid": "5",
//                             "name": "加湿器"
//                         },
//                         {
//                             "id": "20",
//                             "pid": "5",
//                             "name": "电熨斗"
//                         }
//                     ]
//                 }
//             ]
//         },
//         {
//             "id": "2",
//             "pid": "0",
//             "name": "服饰",
//             "children": [
//                 {
//                     "id": "13",
//                     "pid": "2",
//                     "name": "男装"
//                 },
//                 {
//                     "id": "14",
//                     "pid": "2",
//                     "name": "女装"
//                 }
//             ]
//         },
//         {
//             "id": "3",
//             "pid": "0",
//             "name": "化妆",
//             "children": [
//                 {
//                     "id": "11",
//                     "pid": "3",
//                     "name": "面部护理"
//                 },
//                 {
//                     "id": "12",
//                     "pid": "3",
//                     "name": "口腔护理"
//                 }
//             ]
//         }
//     ];
//     vm.tree2 = [
//         {
//             id: 1,
//             name: "p1",
//             des: "des-pp1",
//             bindParam: "bindParam",
//             children: [
//                 {
//                     id: 2,
//                     name: "p11",
//                     des: "des-p1",
//                     bindParam: "bindParam"
//
//                 },
//                 {
//                     id: 2,
//                     name: "p12",
//                     des: "des-p1",
//                     bindParam: "bindParam"
//
//                 },
//                 {
//                     id: 2,
//                     name: "p13",
//                     des: "des-p1",
//                     bindParam: "bindParam",
//                     children: [
//                         {
//                             id: 2,
//                             name: "p131",
//                             des: "des-p1",
//                             bindParam: "bindParam"
//
//                         },
//                         {
//                             id: 2,
//                             name: "p132",
//                             bindParam: "bindParam"
//
//                         },
//                         {
//                             id: 2,
//                             name: "p133",
//                             des: "des-p1",
//
//                         }
//                     ]
//                 }
//             ]
//         },
//         {
//             id: 1,
//             name: "p2",
//             des: "des-pp1",
//             bindParam: "bindParam",
//             children: [
//
//                 {
//                     id: 2,
//                     name: "p21",
//                     des: "des-p1",
//                     bindParam: "bindParam"
//
//                 },
//                 {
//                     id: 2,
//                     name: "p22",
//                     des: "des-p1",
//                     bindParam: "bindParam"
//
//                 }
//             ]
//         },
//
//     ]
//     ;
//     vm.itemClicked = function ($item) {
//         // vm.selectedItem = $item;
//         // console.log($item, 'item clicked');
//         // vm.itemCheckedChanged($item)
//
//     };
//
//     function changeAll(list, status) {
//         for (var j = 0, len = list.length; j < len; j++) {
//             var item = list[j];
//             if (item.children != null && item.children.length > 0) {
//                 item.$$isExpend = status;
//                 changeAll(item.children, status);
//             }
//         }
//     }
//
//     vm.changeAllOpenStatus = function (status) {
//         changeAll(vm.tree2, status);
//         // vm.selectedItem = $item;
//         // console.log($item, 'item clicked');
//         // vm.itemCheckedChanged($item)
//
//     };
//
//     vm.itemCheckedChanged = function ($item) {
//         // 实现单选
//         if ($item.$$isChecked && vm.selectedItem != null && (vm.selectedItem != $item)) {
//             vm.selectedItem.$$isChecked = false;
//         }
//         vm.selectedItem = $item;
//         // $http.post('/url', $item);
//         console.log($item, 'item checked');
//     };
//
//     return vm;
// }])
angular.module('activitiModeler')
    .directive('treeView', ['appResourceRoot', function (appResourceRoot) {

        return {
            restrict: 'E',
            templateUrl: appResourceRoot + 'editor-app/configuration/extjs/libs/json-tree/treeView.html',
            scope: {
                treeData: '=',
                canChecked: '=',
                textField: '@',
                itemClicked: '&',
                itemCheckedChanged: '&',
                itemTemplateUrl: '@'
            },
            controller: ['$scope', function ($scope) {
                $scope.itemExpended = function (item, $event) {
                    item.$$isExpend = !item.$$isExpend;
                    $event.stopPropagation();
                };

                $scope.getItemIcon = function (item) {
                    var isLeaf = $scope.isLeaf(item);

                    if (isLeaf) {
                        return 'fa fa-leaf';
                    }

                    return item.$$isExpend ? 'fa fa-minus' : 'fa fa-plus';
                };

                $scope.isLeaf = function (item) {
                    return !item.children || !item.children.length;
                };

                $scope.warpCallback = function (callback, item, $event) {
                    ($scope[callback] || angular.noop)({
                        $item: item,
                        $event: $event
                    });
                };
            }]
        };
    }]);

/**
 *递归树数据，并执行callback
 * @param tree
 * @param callback
 */
function treeDiGui(tree, callback) {
    for (var j = 0, len = tree.length; j < len; j++) {
        let element = tree[j];
        callback(element);
        let clist = element.children;
        if (clist != null && clist.length > 0) {
            treeDiGui(clist, callback)
        }
    }

}

/**
 *递归遍历树，并可获得父级对象
 * @param parents 当前等级的父级集合
 * @param tree 当前等级的树
 * @param callback 执行的回调方法
 */
function treeDiGuiWithParent(parents, tree, callback) {
    for (var j = 0, len = tree.length; j < len; j++) {
        let element = tree[j];
        callback(parents, element);
        let clist = element.children;
        if (clist != null && clist.length > 0) {
            if (parents == null) {
                parents = [];
            }
            parents.push(element);
            treeDiGuiWithParent(parents, clist, callback)
        }
    }

}

function singleCheckById(tree, valuePath) {
    treeDiGui(tree, function (element) {
        element.$$isChecked = element.path == valuePath;
    })
}

/**
 * 根据id进行单选，并展开其父组件
 * @param tree
 * @param id
 */
function singleCheckByIdWhithParentExpend(tree, valuePath) {
    treeDiGuiWithParent(null, tree, function (parents, element) {
        element.$$isChecked = element.path == valuePath;
        if (element.$$isChecked && parents != null && parents.length > 0) {
            for (let i = 0, len = parents.length; i < len; i++) {
                let parent = parents[i];
                parent.$$isExpend = true;
            }
        }

    })
}

/**
 *遍历数组，可以通过设置 listElement.$$break=true;来打断循环
 * @param list
 * @param callBack
 */
function myForEach(list, callBack) {
    for (let i = 0, len = list.length; i < len; i++) {
        let listElement = list[i];
        callBack(listElement);
        if (listElement.$$break) {
            delete listElement.$$break;
            break;
        }
    }
}

function myForEachWitchIndex(list, callBack) {
    for (let i = 0, len = list.length; i < len; i++) {
        let listElement = list[i];
        callBack(listElement, i);
        if (listElement.$$break) {
            delete listElement.$$break;
            break;
        }
    }
}

//树状结构构建相关*************************** start ***********************************
var gettype = Object.prototype.toString;

/**
 * 获取json节点对应 的类型
 * @param node
 * @returns {string}
 */
function getJsonNodeType(node) {
    let type = gettype.call(node);
    switch (type) {
        case '[object String]':
            return 'string';
        case '[object Boolean]':
            return 'bool';
        case '[object Number]':
            return 'number';
        case '[object Object]':
            return 'object';
        case '[object Array]':
            return 'array';
        default:
            return null;
    }
}

function parseJsonObjectToTree(parentPath, json) {
    let treeBeans = [];
    for (var key in json) {
        let value = json[key];
        if (value != null) {
            let treeBean = parseJsonTreeBean(parentPath, key, value);
            if (treeBean == null) continue;
            treeBeans.push(treeBean);
        }
        console.log(key);
        console.log(value);
        console.log(getJsonNodeType(value))
    }
    return treeBeans;
}

function parseJsonArrayToTree(parentPath, json) {
    let treeBeans = [];
    myForEachWitchIndex(json, function (item, index) {
        let treeBean = parseJsonTreeBean(parentPath, index, item);
        if (treeBean != null)
            treeBeans.push(treeBean);
    })
    return treeBeans;
}

function parseJsonTreeBean(parentPath, key, value) {
    let treeBean = {
        name: key,
        path: parentPath + '.' + key,
        templateValue: value
    };
    let type = getJsonNodeType(value);
    if (type == null) {
        console.error("json 节点 类型获取失败" + treeBean.path);
        return null;
    }
    switch (type) {
        case 'object':
            let objChildren = parseJsonObjectToTree(treeBean.path, value);
            treeBean.children = objChildren;
            break;
        case 'array':
            let arrayChildren = parseJsonArrayToTree(treeBean.path, value);
            treeBean.children = arrayChildren;
            break
    }
    treeBean.type = type;
    return treeBean;

}

/**
 * 把json数据转为对应的树状结构
 * @param json
 * @returns {Array}
 */
function parseJsonToTree(json) {
    let type = getJsonNodeType(json);
    switch (type) {
        case 'object':
            return parseJsonObjectToTree("", json);
        case 'array':
            return parseJsonArrayToTree("", json);
    }


}

//树状结构构建相关*************************** end ***********************************

//流程数据处理工具*************************** start ***********************************
/**
 *
 * @param $scope
 * @returns childShape
 */
function getSelectionShapesData($scope) {
    let editor = $scope.editor;
    let resourceId = editor.selection[0].resourceId;
    let allJson = editor.getJSON();
    let childShapes = allJson.childShapes;
    let mChildShape = null;
    myForEach(childShapes, function (childShape) {
        if (resourceId == childShape.resourceId) {
            console.log('当前选中的节点：' + childShape);
            mChildShape = childShape;
            $$break = true;
        }
    });
    return mChildShape;
}

/**
 * 获取所有全局参数的列表
 * @param $scope
 * @returns {Array}
 */
function getAllGlobalVariablesJson($scope) {
    let allVariables = [];
    let editor = $scope.editor;
    let allJson = editor.getJSON();
    let childShapes = allJson.childShapes;
    myForEach(childShapes, function (childShape) {
        let stencilid = childShape.stencil.id;
        if (stencilid == 'EXTJSServiceTask') {
            let extJSNodeVariables = {
                overrideid: childShape.properties.overrideid,
                resourceId: childShape.resourceId,
                variables: {},
                stencilid: stencilid
            };
            myForEach(childShape.properties.extjsserviceresultset, function (resultSet) {
                extJSNodeVariables.variables[resultSet.name] = resultSet.templateValue;
            });
            allVariables.push(extJSNodeVariables);
        } else if (stencilid == "StartEvent") {
            // TODO  处理开始事件的form参数
            let formNodeVariables = {
                overrideid: childShape.properties.overrideid,
                resourceId: childShape.resourceId,
                variables: {},
                stencilid: stencilid
            };
            // childShape.properties.formproperties.formProperties[""0""].id
            myForEach(childShape.properties.formproperties.formProperties,function (fp) {


            })
        }
    });
    return allVariables;
}

// function getSelectionShapesData($scope) {
//     let editor = $scope.editor;
//     let resourceId = editor.selection[0].resourceId;
//     let allJson = editor.getJSON();
//     let childShapes = allJson.childShapes;
//     myForEach(childShapes, function (childShape) {
//         if (resourceId == childShape.resourceId) {
//             console.log('当前选中的节点：' + childShape);
//             return childShape;
//         }
//     });
//     return null;
// }


//流程数据处理工具*************************** end ***********************************
