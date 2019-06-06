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

function singleCheckById(tree, id) {
    treeDiGui(tree, function (element) {
        element.$$isChecked = element.id == id;
    })
}

/**
 * 根据id进行单选，并展开其父组件
 * @param tree
 * @param id
 */
function singleCheckByIdWhithParentExpend(tree, id) {
    treeDiGuiWithParent(null, tree, function (parents, element) {
        element.$$isChecked = element.id == id;
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
