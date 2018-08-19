let vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        q: {
            date: ''
        }
    },
    methods: {
        exportByVariety: function (event) {
            if ('' == vm.q.date) {
                alert('请选择日期');
                return ;
            }
            var year = vm.q.date.getFullYear();
            var month = (vm.q.date.getMonth() + 1) / 10 < 1 ? '0' + (vm.q.date.getMonth() + 1) : vm.q.date.getMonth() + 1;
            var day = vm.q.date.getDate() / 10 < 1 ? '0' + vm.q.date.getDate() : vm.q.date.getDate();
            var orderDate = '' + year + '-' + month + '-' + day;

            var params = {'orderDate': orderDate};
            var url = '../ordergoods/exportByVariety';

            var $form = $('<form id="hiddenExportRequestForm" name="hiddenExportRequestForm" method="get" action="' + url + '" />').hide();
            for (var key in params) {
                if (typeof (params[key]) != 'function') {
                    var _input = $('<input type="hidden"/>').attr({'name': key, 'value': !!params[key] ? params[key] : null});
                    _input.appendTo($form);
                }
            }
            $form.appendTo($('#rrapp'));
            $form.submit();
            $form.remove();
        },

        exportByNetwork: function (event) {
            if ('' == vm.q.date) {
                alert('请选择日期');
                return ;
            }
            var year = vm.q.date.getFullYear();
            var month = (vm.q.date.getMonth() + 1) / 10 < 1 ? '0' + (vm.q.date.getMonth() + 1) : vm.q.date.getMonth() + 1;
            var day = vm.q.date.getDate() / 10 < 1 ? '0' + vm.q.date.getDate() : vm.q.date.getDate();
            var orderDate = '' + year + '-' + month + '-' + day;

            var params = {'orderDate': orderDate};
            var url = '../ordergoods/exportByNetwork';

            var $form = $('<form id="hiddenExportRequestForm" name="hiddenExportRequestForm" method="get" action="' + url + '" />').hide();
            for (var key in params) {
                if (typeof (params[key]) != 'function') {
                    var _input = $('<input type="hidden"/>').attr({'name': key, 'value': !!params[key] ? params[key] : null});
                    _input.appendTo($form);
                }
            }
            $form.appendTo($('#rrapp'));
            $form.submit();
            $form.remove();
        },

        printByNetwork: function () {
            if ('' == vm.q.date) {
                alert('请选择日期');
                return ;
            }
            var year = vm.q.date.getFullYear();
            var month = (vm.q.date.getMonth() + 1) / 10 < 1 ? '0' + (vm.q.date.getMonth() + 1) : vm.q.date.getMonth() + 1;
            var day = vm.q.date.getDate() / 10 < 1 ? '0' + vm.q.date.getDate() : vm.q.date.getDate();
            var orderDate = '' + year + '-' + month + '-' + day;

            /*openWindow({
                type: 2,
                title: '<i class="fa fa-print"></i>打印票据',
                content: '../shop/printByNetwork.html?orderDate=' + orderDate
            })*/
            window.open('../shop/printByNetwork.html?orderDate=' + orderDate);
        }
    }
});