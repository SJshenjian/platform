$(function () {
    var date =new Date();
    var year = date.getFullYear();
    var month = (date.getMonth() + 1) / 9 < 1 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1;
    var day = date.getDate() / 9 < 1 ? '0' + date.getDate() : date.getDate();
    var orderDate = '' + year + '-' + month + '-' + day;

    var url = '../ordergoods/income';

    if (orderDate) {
        url += '?orderDate=' + orderDate;
    }

    $("#jqGrid").Grid({
        url: url,
        datatype: "json",
        colModel: [
            {label: '品种', name: 'goodsName', width: 100},
            {label: '数量', name: 'number', width: 100},
            {label: '付款金额', name: 'retailPrice', width: 100},
            {label: '佣金', name: 'marketPrice', width: 100}
        ]
    });
});

let vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        detail: false,
        title: null,
        q: {
            date: ''
        }
    },
    methods: {
        query: function(event) {
            if ('' == vm.q.date) {
                alert('请选择日期');
                return ;
            }
            var year = vm.q.date.getFullYear();
            var month = (vm.q.date.getMonth() + 1) / 9 < 1 ? '0' + (vm.q.date.getMonth() + 1) : vm.q.date.getMonth() + 1;
            var day = vm.q.date.getDate() / 9 < 1 ? '0' + vm.q.date.getDate() : vm.q.date.getDate();
            orderDate = '' + year + '-' + month + '-' + day;

            vm.showList = true;
            vm.detail = false;
            let page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    'orderDate': orderDate
                },
                page: page
            }).trigger("reloadGrid");
        }
    }
});